package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.connection.entity.*;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.connection.entity.enums.HttpVersion;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.string.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RinaObject
public class HttpMessageResolver {


    public HttpContent handleRun(List<String> serverReport) {


        //第一次分割
        HttpContent hp = new HttpContent();
        Map<String, Object> httpMap = new HashMap<>();
        String firstLine = serverReport.get(0);
        String[] httpBasicInfo = firstLine.split(" ");

        httpMap.put("httpMethod", httpBasicInfo[0]);
        httpMap.put("path", httpBasicInfo[1]);
        String[] versionLine = httpBasicInfo[2].split("/");
        httpMap.put("version", versionLine[1]);

        for (int j = 1; j < serverReport.size(); j++) {
            String restLine = serverReport.get(j);
            String[] midRest = restLine.split(":", 2);
            httpMap.put(midRest[0], midRest[1]);
        }

        //将属性值传入http
        hp.setHttpMethod(HttpMethod.valueOf((String) httpMap.get("httpMethod")));
        hp.setPath(((String) httpMap.get("path")));

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
        hp.setHost((String) httpMap.get("Host"));
        hp.setPragma((String) httpMap.get("Pragma"));
        hp.setCacheControl((String) httpMap.get("Cache-Control"));
        hp.setUserAgent((String) httpMap.get("User-Agent"));
        String acceptMiddle = (String) httpMap.get("Accept");
        hp.setSecFetchSite((String) httpMap.get("Sec-Fetch-Site"));
        hp.setSecFetchMode((String) httpMap.get("Sec-Fetch-Mode"));
        hp.setReferer((String) httpMap.get("Referer"));
        String acceptEncodingMiddle = (String) httpMap.get("Accept-Encoding");
        String acceptLanguageMiddle = (String) httpMap.get("Accept-Language");

        //acceptLanguage分割
        if (StringUtil.isNotBlank(acceptLanguageMiddle)) {
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
        if (StringUtil.isNotBlank(acceptEncodingMiddle)) {
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
        if (StringUtil.isNotBlank(acceptMiddle)) {
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


