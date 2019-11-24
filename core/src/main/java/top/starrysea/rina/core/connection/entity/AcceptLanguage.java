package top.starrysea.rina.core.connection.entity;

import lombok.Data;

@Data
public class AcceptLanguage {
    private Double q;
    private String acceptLanguage;

    public void setQ(Double q) {
        this.q = q;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }


    public String toString() {
        return "acceptLanguage{" +
                "q=" + q +
                ", accept='" + acceptLanguage + '\'' +
                '}';
    }



}



