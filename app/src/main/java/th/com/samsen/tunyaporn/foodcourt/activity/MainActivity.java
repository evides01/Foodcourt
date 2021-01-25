package th.com.samsen.tunyaporn.foodcourt.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.device.ScanManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import th.com.samsen.tunyaporn.foodcourt.R;
import th.com.samsen.tunyaporn.foodcourt.model.AddCardModel;
import th.com.samsen.tunyaporn.foodcourt.model.CardDetailModel;
import th.com.samsen.tunyaporn.foodcourt.model.Category;
import th.com.samsen.tunyaporn.foodcourt.model.DolfinCancelPaymentModel;
import th.com.samsen.tunyaporn.foodcourt.model.DolfinRequestModel;
import th.com.samsen.tunyaporn.foodcourt.model.DolfinResponseModel;
import th.com.samsen.tunyaporn.foodcourt.model.GHLRequestQRModel;
import th.com.samsen.tunyaporn.foodcourt.model.GHLResponseQRModel;
import th.com.samsen.tunyaporn.foodcourt.model.Sell;
import th.com.samsen.tunyaporn.foodcourt.model.TenderModel;
import th.com.samsen.tunyaporn.foodcourt.model.TrueInquiryRequest;
import th.com.samsen.tunyaporn.foodcourt.model.TrueInquiryResponse;
import th.com.samsen.tunyaporn.foodcourt.model.TruePaymentRequestModel;
import th.com.samsen.tunyaporn.foodcourt.model.TruePaymentResponse;
import th.com.samsen.tunyaporn.foodcourt.service.PrintBillService;
import th.com.samsen.tunyaporn.foodcourt.utility.ConnectionHelper;
import th.com.samsen.tunyaporn.foodcourt.utility.DBHelper;
import th.com.samsen.tunyaporn.foodcourt.utility.GetData;
import th.com.samsen.tunyaporn.foodcourt.utility.Utils;

public class MainActivity extends AppCompatActivity {

    int num = 0;
    private Boolean doubleBackToExitPressedOnce = false;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btnReset;
    ImageButton btnBackspace, btnPayment;
    TextView txtDisplayAmt;
    ListView sellListView;
    EditText edtBarcodeScan;
    String textDisplay, paymentBarcodeString;
    String TAG = "MainActivityTAG";
    LinearLayout topCategoryLinearLayout, bodyLinearLayout;
    private String ip;
    private DBHelper dbHelper;
    private GetData getData;
    private List<Map<String, String>> data = null;
    private String apiUrl = "";
    private String termIP = "";

    private boolean ISWIFICONNECTED;
    private Socket socket;
    private String DateWorking = "";
    private String connectionret;
    private int SERVERPORT = 8020;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String chooseStore = "";
    private String[] companyProfile;
    private TextView branchTextView, companyTextView, posIdTextView, posNoTextView, ipNoTextView, storeNameTextView, totalTextView;

    private ListView itemListView;

    private String choosePosId = "";
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String tendorid = "";
    private TenderModel[] tenderModel;
    private String barcodeStr;
    private boolean scanPowerState;
    private boolean lockTrigglerState;
    private boolean isScaning = false;
    private Button btnscan;
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            edtBarcodeScan.setText("");

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            android.util.Log.i("debug", "----codetype----" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);

