package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.connection.entity.ContentAndQuality;
import top.starrysea.rina.core.connection.entity.HttpContent;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.connection.entity.enums.HttpVersion;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.string.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RinaObject
public class HttpMessageResolver {
    private List<ContentAndQuality> contentAndQualityAcceptLanguageList = new RinaArrayList<>();
    private List<ContentAndQuality> contentAndQualityAcceptEncodingList = new RinaArrayList<>();
    private List<ContentAndQuality> contentAndQualityAcceptList = new RinaArrayList<>();
    private HttpMethod httpMethod;
    private HttpVersion httpVersion;


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
        String version = (String) httpMap.get("version");
        switch (version) {
            case "1.0":
                String versionString1 = "one_zero";
                httpVersion = HttpVersion.valueOf(versionString1);
                break;
            case "1.1":
                String versionString2 = "one_one";
                httpVersion = HttpVersion.valueOf(versionString2);
                break;
            case "2.0":
                String versionString3 = "two_zero";
                httpVersion = HttpVersion.valueOf(versionString3);
                break;
        }
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

