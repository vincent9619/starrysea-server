package top.starrysea.rina.core.connection;


import top.starrysea.rina.util.collection.RinaArrayList;

import java.util.ArrayList;
import java.util.List;

public class Http {
    private String httpMethod ;
    private String path;
    private String version ;
    private String Host ;
    private String connection;
    private String Pragma;
    private String cacheControl;
    private String userAgent;
    private String acceptMiddle;
    private List<Accept> accept = new RinaArrayList<>();
    private String secFetchSite;
    private String secFetchMode;
    private String referer;
    private String acceptEncodingMiddle;
    private List<String> acceptEncoding = new ArrayList();
    private String acceptLanguageMiddle;
    private List<AcceptLanguage> acceptLanguage = new ArrayList();

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getPragma() {
        return Pragma;
    }

    public void setPragma(String pragma) {
        Pragma = pragma;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAcceptMiddle() {
        return acceptMiddle;
    }

    public void setAcceptMiddle(String acceptMiddle) {
        this.acceptMiddle = acceptMiddle;
    }

    public List<Accept> getAccept() {
        return accept;
    }

    public void setAccept(List<Accept> accept) {
        this.accept = accept;
    }

    public String getSecFetchSite() {
        return secFetchSite;
    }

    public void setSecFetchSite(String secFetchSite) {
        this.secFetchSite = secFetchSite;
    }

    public String getSecFetchMode() {
        return secFetchMode;
    }

    public void setSecFetchMode(String secFetchMode) {
        this.secFetchMode = secFetchMode;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getAcceptEncodingMiddle() {
        return acceptEncodingMiddle;
    }

    public void setAcceptEncodingMiddle(String acceptEncodingMiddle) {
        this.acceptEncodingMiddle = acceptEncodingMiddle;
    }

    public List<String> getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(List<String> acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguageMiddle() {
        return acceptLanguageMiddle;
    }

    public void setAcceptLanguageMiddle(String acceptLanguageMiddle) {
        this.acceptLanguageMiddle = acceptLanguageMiddle;
    }

    public List<AcceptLanguage> getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(List<AcceptLanguage> acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }
}
