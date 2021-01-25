package th.com.samsen.tunyaporn.foodcourt.model;

public class TruePaymentResponse {

    private Status status;
    private Data data;

    public class Column {
        public static final String status = "status";
        public static final String data = "data";
    }


    public TruePaymentResponse() {
    }

    public TruePaymentResponse(Status status, Data data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Status{
        private String code;
        private String message;

        public class Column{
            private static final String code  = "code";
            public static final String message = "message";
        }

        public Status() {
        }

        public Status(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Data{
        private String payment_id;

        public class Column{
            public static final String payment_id = "payment_id";
        }

        public Data() {
        }

        public Data(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }
    }


    }




