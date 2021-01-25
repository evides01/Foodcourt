package th.com.samsen.tunyaporn.foodcourt.model;

public class DolfinInquiryRequestModel {
    String merchantOrderNo;
    String merchantNo;

    public DolfinInquiryRequestModel(String merchantOrderNo, String merchantNo) {
        this.merchantOrderNo = merchantOrderNo;
        this.merchantNo = merchantNo;
    }

    public DolfinInquiryRequestModel() {
    }

    public class Column{
        public static final String merchantOrderNo = "merchantOrderNo";
        public static final String merchantNo = "merchantNo";
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
}
