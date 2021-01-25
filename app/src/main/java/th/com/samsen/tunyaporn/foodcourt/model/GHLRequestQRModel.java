package th.com.samsen.tunyaporn.foodcourt.model;

public class GHLRequestQRModel {

    xml xml;

    public GHLRequestQRModel.xml getXml() {
        return xml;
    }

    public void setXml(GHLRequestQRModel.xml xml) {
        this.xml = xml;
    }

    public static class xml {
        double amount;
        String trade_type;
        String transaction_type;
        String pos_ref_no;
        String service_type;


        public String getXML() {

            String s = "<xml><amount>" + amount + "</amount><trade_type>" + trade_type + "</trade_type><transaction_type>" + transaction_type + "</transaction_type><pos_ref_no>" + pos_ref_no + "</pos_ref_no><service_type>" + service_type + "</service_type></xml>";

            return s;

        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getTransaction_type() {
            return transaction_type;
        }

        public void setTransaction_type(String transaction_type) {
            this.transaction_type = transaction_type;
        }

        public String getPos_ref_no() {
            return pos_ref_no;
        }

        public void setPos_ref_no(String pos_ref_no) {
            this.pos_ref_no = pos_ref_no;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }
    }


}
