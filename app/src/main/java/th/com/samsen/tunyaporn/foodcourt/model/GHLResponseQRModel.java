package th.com.samsen.tunyaporn.foodcourt.model;

public class GHLResponseQRModel {

    private xml xml;

    public GHLResponseQRModel.xml getXml() {
        return xml;
    }

    public void setXml(GHLResponseQRModel.xml xml) {
        this.xml = xml;
    }

    public static class xml {
        private String response_msg;
        private String response_code;
        private String pos_ref_no;
        private String transaction_id;
        private String invoice_no;
        private String amount;
        private String card_approval_code;
        private String trade_type;
        private String card_no;

        public String getCard_approval_code() {
            return card_approval_code;
        }

        public void setCard_approval_code(String card_approval_code) {
            this.card_approval_code = card_approval_code;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getCard_no() {
            return card_no;
        }

        public void setCard_no(String card_no) {
            this.card_no = card_no;
        }

        public String getResponse_msg() {
            return response_msg;
        }

        public void setResponse_msg(String response_msg) {
            this.response_msg = response_msg;
        }

        public String getResponse_code() {
            return response_code;
        }

        public void setResponse_code(String response_code) {
            this.response_code = response_code;
        }

        public String getPos_ref_no() {
            return pos_ref_no;
        }

        public void setPos_ref_no(String pos_ref_no) {
            this.pos_ref_no = pos_ref_no;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getInvoice_no() {
            return invoice_no;
        }

        public void setInvoice_no(String invoice_no) {
            this.invoice_no = invoice_no;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }

}
