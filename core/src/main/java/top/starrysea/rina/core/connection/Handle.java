package top.starrysea.rina.core.connection;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.thread.ThreadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class Handle {


    public Http handleRun(List<String> serverReport) {

    //第一次分割
        Http hp = new Http();
        Map<String, String> httpMap = new HashMap<>();
        String firstLine = serverReport.get(0);
        String[] midFirst = firstLine.split("/");
        if (midFirst==null){
            return null;
        }else {

            httpMap.put("httpMethod", midFirst[0]);
            httpMap.put("path", midFirst[1]);
            httpMap.put("version", midFirst[2]);

            for (int j = 1; j < serverReport.size(); j++) {
                String restLine = serverReport.get(j);
                String[] midRest = restLine.split(":", 2);
                httpMap.put(midRest[0], midRest[1]);
            }

            //将属性值传入http
            hp.setHttpMethod(httpMap.get("httpMethod"));
            hp.setPath(httpMap.get("path"));
            hp.setVersion(httpMap.get("version"));
            hp.setHost(httpMap.get("Host"));
            hp.setPragma(httpMap.get("Pragma"));
            hp.setCacheControl(httpMap.get("Cache-Control"));
            hp.setUserAgent(httpMap.get("User-Agent"));
            hp.setAcceptMiddle(httpMap.get("Accept"));
            hp.setSecFetchSite(httpMap.get("Sec-Fetch-Site"));
            hp.setSecFetchMode(httpMap.get("Sec-Fetch-Mode"));
            hp.setReferer(httpMap.get("Referer"));
            hp.setAcceptEncodingMiddle(httpMap.get("Accept-Encoding"));
            hp.setAcceptLanguageMiddle(httpMap.get("Accept-Language"));


            //acceptLanguage分割
            String[] splitLanguageContentFirst = hp.getAcceptLanguageMiddle().split(",");
            List<AcceptLanguage> acceptLanguageList = new ArrayList();
            for (String acceptLanguageFirst : splitLanguageContentFirst) {
                boolean hasSeal = acceptLanguageFirst.contains(";");
                AcceptLanguage acceptLanguage = new AcceptLanguage();
                if (hasSeal) {
                    String[] splitLanguageContentSecond = acceptLanguageFirst.split(";");
                    String[] splitLanguageContentThird = splitLanguageContentSecond[1].split("=");
                    double qContent = Double.parseDouble(splitLanguageContentThird[1]);
                    acceptLanguage.setAcceptLanguage(splitLanguageContentSecond[0]);
                    acceptLanguage.setQ(qContent);
                } else {
                    acceptLanguage.setAcceptLanguage(acceptLanguageFirst);
                    acceptLanguage.setQ(1.0);
                }
                acceptLanguageList.add(acceptLanguage);
            }
            hp.setAcceptLanguage(acceptLanguageList);


            //AcceptEncoding分割
            String[] splitAcceptEncoding = hp.getAcceptEncodingMiddle().split(",");
            for (int k = 0; k < splitAcceptEncoding.length; k++) {
                hp.getAcceptEncoding().add(splitAcceptEncoding[k]);
            }


            //accept分割
            String splitAcceptContentFirst[] = hp.getAcceptMiddle().split(",");
            List<Accept> acceptListNew = new ArrayList();
            for (String acceptFirst : splitAcceptContentFirst) {
                Accept acceptContent = new Accept();
                boolean hasSeal = acceptFirst.contains(";");
                if (hasSeal) {
                    String[] splitAcceptContentSecond = acceptFirst.split(";");
                    String[] splitAcceptContentThird = splitAcceptContentSecond[1].split("=");
                    boolean hasV = splitAcceptContentSecond[1].contains("v");
                    if (hasV) {
                        acceptContent.setV(splitAcceptContentThird[1]);
                        acceptContent.setAcceptContent(splitAcceptContentSecond[0]);
                    } else {
                        double qContent = Double.parseDouble(splitAcceptContentThird[1]);
                        acceptContent.setAcceptContent(splitAcceptContentSecond[0]);
                        acceptContent.setQ(qContent);
                    }
                } else {
                    acceptContent.setAcceptContent(acceptFirst);
                    acceptContent.setQ(1.0);
                }
                acceptListNew.add(acceptContent);
            }
            hp.setAccept(acceptListNew);
            return hp;
        }


 }

}
