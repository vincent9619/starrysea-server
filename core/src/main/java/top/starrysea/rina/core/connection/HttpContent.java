package top.starrysea.rina.core.connection;


import lombok.Data;
import top.starrysea.rina.util.collection.RinaArrayList;

import java.util.ArrayList;
import java.util.List;

@Data
public class HttpContent {

     enum  httpMethodEnum{
        GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS,TRACE,PATCH,COPY,LINK,UNLINK,WRAPPED, ExtensionMethod
    }
    public static class httpMethod{
        httpMethodEnum GET = httpMethodEnum.GET;
        httpMethodEnum HEAD = httpMethodEnum.HEAD;
        httpMethodEnum POST = httpMethodEnum.POST;
        httpMethodEnum PUT = httpMethodEnum.PUT;
        httpMethodEnum DELETE = httpMethodEnum.DELETE;
        httpMethodEnum CONNECT = httpMethodEnum.CONNECT;
        httpMethodEnum OPTIONS = httpMethodEnum.OPTIONS;
        httpMethodEnum TRACE = httpMethodEnum.TRACE;
        httpMethodEnum PATCH = httpMethodEnum.PATCH;
        httpMethodEnum COPY = httpMethodEnum.COPY;
        httpMethodEnum LINK= httpMethodEnum.LINK;
        httpMethodEnum UNLINK = httpMethodEnum.UNLINK;
        httpMethodEnum WRAPPED = httpMethodEnum.WRAPPED;
        httpMethodEnum ExtensionMethod= httpMethodEnum.ExtensionMethod;
    }
    private String path;
    private String version;
    private String host;
    private String connection;
    private String pragma;
    private String cacheControl;
    private String userAgent;
    private String acceptMiddle;
    private List<Accept> accepts;
    private String secFetchSite;
    private String secFetchMode;
    private String referer;
    private String acceptEncodingMiddle;
    private List<String> acceptEncoding;
    private String acceptLanguageMiddle;
    private List<AcceptLanguage> acceptLanguages;


}
