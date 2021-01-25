package th.com.samsen.tunyaporn.foodcourt.utility;

public class ModuleQrPayment {



//    class SurroundingClass {
//
//        public final void Process_Globals() {
//
//        }
//
//        public final void Globals() {
//            //  These global variables will be redeclared each time the activity is created.
//            //  These variables can only be accessed from this module.
//            ByteConverter bc;
//            Bitmap QRbitmap;
//            String QRstr;
//            PrinterManager Prn;
//            Beeper Beeper1;
//            QRCode QR;
//            //  Private PaymentURLUAT As String = "https://ewalletuat.digio.co.th/payment/v1/orders"        '===> Step 1 Normal URL
//            String PaymentURL = "https://ewallet.digio.co.th/payment/v1/orders";
//            //  ===> Step 1 Normal URL
//            String PaymentURL64;
//            //  ===> Step 1 URL base64
//            String BodyJson;
//            //  ===> Step 2 Normal Body Json
//            String BodyJson64;
//            //  ===> Step 2 Body Json base64
//            String URL_BodyJson;
//            //  ===> Step 3 URL + Body Json Base64
//            String URL_BodyJson_SHA256;
//            //  ===> Step 4 Encode 3 with HMACSHA256
//            String HeaderOut;
//            //  ===> Final step APPID + 4
//            //  Private InquiryURLUAT As String = "https://ewalletuat.digio.co.th/payment/v1/payments/inquiry"        '===> Step 1 Normal URL
//            String InquiryURL = "https://ewallet.digio.co.th/payment/v1/payments/inquiry";
//            //  ===> Step 1 Normal URL
//            String InquiryURL64;
//            //  ===> Step 1 URL base64
//            String TQRurl = "https://api.sahatarasst.com:8084/api/request";
//            //  Test Environment
//            //  Private AppID As String = "asjdhioq2312dksaaew"
//            //  Private AppSecret As String = "3128973sdhjdasjkje21edsa21douhjk"
//            String AppID;
//            String AppSecret;
//            HttpClient hc;
//            OkHttpClient hc1;
//            Int TaskRun;
//            Int NONQRY;
//            Int QRY;
//            Int Jobno;
//            String Id = "";
//            String Merchant_Id = "";
//            String Sub_Merchant_Id = "";
//            String amount = "";
//            String currency = "";
//            String partner_transaction_id = "";
//            String customer_id = "";
//            String terminal_id = "";
//            String created = "";
//            String status = "";
//            String metadata = "";
//            String redirect_url = "";
//            String notify_url = "";
//            String funding_source = "";
//            String qr_code = "";
//            String meta_data = "";
//            String partnerTxnUidRet = "";
//            String AccountName = "";
//            String TID = "";
//            String KMID = "";
//            String TQRpartnerid;
//            String TQRpartnersecret;
//            String TQRmerchat;
//            String TQRtid;
//            Int TQrunning;
//            String TQrunningStr;
//            String TxnUid;
//            String TQRcheckUrl;
//            ImageView ImageViewQr;
//            Button BtnPrint;
//            Button BtnPass;
//            Button BtnFail;
//            Button BtnCancel;
//            Label LblPay;
//            ImageView ImageViewlogo;
//            Button BtnCheck;
//            ListView ListViewItem;
//            boolean BtnCancelPress = false;
//            ImageView ImageViewpromt;
//            ImageView ImageViewBottom;
//        }
//
//        public final void Activity_Create(boolean FirstTime) {
//            //  Do not forget to load the layout file created with the visual designer. For example:
//            //  Activity.LoadLayout("QRpreview")
//            //  Dim QR As QRCode
//            Activity.LoadLayout("QRpreview");
//            if ((hc.IsInitialized == false)) {
//                hc.Initialize("hc");
//            }
//
//            hc.InitializeAcceptAll("hc");
//            hc1.InitializeAcceptAll("hc1");
//            QR.initialize();
//            ListViewItem.Clear();
//            ListViewItem.SingleLineLayout.Label.TextSize = 10;
//            ListViewItem.SingleLineLayout.Label.TextColor = Colors.White;
//            ListViewItem.SingleLineLayout.ItemHeight = 30;
//            M;
//            for (var i = 0; (i
//                    <= (Main.ShowList.Size - 1)); i++) {
//                ListViewItem.AddSingleLine(Main.ShowList.Get(i));
//            }
//
//            ListViewItem.Invalidate();
//            BtnCancelPress = false;
//            LblPay.Text = NumberFormat((CardPopup.AmountPay / ((double)(100))), 0, 2);
//            if ((Main.CardProvider == "alipay")) {
//                ImageViewlogo.Bitmap = LoadBitmap(File.DirAssets, "alipay1.png");
//                ImageViewlogo.Gravity = Gravity.FILL;
//                ImageViewlogo.Invalidate();
//                ImageViewBottom.Visible = false;
//                ImageViewpromt.Visible = false;
//                AppID = Main.myAlipayAppId;
//                AppSecret = Main.myAlipayAppSecret;
//                TID = Main.myAlipayTid;
//                KMID = Main.myAlipayMerchantId;
//            }
//            else if ((Main.CardProvider == "wechat")) {
//                ImageViewlogo.Bitmap = LoadBitmap(File.DirAssets, "wechat.png");
//                ImageViewlogo.Gravity = Gravity.FILL;
//                ImageViewlogo.Invalidate();
//                ImageViewBottom.Visible = false;
//                ImageViewpromt.Visible = false;
//                AppID = Main.myWeChatAppId;
//                AppSecret = Main.myWeChatAppSecret;
//                TID = Main.myWeChatTid;
//                KMID = Main.myWeChatMerchantId;
//            }
//            else if ((Main.CardProvider == "Kplus")) {
//
//                ImageViewlogo.Bitmap = LoadBitmap(File.DirAssets, "cat.png");
//                ImageViewlogo.Gravity = Gravity.FILL;
//                ImageViewlogo.Invalidate();
//                ImageViewBottom.Visible = true;
//                ImageViewpromt.Visible = true;
//                TQRmerchat = Main.myTQRMerchantID;
//                TQRpartnerid = Main.myTQRPartnerId;
//                TQRpartnersecret = Main.myTQRPartnerSecret;
//                TQRtid = Main.myShopId;
//            }
//
//            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//            ImageViewQr.Invalidate();
//            if (((Main.CardProvider == "alipay")
//                    | (Main.CardProvider == "wechat"))) {
//                this.PaymentCreate();
//            }
//            else {
//                this.TQRpayment();
//            }
//
//        }
//
//        public final void TQRpayment() {
//            Map BodyMap;
//            String ReqDateTime = "";
//            String metadata = "";
//            Int i;
//            TxnUid = (Main.myShopId.Trim + TQrunningStr);
//            Log(("TxnUid=" + TxnUid));
//            BodyMap.Initialize();
//            BodyMap.Clear();
//            if ((TxnUid.Length > 15)) {
//                TxnUid = TxnUid.SubString((TxnUid.Length - 15));
//            }
//
//            while ((TxnUid.Length < 15)) {
//                TxnUid = ("0" + TxnUid);
//            }
//
//            //  BodyMap.Put("partnerTxnUid","QRH001030118005")
//            TQrunning = TQrunningStr;
//            TQrunning = (TQrunning + 1);
//            if ((TQrunning >= 100000)) {
//                TQrunning = 1;
//            }
//
//            TQrunningStr = TQrunning;
//            if ((TQrunningStr.Trim.Length < 5)) {
//                while ((TQrunningStr.Trim.Length < 5)) {
//                    TQrunningStr = ("0" + TQrunningStr);
//                }
//
//            }
//
//            Tools.WriteIni("TQrunning", TQrunningStr, File.DirInternal, "Config.ini");
//            BodyMap.Put("partnerTxnUid", TxnUid);
//            BodyMap.Put("partnerId", TQRpartnerid);
//            BodyMap.Put("partnerSecret", TQRpartnersecret);
//            DateTime.DateFormat = "yyyy-MM-dd\'T\'HH:mm:ss";
//
//            DateTime.DateFormat = "dd/MM/yyyy";
//            BodyMap.Put("requestDt", ReqDateTime);
//            BodyMap.Put("merchantId", TQRmerchat);
//            BodyMap.Put("terminalId", Main.myShopId.Trim);
//            BodyMap.Put("qrType", "3");
//            BodyMap.Put("txnAmount", LblPay.Text);
//            BodyMap.Put("txnCurrencyCode", "THB");
//            BodyMap.Put("reference1", Main.myPosidStr);
//            BodyMap.Put("reference2", Main.myShopnameOnly);
//            BodyMap.Put("reference3", "null");
//            BodyMap.Put("reference4", "null");
//            for (i = 0; (i
//                    <= (Main.MetaDataList.Size - 1)); i++) {
//                metadata = (metadata + Main.MetaDataList.Get(i));
//            }
//
//            //  BodyMap.Put("metadata",metadata)
//            BodyMap.Put("metadata", "Null");
//            JSONGenerator JsonGenerator;
//            JsonGenerator.Initialize(BodyMap);
//            BodyJson = JsonGenerator.ToString;
//            Log(BodyJson);
//            InputStream inStream;
//            HttpRequest Request;
//            OkHttpRequest Request1;
//            inStream.InitializeFromBytesArray(BodyJson.GetBytes("UTF-8"), 0, BodyJson.Length);
//            Request.InitializePost(TQRurl, inStream, BodyJson.GetBytes("UTF-8").Length);
//            Request.Timeout = 10000;
//            Request.SetContentEncoding("UTF-8");
//            Request.SetContentType("application/json");
//            //  Request.SetHeader("authorization",HeaderOut)
//            Request1.InitializePost(TQRurl, inStream, BodyJson.GetBytes("UTF-8").Length);
//            Request1.Timeout = 10000;
//            Request1.SetContentEncoding("UTF-8");
//            Request1.SetContentType("application/json");
//            //  Request1.SetHeader("authorization",HeaderOut)
//            TaskRun = 2;
//            //  hc.Execute(Request,TaskRun)
//            hc1.Execute(Request1, TaskRun);
//        }
//
//        public final void PaymentCreate() {
//            Base64 B64;
//            Map BodyMap;
//            Map HeaderMap;
//            String HeaderTag = "";
//            if ((LblPay.Text <= 0)) {
//                Msgbox("��Ť�ҵ�ͧ�ҡ���� 0 !!", "Payment");
//                ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                ImageViewQr.Invalidate();
//                StartActivity("CardPopup");
//                Activity.Finish();
//            }
//
//            //  Step 1 = Convert URL to base64
//            PaymentURL64 = B64.EncodeStoS("POST:/payment/v1/orders", "TIS-620");
//            PaymentURL64 = PaymentURL64.Replace("=", "");
//            PaymentURL64 = PaymentURL64.Replace("+", "-");
//            PaymentURL64 = PaymentURL64.Replace("/", "_");
//            Log(("1-PaymentURL=" + PaymentURL));
//            Log(("1-PaymentURL64=" + PaymentURL64));
//            //  Step 2 = Convert Body Json to Base64
//            String transid;
//            DateTime.DateFormat = "yyMMdd";
//            DateTime.TimeFormat = "HHmmss";
//            if ((Main.CardProvider == "alipay")) {
//
//            }
//            else if ((Main.CardProvider == "wechat")) {
//
//            }
//            else {
//                return;
//            }
//
//            Log(transid);
//            BodyMap.Initialize();
//            BodyMap.Clear();
//            //  BodyMap.Put("partner_transaction_id","17120114490000021700000108000201")
//            BodyMap.Put("partner_transaction_id", transid);
//            //  Original
//            //  BodyMap.Put("sub_merchant_id","401000888880002")
//            //  BodyMap.Put("terminal_id","08000201")
//            //  Alipay
//            if ((Main.CardProvider == "alipay")) {
//                //  BodyMap.Put("sub_merchant_id","610042018000001")
//                BodyMap.Put("sub_merchant_id", KMID.trim);
//                //  BodyMap.Put("terminal_id","23455555")
//                BodyMap.Put("terminal_id", TID.trim);
//                BodyMap.Put("funding_source", "alipay");
//            }
//
//            //  'WeChat
//            if ((Main.CardProvider == "wechat")) {
//                //  BodyMap.Put("sub_merchant_id","610042018000002")
//                BodyMap.Put("sub_merchant_id", KMID.trim);
//                //  BodyMap.Put("terminal_id","23455556")
//                BodyMap.Put("terminal_id", TID.trim);
//                BodyMap.Put("funding_source", "wechat");
//            }
//
//            BodyMap.Put("currency", "THB");
//            BodyMap.Put("amount", CardPopup.AmountPay);
//            JSONGenerator JsonGenerator;
//            JsonGenerator.Initialize(BodyMap);
//            BodyJson = JsonGenerator.ToString;
//            BodyJson64 = B64.EncodeStoS(BodyJson, "TIS-620");
//            BodyJson64 = BodyJson64.Replace("=", "");
//            BodyJson64 = BodyJson64.Replace("+", "-");
//            BodyJson64 = BodyJson64.Replace("/", "_");
//            Log(("2-BodyJson=" + BodyJson));
//            Log(("2-BodyJson64=" + BodyJson64));
//            //  Step 3 = Concat 1 + 2 seperate by :
//            //  URL_BodyJson = PaymentURL64 & ":" & BodyJson64
//            URL_BodyJson = (PaymentURL64 + ("." + BodyJson64));
//            Log(("3-URL_BodyJson =" + URL_BodyJson));
//            //  Step 4 = Encode 3 with Hmac SHA256
//            Mac m;
//            KeyGenerator k;
//            k.Initialize("HMACSHA256");
//            k.KeyFromBytes(AppSecret.GetBytes("TIS-620"));
//            m.Initialise("HMACSHA256", k.Key);
//            m.Update(URL_BodyJson.GetBytes("TIS-620"));
//            byte[] b;
//            b = m.Sign;
//            URL_BodyJson_SHA256 = bc.StringFromBytes(b, "TIS-620");
//            Log(("4-URL_BodyJson_SHA256 =" + URL_BodyJson_SHA256));
//            Log(("4-URL_BodyJson_SHA256 hex =" + bc.HexFromBytes(b).ToLowerCase));
//            //  Step 5 = Concat AppId + 4 seperate by :
//            HeaderOut = (AppID + (":" + bc.HexFromBytes(b).ToLowerCase));
//            Log(("5-HeaderOut=" + HeaderOut));
//            InputStream inStream;
//            HttpRequest Request;
//            OkHttpRequest Request1;
//            inStream.InitializeFromBytesArray(BodyJson.GetBytes("UTF-8"), 0, BodyJson.Length);
//            Request.InitializePost(PaymentURL, inStream, BodyJson.GetBytes("UTF-8").Length);
//            Request.Timeout = 10000;
//            Request.SetContentEncoding("UTF-8");
//            Request.SetContentType("application/json");
//            Request.SetHeader("authorization", HeaderOut);
//            Request1.InitializePost(PaymentURL, inStream, BodyJson.GetBytes("UTF-8").Length);
//            Request1.Timeout = 10000;
//            Request1.SetContentEncoding("UTF-8");
//            Request1.SetContentType("application/json");
//            Request1.SetHeader("authorization", HeaderOut);
//            TaskRun = 1;
//            //  hc.Execute(Request,TaskRun)
//            hc1.Execute(Request1, TaskRun);
//        }
//
//        public final void hc1_ResponseError(OkHttpResponse Response, String Reason, Int StatusCode, Int TaskId) {
//            ProgressDialogHide();
//            Log(("Reason:" + Reason));
//            if ((Response != Null)) {
//                Response.Release();
//            }
//
//            ProgressDialogHide();
//            switch (true) {
//                case object:
//                    _ when;
//                    (TaskRun == 1);
//                    Msgbox("POST unsuccessed !!", "POST to KBANK");
//                    break;
//                break;
//                default:
//                    Msgbox("Unknown Error !!", "POST to Kbank");
//                    break;
//                break;
//            }
//        }
//
//        public final void hc1_ResponseSuccess(OkHttpResponse Response, Int TaskId) {
//
//            Response.GetAsynchronously("Response", File.OpenOutput(File.DirDefaultExternal, "kplus.txt", false), true, TaskId);
//        }
//
//        public final void Response_StreamFinish(boolean Success, Int Taskid) {
//            JSONParser parser;
//            //  Dim rows As List
//            Map m;
//            Int UrlPos;
//            Int CommaPos;
//            String RetResponse;
//            if ((Success == false)) {
//                Msgbox("Response not completed !!", "Response");
//            }
//            else {
//                //  Msgbox("Finished !!", "Response")
//                RetResponse = "";
//                RetResponse = File.ReadString(File.DirDefaultExternal, "kplus.txt");
//                //  RetResponse = File.ReadString(File.DirRootExternal,"kplus.txt")
//                //  Log("RetResponse=" & RetResponse)
//                //  Log("Taskrun=" & TaskRun)
//                parser.Initialize(RetResponse);
//                if ((m.IsInitialized == false)) {
//                    m.Initialize();
//                }
//
//                m = parser.NextObject;
//                switch (TaskRun) {
//                    case 1:
//                        UrlPos = RetResponse.IndexOf("qr_code");
//                        CommaPos = RetResponse.IndexOf2(",", UrlPos);
//                        String QrcodeUrl;
//                        //  QrcodeUrl = res.SubString2(UrlPos + 10,CommaPos - 1)
//                        QrcodeUrl = RetResponse.SubString2((UrlPos + 10), (CommaPos - 1));
//                        //  QrcodeUrl = m.Get("qr_code")
//                        if ((QrcodeUrl.Trim() == "")) {
//                            Msgbox("No URL Returned", "Result from Kbank");
//
//                            //  File.Delete(File.DirRootExternal,"kplus.txt")
//                            return;
//                        }
//
//                        ImageViewQr.Bitmap = QR.QR_Encode(QrcodeUrl, 100, M);
//                        ImageViewQr.Gravity = Gravity.FILL;
//                        ImageViewQr.Invalidate();
//                        QRbitmap = QR.QR_Encode(QrcodeUrl, 300, M);
//                        Id = m.Get("id");
//                        Merchant_Id = m.Get("merchant_id");
//                        Sub_Merchant_Id = m.Get("sub_merchant_id");
//                        amount = m.Get("amount");
//                        currency = m.Get("currency");
//                        partner_transaction_id = m.Get("partner_transaction_id");
//                        customer_id = m.Get("customer_id");
//                        terminal_id = m.Get("terminal_id");
//                        created = m.Get("created");
//                        status = m.Get("status");
//                        metadata = m.Get("metadata");
//                        redirect_url = m.Get("redirect_url");
//                        notify_url = m.Get("notify_url");
//                        funding_source = m.Get("funding_source");
//                        qr_code = m.Get("qr_code");
//                        meta_data = m.Get("meta_data");
//                        break;
//                    break;
//                    case 2:
//                        partner_transaction_id = m.Get("partnerTxnUid");
//                        Id = m.Get("partnerId");
//                        status = m.Get("statusCode");
//                        AccountName = m.Get("accountName");
//                        qr_code = m.Get("qrCode");
//                        ImageViewQr.Bitmap = QR.QR_Encode(qr_code, 100, M);
//                        ImageViewQr.Gravity = Gravity.FILL;
//                        ImageViewQr.Invalidate();
//                        QRbitmap = QR.QR_Encode(qr_code, 300, M);
//                        break;
//                    break;
//                }
//            }
//
//        }
//
//        public final void Activity_Resume() {
//            QR.initialize();
//            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//            ImageViewQr.Invalidate();
//            QRstr = CardPopup.QRstr;
//            ImageViewQr.Bitmap = QR.QR_Encode(QRstr, 100, M);
//            ImageViewQr.Invalidate();
//            QRbitmap = QR.QR_Encode(QRstr, 300, M);
//        }
//
//        public final void Activity_Pause(boolean UserClosed) {
//
//        }
//
//        public final void BtnCancel_Click() {
//            //  ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg")
//            //  ImageViewQr.Invalidate
//            //  StartActivity("CardPopup")
//            //  Activity.Finish
//            Int Result;
//            Result = Msgbox2("��ͧ���¡��ԡ��â�����������?", "¡��ԡ��â��", "��", "�����", "", Null);
//            if ((Result == DialogResponse.POSITIVE)) {
//                BtnCancelPress = true;
//                if (((Main.CardProvider == "alipay")
//                        | (Main.CardProvider == "wechat"))) {
//                    this.BtnCheck_Click();
//                }
//                else {
//                    ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                    ImageViewQr.Invalidate();
//                    StartActivity("CardPopup");
//                    Activity.Finish();
//                }
//
//            }
//            else {
//
//            }
//
//        }
//
//        public final void BtnPrint_Click() {
//            Prn.prn_open();
//            if ((Prn.prn_setupPage(-1, -1) != 0)) {
//                Beeper1.Beep();
//                Msgbox("setup page failed", "prn_setupPage");
//                return;
//            }
//
//            //  Prn.prn_drawBarcode(EditText1.Text,1,20,58,4,300dip,0)
//            Prn.prn_drawBitmap(QRbitmap, 1, 10);
//            Prn.prn_drawText(" ", 20, 430, "arial", 20, false, false, 0);
//            Prn.prn_printPage(0);
//            Prn.prn_close();
//        }
//
//        public final void BtnPass_Click() {
//            Main.QRpass = true;
//            Main.DeductStatus = true;
//            Main.DeductAppCode = Main.CardProvider;
//            Main.DeductTrace = "";
//            Main.DeductTid = Main.myPosidStr;
//            Main.DeductAccNo = "TEST";
//            Main.DeductBatch = "12345678";
//            DateTime.DateFormat = "dd/MM/yyyy";
//            DateTime.TimeFormat = "HH:mm:ss";
//
//            Main.DeductTime = DateTime.Time(DateTime.Now);
//            //  Main.CardProvider = "KBANK"
//            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//            ImageViewQr.Invalidate();
//            StartActivity("Main");
//            Activity.Finish();
//        }
//
//        public final void BtnFail_Click() {
//            Main.QRpass = false;
//            Main.DeductStatus = false;
//            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//            ImageViewQr.Invalidate();
//            StartActivity("Main");
//            Activity.Finish();
//        }
//
//        public final void hc_ResponseError(HttpResponse Response, String Reason, Int StatusCode, Int TaskId) {
//            ProgressDialogHide();
//            if ((Response != Null)) {
//                Response.Release();
//            }
//
//            ProgressDialogHide();
//            switch (true) {
//                case object:
//                    _ when;
//                    (TaskRun == 1);
//                    Msgbox("POST unsuccessed !!", "POST to KBANK");
//                    break;
//                break;
//                default:
//                    Msgbox("Unknown Error !!", "POST to Kbank");
//                    break;
//                break;
//            }
//        }
//
//        public final void hc_ResponseSuccess(HttpResponse Response, Int TaskId) {
//            String res;
//            JSONParser parser;
//            List rows;
//            Map m;
//            Int UrlPos;
//            Int CommaPos;
//            ProgressDialogHide();
//            res = Response.GetString("UTF8");
//            Log(res);
//            parser.Initialize(res);
//            if ((QRY == 1)) {
//                if ((rows.IsInitialized == false)) {
//                    rows.Initialize();
//                }
//                else {
//                    rows.Clear();
//                }
//
//                rows = parser.NextArray;
//                QRY = 0;
//            }
//            else {
//                if ((m.IsInitialized == false)) {
//                    m.Initialize();
//                }
//
//                m = parser.NextObject;
//            }
//
//            switch (TaskRun) {
//                case 1:
//                    UrlPos = res.IndexOf("qr_code");
//                    CommaPos = res.IndexOf2(",", UrlPos);
//                    String QrcodeUrl;
//                    QrcodeUrl = res.SubString2((UrlPos + 10), (CommaPos - 1));
//                    if ((QrcodeUrl.Trim() == "")) {
//                        Msgbox("No URL Returned", "Result from Kbank");
//                        return;
//                    }
//
//                    ImageViewQr.Bitmap = QR.QR_Encode(QrcodeUrl, 100, M);
//                    ImageViewQr.Gravity = Gravity.FILL;
//                    ImageViewQr.Invalidate();
//                    QRbitmap = QR.QR_Encode(QrcodeUrl, 300, M);
//                    Id = m.Get("id");
//                    Merchant_Id = m.Get("merchant_id");
//                    Sub_Merchant_Id = m.Get("sub_merchant_id");
//                    amount = m.Get("amount");
//                    currency = m.Get("currency");
//                    partner_transaction_id = m.Get("partner_transaction_id");
//                    customer_id = m.Get("customer_id");
//                    terminal_id = m.Get("terminal_id");
//                    created = m.Get("created");
//                    status = m.Get("status");
//                    metadata = m.Get("metadata");
//                    redirect_url = m.Get("redirect_url");
//                    notify_url = m.Get("notify_url");
//                    funding_source = m.Get("funding_source");
//                    qr_code = m.Get("qr_code");
//                    meta_data = m.Get("meta_data");
//                    break;
//                break;
//                case 2:
//                    Msgbox(res, "Task=2");
//                    break;
//                break;
//                case 10:
//                    if ((res.Length <= 2)) {
//                        if ((BtnCancelPress == false)) {
//                            Msgbox("��ê����Թ�ѧ�����������ó� ���ѡ���� !!", "��Ǩ�ͺ��ê����Թ");
//                            ProgressDialogHide();
//                            return;
//                        }
//                        else {
//                            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                            ImageViewQr.Invalidate();
//                            StartActivity("CardPopup");
//                            Activity.Finish();
//                        }
//
//                    }
//
//                    if ((rows.Size > 0)) {
//                        m = rows.Get(0);
//                        if ((m.Get("statuscode") == "00")) {
//                            Msgbox("��ê����Թ���Ѻ����׹�ѹ���� !!", "��Ǩ�ͺ��ê����Թ");
//                            //  Main.DeductAppCode = "Test"
//                            Main.DeductAppCode = "";
//                            Main.DeductTrace = "";
//                            Main.DeductTid = Main.myShopId.Trim;
//                            Main.DeductMer = m.Get("merchantid");
//                            Main.DeductAccNo = TxnUid;
//                            Main.DeductRemain = "0";
//                            Main.DeductBatch = m.Get("approved_code");
//
//                            Main.DeductTime = DateTime.Time(DateTime.Now);
//                            Main.DeductStatus = true;
//                            Main.CardProvider = Main.CardProvider;
//                            Main.myTendorActualDiscount = 0;
//                            Main.myTendorActualAmt = CardPopup.AmountPay;
//                            Main.myKbankTransId = m.Get("partnertxnuid");
//                            Main.DeductTrace = m.Get("partnertxnuid");
//                            //  Main.TempTrace = Main.TempTrace + 1
//                            Main.BalanceLeft = "0";
//                            ProgressDialogHide();
//                            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                            ImageViewQr.Invalidate();
//                            DateTime.DateFormat = "yyyyMMdd";
//                            //  Dim FilenameSave As String = "Kbank_" & DateTime.Date(DateTime.Now) & "_" & DateTime.Time(DateTime.Now) & ".LOG"
//                            //  Dim MyPath As String = File.DirRootExternal & "/Kbank"
//                            //  File.WriteString(MyPath,FilenameSave,res)
//                            StartActivity("Main");
//                            Activity.Finish();
//                        }
//                        else if ((BtnCancelPress == false)) {
//                            Msgbox("��ê����Թ�ѧ�����������ó� ���ѡ���� !!", "��Ǩ�ͺ��ê����Թ");
//                            return;
//                        }
//                        else {
//                            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                            ImageViewQr.Invalidate();
//                            StartActivity("CardPopup");
//                            Activity.Finish();
//                        }
//
//                    }
//
//                    break;
//                break;
//            }
//        }
//
//        public final void RandomString(Int Length, boolean LowerCase, boolean UpperCase, boolean Numbers, String AdditionalChars) {
//            String source = "";
//            if ((LowerCase == true)) {
//                source = (source + "abcdefghijklmnopqrstuvwxyz");
//            }
//
//            if ((UpperCase == true)) {
//                source = (source + "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//            }
//
//            if ((Numbers == true)) {
//                source = (source + "0123456789");
//            }
//
//            if ((AdditionalChars.Length > 0)) {
//                source = source;
//            }
//
//            StringBuilder sb;
//            sb.Initialize();
//            for (var i = 1; (i <= Length); i++) {
//                Int r = Rnd(0, source.Length);
//                sb.Append(source.SubString2(r, (r + 1)));
//            }
//
//            return sb.ToString();
//        }
//
//        public final void BtnCheck_Click() {
//            Base64 B64;
//            Map BodyMap;
//            //  Dim Request As HttpRequest
//            if (((Main.CardProvider == "alipay")
//                    | (Main.CardProvider == "wechat"))) {
//                //  Step 1 = Convert URL to base64
//                InquiryURL64 = B64.EncodeStoS(("GET:/payment/v1/payments/inquiry" + ("?sub_merchant_id="
//                        + (Sub_Merchant_Id + ("&" + ("partner_transaction_id=" + partner_transaction_id))))), "UTF-8");
//                InquiryURL64 = InquiryURL64.Replace("=", "");
//                InquiryURL64 = InquiryURL64.Replace("+", "-");
//                InquiryURL64 = InquiryURL64.Replace("/", "_");
//                Log(("1-InquiryURL=" + InquiryURL));
//                Log(("1-InquiryURL64=" + InquiryURL64));
//                //  Step 2 = Convert Body Json to Base64
//                String InquiryParameters;
//                String InquiryParameters64;
//                InquiryParameters = "";
//                InquiryParameters64 = B64.EncodeStoS(InquiryParameters, "UTF-8");
//                Log(("2-InquiryParameters=" + InquiryParameters));
//                Log(("2-InquiryParameters64=" + InquiryParameters64));
//                //  Step 3 = Concat 1 + 2 seperate by :
//                URL_BodyJson = (InquiryURL64 + ("." + InquiryParameters64));
//                Log(("3-URL_BodyJson =" + URL_BodyJson));
//                //  Step 4 = Encode 3 with Hmac SHA256
//                Mac m;
//                KeyGenerator k;
//                String ParametersOut;
//                k.Initialize("HMACSHA256");
//                k.KeyFromBytes(AppSecret.GetBytes("UTF-8"));
//                m.Initialise("HMACSHA256", k.Key);
//                m.Update(URL_BodyJson.GetBytes("UTF-8"));
//                byte[] b;
//                b = m.Sign;
//                ParametersOut = bc.StringFromBytes(b, "UTF-8");
//                Log(("4-ParametersOut =" + ParametersOut));
//                Log(("4-ParametersOut hex =" + bc.HexFromBytes(b).ToLowerCase));
//                //  Step 5 = Concat AppId + 4 seperate by :
//                HeaderOut = (AppID + (":" + bc.HexFromBytes(b).ToLowerCase));
//                Log(("5-HeaderOut=" + HeaderOut));
//                HttpJob Job1;
//                Job1.Initialize("Job1", this);
//                Job1.Download((InquiryURL + ("?" + ("sub_merchant_id="
//                        + (Sub_Merchant_Id + ("&" + ("partner_transaction_id=" + partner_transaction_id)))))));
//                Job1.GetRequest.SetHeader("authorization", HeaderOut);
//                Job1.GetRequest.SetContentEncoding("UTF-8");
//                Log((InquiryURL + ("?" + ("sub_merchant_id="
//                        + (Sub_Merchant_Id + ("&" + ("partner_transaction_id=" + partner_transaction_id)))))));
//            }
//            else if ((Main.CardProvider == "Kplus")) {
//                TaskRun = 10;
//                Jobno = 10;
//                String SQL;
//                SQL = ("SELECT * FROM CALLBACK WHERE partnertxnuid = \'"
//                        + (TxnUid.Trim + ("\'" + " AND statusread=0")));
//                //  SQL = "SELECT * FROM CALLBACK WHERE partnertxnuid = '185400023002911'" & " AND statusread = 0"
//                this.ExecuteQuery(SQL, Jobno);
//            }
//
//        }
//
//        public final void ExecuteQuery(String Query, Int TaskId) {
//            //  Log(TaskId)
//            //  Log(Query)
//            if ((Query.Trim() == "")) {
//                return;
//            }
//
//            //  CmdRec = CmdRec & Query & Chr(10)
//            if ((Main.WifiEnable == true)) {
//                if ((CheckSSID == false)) {
//                    Msgbox(("��سҵԴ��� Wifi �Ѻ " + Main.mySSID.Trim), "Check Wifi");
//                    //  Activity.Finish
//                    return;
//                }
//
//            }
//
//            if ((Main.Lang == "T")) {
//                ProgressDialogShow("�Դ��� Server, ���ѡ����...");
//            }
//            else {
//                ProgressDialogShow("Connect to Server, Please wait ...");
//            }
//
//            HttpRequest Req;
//            //  req.InitializePost2("http://192.168.1.10/shoppos.aspx", Query.GetBytes("UTF8"))
//            //  req.InitializePost2("http://192.168.102.61/shoppos.aspx", Query.GetBytes("UTF8"))
//            //  req.InitializePost2("http://192.168.1.61/shoppos.aspx", Query.GetBytes("UTF8"))
//            //  Log(ServerUrl.Trim)
//            TQRcheckUrl = ("http://"
//                    + (Main.myServerIp.trim + "/TQRcheck.aspx"));
//            Req.InitializePost2(TQRcheckUrl.Trim, Query.GetBytes("UTF8"));
//            Req.Timeout = 60000;
//            QRY = 1;
//            hc.Execute(Req, TaskId);
//        }
//
//        public final void CheckSSID() {
//            boolean r;
//            //  Dim a As Int
//            r = Main.myWifi.ABLoadWifi();
//            if ((r == true)) {
//                //  Msgbox (myWifi.ABGetCurrentWifiInfo().SSID, "")
//                //  For a = 0 To myWifi.ABNumberOfAvailableWifiNetworks - 1
//                //  Msgbox (myWifi.ABGetWifiNetwork(a).SSID & " " & myWifi.ABGetWifiNetwork(a).Level, "")
//                //  Next
//                //  Msgbox(myWifi.ABGetCurrentWifiInfo().SSID,"mywifi")
//                if ((Main.myWifi.ABGetCurrentWifiInfo().SSID != Main.mySSID.Trim)) {
//                    return false;
//                }
//                else {
//                    return true;
//                }
//
//            }
//            else {
//                return true;
//            }
//
//        }
//
//        public final void JobDone(HttpJob job) {
//            String res;
//            JSONParser parser;
//            Map m;
//            Log(("JobName = "
//                    + (job.JobName + (", Success = " + job.Success))));
//            if ((job.Success == true)) {
//                res = job.GetString;
//                Log(res);
//                parser.Initialize(res);
//                if ((m.IsInitialized == false)) {
//                    m.Initialize();
//                }
//
//                m = parser.NextObject;
//                switch (job.JobName) {
//                    case "Job1":
//                        //  print the result to the logs
//                        //  ListView1.AddSingleLine("paymentId:" & m.Get("paymentid"))
//                        //  ListView1.AddSingleLine("id:" & m.Get("id"))
//                        //  ListView1.AddSingleLine("merchant_id:" & m.Get("merchant_id"))
//                        //  ListView1.AddSingleLine("sub_merchant_id:" & m.Get("sub_merchant_id"))
//                        //  ListView1.AddSingleLine("amount:" & m.Get("amount"))
//                        //  ListView1.AddSingleLine("currency:" & m.Get("currency"))
//                        //  ListView1.AddSingleLine("invoiceid:" & m.Get("invoiceid"))
//                        //  ListView1.AddSingleLine("partner_transaction_id:" & m.Get("partner_transaction_id"))
//                        //  ListView1.AddSingleLine("customer_id:" & m.Get("customer_id"))
//                        //  ListView1.AddSingleLine("funding_source:" & m.Get("funding_source"))
//                        //  ListView1.AddSingleLine("status:" & m.Get("status"))
//                        //  ListView1.AddSingleLine("metadata:" & m.Get("metadata"))
//                        //  ListView1.AddSingleLine("created:" & m.Get("created"))
//                        //  ListView1.AddSingleLine("orderid:" & m.Get("orderid"))
//                        //  ListView1.AddSingleLine("terminal_id:" & m.Get("terminal_id"))
//                        //  ListView1.AddSingleLine("auth_code:" & m.Get("auth_code"))
//                        //  ListView1.AddSingleLine("transaction_ref:" & m.Get("transaction_ref"))
//                        //  ListView1.AddSingleLine("alipay_pay_time:" & m.Get("alipay_pay_time"))
//                        //  ListView1.AddSingleLine("trans_amount:" & m.Get("trans_amount"))
//                        //  ListView1.AddSingleLine("alipay_buyer_user_id:" & m.Get("alipay_buyer_user_id"))
//                        //  ListView1.AddSingleLine("alipay_buyer_login_id:" & m.Get("alipay_buyer_login_id"))
//                        //  ListView1.AddSingleLine("exchange_rate:" & m.Get("exchange_rate"))
//                        //  ListView1.AddSingleLine("approved_code:" & m.Get("approved_code"))
//                        //  ListView1.Invalidate
//                        //  DeductStatus = True
//                        if ((m.Get("status") == "APPROVED")) {
//                            Msgbox("��ê����Թ���Ѻ����׹�ѹ���� !!", "��Ǩ�ͺ��ê����Թ");
//                            //  Main.DeductAppCode = "Test"
//                            Main.DeductAppCode = Main.myAlipayAppId;
//                            Main.DeductTrace = "";
//                            Main.DeductTid = m.Get("terminal_id");
//                            Main.DeductMer = m.Get("merchant_id");
//                            if ((Main.CardProvider == "alipay")) {
//                                Main.DeductAccNo = m.Get("alipay_buyer_user_id");
//                            }
//
//                            if ((Main.CardProvider == "wechat")) {
//                                Main.DeductAccNo = (m.Get("terminal_id") + m.Get("approved_code"));
//                            }
//
//                            Main.DeductRemain = "0";
//                            Main.DeductBatch = m.Get("approved_code");
//
//                            Main.DeductTime = DateTime.Time(DateTime.Now);
//                            Main.DeductStatus = true;
//                            Main.CardProvider = Main.CardProvider;
//                            Main.myTendorActualDiscount = 0;
//                            Main.myTendorActualAmt = CardPopup.AmountPay;
//                            Main.myKbankTransId = m.Get("partner_transaction_id");
//                            Main.DeductTrace = m.Get("partner_transaction_id");
//                            //  Main.TempTrace = Main.TempTrace + 1
//                            Main.BalanceLeft = "0";
//                            ProgressDialogHide();
//                            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                            ImageViewQr.Invalidate();
//                            DateTime.DateFormat = "yyyyMMdd";
//
//                            String MyPath = (File.DirRootExternal + "/Kbank");
//                            File.WriteString(MyPath, FilenameSave, res);
//                            StartActivity("Main");
//                            Activity.Finish();
//                        }
//                        else if ((BtnCancelPress == false)) {
//                            Msgbox("��ê����Թ�ѧ�����������ó� ���ѡ���� !!", "��Ǩ�ͺ��ê����Թ");
//                            return;
//                        }
//                        else {
//                            ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                            ImageViewQr.Invalidate();
//                            StartActivity("CardPopup");
//                            Activity.Finish();
//                        }
//
//                        break;
//                    break;
//                    case "Job2":
//                        break;
//                    break;
//                }
//            }
//            else {
//                Log(("Error: " + job.ErrorMessage));
//                //  ToastMessageShow("Error: " & job.ErrorMessage, True)
//                ImageViewQr.Bitmap = LoadBitmap(File.DirAssets, "Blank.jpg");
//                ImageViewQr.Invalidate();
//                StartActivity("CardPopup");
//                Activity.Finish();
//            }
//
//            job.Release();
//        }
//    }
}
