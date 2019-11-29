package top.starrysea.rina.core.connection;

import lombok.Data;
import top.starrysea.rina.core.connection.entity.enums.HttpContentType;
import top.starrysea.rina.core.connection.entity.enums.HttpStatus;

@Data
public class HttpResponse {

    private HttpStatus httpStatus;
    private String cacheControl;
    private String location;
    private String server;
    private String connection;
    private String responseContent;
    private HttpContentType httpContentType;



    public StringBuilder resolve2String() {
        StringBuilder sendMsg = new StringBuilder();
        sendMsg.append("HTTP/1.1" + this.httpStatus.toString() +"\r\n")
               .append("cache-control:" +this.cacheControl+ "\r\n")
                .append("Content-Type:" +this.httpContentType +"\r\n")
                .append("\r\n")
                .append("<!DOCTYPE html><html lang=\"zh-cn\">")
                .append("<head><meta charset=\"utf-8\"/><title>HttpServer</title></head>")
                .append("<body><h3>服务端接收到的请求报文</h3>");


               sendMsg.append("</body>")
                      .append(this.responseContent);

                return sendMsg;

    }
}