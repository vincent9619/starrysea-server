package top.starrysea.rina.core.connection;

public class AcceptClassWhichBelongToHttpClass {
    private Double q;
    private String acceptContent;
    private String v;


    public void setQ(Double q) {
        this.q = q;
    }

    public void setAcceptContent(String acceptContent) {
        this.acceptContent = acceptContent;
    }

    public void setV(String v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "accept{" +
                "q=" + q +
                ", acceptContent='" + acceptContent + '\'' +
                '}';
    }

    public Double getQ() {
        return q;
    }

    public String getAcceptContent() {
        return acceptContent;
    }


}

