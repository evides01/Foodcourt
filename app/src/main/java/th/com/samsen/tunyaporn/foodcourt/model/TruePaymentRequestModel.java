package th.com.samsen.tunyaporn.foodcourt.model;

public class TruePaymentRequestModel {

    private String isv_payment_ref;
    private String merchant_id;
    private String currency;
    private String request_amount;
    private String payment_method;
    private String payment_code;
    private String description;
    private Metadata metadata;

    public class Column{
        public static final String isv_payment_ref = "isv_payment_ref";
        public static final String merchant_id = "merchant_id";
        public static final String currency = "currency";
        public static final String request_amount = "request_amount";
        public static final String payment_method = "payment_method";
        public static final String payment_code = "payment_code";
        public static final String description = "description";
        public static final String metadata = "metadata";
    }

    public TruePaymentRequestModel() {
    }

    public TruePaymentRequestModel(String isv_payment_ref, String merchant_id, String currency, String request_amount, String payment_method, String payment_code, String description, Metadata metadata) {
        this.isv_payment_ref = isv_payment_ref;
        this.merchant_id = merchant_id;
        this.currency = currency;
        this.request_amount = request_amount;
        this.payment_method = payment_method;
        this.payment_code = payment_code;
        this.description = description;
        this.metadata = metadata;
    }

    public String getIsv_payment_ref() {
        return isv_payment_ref;
    }

    public void setIsv_payment_ref(String isv_payment_ref) {
        this.isv_payment_ref = isv_payment_ref;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public static class Metadata{
        private String partner_shop_id;

        private String partner_shop_name;

        private String shop_id;

        public class Column{
            public static final String partner_shop_id = "partner_shop_id";
            public static final String partner_shop_name = "partner_shop_name";
        }

        public Metadata() {
        }

        public Metadata(String partner_shop_id, String partner_shop_name, String shop_id) {
            this.partner_shop_id = partner_shop_id;
            this.partner_shop_name = partner_shop_name;
            this.shop_id = shop_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getPartner_shop_name() {
            return partner_shop_name;
        }

        public void setPartner_shop_name(String partner_shop_name) {
            this.partner_shop_name = partner_shop_name;
        }

        public String getPartner_shop_id() {
            return partner_shop_id;
        }

        public void setPartner_shop_id(String partner_shop_id) {
            this.partner_shop_id = partner_shop_id;
        }
    }

}
