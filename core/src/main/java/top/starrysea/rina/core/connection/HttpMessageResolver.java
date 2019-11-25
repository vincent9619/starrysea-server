package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.connection.entity.ContentAndQuality;
import top.starrysea.rina.core.connection.entity.HttpContent;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.string.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RinaObject
public class HttpMessageResolver {


    List<ContentAndQuality> contentAndQualityAcceptLanguageList = new RinaArrayList<>();
    List<ContentAndQuality> contentAndQualityAcceptEncodingList = new RinaArrayList<>();
    List<ContentAndQuality> contentAndQualityAcceptList = new RinaArrayList<>();
    HttpMethod httpMethod;


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

        httpMethod = HttpMethod.valueOf((String) httpMap.get("httpMethod"));
        hp.setPath(((String) httpMap.get("path")));
        hp.setVersion((String) httpMap.get("version"));
        hp.setHost((String) httpMap.get("Host"));
        hp.setPragma((String) httpMap.get("Pragma"));
        hp.setCacheControl((String) httpMap.get("Cache-Control"));
        hp.setUserAgent((String) httpMap.get("User-Agent"));
        hp.setAcceptMiddle((String) httpMap.get("Accept"));
        hp.setSecFetchSite((String) httpMap.get("Sec-Fetch-Site"));
        hp.setSecFetchMode((String) httpMap.get("Sec-Fetch-Mode"));
        hp.setReferer((String) httpMap.get("Referer"));
        hp.setAcceptEncodingMiddle((String) httpMap.get("Accept-Encoding"));
        hp.setAcceptLanguageMiddle((String) httpMap.get("Accept-Language"));


        //acceptLanguage分割
        if (StringUtil.isBlank(hp.getAcceptLanguageMiddle())) {
            return null;
        }
        contentAndQualityAcceptLanguageList = resolve2ContentAndQuality(hp.getAcceptLanguageMiddle());


        //AcceptEncoding分割
        if (StringUtil.isBlank(hp.getAcceptEncodingMiddle())) {
            return null;
        }
        contentAndQualityAcceptEncodingList = resolve2ContentAndQuality(hp.getAcceptEncodingMiddle());


        //accept分割
        if (StringUtil.isBlank(hp.getAcceptMiddle())) {
            return null;
        }
        contentAndQualityAcceptList = resolve2ContentAndQuality(hp.getAcceptMiddle());
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

