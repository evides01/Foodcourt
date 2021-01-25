package th.com.samsen.tunyaporn.foodcourt.model;

public class TrueInquiryRequest {

    private String isvPaymentRef;
    private String transactionDate;

    public String getISVPaymentRef() { return isvPaymentRef; }
    public void setISVPaymentRef(String value) { this.isvPaymentRef = value; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String value) { this.transactionDate = value; }
}
