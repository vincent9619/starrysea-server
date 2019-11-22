package top.starrysea.rina.core.connection;

public class AcceptLanguage {
    private Double q;
    private String acceptLanguage;

    public void setQ(Double q) {
        this.q = q;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    @Override
    public String toString() {
        return "acceptLanguage{" +
                "q=" + q +
                ", accept='" + acceptLanguage + '\'' +
                '}';
    }

    public Double getQ() {
        return q;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

}



