package top.starrysea.rina.core.connection.entity;


import lombok.Data;
import top.starrysea.rina.core.connection.entity.enums.HttpContentType;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.connection.entity.enums.HttpVersion;

import java.util.List;

@Data
public class HttpContent {
    private HttpMethod httpMethod;
    private HttpVersion httpVersion;
    private String path;
    private String host;
    private String connection;
    private String pragma;
    private String cacheControl;
    private String userAgent;
    private String secFetchSite;
    private String secFetchMode;
    private String referer;
    private List<Accept> accept;
    private List<AcceptLanguage> acceptLanguage;
    private List<AcceptEncoding> acceptEncoding;
    private HttpContentType httpContentType;
    private List<PostContent> postContent;
    private int contentLength;
    private String origin;
    private String postmanToken;

}
