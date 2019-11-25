package top.starrysea.rina.core.connection.entity;

import lombok.Data;

@Data
public class Accept {
    private Double quality;
    private String content;


    public String toString() {
        return "accept{" +
                "q=" + quality +
                ", acceptContent='" + content + '\'' +
                '}';
    }
}

