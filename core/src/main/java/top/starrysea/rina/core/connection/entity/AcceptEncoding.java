package top.starrysea.rina.core.connection.entity;

import lombok.Data;

@Data
public class AcceptEncoding {
    private Double quality;
    private String content;

    public String toString() {
        return "AcceptEncoding{" +
                "q=" + quality +
                ", acceptEncodingContent='" + content + '\'' +
                '}';
    }
}