            edtBarcodeScan.setText(barcodeStr);

        }

    };


    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode(0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    private String[][] queryStore() {
        String query = "select vs.* from dbfood..VSHOPS vs join dbfood..SHOPS s on s.SHOPID = vs.SHOPID where vs.MRIP = '" + ip + "' and s.CONTSTATUS <> '3' ";

        String[][] dataStrings;
        getData = new GetData(query, "GetStore", MainActivity.this);
        data = getData.doInBackground();
        if (data.size() > 0) {
            dataStrings = new String[data.size()][];

            for (int i = 0; i < data.size(); i++) {
                dataStrings[i] = new String[]{data.get(i).get("MRIP"), data.get(i).get("POSID"), data.get(i).get("SHOPNAME"), data.get(i).get("OWNER")};
            }

            return dataStrings;
        } else {
            return new String[0][0];
        }


    }

    private void setCategory(boolean enabledCategory) {
        dbHelper = new DBHelper(MainActivity.this);
        if (companyProfile[8] != null) {

            List<Category> categories = dbHelper.getCategory(companyProfile[8]);

            Log.d(TAG, "POSID: " + companyProfile[8]);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

            if (categories != null) {
                topCategoryLinearLayout.removeAllViews();
                for (int i = 0; i < categories.size(); i++) {
                    Button button = new Button(MainActivity.this);
                    Log.d(TAG, "setCategory: " + categories.get(i).getCategoryName());
                    button.setTag(categories.get(i).getCategoryName());
                    button.setText(categories.get(i).getCategoryName());
                    button.setTextSize(20);
                    button.setEnabled(enabledCategory);
                    button.setOnClickListener(buttonOnClickListener);

                    topCategoryLinearLayout.addView(button, layoutParams);
                }

            }
        }
    }

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {
            if (view.getTag().equals("Result")) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("Class", "MainActivity");
                intent.putExtra("ShopName", companyProfile[9]);
                intent.putExtra("Status", "True");
//                try {
//                    serialport2.close();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("Category", (String) view.getTag());
                intent.putExtra("IP", ip);
                intent.putExtra("CompanyProfile", companyProfile);
                intent.putExtra("ChooseStore", chooseStore);
//                try {
//                    serialport2.close();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                startActivity(intent);
                finish();
            }
        }
    };

    public void QueryCatagoryAndShow() {

        dbHelper.clearCategory();
        String query = "select distinct sb.BTNGROUP, s.POSID from dbfood..SHOPBUTTONS sb inner join dbfood..SHOPS s on s.SHOPID = sb.SHOPID where s.CONTSTATUS <> '3' and sb.SHOPID = '" + companyProfile[10] + "'";
        getData = new GetData(query, "GetCategory", MainActivity.this);

        data = getData.doInBackground();

        Log.d(TAG, "QueryCatagoryAndShow: Data " + data);
        Log.d(TAG, "QueryCatagoryAndShow: Data Size " + data.size());
        Category category = new Category();
        dbHelper = new DBHelper(MainActivity.this);

        for (int i = 0; i < data.size(); i++) {

            category.setPOSID(data.get(i).get("POSID"));
            category.setCategoryName(data.get(i).get("BTNGROUP"));

            Log.d(TAG, "queryCategory: " + data.get(i).get("BTNGROUP"));

            dbHelper.addCategory(category);

        }

        setCategory(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        return true;
    }

    private String[] getListStoreName() {
        String[][] result = queryStore();
        if (result.length > 0) {
            String[] storeNameStrings = new String[result.length];

            for (int i = 0; i < result.length; i++) {

                storeNameStrings[i] = result[i][2];
            }

            return storeNameStrings;

        }

        return new String[0];
    }

    private String[] getListPOSID() {
        String[][] result = queryStore();
        if (result.length > 0) {
            String[] posIdStrings = new String[result.length];

            for (int i = 0; i < result.length; i++) {

                posIdStrings[i] = result[i][1];
            }

            return posIdStrings;

        }

        return new String[0];
    }

    private void queryCompany(String chooseStoreName) {
        String query = "select * from dbfood..COMPANYPROFILE";
        getData = new GetData(query, "GetCompanyProfile", MainActivity.this);
        data = getData.doInBackground();

        Log.d(TAG, "queryCompany: " + data);
        companyProfile = new String[12];
        if (data.size() > 0) {
            companyProfile[0] = data.get(0).get("COMID");
            companyProfile[1] = data.get(0).get("BRNID");
            companyProfile[2] = data.get(0).get("BRNNAMETHAI");
            companyProfile[3] = data.get(0).get("BRNNAMEENG");
            companyProfile[4] = data.get(0).get("BRNTAXID");
            companyProfile[5] = data.get(0).get("BRNVAT");
            companyProfile[6] = data.get(0).get("ADDRESS1") + " " + data.get(0).get("ADDRESS2") + " " + data.get(0).get("REMARK");
        }

        query = "select distinct MRIP,POSID,SHOPNAME,OWNER,SHOPID from dbfood..VSHOPS where MRIP = '" + ip + "' and SHOPNAME like '%" + chooseStoreName + "%' and POSID like '%" + choosePosId + "%' and CONTSTATUS <> '3'";
        Log.d(TAG, "onActivityCreated: " + query);

        getData = new GetData(query, "GetPOSData", MainActivity.this);
        data = getData.doInBackground();

        if (data.size() > 0) {
            companyProfile[7] = data.get(0).get("MRIP");
            companyProfile[8] = data.get(0).get("POSID");
            companyProfile[9] = data.get(0).get("SHOPNAME");
            companyProfile[10] = data.get(0).get("SHOPID");
            chooseStore = companyProfile[9];
        }

        query = "select s.*,t.TTAXID,t.TERMINALID,t.TID,t.DESCRIPTION,t.INVOICE,t.CREDITNOTE from dbfood..SHOPS s inner join dbfood..TERMINAL t on t.TIP = s.MRIP where MRIP = '" + ip + "' and POSID = '" + companyProfile[8] + "'";
        getData = new GetData(query, "GetStoreAndTerminal", MainActivity.this);
        data = getData.doInBackground();

        Log.d(TAG, "loadCompanyProfile: " + data.size());

        if (data.size() == 1) {
            companyProfile[11] = data.get(0).get("TTAXID");

        } else {

            if (data.size() == 0) {

                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("ไม่พบการตั้งค่าจุดขาย กรุณาตรวจสอบ ip ที่ backoffice");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("SETTING", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
                        intent.putExtra("Class", "MainActivity");
                        intent.putExtra("ShopName", chooseStore);
                        intent.putExtra("Status", "True");

                        startActivity(intent);
                        finish();

                    }
                });
                alertDialog = builder.create();
                alertDialog.show();

            } else {

                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert");
                builder.setCancelable(false);
                builder.setMessage("จุดขายมีมากกว่า 1 จุด กรุณาตรวจสอบ ip ที่ backoffice");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();

            }
        }


        Log.d(TAG, "queryCompany: " + data);

        companyTextView.setText(companyProfile[2]);
        branchTextView.setText(companyProfile[3]);
        posIdTextView.setText(companyProfile[8]);
        posNoTextView.setText(companyProfile[11]);
        ipNoTextView.setText(Utils.getIPAddress(true));
        storeNameTextView.setText(companyProfile[9]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
            intent.putExtra("Class", "MainActivity");
            intent.putExtra("ShopName", chooseStore);
            intent.putExtra("Status", "True");

            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_day_end) {
            int total = 0;
            int total_ewallet = 0;
            int cardend = 0;
            String query = "select ISNULL(CAST(SUM(MRTAMOUNT) AS VARCHAR(10)), '0') as SUMAMT from dbfood..CARDTRANS where convert(varchar(10),MRTLUPDATE, 120) = CONVERT(varchar(10),getdate(),120) and SHOPID = '" + companyProfile[10] + "' and MRTSTATUS = 'S' and CARDTENDOR = ''";
            getData = new GetData(query, "GetSumAmt", MainActivity.this);
            data = getData.doInBackground();
            if (data.size() > 0) {
                total = Integer.parseInt(data.get(0).get("SUMAMT"));
            }
            query = "select ISNULL(CAST(SUM(MRTAMOUNT) AS VARCHAR(10)), '0') as SUMAMT from dbfood..CARDTRANS where convert(varchar(10),MRTLUPDATE, 120) = CONVERT(varchar(10),getdate(),120) and SHOPID = '" + companyProfile[10] + "' and MRTSTATUS = 'S' and CARDTENDOR <> ''";
            getData = new GetData(query, "GetSumAmt", MainActivity.this);
            data = getData.doInBackground();
            if (data.size() > 0) {
                total_ewallet = Integer.parseInt(data.get(0).get("SUMAMT"));
            }

            query = "select SCEID,totalcard from dbfood..SHOPCARDEND where convert(varchar(10),CARDENDDATE, 120) = CONVERT(varchar(10),getdate(),120) and SHOPID = '" + companyProfile[10] + "'";
            getData = new GetData(query, "GetShopCardEnd", MainActivity.this);
            data = getData.doInBackground();

            Log.d(TAG, "onOptionsItemSelected: " + query);
            if (data.size() > 0) {
                cardend = Integer.parseInt(data.get(0).get("TOTALCARD"));
            }

            doprintwork("ร้าน " + companyProfile[9], "center_body3");
            doprintwork("วันที่ " + Utils.getCurrentDateFromFormat("dd/MM/") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yyyy")) + 543) + " " + Utils.getCurrentDateFromFormat("kk:mm:ss"), "left_body");
            doprintwork("ยอดขายร้านค้า - Cash Card " + total + ".00 บาท", "left_body");
            doprintwork("ยอดขายร้านค้า - E-Wallet " + total_ewallet + ".00 บาท", "left_body");
            doprintwork("บัตรหมดหมดอายุที่ร้านค้า " + cardend + " ใบ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");
            doprintwork(" ", "left_body");

            finish();

        } else if (id == R.id.action_refresh) {

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.action_change_store) {
            final String[] result = getListStoreName();
            final String[] posResult = getListPOSID();
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater layoutInflater = getLayoutInflater();
            View convertView = layoutInflater.inflate(R.layout.custom_dialog_listview, null);
            builder.setView(convertView);
            builder.setTitle("Select Store");
            ListView listView = convertView.findViewById(R.id.list_store);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
            listView.setAdapter(arrayAdapter);

            final AlertDialog alertDialog = builder.create();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    chooseStore = result[i];
                    choosePosId = posResult[i];

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("ChooseStore", chooseStore);
                    intent.putExtra("ShopName", chooseStore);
                    intent.putExtra("IP", ip);
//                    intent.putExtra("SAVE", "SAVE");
                    startActivity(intent);
                    finish();
                }
            });

            alertDialog.show();
            alertDialog.getWindow().getAttributes();

        }
        return super.onOptionsItemSelected(item);
    }

    private void queryTotalSellDate() {
        int total = 0;
        int divide = 0;

        Log.d(TAG, "queryTotalSellDate: " + Arrays.toString(companyProfile));

        String query = "select ISNULL(CAST(SUM(MRTAMOUNT) AS VARCHAR(10)), '0') as SUMAMT from dbfood..CARDTRANS where convert(varchar(10),MRTLUPDATE, 120) = CONVERT(varchar(10),getdate(),120) and SHOPID = '" + companyProfile[10] + "' and MRTSTATUS = 'S'";
        getData = new GetData(query, "GetSumAmt", MainActivity.this);
        data = getData.doInBackground();
        if (data.size() > 0) {
            total = Integer.parseInt(data.get(0).get("SUMAMT"));
        }
//        query = "select ISNULL(CAST(SUM(MRTAMOUNT) AS VARCHAR(10)), '0') as SUMAMT from CARDTRANS where DATEADD(dd, 0, DATEDIFF(dy, 0, MRTLUPDATE)) = '" + getDate() + "' and SHOPID = '" + companyProfile[10] + "' and (MRTSTATUS = '2')";
//        getData = new GetData(query, "GetSumAmt", MainActivity.this);
//        data = getData.doInBackground();
//        if (data.size() > 0) {
//            divide = Integer.parseInt(data.get(0).get("SUMAMT"));
//        }
//        total -= divide;

        totalTextView.setText(String.valueOf(total));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        bindWidget();
        barcodeController();

        ReadConfigFile();

        GetIntent();

        if (!GetDateWorking()) {

            disabledAnything();
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Alert!!");
            builder.setMessage("กรุณาปิดสิ้นวันก่อนหรือตั้งค่าการเชื่อมต่อก่อน");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    resetAll();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().getAttributes();


        } else {
            loadCompanyProfile();

            GetTender();

            enableAnything();

            dbHelper.clearCategory();


            QueryCatagoryAndShow();

            querySell();

            queryItemSell();
        }

        edtBarcodeScan.setFocusable(true);
        edtBarcodeScan.setFocusableInTouchMode(true);

        edtBarcodeScan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                edtBarcodeScan.setFocusable(true);
                edtBarcodeScan.setFocusableInTouchMode(true);
                edtBarcodeScan.setText("");


                return true;
            }
        });
    }

    private void GetTender() {

        String query = " SELECT t1.TID, t1.CODE, t1.NAME, t1.VENDERCODE, t1.VENDERNAME, t1.TYPE, t1.CERRENCY, t1.PAYMENT_METHOD,  t1.VALUE_CONVERT, t1.Server  ,t2.SEQ, t2.TERMINALID, t2.POSID, t2.MERCHAND_ID, t2.APP_ID, t2.CLIENT_KEY, t2.CLIENT_SECRET ,T1.PREFIX_BARCODE,T1.REMARK1,T1.REMARK2,T1.REMARK3,T1.SCAN_TYPE  ,isnull(t2.VENDER_SHOP_ID,'') as VENDER_SHOP_ID   FROM  dbfoodbackup..TENDER t1 inner join  dbfoodbackup..TENDER_ITEMS t2 on t1.code = t2.code  where t1.STATUS = '1' and t2.STATUS = '1'";
        getData = new GetData(query, "GetTendor", MainActivity.this);
        data = getData.doInBackground();


        if (data.size() > 0) {
            tenderModel = new TenderModel[data.size()];
            for (int i = 0; i < data.size(); i++) {
                TenderModel model = new TenderModel();
                model.setAPP_ID(data.get(i).get("APP_ID"));
                model.setCERRENCY(data.get(i).get("CERRENCY"));
                model.setCLIENT_KEY(data.get(i).get("CLIENT_KEY"));
                model.setCLIENT_SECRET(data.get(i).get("CLIENT_SECRET"));
                model.setCODE(data.get(i).get("CODE"));
                model.setMERCHAND_ID(data.get(i).get("MERCHAND_ID"));
                model.setNAME(data.get(i).get("NAME"));
                model.setPAYMENT_METHOD(data.get(i).get("PAYMENT_METHOD"));
                model.setPOSID(data.get(i).get("POSID"));
                model.setPREFIX_BARCODE(data.get(i).get("PREFIX_BARCODE"));
                model.setREMARK1(data.get(i).get("REMARK1"));
                model.setREMARK2(data.get(i).get("REMARK2"));
                model.setREMARK3(data.get(i).get("REMARK3"));
                model.setSCAN_TYPE(data.get(i).get("SCAN_TYPE"));
                model.setSEQ(data.get(i).get("SEQ"));
                model.setServer(data.get(i).get("Server"));
                model.setTERMINALID(data.get(i).get("TERMINALID"));
                model.setTID(data.get(i).get("TID"));
                model.setVALUE_CONVERT(data.get(i).get("VALUE_CONVERT"));
                model.setVENDER_SHOP_ID(data.get(i).get("VENDER_SHOP_ID"));
                model.setVENDERCODE(data.get(i).get("VENDERCODE"));
                model.setVENDERNAME(data.get(i).get("VENDERNAME"));

                tenderModel[i] = model;

                Log.d(TAG, "GetTender: " + model.getCODE() + " " + model.getVENDER_SHOP_ID());

            }
        }

    }

    private void ReadConfigFile() {

        try {
            String filePath = "/mnt/sdcard/config/config.cd.txt";
            File file = new File(filePath);

            if (!file.exists()) {
                //write config
                String path = "/mnt/sdcard/config";
                file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }

                path += "/config.cd.txt";
                file = new File(path);


                if (!file.exists()) {
                    file.createNewFile();
                    FileWriter writer = new FileWriter(file, true); //True = Append to file, false = Overwrite
                    writer.close();

                }

                apiUrl = "http://api.sahatarasst.com:8028";
                termIP = "192.168.1.68";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("apiurl", apiUrl);
                    jsonObject.put("termIP", termIP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                FileWriter writer = new FileWriter(file, true); //True = Append to file, false = Overwrite
                writer.write(apiUrl);
                writer.close();


            } else {
                //read config

                File file1 = new File("/mnt/sdcard/config/", "config.cd.txt");


                BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                Log.d(TAG, "ReadConfigFile: " + sb);

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(sb));

                    apiUrl = jsonObject.getString("apiurl");
                    termIP = jsonObject.getString("termIP");
                } catch (JSONException e) {
                    e.printStackTrace();
                    file1.delete();
                    ReadConfigFile();
                }

            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }

    private class GHLWifiConnect extends AsyncTask<String, Void, String> {

        private String res = "";


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute: " + s);

            JSONObject jsonObject = null;
            try {
                jsonObject = XML.toJSONObject(s);

                JSONObject jsonObject1 = jsonObject.getJSONObject("xml");

                Log.d(TAG, "onPostExecute: ---> " + jsonObject1.toString());

                if (jsonObject1.getInt("response_code") == 1) {
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("การจ่ายเงินไม่สำเร็จ กรุณาทำรายการอีกครั้ง");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetScan();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    GHLResponseQRModel.xml xml = new GHLResponseQRModel.xml();

                    xml.setAmount(String.valueOf(jsonObject1.getInt("amount") / 10));
                    xml.setInvoice_no(jsonObject1.getString("invoice_no"));
                    xml.setPos_ref_no(jsonObject1.getString("pos_ref_no"));
                    xml.setResponse_code(jsonObject1.getString("response_code"));
                    xml.setResponse_msg(jsonObject1.getString("response_msg"));
                    xml.setTransaction_id(jsonObject1.getString("transaction_id"));
                    xml.setCard_approval_code(jsonObject1.getString("card_approval_code"));
                    xml.setCard_no(jsonObject1.getString("card_no"));
                    xml.setTrade_type(jsonObject1.getString("trade_type"));


                    Log.d(TAG, "onPostExecute: " + xml.getTransaction_id());

                    String tax_inv = SaveGHLDataToDatabase(xml);

                    if (tax_inv.equals("")) {
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("การจ่ายเงินไม่สำเร็จ กรุณาทำรายการอีกครั้ง");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetScan();
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    } else {

                        Log.d(TAG, "onPostExecute: tax " + tax_inv);

                        String comp = companyProfile[2].replace("สาขา", "\nสาขา");
                        List<Sell> sells = dbHelper.getSell();

                        doprintwork(comp, "left_header");
                        doprintwork("     ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ", "left_body");
//                            doprintwork("Receipt/Tax Invoice(ABB)", "center_body2");
                        doprintwork("  (VAT Included)", "center_body");
                        doprintwork("---------------------------------", "left_body");
                        doprintwork("TAX#" + companyProfile[11], "left_body");
                        doprintwork("POS#" + companyProfile[8], "left_body");
                        doprintwork("TAX INV : " + tax_inv, "left_body");
                        doprintwork("ชื่อร้าน : " + companyProfile[9], "left_body");
                        doprintwork("---------------------------------", "left_body");

                        int total = 0;
                        for (int i = 0; i < sells.size(); i++) {

                            total = total + Integer.parseInt(sells.get(i).getMrtAmount());
//                                            doprintwork(sells.get(i).getFoodName() + "         " + sells.get(i).getMrtAmount(), "left_body");
                        }
                        doprintwork("อาหารและเครื่องดื่ม           " + total + ".00", "left_body");
                        doprintwork("---------------------------------", "left_body");
                        doprintwork("จำนวนรวม                                    " + txtDisplayAmt.getText().toString() + ".00", "left_body");

                        doprintwork("     **** ชำระด้วย "+xml.getTrade_type()+" ****", "left_body");
                        doprintwork("Card No. \n" + xml.getCard_no(), "left_body");
                        doprintwork("Transaction id \n" + xml.getTransaction_id(), "left_body");
                        doprintwork("Ref No. \n" + xml.getInvoice_no(), "left_body");
                        doprintwork("วันที่ " + Utils.getCurrentDateFromFormat("dd/MM/") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yyyy")) + 543) + " " + Utils.getCurrentDateFromFormat("kk:mm:ss"), "left_body");

                        doprintwork(" ", "");
                        doprintwork(" ", "");
                        doprintwork(" ", "");
                        doprintwork(" ", "");
                        doprintwork(" ", "");
                        doprintwork(" ", "");
                        doprintwork(" ", "");
                        doprintwork(" ", "");

                        resetAll();


                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onPostExecute: " + jsonObject.toString());


        }

        @Override
        protected String doInBackground(String... strings) {

            boolean connectx = false;
            connectionret = "";
            int round = 0;

            if (!strings[0].equals("")) {

                while (!connectx) {

                    if (socket != null) {

                        if (socket.isConnected()) {
                            connectx = true;
                            connectionret = "SUCCESS";

                            ISWIFICONNECTED = true;
                        } else {
                            //failed connect
                            connectx = false;
                            connectionret = "FAILED";

                            ISWIFICONNECTED = false;

                        }
                    } else {

                        try {
                            //AUTO CONNECT
                            // connect_customerscreen_V2();


//                            InetAddress serverAddr = InetAddress.getByName(strings[0]);
                            socket = new Socket(strings[0], SERVERPORT);
                            socket.setTcpNoDelay(true);
                            socket.setKeepAlive(true);
                            socket.setReceiveBufferSize(50000);

                            if (socket.isConnected()) {
                                //Send Command

                                String CSMESSAGE = strings[1];

                                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                                out.println(CSMESSAGE);
                                out.flush();

                                res = "";

                                InputStreamReader inputStreamReader = null;
                                inputStreamReader = new InputStreamReader(socket.getInputStream());
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                                res = bufferedReader.readLine();

                                out.close();
                                inputStreamReader.close();


                            }

                            round++;

                            Thread.sleep(1000);

                            if (round == 50) {
                                connectionret = "TIMEOUT";
                                ISWIFICONNECTED = false;
                                connectx = true;
                            }


                        } catch (UnknownHostException e1) {
                            e1.printStackTrace();
                            connectionret = "ERROR " + e1.getMessage().toString();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            connectionret = "ERROR " + e1.getMessage().toString();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            connectionret = "ERROR " + e1.getMessage().toString();
                        }
                    }
                }
            }

            return res;

        }
    }

//    private class ItemArrayAdapter extends ArrayAdapter<Sell> {
//
//        private Context context;
//        private Sell[] sells;
//
//
//
//        public ItemArrayAdapter(@NonNull Context context, int resource, Context context1, Sell[] sells) {
//            super(context, resource);
//            this.context = context;
//            this.sells = sells;
//        }
//
//        public ItemArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, Context context1, Sell[] sells) {
//            super(context, resource, textViewResourceId);
//            this.context = context1;
//            this.sells = sells;
//        }
//
//        public ItemArrayAdapter(@NonNull Context context, int resource, @NonNull Sell[] objects, Context context1, Sell[] sells) {
//            super(context, resource, objects);
//            this.context = context1;
//            this.sells = sells;
//        }
//
//        public ItemArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Sell[] objects, Context context1, Sell[] sells) {
//            super(context, resource, textViewResourceId, objects);
//            this.context = context1;
//            this.sells = sells;
//        }
//
//        public ItemArrayAdapter(@NonNull Context context, int resource, @NonNull List<Sell> objects, Context context1, Sell[] sells) {
//            super(context, resource, objects);
//            this.context = context1;
//            this.sells = sells;
//        }
//
//        public ItemArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Sell> objects, Context context1, Sell[] sells) {
//            super(context, resource, textViewResourceId, objects);
//            this.context = context1;
//            this.sells = sells;
//        }
//
//        private class ViewHolder {
//            public TextView itemTextView,descTextView,qtyTextView;
//
//            public ViewHolder(View convertView) {
//                itemListView = convertView.findViewById(R.id.txt_no);
//                descTextView = convertView.findViewById(R.id.txt_desc);
//                qtyTextView = convertView.findViewById(R.id.txt_qty);
//            }
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//            ViewHolder viewHolder;
//
//            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            if (convertView == null) {
//
//                convertView = layoutInflater.inflate(R.layout.lis_item_show_header, parent, false);
//                viewHolder = new ViewHolder(convertView);
//                convertView.setTag(viewHolder);
//
//
//
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//
//            viewHolder.qtyTextView.setText(sells[position].getCount());
//            viewHolder.descTextView.setText(sells[position].getFoodName());
//            viewHolder.itemTextView.setText(position+1);
//
//
//
//
//            return convertView;
//        }
//    }

    private void queryItemSell() {
        List<Sell> sells = dbHelper.getSellWithCount();

        bodyLinearLayout.removeAllViews();
        if (!(sells.get(0).getFoodName().equals("No Data") || sells.get(0).getFoodName().equals("Error"))) {


            Sell sell = new Sell();
            sell.setFoodName("รายการ");
            sell.setCount("จำนวน");
            sell.setMrtAmount("ราคา");

            sells.add(0, sell);

            for (int i = 0; i < sells.size(); i++) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.lis_item_show_header, null);
                TextView itemTextView = view.findViewById(R.id.txt_price);
                TextView qtyTextView = view.findViewById(R.id.txt_qty);
                TextView descTextView = view.findViewById(R.id.txt_desc);

                if (i != 0) {
                    descTextView.setGravity(Gravity.LEFT);
                }

                itemTextView.setText(sells.get(i).getMrtAmount());
                qtyTextView.setText(sells.get(i).getCount());
                descTextView.setText(sells.get(i).getFoodName());

                bodyLinearLayout.addView(view);
            }

//            Sell[] sells1 = new Sell[sells.size()];
//            sells.toArray(sells1);
//            ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(MainActivity.this,sells1.length,MainActivity.this,sells1);
//            sellListView.setAdapter(itemArrayAdapter);

        }


    }

    private void querySell() {
        Log.d(TAG, "querySell: " + dbHelper.getTotalSell());

        String sell = String.valueOf(dbHelper.getTotalSell());


        txtDisplayAmt.setText(sell);
        textDisplay = sell;


    }

    private void GetIntent() {
        if (getIntent().getStringExtra("ChooseStore") != null) {
            chooseStore = getIntent().getStringExtra("ChooseStore");
            ip = getIntent().getStringExtra("IP");
        }
        if (getIntent().getStringExtra("SAVE") != null) {

        } else {
            resetAll();
            dbHelper.clearSell();
            queryItemSell();
        }
    }

    private void enableAnything() {
        btnReset.setEnabled(true);
        btnscan.setEnabled(true);
        edtBarcodeScan.setEnabled(true);
        edtBarcodeScan.setFocusable(true);
        edtBarcodeScan.setFocusableInTouchMode(true);
        edtBarcodeScan.requestFocus();
    }

    private void disabledAnything() {
        btnscan.setEnabled(false);
        btnReset.setEnabled(false);
        edtBarcodeScan.setEnabled(false);
    }

    private void loadCompanyProfile() {

        ip = Utils.getIPAddress(true);
        if (getIntent().getStringExtra("ShopName") != null) {
            if (getIntent().getStringExtra("ShopName").equals("")) {
                chooseStore = "";
                dbHelper.clearSell();
            } else {
                chooseStore = getIntent().getStringExtra("ShopName");
            }

        } else {
            chooseStore = "";
        }

        ConnectionHelper connectionHelper = new ConnectionHelper();
        Connection connection = connectionHelper.connection(MainActivity.this);
        if (connection == null) {
            Toast.makeText(MainActivity.this, "Check your internet access", Toast.LENGTH_SHORT).show();
        } else {

            String query = "select * from dbfood..COMPANYPROFILE";
            getData = new GetData(query, "GetCompanyProfile", MainActivity.this);
            data = getData.doInBackground();

            Log.d(TAG, "queryCompany: " + data);
            companyProfile = new String[12];
            if (data.size() > 0) {
                companyProfile[0] = data.get(0).get("COMID");
                companyProfile[1] = data.get(0).get("BRNID");
                companyProfile[2] = data.get(0).get("BRNNAMETHAI");
                companyProfile[3] = data.get(0).get("BRNNAMEENG");
                companyProfile[4] = data.get(0).get("BRNTAXID");
                companyProfile[5] = data.get(0).get("BRNVAT");
                companyProfile[6] = data.get(0).get("ADDRESS1") + " " + data.get(0).get("ADDRESS2") + " " + data.get(0).get("REMARK");
            }

            query = "select distinct MRIP,POSID,SHOPNAME,OWNER,SHOPID from dbfood..VSHOPS where MRIP = '" + ip + "' and SHOPNAME like '%" + chooseStore + "%'  and CONTSTATUS = '1'";
            Log.d(TAG, "onActivityCreated: " + query);

            getData = new GetData(query, "GetPOSData", MainActivity.this);
            data = getData.doInBackground();

            if (data.size() > 0) {
                companyProfile[7] = data.get(0).get("MRIP");
                companyProfile[8] = data.get(0).get("POSID");
                companyProfile[9] = data.get(0).get("SHOPNAME");
                companyProfile[10] = data.get(0).get("SHOPID");
                chooseStore = companyProfile[9];
            } else {

            }

            query = "select s.*,t.TTAXID,t.TERMINALID,t.TID,t.DESCRIPTION,t.INVOICE,t.CREDITNOTE from dbfood..SHOPS s inner join dbfood..TERMINAL t on t.TIP = s.MRIP where MRIP = '" + ip + "' and POSID = '" + companyProfile[8] + "'";
            getData = new GetData(query, "GetStoreAndTerminal", MainActivity.this);
            data = getData.doInBackground();

            Log.d(TAG, "loadCompanyProfile: " + data.size());

            if (data.size() == 1) {


                companyProfile[11] = data.get(0).get("TTAXID");

            } else {
                if (data.size() == 0) {

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage("ไม่พบการตั้งค่าจุดขาย กรุณาตรวจสอบ ip ที่ backoffice");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("SETTING", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
                            intent.putExtra("Class", "MainActivity");
                            intent.putExtra("ShopName", chooseStore);
                            intent.putExtra("Status", "True");

                            startActivity(intent);
                            finish();

                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();

                } else {

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Alert");
                    builder.setCancelable(false);
                    builder.setMessage("จุดขายมีมากกว่า 1 จุด กรุณาตรวจสอบ ip ที่ backoffice");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();

                }

            }

            Log.d(TAG, "queryCompany: " + data);


            companyTextView.setText(companyProfile[2]);
            branchTextView.setText(companyProfile[3]);
            posIdTextView.setText(companyProfile[8]);
            posNoTextView.setText(companyProfile[11]);
            ipNoTextView.setText(Utils.getIPAddress(true));
            storeNameTextView.setText(companyProfile[9]);

            queryTotalSellDate();
        }


    }

    @Override
    public void onBackPressed() {

    }

    private void barcodeController() {

        edtBarcodeScan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                Log.d(TAG, "onKey: " + edtBarcodeScan.getText());
                Log.d(TAG, "onKey: " + edtBarcodeScan.length());

                Log.d(TAG, "onKey: " + edtBarcodeScan.getText().toString().trim().length());
                Log.d(TAG, "onKey: " + edtBarcodeScan.getText().toString().length());
//                Log.d(TAG, "onKey: " + edtBarcodeScan.getText().toString().trim().length());
//                if (keyEvent.getAction() == KeyEvent.ACTION_UP) {


                if (edtBarcodeScan.length() >= 11) {


                    if (edtBarcodeScan.length() >= 12) {
                        if (edtBarcodeScan.getText().charAt(11) == '\n' || edtBarcodeScan.getText().charAt(12) == '\n')
                            Log.d(TAG, "onKey: " + edtBarcodeScan.getText().toString());
                        paymentBarcodeString = edtBarcodeScan.getText().toString();
                        paymentCash(paymentBarcodeString.trim());

                    } else if (edtBarcodeScan.length() == 12 || edtBarcodeScan.length() == 11) {

//
                        Log.d(TAG, "onKey: " + edtBarcodeScan.getText().toString());
                        paymentBarcodeString = edtBarcodeScan.getText().toString();
                        paymentCash(paymentBarcodeString.trim());

                    }
                }
//                }

                return false;
            }
        });

    }

    private boolean GetDateWorking() {
        String query = "select top 1 case when DATEDIFF(dy, GETDATE(), DATEWORKING) = '0' then 'Y' else 'N' end as date_flag  from dbfood..WORKINGDATE";
        getData = new GetData(query, "GetWorkingDate", MainActivity.this);
        data = getData.doInBackground();

        Log.d(TAG, "GetDateWorking: " + data);
        Log.d(TAG, "GetDateWorking: " + query);
        if (data.size() > 0) {
            if (data.get(0).get("date_flag").equals("Y")) {
                return true;
            } else if (data.get(0).get("date_flag").equals("N")) {
                return false;
            }
        }

        return false;

    }

    private void paymentCash(final String paymentBarcodeString) {

        num++;
        final String cused;
        String barcode = paymentBarcodeString;
        String shopid = companyProfile[10];
        String posid = companyProfile[8];
        String mrtamount = "";
        String mrtstatus = "S";
        String mrtslipno = "";
        String tid = "";
        String uid = "";
        String backupflg = "0";
        String chkflg = "0";
        String voidno = "";
        String cused1 = "";
        String foodname = "อาหารและเครื่องดื่ม";
        String shopendflg = "0";
        String gp = "";
        String gpamount = "";
        String gprule = "";
        String vendorcode = "";
        final String cbalance;
        String ref = "";
        String sh_id = "";
        String running = "";
        String mrtslipnowithno = "";

        final String amount = txtDisplayAmt.getText().toString();

        CardDetailModel cardDetailModel = new CardDetailModel();
        final List<CardDetailModel> cardDetailModels = new ArrayList<>();

        Log.d(TAG, "paymentCash: " + posid);

        if (num == 1) {
            if (Integer.parseInt(amount) == 0) {
                //Add Amount

                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert!!");
                builder.setMessage("Please Enter PRICE Before Payment");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                    resetAll();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();


            } else {

                String[] waterStrings = new String[0];

                String query = "select typeid from dbfood..cardtype where TYPEDESCRIPTION like '%โปร 19 ทรูพอยท์%'";
                getData = new GetData(query, "GetTypeTrueFreeWater", MainActivity.this);
                data = getData.doInBackground();
                if (data.size() > 0) {
                    waterStrings = new String[data.size()];

                    for (int i = 0; i < data.size(); i++) {
                        waterStrings[i] = data.get(i).get("TYPEID");
                    }
                }


                query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + paymentBarcodeString.trim() + "'";
                getData = new GetData(query, "GetCardDetail", MainActivity.this);
                data = getData.doInBackground();

                Log.d(TAG, "onKey: Query 1 " + query);
                Log.d(TAG, "onKey: Data 1 " + data);

                if (data.size() > 0) {
                    cused = data.get(0).get("CUSED");
                    cused1 = data.get(0).get("CUSED1");
                    cbalance = data.get(0).get("CBALANCE");

                    String typeid = data.get(0).get("TYPEID");
                    int c = 0;
                    if (waterStrings.length != 0) {
                        for (int i = 0; i < waterStrings.length; i++) {
                            if (typeid.equals(waterStrings[i].trim())) {
                                c++;
                            }
                        }
                    }
                    if (c != 0) {
                        //Promotion True แลกน้ำ
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                            resetAll();
                                resetScan();
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    } else {
                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                            //Card Expired


                            AlertDialog alertDialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Payment Fail!!");
                            builder.setMessage("Card Expired on " + data.get(0).get("CEXPIRED"));
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                resetAll();
                                }
                            });

                            alertDialog = builder.create();
                            alertDialog.show();

                        } else {
                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                //Card Status Can Use

                                final AddCardModel addCardModel = new AddCardModel();


                                query = "select * from dbfood..SHOPS where MRIP = '" + ip + "'  and SHOPNAME like '%" + chooseStore + "%' ";
                                getData = new GetData(query, "GetStore", MainActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    running = data.get(0).get("SLIPNO");
                                    int running_int = Integer.parseInt(running);
                                    running_int++;
                                    running = Utils.GetRunningFormat6Unit(String.valueOf(running_int));
                                    mrtslipno = data.get(0).get("POSID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running;
                                    mrtslipnowithno = mrtslipno + "-1";
                                    gp = data.get(0).get("SHAREPERCENT");

                                    gprule = data.get(0).get("GPRULE");
                                    double gpamount_float = 0.000;
                                    gpamount_float = Double.parseDouble(gp) * 1.000;
                                    gpamount_float = gpamount_float * Double.parseDouble(amount) / 100.000;
                                    gpamount = String.format("%.3f", gpamount_float);

                                }

                                if (Integer.parseInt(cbalance) < Integer.parseInt(amount)) {
                                    //Card Balance NOT Enough
                                    final int[] total_cbalance = {0};
                                    final boolean[] checkComplete = {false};

                                    LayoutInflater layoutInflater = getLayoutInflater();

                                    final View view1 = layoutInflater.inflate(R.layout.payment_dialog, null);
                                    final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(MainActivity.this)
                                            .setView(view1)
                                            .setCancelable(false)
                                            .setPositiveButton("ตกลง", null)
                                            .setNegativeButton("ต่อบัตร", null)
                                            .create();
                                    final String finalMrtslipno = mrtslipno;
                                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialogInterface) {

                                            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                            final LinearLayout linearLayout = view1.findViewById(R.id.linDialog);
                                            final EditText editText = view1.findViewById(R.id.edt_payment);
                                            final TextView titleTextView = view1.findViewById(R.id.txt_title);
                                            final TextView messageTextView = view1.findViewById(R.id.txt_message);

                                            String barcode = "";
                                            barcode = paymentBarcodeString.replace("\n", "");

                                            Log.d(TAG, "onShow: " + barcode);


                                            linearLayout.setVisibility(View.VISIBLE);
                                            editText.setVisibility(View.GONE);

                                            int lmoney = Integer.valueOf(amount) - total_cbalance[0] - Integer.valueOf(cbalance);

                                            titleTextView.setText("ยอดเงินไม่พอ");
                                            messageTextView.setGravity(Gravity.LEFT);
                                            String messageText = "ราคาอาหาร " + amount + "\nยอดเงินคงเหลือในบัตร " + cbalance + " บาท\nขาดเงิน " + lmoney + " บาท";
                                            messageTextView.setText(messageText);


                                            final String finalBarcode = barcode;
                                            negativeButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    alertDialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, AddMoreCardsActivity.class);

                                                    intent.putExtra("amount_remain", amount);
                                                    intent.putExtra("first_card", paymentBarcodeString);
                                                    intent.putExtra("Class", "MainActivity");
                                                    intent.putExtra("ShopName", chooseStore);
                                                    intent.putExtra("Status", "True");
                                                    intent.putExtra("ip", ip);
                                                    intent.putExtra("companyprofile", companyProfile);

                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });

                                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    checkComplete[0] = true;
                                                    alertDialog.dismiss();
                                                    resetScan();
                                                }

                                            });

                                        }
                                    });

                                    alertDialog.show();


                                } else {

                                    query = "select * from dbfood..SHOPS where MRIP = '" + ip + "'  and SHOPNAME like '%" + chooseStore + "%' ";
                                    getData = new GetData(query, "GetStore", MainActivity.this);
                                    data = getData.doInBackground();

                                    if (data.size() > 0) {
                                        int cbalance_after = Integer.valueOf(cbalance) - Integer.valueOf(amount);

                                        StringBuilder stringBuilder = new StringBuilder();

//                                    ........................................................

                                        List<Sell> sells = new ArrayList<>();
                                        sells = dbHelper.getSell();

                                        if (!(sells.get(0).getFoodName().equals("No Data") || sells.get(0).getFoodName().equals("Error"))) {


                                            for (int i = 0; i < sells.size(); i++) {

                                                mrtslipnowithno = mrtslipno + "-" + (i + 1);

                                                if (data.get(0).get("SHAREPERCENTFLAG").equals("1")) {
                                                    gp = data.get(0).get("SHAREPERCENT");
                                                    gprule = data.get(0).get("GPRULE");
                                                } else {
                                                    if (sells.get(i).getBtnFlag().equals("1")) {

                                                        gp = sells.get(i).getBtnGP();
                                                        gprule = sells.get(i).getBtnGPRule();

                                                    } else {

                                                        gp = sells.get(i).getBtnGP2();
                                                        gprule = sells.get(i).getBtnGPRule2();
                                                    }
                                                }

                                                Double gpamount_float = 0.000;
                                                gpamount_float = Double.parseDouble(gp) * 1.000;
                                                Log.d(TAG, "List Sells : " + gpamount_float);
                                                Log.d(TAG, "List Sells : " + sells.get(i).getMrtAmount());
                                                Log.d(TAG, "List Sells : " + Double.parseDouble(sells.get(i).getMrtAmount()));
                                                gpamount_float = gpamount_float * Double.parseDouble(sells.get(i).getMrtAmount()) / 100.000;
                                                gpamount = String.format("%.3f", gpamount_float);

                                                stringBuilder.append("INSERT INTO CARDTRANS (CUSED ,BARCODE ,SHOPID ,POSID ,MRTAMOUNT ,MRTSTATUS ,MRTSLIPNO ,MRTLUPDATE ,TID ,UID ,LUPDATE ,BACKUPFLG ,CHKFLG ,VOIDNO ,CUSED1 ,FOODNAME ,SHOPENDFLG ,GP ,GPAMOUNT, GPRULE ) VALUES " +
                                                        "('" + cused + "' ,'" + paymentBarcodeString + "' ,'" + shopid + "','" + posid + "','" + sells.get(i).getMrtAmount() + "' ,'" + mrtstatus + "','" + mrtslipnowithno + "' ,getdate() ,'' ,'' ,getdate(),'0' ,'0' ,'' ," + cused1 + " ,'" + sells.get(i).getFoodName() + "' ,'0' ,'" + gp + "' ,'" + gpamount + "','" + gprule + "');");

                                            }

                                            stringBuilder.append("UPDATE CARDS set cbalance = '" + cbalance_after + "' where BARCODE = '" + paymentBarcodeString.trim() + "' and cused = '" + cused + "';");
                                        }


                                        if (Integer.parseInt(running) > 999999) {
                                            running = "000001";
                                        }
                                        stringBuilder.append("update SHOPS set SLIPNO = '" + running + "' where SHOPID = '" + shopid + "';");
                                        getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
                                        getData.doInBackgroundUpdate();


                                        if (cbalance_after == 0) {
                                            //Card End
                                            stringBuilder = new StringBuilder("update dbfood..CARDS set CSTATUS = 'E' , CLUPDATE = GETDATE()  where BARCODE = '" + paymentBarcodeString.trim() + "' and cused = '" + cused + "';");
                                            Log.d(TAG, "insertCardtrans: " + query);
                                            getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
                                            getData.doInBackgroundUpdate();
                                            stringBuilder = new StringBuilder("select SCEID,TOTALCARD from dbfood..SHOPCARDEND where POSID = '" + companyProfile[8] + "' and convert(varchar(10),CARDENDDATE, 120) = CONVERT(varchar(10),getdate(),120) and SHOPID = '" + companyProfile[10] + "';");
                                            Log.d(TAG, "insertCardtrans: " + query);
                                            getData = new GetData(stringBuilder.toString(), "GetShopCardEnd", MainActivity.this);
                                            data = getData.doInBackground();
                                            Log.d(TAG, "insertCardtrans: =====> " + data);
                                            if (data.size() > 0) {
                                                String id = data.get(0).get("SCEID");
                                                String totalCard = data.get(0).get("TOTALCARD");
                                                stringBuilder = new StringBuilder("update dbfood..SHOPCARDEND set TOTALCARD = '" + (Integer.valueOf(totalCard) + 1) + "',LUPDATE = getdate() where SCEID = '" + id + "';");
                                                Log.d(TAG, "insertCardtrans: " + stringBuilder);
                                                getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
                                                getData.doInBackgroundUpdate();
                                            } else {
                                                stringBuilder = new StringBuilder("insert into dbfood..SHOPCARDEND (POSID,TOTALCARD,CARDENDDATE,RETURNCARD,LUPDATE,UID,RETURNFLG,SHOPID) VALUES ('" + companyProfile[8] + "','1',getdate(),'0',getdate(),'" + companyProfile[8] + "','0','" + companyProfile[10] + "');");
                                                Log.d(TAG, "insertCardtrans: " + query);
                                                getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
                                                getData.doInBackgroundUpdate();
                                            }

                                            MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.beep);
                                            mediaPlayer.setLooping(true);
                                            mediaPlayer.start();
                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setTitle("Alert!!!");
                                            builder.setMessage("บัตรหมดมูลค่า กรุณาเก็บบัตรไว้ที่ร้านค้า\nPlease keep card at the store");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                resetAll();
                                                }
                                            });
                                            alertDialog = builder.create();
                                            alertDialog.show();
                                            alertDialog.getWindow().getAttributes();

                                            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                                            textView.setTextSize(30);
                                            for (int i = 0; i < 3; i++) {
                                                try {
                                                    Thread.sleep(950);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                if (i == 2) {
                                                    mediaPlayer.stop();
                                                }

                                            }

                                        }

                                        doprintwork("ใบบันทึกรายการ", "center_header");
                                        doprintwork("POS#" + companyProfile[8], "left_body");
                                        doprintwork("Slip No :" + mrtslipno, "left_body");
                                        doprintwork("ชื่อร้าน : " + companyProfile[9], "left_body");
                                        doprintwork("---------------------------------", "left_body");

                                        int total = 0;
                                        for (int i = 0; i < sells.size(); i++) {

                                            total = total + Integer.parseInt(sells.get(i).getMrtAmount());
//                                            doprintwork(sells.get(i).getFoodName() + "         " + sells.get(i).getMrtAmount(), "left_body");
                                        }
                                        doprintwork("อาหารและเครื่องดื่ม           " + total + ".00", "left_body");

                                        doprintwork("---------------------------------", "left_body");
                                        doprintwork("รวม                       " + txtDisplayAmt.getText().toString() + " บาท", "left_body");
                                        doprintwork(paymentBarcodeString.trim() + " : " + cused + " เหลือ " + cbalance_after + " บาท", "");
                                        doprintwork("วันที่ " + Utils.getCurrentDateFromFormat("dd/MM/") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yyyy")) + 543) + " " + Utils.getCurrentDateFromFormat("kk:mm:ss"), "left_body");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");
                                        doprintwork(" ", "");


                                        resetAll();
                                    }
                                }

                            } else {
                                //Card Status Can NOT Use

                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Payment Fail!!");
                                builder.setMessage("Card NOT have money.\nPlease!! contact OFFICER.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                    resetAll();
                                        resetScan();
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }

                    }
                } else {
                    //Card Not Have In DB

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Payment Fail!!");
                    builder.setMessage("Card can NOT used.\nPlease contact OFFICER.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                        resetAll();
                            resetScan();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }


    }

    private void resetScan() {

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("ChooseStore", chooseStore);
        intent.putExtra("ShopName", companyProfile[9]);
        intent.putExtra("IP", ip);
        intent.putExtra("SAVE", "SAVE");
        startActivity(intent);
        finish();
    }

    private CardDetailModel addCard(List<Map<String, String>> data) {
        Log.d(TAG, "addCard: " + data);
        CardDetailModel cardDetailModel = new CardDetailModel();

        cardDetailModel.setBARCODE(data.get(0).get(CardDetailModel.Column.BARCODE));
        cardDetailModel.setCBALANCE(data.get(0).get(CardDetailModel.Column.CBALANCE));
        cardDetailModel.setCEXPIRED(data.get(0).get(CardDetailModel.Column.CEXPIRED));
        cardDetailModel.setCEXPIRED_FLAG(data.get(0).get(CardDetailModel.Column.CEXPIRED_FLAG));
        cardDetailModel.setCSTATUS(data.get(0).get(CardDetailModel.Column.CSTATUS));
        cardDetailModel.setCSTATUS_FLAG(data.get(0).get(CardDetailModel.Column.CSTATUS_FLAG));
        cardDetailModel.setCUSED(data.get(0).get(CardDetailModel.Column.CUSED));
        cardDetailModel.setCUSED1(data.get(0).get(CardDetailModel.Column.CUSED1));
        cardDetailModel.setTYPEID(data.get(0).get(CardDetailModel.Column.TYPEID));

        return cardDetailModel;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                resetAll();
                dbHelper.clearSell();
                break;
            case R.id.btn_payment:

                //Payment Choose
                if (!txtDisplayAmt.getText().toString().equals("0")) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                    builderSingle.setTitle("Select Payment Method");

//                    final String[] paymentStrings = {"TRUE WALLET", "ALIPAY", "WECHAT", "THAIQR"};
                    final String[] paymentStrings = {"TRUE WALLET"};
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.addAll(paymentStrings);

                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            String isv = companyProfile[8] + Utils.getCurrentDateFromFormat("yyyyMMddkkmmss"); ///////////////////////////////////////////////////////////////////////////////////////////

                            switch (paymentStrings[which]) {
                                case "TRUE WALLET":

                                    //Try
                                    GHLRequestQRModel.xml xml = new GHLRequestQRModel.xml();

                                    xml.setPos_ref_no(isv);
                                    xml.setAmount(Double.parseDouble(txtDisplayAmt.getText().toString()) * 10.00);
                                    xml.setService_type("SHOWQR");
                                    xml.setTrade_type("TRUEMONEY");
                                    xml.setTransaction_type("SALE");


                                    String cmd = xml.getXML();
                                    Log.d(TAG, "onClick: " + cmd);


                                    GHLWifiConnect ghlWifiConnect = new GHLWifiConnect();
//                                    ghlWifiConnect.execute(termIP, cmd);

                                    //Try


                                    DolfinPayProcess();
                                    dialog.dismiss();
                                    break;
                                case "ALIPAY":


                                    //Try
                                    xml = new GHLRequestQRModel.xml();

                                    xml.setPos_ref_no(isv);
                                    xml.setAmount(Float.parseFloat(txtDisplayAmt.getText().toString()) * 10.00);
                                    xml.setService_type("SHOWQR");
                                    xml.setTrade_type("ALIPAY");
                                    xml.setTransaction_type("SALE");


                                    cmd = xml.getXML();
                                    Log.d(TAG, "onClick: " + cmd);


                                    ghlWifiConnect = new GHLWifiConnect();
                                    ghlWifiConnect.execute(termIP, cmd);

                                    //Try


//                                    DolfinPayProcess();
                                    dialog.dismiss();
                                    break;
                                case "WECHAT":


                                    //Try
                                    xml = new GHLRequestQRModel.xml();

                                    xml.setPos_ref_no(isv);
                                    xml.setAmount(Float.parseFloat(txtDisplayAmt.getText().toString()) * 10.00);
                                    xml.setService_type("SHOWQR");
                                    xml.setTrade_type("WECHATPAY");
                                    xml.setTransaction_type("SALE");


                                    cmd = xml.getXML();
                                    Log.d(TAG, "onClick: " + cmd);


                                    ghlWifiConnect = new GHLWifiConnect();
                                    ghlWifiConnect.execute(termIP, cmd);

                                    //Try


//                                    DolfinPayProcess();
                                    dialog.dismiss();
                                    break;
                                case "THAIQR":


                                    //Try
                                    xml = new GHLRequestQRModel.xml();

                                    xml.setPos_ref_no(isv);
                                    xml.setAmount(Float.parseFloat(txtDisplayAmt.getText().toString()) * 10.00);
                                    xml.setService_type("SHOWQR");
                                    xml.setTrade_type("THAIQRCODE");
                                    xml.setTransaction_type("SALE");


                                    cmd = xml.getXML();
                                    Log.d(TAG, "onClick: " + cmd);


                                    ghlWifiConnect = new GHLWifiConnect();
                                    ghlWifiConnect.execute(termIP, cmd);

                                    //Try


//                                    DolfinPayProcess();
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });
                    builderSingle.show();
                } else {

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Alert!!");
                    builder.setMessage("Please Enter PRICE Before Payment");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();

                }
