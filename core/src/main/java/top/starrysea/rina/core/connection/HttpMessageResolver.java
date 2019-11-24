package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.string.StringUtil;

import java.util.*;

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
        httpMap.put("version", httpBasicInfo[2]);

        for (int j = 1; j < serverReport.size(); j++) {
            String restLine = serverReport.get(j);
            String[] midRest = restLine.split(":", 2);
            httpMap.put(midRest[0], midRest[1]);
        }

        //将属性值传入http
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
        } else {
            String[] splitLanguageContentFirst = hp.getAcceptLanguageMiddle().split(",");
            List<AcceptLanguage> acceptLanguageList = new RinaArrayList();
            for (String acceptLanguageFirst : splitLanguageContentFirst) {
                AcceptLanguage acceptLanguage = new AcceptLanguage();
                String[] splitLanguageContentSecond = acceptLanguageFirst.split(";");
                if (splitLanguageContentSecond.length <= 1) {
                    acceptLanguage.setAcceptLanguage(acceptLanguageFirst);
                    acceptLanguage.setQ(1.0);
                } else {
                    String[] splitLanguageContentThird = splitLanguageContentSecond[1].split("=");
                    double qContent = Double.parseDouble(splitLanguageContentThird[1]);
                    acceptLanguage.setAcceptLanguage(splitLanguageContentSecond[0]);
                    acceptLanguage.setQ(qContent);
                }
                acceptLanguageList.add(acceptLanguage);
            }
            hp.setAcceptLanguages(acceptLanguageList);
        }


        //AcceptEncoding分割
        if (StringUtil.isBlank(hp.getAcceptEncodingMiddle())) {
            return null;
        } else {
            List<String> splitAcceptEncoding = Arrays.asList(hp.getAcceptEncodingMiddle().split(","));
            for (int k = 0; k <= splitAcceptEncoding.size() - 1; k++) {
                hp.setAcceptEncoding(Collections.singletonList(splitAcceptEncoding.get(k)));
            }
        }

        //accept分割
        if (StringUtil.isBlank(hp.getAcceptMiddle())) {
            return null;
        } else {
            String splitAcceptContentFirst[] = hp.getAcceptMiddle().split(",");
            List<Accept> acceptListNew = new RinaArrayList();
            for (String acceptFirst : splitAcceptContentFirst) {
                Accept acceptContent = new Accept();
                String[] splitAcceptContentSecond = acceptFirst.split(";");
                if (splitAcceptContentSecond.length <= 1) {
                    acceptContent.setAcceptContent(acceptFirst);
                    acceptContent.setQ(1.0);
                } else {
                    String[] splitAcceptContentThird = splitAcceptContentSecond[1].split("=");
                    boolean hasVersion = splitAcceptContentSecond[1].contains("v");
                    if (hasVersion) {
                        acceptContent.setV(splitAcceptContentThird[1]);
                        acceptContent.setAcceptContent(splitAcceptContentSecond[0]);
                    } else {
                        double qContent = Double.parseDouble(splitAcceptContentThird[1]);
                        acceptContent.setAcceptContent(splitAcceptContentSecond[0]);
                        acceptContent.setQ(qContent);
                    }
                }
                acceptListNew.add(acceptContent);
            }
            hp.setAccepts(acceptListNew);
        }
        return hp;
    }
}


