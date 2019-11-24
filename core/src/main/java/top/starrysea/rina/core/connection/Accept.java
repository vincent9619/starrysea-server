package top.starrysea.rina.core.connection;

import lombok.Data;

@Data
public class Accept {
    private Double q;
    private String acceptContent;
    private String v;


    public String toString() {
        return "accept{" +
                "q=" + q +
                ", acceptContent='" + acceptContent + '\'' +
                '}';
    }


}

