package top.starrysea.rina.core.connection.entity;


import lombok.Data;

@Data
public class HttpContent {
    private String path;
    private String host;
    private String connection;
    private String pragma;
    private String cacheControl;
    private String userAgent;
    private String acceptMiddle;
    private String secFetchSite;
    private String secFetchMode;
    private String referer;
    private String acceptEncodingMiddle;
    private String acceptLanguageMiddle;
}
