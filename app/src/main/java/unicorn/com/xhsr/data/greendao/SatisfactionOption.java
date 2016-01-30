package unicorn.com.xhsr.data.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SATISFACTION_OPTION".
 */
public class SatisfactionOption implements java.io.Serializable {

    /** Not-null value. */
    private String objectId;
    /** Not-null value. */
    private String title;
    /** Not-null value. */
    private String content;
    private int numerator;
    private int denominator;
    private int orderNo;
    private int score;

    public SatisfactionOption() {
    }

    public SatisfactionOption(String objectId) {
        this.objectId = objectId;
    }

    public SatisfactionOption(String objectId, String title, String content, int numerator, int denominator, int orderNo, int score) {
        this.objectId = objectId;
        this.title = title;
        this.content = content;
        this.numerator = numerator;
        this.denominator = denominator;
        this.orderNo = orderNo;
        this.score = score;
    }

    /** Not-null value. */
    public String getObjectId() {
        return objectId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Not-null value. */
    public String getContent() {
        return content;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContent(String content) {
        this.content = content;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
