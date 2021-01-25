package th.com.samsen.tunyaporn.foodcourt.model;

public class TrueInquiryResponse {


    private Status status;
    private Data data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status value) {
        this.status = value;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data value) {
        this.data = value;
    }

    public static class Data {
        private String paymentID;
        private String isvPaymentRef;
        private String isv;
        private String merchant;
        private String amount;
        private String currency;
        private String customer;
        private String status;
        private String responseCode;
        private String responseMessage;
        private String refunded;
        private String created;
        private String updated;
        private Metadata metadata;
        private Object[] refunds;

        public String getPaymentID() {
            return paymentID;
        }

        public void setPaymentID(String value) {
            this.paymentID = value;
        }

        public String getISVPaymentRef() {
            return isvPaymentRef;
        }

        public void setISVPaymentRef(String value) {
            this.isvPaymentRef = value;
        }

        public String getISV() {
            return isv;
        }

        public void setISV(String value) {
            this.isv = value;
        }

        public String getMerchant() {
            return merchant;
        }

        public void setMerchant(String value) {
            this.merchant = value;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String value) {
            this.amount = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String value) {
            this.currency = value;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String value) {
            this.customer = value;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String value) {
            this.status = value;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String value) {
            this.responseCode = value;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(String value) {
            this.responseMessage = value;
        }

        public String getRefunded() {
            return refunded;
        }

        public void setRefunded(String value) {
            this.refunded = value;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String value) {
            this.created = value;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String value) {
            this.updated = value;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata value) {
            this.metadata = value;
        }

        public Object[] getRefunds() {
            return refunds;
        }

        public void setRefunds(Object[] value) {
            this.refunds = value;
        }
    }


    public class Metadata {
        private String description;
        private String userMobile;
        private String shopID;
        private String clientID;
        private String partnerShopID;
        private String orionChannel;
        private String terminalID;
        private String paymentCode;

        public String getDescription() {
            return description;
        }

        public void setDescription(String value) {
            this.description = value;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String value) {
            this.userMobile = value;
        }

        public String getShopID() {
            return shopID;
        }

        public void setShopID(String value) {
            this.shopID = value;
        }

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String value) {
            this.clientID = value;
        }

        public String getPartnerShopID() {
            return partnerShopID;
        }

        public void setPartnerShopID(String value) {
            this.partnerShopID = value;
        }

        public String getOrionChannel() {
            return orionChannel;
        }

        public void setOrionChannel(String value) {
            this.orionChannel = value;
        }

        public String getTerminalID() {
            return terminalID;
        }

        public void setTerminalID(String value) {
            this.terminalID = value;
        }

        public String getPaymentCode() {
            return paymentCode;
        }

        public void setPaymentCode(String value) {
            this.paymentCode = value;
        }
    }

    public class Status {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String value) {
            this.code = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String value) {
            this.message = value;
        }
    }

}
