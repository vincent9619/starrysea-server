package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.connection.entity.*;
import top.starrysea.rina.core.connection.entity.enums.HttpContentType;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.connection.entity.enums.HttpVersion;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.string.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RinaObject
public class HttpMessageResolver {


    public HttpContent handleRun(String receiveMessage) {

        List<String> serverReport = Arrays.asList(receiveMessage.split("\r\n"));
        serverReport.stream().forEach(log::info);


        //第一次分割
        HttpContent hp = new HttpContent();
        Map<String, Object> httpMap = new HashMap<>();
        String firstLine = serverReport.get(0);
        String[] httpBasicInfo = firstLine.split(" ");

        httpMap.put("httpMethod", httpBasicInfo[0]);
        httpMap.put("path", httpBasicInfo[1]);
        String[] versionLine = httpBasicInfo[2].split("/");
        httpMap.put("version", versionLine[1]);

        for (int j = 1; j < serverReport.size() - 1; j++) {
            String restLine = serverReport.get(j);
            String[] midRest = restLine.split(":", 2);
            if (midRest.length == 1) {
            } else if (StringUtil.isNotBlank(midRest[0]) && StringUtil.isNotBlank(midRest[1])) {
                httpMap.put(midRest[0], midRest[1]);
            }
        }

        //将属性值传入http
        String httpMethodSave = (String) httpMap.get("httpMethod");
        if (StringUtil.isNotBlank(httpMethodSave)) {
            hp.setHttpMethod(HttpMethod.valueOf(httpMethodSave));
        }

        String pathSave = (String) httpMap.get("path");
        if (StringUtil.isNotBlank(pathSave)) {
            hp.setPath(pathSave);
        }

        if (StringUtil.isNotBlank(((String) httpMap.get("version")))) {
            String version = (String) httpMap.get("version");
            switch (version) {
                case "1.0":
                    hp.setHttpVersion(HttpVersion.valueOf("HTTP1"));
                    break;
                case "1.1":
                    hp.setHttpVersion(HttpVersion.valueOf("HTTP1_1"));
                    break;
                case "2.0":
                    hp.setHttpVersion(HttpVersion.valueOf("HTTP2"));
                    break;
            }
        }


        String type = (String) httpMap.get("Content-Type");
        type = type.trim().toLowerCase();//有些http客户端会传带空格和大小写混合的body
        if (StringUtil.isNotBlank(type)) {
            switch (type) {
                case "application/x-www-form-urlencoded":
                    hp.setHttpContentType(HttpContentType.valueOf("APPLICATION_X_WWW_FORM_URLENCODED"));
                    if (StringUtil.isNotBlank(type)) {
                        String[] postContentSave = serverReport.get(serverReport.size() - 1).split("&");
                        Map<String, String> formData = new HashMap<>();
                        for (String postContentSaveContent : postContentSave) {
                            String[] postContentSplit = postContentSaveContent.split("=");
                            formData.put(postContentSplit[0], postContentSplit[1]);
                        }
                        hp.setFormData(formData);
                    }
                    break;
                case "application/json":
                    hp.setHttpContentType(HttpContentType.valueOf("APPLICATION_JSON"));
                    if (StringUtil.isNotBlank(type)) {
                        String[] jsonFirstSplit = receiveMessage.split("\\{", 2);
                        String jsonContent = "{" + jsonFirstSplit[1];
                        hp.setJsonData(jsonContent);
                    }
                    break;
            }
        }
        hp.setHost((String) httpMap.getOrDefault("Host", ""));
        hp.setPragma((String) httpMap.getOrDefault("Pragma", ""));
        hp.setCacheControl((String) httpMap.getOrDefault("Cache-Control", ""));
        hp.setUserAgent((String) httpMap.getOrDefault("User-Agent", ""));
        hp.setSecFetchSite((String) httpMap.getOrDefault("Sec-Fetch-Site", ""));
        hp.setSecFetchMode((String) httpMap.getOrDefault("Sec-Fetch-Mode", ""));
        hp.setReferer((String) httpMap.getOrDefault("Referer", ""));
        hp.setOrigin((String) httpMap.getOrDefault("Origin", ""));


        if (StringUtil.isNotBlank(((String) httpMap.get("Referer")))) {
            String contentLength = (String) httpMap.get("Content-Length");
            hp.setContentLength(Integer.valueOf(contentLength.trim()).intValue());
        }


        //acceptLanguage分割
        if (StringUtil.isNotBlank(((String) httpMap.get("Accept-Language")))) {
            String acceptLanguageMiddle = (String) httpMap.get("Accept-Language");
            List<ContentAndQuality> contentAndQualityAcceptLanguageList = resolve2ContentAndQuality(acceptLanguageMiddle);
            List<AcceptLanguage> acceptLanguageList = new RinaArrayList<>();

            for (ContentAndQuality contentAndQualityAcceptLanguage : contentAndQualityAcceptLanguageList) {
                AcceptLanguage acceptLanguage = new AcceptLanguage();
                acceptLanguage.setContent(contentAndQualityAcceptLanguage.getContent());
                acceptLanguage.setQuality(contentAndQualityAcceptLanguage.getQuality());
                acceptLanguageList.add(acceptLanguage);
            }
            hp.setAcceptLanguage(acceptLanguageList);
        }

        //AcceptEncoding分割
        if (StringUtil.isNotBlank(((String) httpMap.get("Accept-Encoding")))) {
            String acceptEncodingMiddle = (String) httpMap.get("Accept-Encoding");
            List<ContentAndQuality> contentAndQualityAcceptEncodingList = resolve2ContentAndQuality(acceptEncodingMiddle);
            ;
            List<AcceptEncoding> acceptEncodingList = new RinaArrayList<>();

            for (ContentAndQuality contentAndQualityAcceptEncoding : contentAndQualityAcceptEncodingList) {
                AcceptEncoding acceptEncoding = new AcceptEncoding();
                acceptEncoding.setContent(contentAndQualityAcceptEncoding.getContent());
                acceptEncoding.setQuality(contentAndQualityAcceptEncoding.getQuality());
                acceptEncodingList.add(acceptEncoding);
            }
            hp.setAcceptEncoding(acceptEncodingList);
        }

        //accept分割
        if (StringUtil.isNotBlank(((String) httpMap.get("Accept")))) {
            String acceptMiddle = (String) httpMap.get("Accept");
            List<ContentAndQuality> contentAndQualityAcceptList = resolve2ContentAndQuality(acceptMiddle);
            List<Accept> acceptList = new RinaArrayList<>();

            for (ContentAndQuality contentAndQualityAccept : contentAndQualityAcceptList) {
                Accept accept = new Accept();
                accept.setContent(contentAndQualityAccept.getContent());
                accept.setQuality(contentAndQualityAccept.getQuality());
                acceptList.add(accept);
            }
            hp.setAccept(acceptList);
        }
        return hp;
    }

    private List<ContentAndQuality> resolve2ContentAndQuality(String httpHeaderValue) {
        List<String> contentAndQualityItemList = Arrays.asList(httpHeaderValue.split(","));
        return contentAndQualityItemList
                .stream()
                .map(contentAndQualityItem -> {
                    List<String> contentAndQualityList = Arrays.asList(contentAndQualityItem.split(";"));
                    ContentAndQuality contentAndQuality = new ContentAndQuality();
                    contentAndQuality.setContent(contentAndQualityList.get(0).trim());
                    if (contentAndQualityList.size() > 1 && contentAndQualityList.get(1).startsWith("q=")) {
                        contentAndQuality.setQuality(Double.parseDouble(contentAndQualityList.get(1).trim().substring(2)));
                    }
                    return contentAndQuality;
                })
                .collect(Collectors.toList());
    }
}


