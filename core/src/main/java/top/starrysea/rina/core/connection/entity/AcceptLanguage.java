package top.starrysea.rina.core.connection.entity;

import lombok.Data;

@Data
public class AcceptLanguage {
    private Double quality;
    private String content;


    public String toString() {
        return "acceptLanguage{" +
                "q=" + quality +
                ", accept='" + content + '\'' +
                '}';
    }


}



