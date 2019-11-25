package top.starrysea.rina.core.connection.entity;


import lombok.Data;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.util.collection.RinaArrayList;

import java.util.ArrayList;
import java.util.List;

@Data
public class HttpContent {

    private HttpMethod httpMethod;
    private String path;
    private String version;
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