//                DolfinPayProcess();


                break;

            case R.id.btn_scan:


                break;
        }
    }

    public InputStream openBin(String filename) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getPath());
        InputStream input = new FileInputStream(file);
        return input;
    }

    private void DolfinPayProcess() {

        //find tender shop id

        for (int i = 0; i < tenderModel.length; i++) {
            if (tenderModel[i].getCODE().equals("TRUEWALLET")) {

                Log.d(TAG, "onClick: " + tenderModel[i].getCODE() + " " + tenderModel[i].getVENDER_SHOP_ID());

                tendorid = tenderModel[i].getVENDER_SHOP_ID();

                i = tenderModel.length;

            }
        }


        LayoutInflater layoutInflater = getLayoutInflater();

        final View view1 = layoutInflater.inflate(R.layout.payment_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(view1)
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Pay", null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {


                final EditText disableSoftKeyBoardEditText = view1.findViewById(R.id.edt_payment);
//                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!disableSoftKeyBoardEditText.getText().equals("")) {



                            int int_total = Integer.parseInt(txtDisplayAmt.getText().toString());

                            if (int_total > 0) {
                                String isv = companyProfile[8] + Utils.getCurrentDateFromFormat("yyyyMMddHHmmss"); ///////////////////////////////////////////////////////////////////////////////////////////


                                TruePaymentRequestModel.Metadata metadata = new TruePaymentRequestModel.Metadata(companyProfile[10], companyProfile[9],tendorid);
                                TruePaymentRequestModel truePaymentRequestModel = new TruePaymentRequestModel();
                                truePaymentRequestModel.setIsv_payment_ref(isv);
                                truePaymentRequestModel.setCurrency("THB");
                                truePaymentRequestModel.setDescription(companyProfile[9]);
                                truePaymentRequestModel.setMerchant_id("010000000009610012083");
                                truePaymentRequestModel.setPayment_code(disableSoftKeyBoardEditText.getText().toString().replace("\n", ""));
                                truePaymentRequestModel.setPayment_method("BALANCE");
                                truePaymentRequestModel.setRequest_amount(String.valueOf(int_total * 100));
                                truePaymentRequestModel.setMetadata(metadata);

                                Gson gson = new Gson();
                                String json = gson.toJson(truePaymentRequestModel);
                                String url = apiUrl + "/trueapi/request";

                                Log.d(TAG, "onClick: URL " + url);
                                Log.d(TAG, "onClick: JSON " + json);

                                TruePayment truePayment = new TruePayment();
                                truePayment.setDolfinRequestModel(truePaymentRequestModel);
                                truePayment.execute(url, json);

                                alertDialog.dismiss();


                            }


                        }
                    }
                });

                disableSoftKeyBoardEditText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            if (!disableSoftKeyBoardEditText.getText().equals("")) {

                                int int_total = Integer.parseInt(txtDisplayAmt.getText().toString());

                                if (int_total > 0) {
                                    String isv = companyProfile[8] + Utils.getCurrentDateFromFormat("yyyyMMddHHmmss"); ///////////////////////////////////////////////////////////////////////////////////////////


                                    TruePaymentRequestModel.Metadata metadata = new TruePaymentRequestModel.Metadata(companyProfile[10], companyProfile[9],tendorid);
                                    TruePaymentRequestModel truePaymentRequestModel = new TruePaymentRequestModel();
                                    truePaymentRequestModel.setIsv_payment_ref(isv);
                                    truePaymentRequestModel.setCurrency("THB");
                                    truePaymentRequestModel.setDescription(companyProfile[9]);
                                    truePaymentRequestModel.setMerchant_id("010000000009610012083");
                                    truePaymentRequestModel.setPayment_code(disableSoftKeyBoardEditText.getText().toString().replace("\n", ""));
                                    truePaymentRequestModel.setPayment_method("BALANCE");
                                    truePaymentRequestModel.setRequest_amount(String.valueOf(int_total * 100));
                                    truePaymentRequestModel.setMetadata(metadata);

                                    Gson gson = new Gson();
                                    String json = gson.toJson(truePaymentRequestModel);
                                    String url = apiUrl + "/trueapi/request";

                                    Log.d(TAG, "onClick: URL " + url);
                                    Log.d(TAG, "onClick: JSON " + json);

                                    TruePayment truePayment = new TruePayment();
                                    truePayment.setDolfinRequestModel(truePaymentRequestModel);
                                    truePayment.execute(url, json);

                                    alertDialog.dismiss();


                                }


                            }

                        }
                        return false;
                    }
                });

            }
        });

        alertDialog.show();
    }

    void doprintwork(String msg, String op) {
        Intent intentService = new Intent(MainActivity.this, PrintBillService.class);
        intentService.putExtra("SPRT", msg);
        intentService.putExtra("OP", op);
        MainActivity.this.startService(intentService);
    }

    private Bitmap createBitmapFromLayout(View tv) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(spec, spec);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(tv.getMeasuredWidth(), tv.getMeasuredWidth(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate((-tv.getScrollX()), (-tv.getScrollY()));
        tv.draw(c);
        return b;
    }

    public void convertCertViewToImage(View view) {

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false); // clear drawing cache
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(getExternalFilesDir(null).getAbsolutePath() + File.separator + "Test" + File.separator + "test.jpg");

        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class TrueInquiry extends AsyncTask<String, Void, String> {


        TrueInquiryRequest trueInquiryRequest;

        String url, json;
        ProgressDialog progressDialog;
        int count;

        public void setCount(int count) {
            this.count = count;
        }

        public void setTrueInquiryRequest(TrueInquiryRequest trueInquiryRequest) {
            this.trueInquiryRequest = trueInquiryRequest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Inquiry", "Waiting...", true, false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute: " + s);
            progressDialog.dismiss();
            if (!s.equals("")) {
                final Gson gson = new Gson();

                TrueInquiryResponse trueInquiryResponse = gson.fromJson(s, TrueInquiryResponse.class);

                if (trueInquiryResponse != null) {

                    if (trueInquiryResponse.getStatus().getCode().equals("success")) {
                        final String tax_inv = SaveDolfinDataToDatabaseInquiry(trueInquiryResponse, trueInquiryRequest);
                        if (tax_inv.equals("")) {

                            Log.d(TAG, "onPostExecute: tax " + tax_inv);

                            String comp = companyProfile[2].replace("สาขา", "\nสาขา");
                            List<Sell> sells = dbHelper.getSell();

                            doprintwork(comp, "left_header");
                            doprintwork("     ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ", "left_body");
//                            doprintwork("Receipt/Tax Invoice(ABB)", "center_body2");
                            doprintwork("  (VAT Included)", "center_body");
                            doprintwork("---------------------------------", "left_body");
                            doprintwork("TAX#" + companyProfile[11], "left_body");
                            doprintwork("POS#" + companyProfile[8], "left_body");
                            doprintwork("TAX INV : " + tax_inv, "left_body");
                            doprintwork("ชื่อร้าน : " + companyProfile[9], "left_body");
                            doprintwork("---------------------------------", "left_body");

                            int total = 0;
                            for (int i = 0; i < sells.size(); i++) {

                                total = total + Integer.parseInt(sells.get(i).getMrtAmount());
//                                            doprintwork(sells.get(i).getFoodName() + "         " + sells.get(i).getMrtAmount(), "left_body");
                            }
                            doprintwork("อาหารและเครื่องดื่ม           " + total + ".00", "left_body");
                            doprintwork("---------------------------------", "left_body");
                            doprintwork("จำนวนรวม                                    " + txtDisplayAmt.getText().toString() + ".00", "left_body");
                            doprintwork("     **** บัตร True Money Wallet ****", "left_body");
                            doprintwork("Payment ID \n" + trueInquiryResponse.getData().getMetadata().getPaymentCode(), "left_body");
                            doprintwork("วันที่ " + Utils.getCurrentDateFromFormat("dd/MM/") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yyyy")) + 543) + " " + Utils.getCurrentDateFromFormat("kk:mm:ss"), "left_body");

                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");

                            resetAll();
                        }
                    } else if (trueInquiryResponse.getStatus().getCode().equals("pending")) {
                        //inquiry data

                    } else {
                        AlertDialog alertDialog;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Payment Fail!!");
                        builder.setMessage(trueInquiryResponse.getStatus().getMessage());
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }

                } else {
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("จ่ายเงินไม่สำเร็จ");
                    builder.setMessage("ทำรายการจ่ายเงินไม่สำเร็จ กรุณากดปุ่มตรวจสอบ");
                    builder.setNegativeButton("ตรวจสอบ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            count++;

                            TrueInquiry trueInquiry = new TrueInquiry();
                            trueInquiry.setTrueInquiryRequest(trueInquiryRequest);
                            trueInquiry.setCount(count);
                            trueInquiry.execute(url, json);
                        }
                    });

                    if (count > 3) {
                        builder.setPositiveButton("ออก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetAll();
                            }
                        });
                    }
                    alertDialog = builder.create();
                    alertDialog.show();
                }

            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String json = strings[1];

            this.url = url;
            this.json = json;

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
            okHttpClient.setReadTimeout(600, TimeUnit.SECONDS);    // socket timeout
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = null;

            try {
                response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    public class TruePayment extends AsyncTask<String, Void, String> {

        TruePaymentRequestModel dolfinRequestModel = new TruePaymentRequestModel();
        ProgressDialog progressDialog;

        public void setDolfinRequestModel(TruePaymentRequestModel dolfinRequestModel) {
            this.dolfinRequestModel = dolfinRequestModel;
        }

        TruePayment context = this;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (companyProfile[11] == null) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error!!");
                builder.setMessage("กรุณาตั้งค่า Terminal ของร้านค้าก่อนทำการขาย");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        context.cancel(true);

                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            } else {
                Log.d(TAG, "onPreExecute: 1");
                progressDialog = ProgressDialog.show(MainActivity.this, "Payment", "Waiting...", true, false);

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Log.d(TAG, "onPostExecute: " + s);
            progressDialog.dismiss();
            if (!s.equals("")) {
                if (s.equals("DateTimeError")) {
                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Payment Fail!!");
                    builder.setMessage("กรุณาตั้งค่าวันที่และเวลาใหม่ ก่อนชำระเงิน");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            resetAll();
                            resetScan();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else if (s.equals("TimeOut")) {

//                    AlertDialog alertDialog;
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Payment Fail!!");
//                    builder.setMessage("การจ่ายเงินไม่สำเร็จ กรุณาลองใหม่อีกครั้ง");
//                    builder.setCancelable(false);
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
////                            resetAll();
//                            resetScan();
//                        }
//                    });
//                    alertDialog = builder.create();
//                    alertDialog.show();

                    InquiryTrue(dolfinRequestModel);

                } else if (s.equals("null")) {
//                    AlertDialog alertDialog;
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Payment Fail!!");
//                    builder.setMessage("การจ่ายเงินไม่สำเร็จ กรุณาลองใหม่อีกครั้ง");
//                    builder.setCancelable(false);
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
////                            resetAll();
//                            resetScan();
//                        }
//                    });
//                    alertDialog = builder.create();
//                    alertDialog.show();
//
                    InquiryTrue(dolfinRequestModel);

                }else if (s.equals("{\"Message\":\"An error has occurred.\"}")){

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Payment Fail!!");
                    builder.setMessage("Server Error Please Try again later");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            resetAll();
                            resetScan();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }else {

                    final Gson gson = new Gson();

                    TruePaymentResponse dolfinResponseModel = gson.fromJson(s, TruePaymentResponse.class);


                    if (dolfinResponseModel.getStatus().getCode().equals("success")) {

                        final String tax_inv = SaveTrueDataToDatabase(dolfinResponseModel, dolfinRequestModel);

                        Log.d(TAG, "onPostExecute: tax " + tax_inv);

                        if (!tax_inv.equals("")) {

                            String comp = companyProfile[2].replace("สาขา", "\nสาขา");
                            List<Sell> sells = dbHelper.getSell();

                            doprintwork(comp, "left_header");
                            doprintwork("     ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ", "left_body");
//                            doprintwork("Receipt/Tax Invoice(ABB)", "center_body2");
                            doprintwork("  (VAT Included)", "center_body");
                            doprintwork("---------------------------------", "left_body");
                            doprintwork("TAX#" + companyProfile[11], "left_body");
                            doprintwork("POS#" + companyProfile[8], "left_body");
                            doprintwork("TAX INV : " + tax_inv, "left_body");
                            doprintwork("ชื่อร้าน : " + companyProfile[9], "left_body");
                            doprintwork("---------------------------------", "left_body");

                            int total = 0;
                            for (int i = 0; i < sells.size(); i++) {

                                total = total + Integer.parseInt(sells.get(i).getMrtAmount());
//                                            doprintwork(sells.get(i).getFoodName() + "         " + sells.get(i).getMrtAmount(), "left_body");
                            }
                            doprintwork("อาหารและเครื่องดื่ม           " + total + ".00", "left_body");
                            doprintwork("---------------------------------", "left_body");
                            doprintwork("จำนวนรวม                                    " + txtDisplayAmt.getText().toString() + ".00", "left_body");
                            doprintwork("     **** บัตร True Money Wallet ****", "left_body");
                            doprintwork("Payment ID \n" + dolfinResponseModel.getData().getPayment_id(), "left_body");
                            doprintwork("วันที่ " + Utils.getCurrentDateFromFormat("dd/MM/") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yyyy")) + 543) + " " + Utils.getCurrentDateFromFormat("kk:mm:ss"), "left_body");

                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");
                            doprintwork(" ", "");

                            resetAll();
                        }
                    } else if (dolfinResponseModel.getStatus().getCode().equals("pending")) {

                        //inquiry data


                    } else {
                        AlertDialog alertDialog;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Payment Fail!!");
                        builder.setMessage(dolfinResponseModel.getStatus().getMessage());
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        }


        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "doInBackground: " + isCancelled());
            if (!isCancelled()) {
                Log.d(TAG, "onPreExecute: 2");
                String url = strings[0];
                String json = strings[1];


                Log.d(TAG, "onPreExecute: 3 " + url + " ---- " + json);
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(url).post(requestBody).build();
                Response response = null;

                try {
                    response = okHttpClient.newCall(request).execute();


                    return response.body().string();
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: " + e.getMessage() + " Line : " + e.getStackTrace()[0].getLineNumber());
                    Log.d(TAG, "doInBackground: " + Arrays.toString(e.getStackTrace()) + " Line : " + e.getStackTrace()[0].getLineNumber());

                    if (e.getMessage().contains("Could not validate certificate: current time:")) {
                        return "DateTimeError";
                    } else {

                        e.printStackTrace();
                        return "TimeOut";
                    }
                }
            } else {
                return "";
            }
        }
    }

    public class QueryIsvAsyn extends AsyncTask<String, Void, String> {

        Context context;


        @Override
        protected String doInBackground(String... strings) {


            return null;
        }
    }

    private void InquiryTrue(TruePaymentRequestModel dolfinRequestModel) {

        TrueInquiryRequest trueInquiryRequest = new TrueInquiryRequest();

        trueInquiryRequest.setISVPaymentRef(dolfinRequestModel.getIsv_payment_ref());
        trueInquiryRequest.setTransactionDate(Utils.getCurrentDateFromFormat("yyyy-MM-dd'T'HH:mm:ss'+07:00'"));

        Gson gson = new Gson();

        String url = apiUrl + "/trueapi/queryisr";

        String json = gson.toJson(trueInquiryRequest);


        TrueInquiry trueInquiry = new TrueInquiry();
        trueInquiry.setTrueInquiryRequest(trueInquiryRequest);
        trueInquiry.setCount(0);
        trueInquiry.execute(url, json);


    }

    private void SaveDolfinPaymentLog(String option, DolfinRequestModel dolfinRequestModel, DolfinResponseModel dolfinResponseModel, DolfinCancelPaymentModel dolfinCancelPaymentModel) {
        StringBuilder query = new StringBuilder();


        if (option.equals("request")) {

            query.append("INSERT INTO dbfoodbackup..DOLFINPAYMENTLOG (df_transaction_type,df_auth_code,df_currency,df_merchant_no,df_merchant_order_no,df_total_amount) ")
                    .append("VALUES (").append("'RQ'").append(",'").append(dolfinRequestModel.getAuthCode()).append("','").append(dolfinRequestModel.getCurrency()).append("','")
                    .append(dolfinRequestModel.getMerchantNo()).append("','").append(dolfinRequestModel.getMerchantOrderNo()).append("','").append(dolfinRequestModel.getTotalAmount())
                    .append("');");

//            query.append("INSERT INTO dbfoodbackup..DOLFINLOG (df_ttype,df_auth_code,df_currency,df_merchant_no,df_merchant_order_no,df_total_amount,df_result_flag,df_result_code,df_result_info,df_merchant_user_id,df_fin_customer_id,df_device_info,df_transaction_no,df_time_end,df_attach,df_transaction_type,df_order_country,df_order_type,df_product_type) ")
//            .append("VALUES ('RQ',").append();
        } else if (option.equals("response")) {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            query.append("INSERT INTO dbfoodbackup..DOLFINPAYMENTLOG (df_transaction_type,df_auth_code,df_currency,df_merchant_no,df_merchant_order_no,df_total_amount,df_result_flag,df_result_code,df_result_info,df_merchant_user_id,df_fin_customer_id,df_device_info,df_transaction_no,df_time_end,df_attach) ")
                    .append("VALUES (").append("'RP'").append(",'").append(dolfinRequestModel.getAuthCode()).append("','").append(dolfinRequestModel.getCurrency()).append("','")
                    .append(dolfinRequestModel.getMerchantNo()).append("','").append(dolfinRequestModel.getMerchantOrderNo()).append("','").append(dolfinRequestModel.getTotalAmount())
                    .append("','").append(dolfinResponseModel.getResultFlag()).append("','").append(dolfinResponseModel.getResultCode()).append("','").append(dolfinResponseModel.getResultInfo())
                    .append("','").append(dolfinResponseModel.getMerchantUserId()).append("','").append(dolfinResponseModel.getFinCustomerId()).append("','")
                    .append(dolfinResponseModel.getDeviceInfo()).append("','").append(dolfinResponseModel.getTransactionNo()).append("','")
                    .append(dolfinResponseModel.getTimeEnd()).append("','").append(dolfinResponseModel.getAttach()).append("');");
        } else if (option.equals("cancel")) {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            query.append("INSERT INTO dbfoodbackup..DOLFINPAYMENTLOG (df_transaction_type,df_auth_code,df_currency,df_merchant_no,df_merchant_order_no,df_total_amount,df_result_flag,df_result_code,df_result_info,df_transaction_no) ")
                    .append("VALUES (").append("'CC'").append(",'").append(dolfinRequestModel.getAuthCode()).append("','").append(dolfinRequestModel.getCurrency()).append("','")
                    .append(dolfinRequestModel.getMerchantNo()).append("','").append(dolfinRequestModel.getMerchantOrderNo()).append("','").append(dolfinRequestModel.getTotalAmount())
                    .append("','").append(dolfinCancelPaymentModel.getResultFlag()).append("','").append(dolfinCancelPaymentModel.getResultCode()).append("','").append(dolfinCancelPaymentModel.getResultInfo())
                    .append("','").append(dolfinCancelPaymentModel.getTransactionNo()).append("');");
        }

        getData = new GetData(query.toString(), "Update", MainActivity.this);
        getData.doInBackgroundUpdate();


    }

    private File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            Log.d(TAG, "saveBitmap: " + path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    Log.d(TAG, "saveBitmap: Exception 1 " + e.getMessage() + " " + e.getStackTrace()[0].getLineNumber());
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        Log.d(TAG, "saveBitmap: IOException " + e.getMessage() + " " + e.getStackTrace()[0].getLineNumber());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "saveBitmap: Exception 2 " + e.getMessage() + " " + e.getStackTrace()[0].getLineNumber());
                e.printStackTrace();
            }
        }
        return file;
    }

    private void resetAll() {
        dbHelper.clearSell();

        txtDisplayAmt.setText("0");
        textDisplay = "0";
        edtBarcodeScan.setText("");


        queryItemSell();
//        queryTotalSellDate();
    }

    private String SaveGHLDataToDatabase(GHLResponseQRModel.xml responseXml) {


        String query = "select s.*,t.TTAXID,t.TERMINALID,t.TID,t.DESCRIPTION,t.INVOICE,t.CREDITNOTE from dbfood..SHOPS s inner join dbfood..TERMINAL t on t.tip = s.MRIP where MRIP = '" + ip + "' and POSID = '" + companyProfile[8] + "'";
        getData = new GetData(query, "GetStoreAndTerminal", MainActivity.this);
        data = getData.doInBackground();

        String amount = String.valueOf(Double.parseDouble(responseXml.getAmount()) / 10);
        if (data.size() > 0) {
//            String running = data.get(0).get("SLIPNO");
//            String shopid = data.get(0).get("SHOPID");
//            int running_int = Integer.parseInt(running);
//            running_int++;
//            running = Utils.GetRunningFormat6Unit(String.valueOf(running_int));
            String running_inv = data.get(0).get("INVOICE");

            int running_inv_int = Integer.parseInt(running_inv);
            running_inv_int++;
            running_inv = Utils.GetRunningFormat6Unit(String.valueOf(running_inv_int));

            String ttaxinv = data.get(0).get("TID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running_inv;
//            String mrtslipno = data.get(0).get("TID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running;

            String mrtslipnowithno = ttaxinv + "-1";

            String terminalid = data.get(0).get("TERMINALID");
            String gp = data.get(0).get("GPRULE");
            double gpamount_float = 0.000;
            gpamount_float = Double.parseDouble(gp) * 1.000;
            Log.d(TAG, "SaveDolfinDataToDatabase: " + gpamount_float);
            Log.d(TAG, "SaveDolfinDataToDatabase: " + amount);
            Log.d(TAG, "SaveDolfinDataToDatabase: " + Double.parseDouble(amount));
            gpamount_float = gpamount_float * Double.parseDouble(amount) / 100.000;
            String gpamount = String.format("%.3f", gpamount_float);
//            String vendorcode = data.get(0).get("VENDORCODE");
            String tid = data.get(0).get("TID");

            String ref = data.get(0).get("REF");
            String sh_id = data.get(0).get("SH_ID");

            StringBuilder stringBuilder = new StringBuilder();


//            stringBuilder.append("INSERT INTO CARDTRANS (CUSED,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,LUPDATE,FOODNAME,GP,GPAMOUNT,CBALANCE,REF,SH_ID,cardtendor,cardref,cardpurse,taxinvoice) VALUES ('0','TMN" + dolfinRequestModel.getPaymentCode() + "','" + companyProfile[10] + "','" + companyProfile[8] + "','" + amount + "','S','" + mrtslipno + "',getdate(),'" + tid + "',getdate(),'อาหารและเครื่องดื่ม','" + gp + "','" + gpamount + "','0','" + ref + "','" + sh_id + "','TrueWallet','" + dolfinResponseModel.getData().getPaymentID() + "','" + amount + "','" + ttaxinv + "');");
            List<Sell> sells = new ArrayList<>();
            sells = dbHelper.getSell();

            if (!(sells.get(0).getFoodName().equals("No Data") || sells.get(0).getFoodName().equals("Error"))) {


                for (int i = 0; i < sells.size(); i++) {

                    mrtslipnowithno = ttaxinv + "-" + (i + 1);

                    gp = sells.get(i).getBtnGP();
                    gpamount_float = 0.000;
                    gpamount_float = Double.parseDouble(gp) * 1.000;
                    Log.d(TAG, "List Sells : " + gpamount_float);
                    Log.d(TAG, "List Sells : " + sells.get(i).getMrtAmount());
                    Log.d(TAG, "List Sells : " + Double.parseDouble(sells.get(i).getMrtAmount()));
                    gpamount_float = gpamount_float * Double.parseDouble(sells.get(i).getMrtAmount()) / 100.000;
                    gpamount = String.format("%.3f", gpamount_float);

                    stringBuilder.append("INSERT INTO CARDTRANS (CUSED,CUSED1,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,UID,LUPDATE,FOODNAME,GP,GPAMOUNT,cardtendor,cardref,cardpurse,taxinvoice) VALUES (1,1,'G" + responseXml.getCard_no() + "'," + companyProfile[10] + ",'" + companyProfile[8] + "'," + sells.get(i).getMrtAmount() + ",'S','" + mrtslipnowithno + "',getdate(),'" + tid + "','" + tid + "',getdate(),'" + sells.get(i).getFoodName() + "'," + gp + "," + gpamount + ",'" + responseXml.getTrade_type() + "','" + responseXml.getTransaction_id() + "'," + sells.get(i).getMrtAmount() + ",'" + ttaxinv + "');");

                }

                stringBuilder.append("INSERT INTO PAYMENT (barcode,clamount,clreceive,cused,cused1,tid,uid,clstatus,clupdate,processdate,tendor,tendortype) VALUES ('G" + responseXml.getCard_no() + "'," + dbHelper.getTotalSell() + "," + dbHelper.getTotalSell() + ",1,1,'" + tid + "','" + tid + "','S',GETDATE(),GETDATE(),'" + responseXml.getTrade_type() + "',1);");
            }


            if (Integer.parseInt(running_inv) > 999999) {
                running_inv = "000001";
            }

            stringBuilder.append("update TERMINAL set INVOICE = '" + running_inv + "' where TERMINALID = '" + terminalid + "';");
//            stringBuilder.append("update SHOPS set SLIPNO = '" + running + "' where SHOPID = '" + shopid + "';");
            getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
            if (getData.doInBackgroundUpdate()) {

                return ttaxinv;
            } else {
                return "";
            }


        }

        return "";

    }

    private String SaveTrueDataToDatabase(TruePaymentResponse trueResponseModel, TruePaymentRequestModel trueRequestModel) {


        String query = "select s.*,t.TTAXID,t.TERMINALID,t.TID,t.DESCRIPTION,t.INVOICE,t.CREDITNOTE from dbfood..SHOPS s inner join dbfood..TERMINAL t on t.tip = s.MRIP where MRIP = '" + ip + "' and POSID = '" + companyProfile[8] + "'";
        getData = new GetData(query, "GetStoreAndTerminal", MainActivity.this);
        data = getData.doInBackground();

        String amount = String.valueOf(Double.parseDouble(trueRequestModel.getRequest_amount()) / 100);
        if (data.size() > 0) {
//            String running = data.get(0).get("SLIPNO");
//            String shopid = data.get(0).get("SHOPID");
//            int running_int = Integer.parseInt(running);
//            running_int++;
//            running = Utils.GetRunningFormat6Unit(String.valueOf(running_int));
            String running_inv = data.get(0).get("INVOICE");

            int running_inv_int = Integer.parseInt(running_inv);
            running_inv_int++;
            running_inv = Utils.GetRunningFormat6Unit(String.valueOf(running_inv_int));

            String ttaxinv = data.get(0).get("TID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running_inv;
//            String mrtslipno = data.get(0).get("TID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running;

            String mrtslipnowithno = ttaxinv + "-1";

            String terminalid = data.get(0).get("TERMINALID");
            String gp = data.get(0).get("GPRULE");
            double gpamount_float = 0.000;
            gpamount_float = Double.parseDouble(gp) * 1.000;
            Log.d(TAG, "SaveDolfinDataToDatabase: " + gpamount_float);
            Log.d(TAG, "SaveDolfinDataToDatabase: " + amount);
            Log.d(TAG, "SaveDolfinDataToDatabase: " + Double.parseDouble(amount));
            gpamount_float = gpamount_float * Double.parseDouble(amount) / 100.000;
            String gpamount = String.format("%.3f", gpamount_float);
//            String vendorcode = data.get(0).get("VENDORCODE");
            String tid = data.get(0).get("TID");

            String ref = data.get(0).get("REF");
            String sh_id = data.get(0).get("SH_ID");

            StringBuilder stringBuilder = new StringBuilder();


//            stringBuilder.append("INSERT INTO CARDTRANS (CUSED,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,LUPDATE,FOODNAME,GP,GPAMOUNT,CBALANCE,REF,SH_ID,cardtendor,cardref,cardpurse,taxinvoice) VALUES ('0','TMN" + dolfinRequestModel.getPaymentCode() + "','" + companyProfile[10] + "','" + companyProfile[8] + "','" + amount + "','S','" + mrtslipno + "',getdate(),'" + tid + "',getdate(),'อาหารและเครื่องดื่ม','" + gp + "','" + gpamount + "','0','" + ref + "','" + sh_id + "','TrueWallet','" + dolfinResponseModel.getData().getPaymentID() + "','" + amount + "','" + ttaxinv + "');");
            List<Sell> sells = new ArrayList<>();
            sells = dbHelper.getSell();

            if (!(sells.get(0).getFoodName().equals("No Data") || sells.get(0).getFoodName().equals("Error"))) {


                for (int i = 0; i < sells.size(); i++) {

                    mrtslipnowithno = ttaxinv + "-" + (i + 1);

                    gp = sells.get(i).getBtnGP();
                    gpamount_float = 0.000;
                    gpamount_float = Double.parseDouble(gp) * 1.000;
                    Log.d(TAG, "List Sells : " + gpamount_float);
                    Log.d(TAG, "List Sells : " + sells.get(i).getMrtAmount());
                    Log.d(TAG, "List Sells : " + Double.parseDouble(sells.get(i).getMrtAmount()));
                    gpamount_float = gpamount_float * Double.parseDouble(sells.get(i).getMrtAmount()) / 100.000;
                    gpamount = String.format("%.3f", gpamount_float);

                    stringBuilder.append("INSERT INTO CARDTRANS (CUSED,CUSED1,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,UID,LUPDATE,FOODNAME,GP,GPAMOUNT,cardtendor,cardref,cardpurse,taxinvoice) VALUES (1,1,'M" + trueRequestModel.getPayment_code() + "'," + companyProfile[10] + ",'" + companyProfile[8] + "'," + sells.get(i).getMrtAmount() + ",'S','" + mrtslipnowithno + "',getdate(),'" + tid + "','" + tid + "',getdate(),'" + sells.get(i).getFoodName() + "'," + gp + "," + gpamount + ",'TRUEWALLET','" + trueResponseModel.getData().getPayment_id() + "'," + sells.get(i).getMrtAmount() + ",'" + ttaxinv + "');");

                }

                stringBuilder.append("INSERT INTO PAYMENT (barcode,clamount,clreceive,cused,cused1,tid,uid,clstatus,clupdate,processdate,tendor,tendortype) VALUES ('M" + trueRequestModel.getPayment_code() + "'," + dbHelper.getTotalSell() + "," + dbHelper.getTotalSell() + ",1,1,'" + tid + "','" + tid + "','S',GETDATE(),GETDATE(),'TRUEWALLET',1);");
            }


            if (Integer.parseInt(running_inv) > 999999) {
                running_inv = "000001";
            }

            stringBuilder.append("update TERMINAL set INVOICE = '" + running_inv + "' where TERMINALID = '" + terminalid + "';");
//            stringBuilder.append("update SHOPS set SLIPNO = '" + running + "' where SHOPID = '" + shopid + "';");
            getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
            if (getData.doInBackgroundUpdate()) {

                return ttaxinv;
            } else {
                return "";
            }


        }

        return "";

    }

    private String SaveDolfinDataToDatabaseInquiry(TrueInquiryResponse trueInquiryResponse, TrueInquiryRequest trueInquiryRequest) {


        String query = "select s.*,t.TTAXID,t.TERMINALID,t.TID,t.DESCRIPTION,t.INVOICE,t.CREDITNOTE from dbfood..SHOPS s inner join dbfood..TERMINAL t on t.tip = s.MRIP where MRIP = '" + ip + "' and POSID = '" + companyProfile[8] + "'";
        getData = new GetData(query, "GetStoreAndTerminal", MainActivity.this);
        data = getData.doInBackground();

        String amount = String.valueOf(Double.parseDouble(trueInquiryResponse.getData().getAmount()) / 100);
        if (data.size() > 0) {
//            String running = data.get(0).get("SLIPNO");
//            String shopid = data.get(0).get("SHOPID");
//            int running_int = Integer.parseInt(running);
//            running_int++;
//            running = Utils.GetRunningFormat6Unit(String.valueOf(running_int));
            String running_inv = data.get(0).get("INVOICE");

            int running_inv_int = Integer.parseInt(running_inv);
            running_inv_int++;
            running_inv = Utils.GetRunningFormat6Unit(String.valueOf(running_inv_int));

            String ttaxinv = data.get(0).get("TID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running_inv;
//            String mrtslipno = data.get(0).get("TID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running;

            String mrtslipnowithno = ttaxinv + "-1";

            String terminalid = data.get(0).get("TERMINALID");
            String gp = data.get(0).get("GPRULE");
            double gpamount_float = 0.000;
            gpamount_float = Double.parseDouble(gp) * 1.000;
            Log.d(TAG, "SaveDolfinDataToDatabase: " + gpamount_float);
            Log.d(TAG, "SaveDolfinDataToDatabase: " + amount);
            Log.d(TAG, "SaveDolfinDataToDatabase: " + Double.parseDouble(amount));
            gpamount_float = gpamount_float * Double.parseDouble(amount) / 100.000;
            String gpamount = String.format("%.3f", gpamount_float);
//            String vendorcode = data.get(0).get("VENDORCODE");
            String tid = data.get(0).get("TID");

            String ref = data.get(0).get("REF");
            String sh_id = data.get(0).get("SH_ID");

            StringBuilder stringBuilder = new StringBuilder();


//            stringBuilder.append("INSERT INTO CARDTRANS (CUSED,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,LUPDATE,FOODNAME,GP,GPAMOUNT,CBALANCE,REF,SH_ID,cardtendor,cardref,cardpurse,taxinvoice) VALUES ('0','TMN" + dolfinRequestModel.getPaymentCode() + "','" + companyProfile[10] + "','" + companyProfile[8] + "','" + amount + "','S','" + mrtslipno + "',getdate(),'" + tid + "',getdate(),'อาหารและเครื่องดื่ม','" + gp + "','" + gpamount + "','0','" + ref + "','" + sh_id + "','TrueWallet','" + dolfinResponseModel.getData().getPaymentID() + "','" + amount + "','" + ttaxinv + "');");
            List<Sell> sells = new ArrayList<>();
            sells = dbHelper.getSell();

            if (!(sells.get(0).getFoodName().equals("No Data") || sells.get(0).getFoodName().equals("Error"))) {


                for (int i = 0; i < sells.size(); i++) {

                    mrtslipnowithno = ttaxinv + "-" + (i + 1);

                    gp = sells.get(i).getBtnGP();
                    gpamount_float = 0.000;
                    gpamount_float = Double.parseDouble(gp) * 1.000;
                    Log.d(TAG, "List Sells : " + gpamount_float);
                    Log.d(TAG, "List Sells : " + sells.get(i).getMrtAmount());
                    Log.d(TAG, "List Sells : " + Double.parseDouble(sells.get(i).getMrtAmount()));
                    gpamount_float = gpamount_float * Double.parseDouble(sells.get(i).getMrtAmount()) / 100.000;
                    gpamount = String.format("%.3f", gpamount_float);

                    stringBuilder.append("INSERT INTO CARDTRANS (CUSED,CUSED1,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,UID,LUPDATE,FOODNAME,GP,GPAMOUNT,cardtendor,cardref,cardpurse,taxinvoice) VALUES (1,1,'M" + trueInquiryResponse.getData().getMetadata().getPaymentCode() + "'," + companyProfile[10] + ",'" + companyProfile[8] + "'," + sells.get(i).getMrtAmount() + ",'S','" + mrtslipnowithno + "',getdate(),'" + tid + "','" + tid + "',getdate(),'" + sells.get(i).getFoodName() + "'," + gp + "," + gpamount + ",'TRUEWALLET','" + trueInquiryResponse.getData().getPaymentID() + "'," + sells.get(i).getMrtAmount() + ",'" + ttaxinv + "');");

                }

                stringBuilder.append("INSERT INTO PAYMENT (barcode,clamount,clreceive,cused,cused1,tid,uid,clstatus,clupdate,processdate,tendor,tendortype) VALUES ('M" + trueInquiryResponse.getData().getMetadata().getPaymentCode() + "'," + dbHelper.getTotalSell() + "," + dbHelper.getTotalSell() + ",1,1,'" + tid + "','" + tid + "','S',GETDATE(),GETDATE(),'TRUEWALLET',1);");
            }


            if (Integer.parseInt(running_inv) > 999999) {
                running_inv = "000001";
            }

            stringBuilder.append("update TERMINAL set INVOICE = '" + running_inv + "' where TERMINALID = '" + terminalid + "';");
//            stringBuilder.append("update SHOPS set SLIPNO = '" + running + "' where SHOPID = '" + shopid + "';");
            getData = new GetData(stringBuilder.toString(), "Update", MainActivity.this);
            if (getData.doInBackgroundUpdate()) {

                return ttaxinv;
            } else {
                return "";
            }


        }

        return "";

    }

    private void bindWidget() {

//        sellListView = findViewById(R.id.list_item);
        bodyLinearLayout = findViewById(R.id.lin_body);
        topCategoryLinearLayout = findViewById(R.id.lin_top_category);
        totalTextView = findViewById(R.id.txt_total);
        btnscan = findViewById(R.id.btn_scan);
        btnReset = findViewById(R.id.btn_reset);
        txtDisplayAmt = findViewById(R.id.txt_display_amt);
        edtBarcodeScan = findViewById(R.id.edt_barcode);
        branchTextView = findViewById(R.id.txt_branch);
        companyTextView = findViewById(R.id.txt_company);
        posIdTextView = findViewById(R.id.txt_pos_id);
        posNoTextView = findViewById(R.id.txt_tax_id);
        ipNoTextView = findViewById(R.id.txt_ip_no);
        storeNameTextView = findViewById(R.id.txt_store_name);


        edtBarcodeScan.setInputType(InputType.TYPE_NULL);

    }


}
