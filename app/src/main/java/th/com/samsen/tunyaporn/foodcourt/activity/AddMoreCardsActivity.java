package th.com.samsen.tunyaporn.foodcourt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import th.com.samsen.tunyaporn.foodcourt.R;
import th.com.samsen.tunyaporn.foodcourt.model.AddCardModel;
import th.com.samsen.tunyaporn.foodcourt.model.Sell;
import th.com.samsen.tunyaporn.foodcourt.service.PrintBillService;
import th.com.samsen.tunyaporn.foodcourt.utility.DBHelper;
import th.com.samsen.tunyaporn.foodcourt.utility.GetData;
import th.com.samsen.tunyaporn.foodcourt.utility.Utils;

public class AddMoreCardsActivity extends AppCompatActivity {


    private String[] companyProfile;
    private String ip, total, shopName, status, classss;
    private DBHelper dbHelper;
    private GetData getData;

    AlertDialog alertDialog2;

    int num = 0;

    private List<AddCardModel> addCardModels;

    private int ltotal;

    String TAG = "AddCardTAG";
    Boolean payABoolean = false;

    private LinearLayout waitingLinearLayout;
    private TextView textSwitcher;
    private Button payButton, cancelButton;
    private EditText firstEditText, secondEditText, thirdEditText, fourthEditText, fifthEditText;
    private TextView firstTextView, secondTextView, thirdTextView, fourthTextView, fifthTextView, totalTextView;
    private String firstCardBarcode, secondCardBarcode, thirdCardBarcode, fourthCardBarcode, fifthCardBarcode;
    private String firstCardMoney, secondCardMoney, thirdCardMoney, fourthCardMoney, fifthCardMoney;
    private String lastCbalanceCard;
    private List<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_cards);

        ltotal = 0;
        num = 0;


        dbHelper = new DBHelper(AddMoreCardsActivity.this);

        dbHelper.clearAddCard();

        GetIntentExtra();

        BindWidget();

        SetFirstCardToLayout();

        BarcodeReaderController();

        ButtonController();


    }

    private void ButtonController() {


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMoreCardsActivity.this, MainActivity.class);
                intent.putExtra("ChooseStore", shopName);
                intent.putExtra("ShopName", shopName);
                intent.putExtra("IP", ip);
                intent.putExtra("SAVE", "SAVE");
                startActivity(intent);
                finish();
            }
        });

    }

    private String getMRTSlipNo() {


        String cused;
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
        String vendorcode = "";
        String cbalance;
        String ref = "";
        String sh_id = "";
        String running = "";
        String mrtslipnowithno = "";

        String query = "select * from dbfood..SHOPS where MRIP = '" + ip + "'  and SHOPNAME like '%" + shopName + "%' ";
        getData = new GetData(query, "GetStore", AddMoreCardsActivity.this);
        data = getData.doInBackground();

        if (data.size() > 0) {
            running = data.get(0).get("SLIPNO");
            int running_int = Integer.parseInt(running);
            running_int++;
            running = Utils.GetRunningFormat6Unit(String.valueOf(running_int));
            mrtslipno = data.get(0).get("POSID") + Utils.getCurrentDateFromFormat("yyMMdd") + running;
            mrtslipnowithno = mrtslipno + "-1";

            if (!data.get(0).get("SHAREPERCENT").equals("0")) {
                gp = data.get(0).get("SHAREPERCENT");
            }
            double gpamount_float = 0.000;
//            gpamount_float = Double.parseDouble(gp) * 1.000;
//            gpamount_float = gpamount_float * Double.parseDouble(amount) / 100.000;
            gpamount = String.format("%.3f", gpamount_float);
            vendorcode = data.get(0).get("VENDORCODE");

            ref = data.get(0).get("REF");
            sh_id = data.get(0).get("SH_ID");
        }

        return mrtslipno;
    }

    private class PayAsyn extends AsyncTask<String[], Void, String> {

        Context context;
        AlertDialog alertDialog;


        String lastBarcode = "";
        String lastCbalance = "";

        String cused = "";
        String mrtamount = "";
        String mrtstatus = "S";
        String mrtslipno = "";
        String tid = "";
        String shopid = "";
        String posid = "";
        String uid = "";
        String backupflg = "0";
        String chkflg = "0";
        String voidno = "";
        String cused1 = "";
        String foodname = "อาหารและเครื่องดื่ม";
        String shopendflg = "0";
        String gp = "";
        String gprule = "";
        String gpamount = "";
        String vendorcode = "";
        String cbalance = "";
        String ref = "";
        List<Sell> sellList;
        String sh_id = "";
        String running = "";
        String mrtslipnowithno = "";
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder cardBuilder = new StringBuilder();
        StringBuilder lastBuilder = new StringBuilder();

        StringBuffer stringBuffer = new StringBuffer();

        public PayAsyn(Context context) {
            this.context = context;
            num += 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Log.d(TAG, "SetFirstCardToLayout: Test0001 Before Pay " + Arrays.toString(dbHelper.getAddCard().toArray()));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pay Processing");
            builder.setMessage("Waiting...");
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Log.d(TAG, "SetFirstCardToLayout: Test0001 After Pay " + Arrays.toString(dbHelper.getAddCard().toArray()));

            alertDialog.dismiss();
            if (s.equals("TRUE")) {


                doprintwork("ใบบันทึกรายการ", "center_header");
                doprintwork("POS#" + companyProfile[8], "left_body");
                doprintwork("Slip No :" + mrtslipno, "left_body");
                doprintwork("ชื่อร้าน : " + companyProfile[9], "left_body");
                doprintwork("---------------------------------", "left_body");

                sellList = dbHelper.getSell();
                int total = 0;
                for (int i = 0; i < sellList.size(); i++) {

                    total = total + Integer.parseInt(sellList.get(i).getMrtAmount());
//                                            doprintwork(sells.get(i).getFoodName() + "         " + sells.get(i).getMrtAmount(), "left_body");
                }


                doprintwork("อาหารและเครื่องดื่ม           " + total + ".00", "left_body");
//                for (int i = 0; i < sellList.size(); i++) {
//
//                    doprintwork( sellList.get(i).getFoodName() +"          " + sellList.get(i).getMrtAmount(), "left_body");
//                }

                doprintwork("---------------------------------", "left_body");
                doprintwork("รวม                       " + total + " บาท", "left_body");
//                for (int i = 0; i < addCardModels.size(); i++) {
//                    if (i != addCardModels.size() - 1) {
//                        doprintwork(addCardModels.get(i).getBARCODE() + " : " + addCardModels.get(i).getCUSED() + " เหลือ 0 บาท", "");
//                    } else {
                        doprintwork(lastBarcode + " : " + addCardModels.get(addCardModels.size()-1).getCUSED() + " เหลือ " + lastCbalanceCard + " บาท", "");
//
//                    }
//                }


                doprintwork("วันที่ " + Utils.getCurrentDateFromFormat("dd/MM/") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yyyy")) + 543 ) + " " + Utils.getCurrentDateFromFormat("kk:mm:ss"), "left_body");
                doprintwork(" ", "");
                doprintwork(" ", "");
                doprintwork(" ", "");
                doprintwork(" ", "");
                doprintwork(" ", "");
                doprintwork(" ", "");
                doprintwork(" ", "");
                doprintwork(" ", "");

                MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.beep);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                for (int i = 0; i < 2; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 1) {
                        mediaPlayer.stop();
                    }

                }


                AlertDialog.Builder builder;


                builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                builder.setTitle("Payment Success!!!");
                if (Integer.parseInt(lastCbalance) - ltotal <= 0) {
                    builder.setMessage(" บัตรหมดมูลค่า กรุณาเก็บบัตรไว้ที่ร้านค้า ");
                } else {
                    builder.setMessage(" กรุณาเก็บบัตรไว้ที่ร้านค้า \n ยกเว้นบัตร " + lastBarcode + " ยอดเงินคงเหลือ " + lastCbalanceCard);
                }
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Intent intent = new Intent(AddMoreCardsActivity.this, MainActivity.class);
                        intent.putExtra("ChooseStore", shopName);
                        intent.putExtra("ShopName", shopName);
                        intent.putExtra("IP", ip);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
//                alertDialog.getWindow().getAttributes();

//                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//                textView.setTextSize(26);
            } else if (s.equals("CHANGEMONEY")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                        builder.setTitle("จ่ายเงินไม่สำเร็จ");
                        builder.setMessage("ยอดเงินในบัตรเปลี่ยนแปลง กรุณาทำรายการใหม่อีกครั้ง");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Intent intent = new Intent(AddMoreCardsActivity.this, MainActivity.class);
                                intent.putExtra("ChooseStore", shopName);
                                intent.putExtra("ShopName", shopName);
                                intent.putExtra("IP", ip);
                                intent.putExtra("SAVE", "SAVE");
                                startActivity(intent);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
            }
        }

        @Override
        protected String doInBackground(String[]... strings) {

            Log.d(TAG, "payProcess: Start ");

            Log.d(TAG, "SetFirstCardToLayout: Test0001 Between Pay " + Arrays.toString(dbHelper.getAddCard().toArray()));

            int endcard = 0;

            String query = "select * from dbfood..SHOPS where MRIP = '" + ip + "'  and SHOPNAME like '%" + shopName + "%' ";
            getData = new GetData(query, "GetStore", AddMoreCardsActivity.this);
            data = getData.doInBackground();

            Log.d(TAG, "payProcess: " + data);

            if (data.size() > 0) {
                running = data.get(0).get("SLIPNO");
                int running_int = Integer.parseInt(running);
                running_int++;
                running = Utils.GetRunningFormat6Unit(String.valueOf(running_int));
                mrtslipno = data.get(0).get("POSID") + (Integer.parseInt(Utils.getCurrentDateFromFormat("yy")) + 43) + Utils.getCurrentDateFromFormat("MMdd") + running;
                int last_item_sell = 0;

                if (data.get(0).get("SHAREPERCENTFLAG").equals("1")) {
                    gp = data.get(0).get("SHAREPERCENT");
                    gprule = data.get(0).get("GPRULE");
                } else {
                    gp = "";
                    gprule = "";
                }


                List<Sell> sells = dbHelper.getSell();

                if (!(sells.get(0).getFoodName().equals("No Data") || sells.get(0).getFoodName().equals("Error"))) {

                    addCardModels = new ArrayList<>();
                    int temp_card_count = 0;
                    int temp_sell = 0;
                    int temp_balance;
                    int temp_sell_list = 0;
                    int temp_add_list = 0;

                    temp_balance = dbHelper.getTotalSell();

                    boolean flagC = true;
                    boolean flagS = true;

                    int temp_card_balance;


                    temp_card_balance = getCardBalance();
                    temp_card_count = getCardCount();

                    int temp_balance_count = 0;
                    temp_balance = dbHelper.getTotalSell();
                    int[] temp_card_money;
                    temp_card_money = getCardMoney();
                    int alphabetNo = 0;
                    List<AddCardModel> tempAddCardAlphabetModels = new ArrayList<>();

                    int final_card_balance;

                    if (temp_card_balance >= temp_balance) {

                        for (int i = 0; i < sells.size(); i++) {

                            flagS = true;
                            temp_sell = Integer.parseInt(sells.get(i).getMrtAmount());
                            for (int j = 0; j < temp_card_count; j++) {

                                switch (j) {
                                    case 0:
                                        if (Integer.parseInt(sells.get(i).getMrtAmount()) >= temp_card_money[j]) {

                                            mrtslipnowithno = mrtslipno + "-" + (i + 1) + "X";


                                            if (flagC) {


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setBARCODE(firstCardBarcode);
                                                addCardModel.setITEMID(String.valueOf(i));
                                                addCardModel.setAMT(String.valueOf(temp_card_money[j]));
                                                temp_sell -= temp_card_money[j];
                                                temp_balance_count += temp_card_money[j];
                                                addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                addCardModel.setSLIPNO(mrtslipnowithno);

                                                addCardModels.add(addCardModel);
                                                temp_card_money[j] = 0;

                                                endcard++;
                                                cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                if (temp_sell == 0) {
                                                    flagS = false;
                                                } else {
                                                    flagS = true;
                                                }
                                                flagC = false;

                                            } else {


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setBARCODE(firstCardBarcode);
                                                addCardModel.setAMT("0");
                                                addCardModel.setITEMID(String.valueOf(i));
                                                temp_sell -= temp_card_money[j];
                                                addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                addCardModel.setSLIPNO(mrtslipnowithno);

                                                temp_balance_count += 0;

                                                addCardModels.add(addCardModel);
                                                temp_card_money[j] = 0;

                                                if (temp_sell == 0) {
                                                    flagS = false;
                                                }

                                                final_card_balance = getTempCardBalance(temp_card_money);
                                                if (temp_balance_count == dbHelper.getTotalSell()) {
                                                    //finish
                                                    if (temp_card_money[j] == 0) {
                                                        endcard++;
                                                        cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");
                                                    } else {
                                                        cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "'  where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                    }
                                                }
                                            }
                                        } else {

                                            mrtslipnowithno = mrtslipno + "-" + (i + 1) + "X";

                                            temp_card_money[j] = temp_card_money[j] - Integer.parseInt(sells.get(i).getMrtAmount());
                                            AddCardModel addCardModel = new AddCardModel();
                                            addCardModel.setBARCODE(firstCardBarcode);
                                            addCardModel.setITEMID(String.valueOf(i));
                                            addCardModel.setAMT(String.valueOf(temp_sell));
                                            temp_balance_count += temp_sell;
                                            temp_sell -= temp_sell;
                                            addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                            addCardModel.setSLIPNO(mrtslipnowithno);
                                            flagS = false;

                                            addCardModels.add(addCardModel);

                                            final_card_balance = getTempCardBalance(temp_card_money);

                                            if (temp_balance_count == dbHelper.getTotalSell()) {
                                                //finish
                                                if (temp_card_money[j] == 0) {
                                                    endcard++;
                                                    cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");
                                                } else {
                                                    cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "'  where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                }
                                            }
                                        }
                                        break;
                                    case 1:

                                        if (flagS) {

                                            if (temp_card_money[j] != 0) {
                                                if (temp_sell >= temp_card_money[j]) {

                                                    mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                    alphabetNo++;

//                                            if (flagC) {


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setBARCODE(secondCardBarcode);
                                                    addCardModel.setAMT(String.valueOf(temp_card_money[j]));
                                                    temp_sell -= temp_card_money[j];
                                                    addCardModel.setITEMID(String.valueOf(i));
                                                    addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                    addCardModel.setSLIPNO(mrtslipnowithno);

                                                    temp_balance_count += temp_card_money[j];

                                                    tempAddCardAlphabetModels.add(addCardModel);
                                                    temp_card_money[j] = 0;
                                                    endcard++;
                                                    cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                    if (temp_sell == 0) {
                                                        flagS = false;
                                                    }
                                                } else {

                                                    mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                    alphabetNo++;

                                                    temp_card_money[j] = temp_card_money[j] - temp_sell;
                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setBARCODE(secondCardBarcode);
                                                    addCardModel.setITEMID(String.valueOf(i));
                                                    addCardModel.setAMT(String.valueOf(temp_sell));
                                                    temp_balance_count += temp_sell;
                                                    temp_sell -= temp_sell;
                                                    addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                    addCardModel.setSLIPNO(mrtslipnowithno);


                                                    tempAddCardAlphabetModels.add(addCardModel);

                                                    if (temp_balance_count == dbHelper.getTotalSell()) {
                                                        //finish

                                                        if (temp_card_money[j] == 0) {
                                                            endcard++;
                                                            cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");
                                                        } else {
                                                            cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "'  where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                        }
                                                    }
                                                    flagS = false;
                                                }
                                            }
                                        }
                                        break;
                                    case 2:

                                        if (flagS) {
                                            if (temp_card_money[j] != 0) {
                                                if (temp_sell >= temp_card_money[j]) {

                                                    mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                    alphabetNo++;



                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setBARCODE(thirdCardBarcode);
                                                    addCardModel.setAMT(String.valueOf(temp_card_money[j]));
                                                    temp_sell -= temp_card_money[j];
                                                    addCardModel.setITEMID(String.valueOf(i));
                                                    addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                    addCardModel.setSLIPNO(mrtslipnowithno);

                                                    temp_balance_count += temp_card_money[j];

                                                    tempAddCardAlphabetModels.add(addCardModel);
                                                    temp_card_money[j] = 0;

                                                    endcard++;
                                                    cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                    if (temp_sell == 0) {
                                                        flagS = false;
                                                    }
                                                } else {
                                                    mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                    alphabetNo++;

                                                    temp_card_money[j] = temp_card_money[j] - temp_sell;
                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setBARCODE(thirdCardBarcode);
                                                    addCardModel.setITEMID(String.valueOf(i));
                                                    addCardModel.setAMT(String.valueOf(temp_sell));
                                                    temp_balance_count += temp_sell;
                                                    temp_sell -= temp_sell;
                                                    addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                    addCardModel.setSLIPNO(mrtslipnowithno);


                                                    tempAddCardAlphabetModels.add(addCardModel);


                                                    if (temp_balance_count == dbHelper.getTotalSell()) {
                                                        //finish

                                                        if (temp_card_money[j] == 0) {
                                                            endcard++;
                                                            cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");
                                                        } else {
                                                            cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "'  where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                        }
                                                    }

                                                    flagS = false;
                                                }
                                            }
                                        }
                                        break;
                                    case 3:

                                        if (flagS) {
                                            if (temp_card_money[j] != 0) {
                                                if (temp_sell >= temp_card_money[j]) {

                                                    mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                    alphabetNo++;

                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setBARCODE(fourthCardBarcode);
                                                    addCardModel.setAMT(String.valueOf(temp_card_money[j]));
                                                    temp_sell -= temp_card_money[j];
                                                    addCardModel.setITEMID(String.valueOf(i));
                                                    addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                    addCardModel.setSLIPNO(mrtslipnowithno);

                                                    temp_balance_count += temp_card_money[j];

                                                    tempAddCardAlphabetModels.add(addCardModel);
                                                    temp_card_money[j] = 0;
                                                    endcard++;
                                                    cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                    if (temp_sell == 0) {
                                                        flagS = false;
                                                    }
                                                } else {
                                                    mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                    alphabetNo++;

                                                    temp_card_money[j] = temp_card_money[j] - temp_sell;
                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setBARCODE(fourthCardBarcode);
                                                    addCardModel.setITEMID(String.valueOf(i));
                                                    addCardModel.setAMT(String.valueOf(temp_sell));
                                                    temp_balance_count += temp_sell;
                                                    temp_sell -= temp_sell;
                                                    addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                    addCardModel.setSLIPNO(mrtslipnowithno);


                                                    tempAddCardAlphabetModels.add(addCardModel);


                                                    if (temp_balance_count == dbHelper.getTotalSell()) {
                                                        //finish

                                                        if (temp_card_money[j] == 0) {
                                                            endcard++;
                                                            cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");
                                                        } else {
                                                            cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "'  where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                        }
                                                    }

                                                    flagS = false;
                                                }
                                            }
                                        }
                                        break;
                                    case 4:

                                        if (flagS) {

                                            if (temp_sell != 0) {
                                                if (temp_card_money[j] != 0) {
                                                    if (temp_sell >= temp_card_money[j]) {

                                                        mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                        alphabetNo++;


                                                        AddCardModel addCardModel = new AddCardModel();
                                                        addCardModel.setBARCODE(fifthCardBarcode);
                                                        addCardModel.setAMT(String.valueOf(temp_card_money[j]));
                                                        temp_sell -= temp_card_money[j];
                                                        addCardModel.setITEMID(String.valueOf(i));
                                                        addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                        addCardModel.setSLIPNO(mrtslipnowithno);

                                                        temp_balance_count += temp_card_money[j];

                                                        tempAddCardAlphabetModels.add(addCardModel);
                                                        temp_card_money[j] = 0;

                                                        endcard++;
                                                        cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                        if (temp_sell == 0) {
                                                            flagS = false;
                                                        }
                                                    } else {
                                                        mrtslipnowithno = mrtslipno + "-" + getCharFromIndex(alphabetNo) + "X";

                                                        alphabetNo++;

                                                        temp_card_money[j] = temp_card_money[j] - temp_sell;
                                                        AddCardModel addCardModel = new AddCardModel();
                                                        addCardModel.setBARCODE(fifthCardBarcode);
                                                        addCardModel.setITEMID(String.valueOf(i));
                                                        addCardModel.setAMT(String.valueOf(temp_sell));
                                                        temp_balance_count += temp_sell;
                                                        temp_sell -= temp_sell;
                                                        addCardModel.setITEMBALANCE(String.valueOf(temp_sell));
                                                        addCardModel.setSLIPNO(mrtslipnowithno);


                                                        tempAddCardAlphabetModels.add(addCardModel);


                                                        if (temp_balance_count == dbHelper.getTotalSell()) {
                                                            //finish

                                                            if (temp_card_money[j] == 0) {
                                                                endcard++;
                                                                cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "' , CSTATUS = 'E' where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");
                                                            } else {
                                                                cardBuilder.append("UPDATE CARDS set cbalance = '" + temp_card_money[j] + "'  where BARCODE = '" + addCardModel.getBARCODE().trim() + "';");

                                                            }
                                                        }
                                                        flagS = false;
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                }

                            }
                        }
                    }
                    addCardModels.addAll(tempAddCardAlphabetModels);

                    lastCbalance = String.valueOf(temp_card_balance);

                    int[] the_temp_card = getCardMoney();
                    Log.d(TAG, "doInBackground: temp " + getTempCardBalance(the_temp_card));
                    Log.d(TAG, "doInBackground: final " + getFinalCardBalance(firstCardBarcode,secondCardBarcode,thirdCardBarcode,fourthCardBarcode,fifthCardBarcode));

                    if (getTempCardBalance(the_temp_card) == getFinalCardBalance(firstCardBarcode, secondCardBarcode, thirdCardBarcode, fourthCardBarcode, fifthCardBarcode)) {


                        Log.d(TAG, "doInBackground: " + addCardModels.size());

                        for (int i = 0; i < addCardModels.size(); i++) {

                            Log.d(TAG, "doInBackground: " + addCardModels.get(i).getBARCODE() + " : " + addCardModels.get(i).getAMT() + " : " + addCardModels.get(i).getITEMID());


                            query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + addCardModels.get(i).getBARCODE().trim() + "'";
                            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                            data = getData.doInBackground();

                            if (data.size() > 0) {
                                String cused = data.get(0).get("CUSED");
                                String cused2 = data.get(0).get("CUSED1");

                                addCardModels.get(i).setCUSED(cused);

                                if (gp.equals("")) {
                                    if (sells.get(Integer.parseInt(addCardModels.get(i).getITEMID())).getBtnFlag().equals("2")) {

                                        gp = sells.get(Integer.parseInt(addCardModels.get(i).getITEMID())).getBtnGP2();
                                        gprule = sells.get(Integer.parseInt(addCardModels.get(i).getITEMID())).getBtnGPRule2();
                                    } else {

                                        gp = sells.get(Integer.parseInt(addCardModels.get(i).getITEMID())).getBtnGP();
                                        gprule = sells.get(Integer.parseInt(addCardModels.get(i).getITEMID())).getBtnGPRule();
                                    }
                                }
                                Double gpamount_float = 0.000;
                                gpamount_float = Double.parseDouble(gp) * 1.000;
                                gpamount_float = gpamount_float * Double.parseDouble(addCardModels.get(i).getAMT()) / 100.000;
                                gpamount = String.format("%.3f", gpamount_float);


                                stringBuffer.append("INSERT INTO CARDTRANS (CUSED,CUSED1,BARCODE,SHOPID,POSID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,TID,UID,LUPDATE,FOODNAME,GP,GPAMOUNT,GPRULE) VALUES ('" + cused + "','" + cused2 + "','" + addCardModels.get(i).getBARCODE().trim() + "'," + companyProfile[10] + ",'" + companyProfile[8] + "'," + addCardModels.get(i).getAMT() + ",'S','" + addCardModels.get(i).getSLIPNO() + "',getdate(),'" + tid + "','" + tid + "',getdate(),'" + sells.get(Integer.parseInt(addCardModels.get(i).getITEMID())).getFoodName() + "'," + gp + "," + gpamount + ",'"+gprule+"');");


                                if (i == addCardModels.size() - 1) {

                                    lastBarcode = addCardModels.get(i).getBARCODE();

                                    stringBuffer.append(cardBuilder);

                                }

//
                            }
                        }


                        query = "select SCEID,TOTALCARD from dbfood..SHOPCARDEND where POSID = '" + companyProfile[8] + "' and convert(varchar(10),CARDENDDATE, 120) = CONVERT(varchar(10),getdate(),120) and SHOPID = '" + companyProfile[10] + "'";
                        Log.d(TAG, "insertCardtrans: " + query);
                        getData = new GetData(query, "GetShopCardEnd", AddMoreCardsActivity.this);
                        data = getData.doInBackground();

                        if (data.size() > 0) {
                            String id = data.get(0).get("SCEID");
                            String totalCard = data.get(0).get("TOTALCARD");
                            stringBuffer.append("update dbfood..SHOPCARDEND set TOTALCARD = '" + (Integer.valueOf(totalCard) + endcard) + "',LUPDATE = getdate() where SCEID = '" + id + "';");
                        } else {
                            stringBuffer.append("insert into dbfood..SHOPCARDEND (POSID,TOTALCARD,CARDENDDATE,RETURNCARD,LUPDATE,UID,RETURNFLG,SHOPID) VALUES ('" + companyProfile[8] + "','" + endcard + "',getdate(),'0',getdate(),'" + companyProfile[8] + "','0','" + companyProfile[10] + "');");
                        }

                        stringBuffer.append("update dbfood..SHOPS set SLIPNO = '" + running + "' where SHOPID = '" + companyProfile[10] + "';");

                        Log.d(TAG, "payProcess: Query ------> " + stringBuffer);
//                    Log.d(TAG, "payProcess: Card Update ------> " + cardBuilder);

                        getData = new GetData(stringBuffer.toString(), "Update", AddMoreCardsActivity.this);
                        getData.doInBackgroundUpdate();
                    } else {
                        return "CHANGEMONEY";
                    }


//                    getData = new GetData(cardBuilder.toString(), "Update", AddMoreCardsActivity.this);
//                    getData.doInBackgroundUpdate();
                    return "TRUE";


//                    stringBuilder.append("INSERT INTO PAYMENT (barcode,clamount,clreceive,cused,cused1,tid,uid,clstatus,clupdate,processdate,tendor,tendortype) VALUES ('M"+dolfinRequestModel.getPayment_code()+"',"+dbHelper.getTotalSell()+","+dbHelper.getTotalSell()+",1,1,'"+tid+"','"+tid+"','S',GETDATE(),GETDATE(),'TRUEWALLET',1);");
                }

            }


            return "";
        }

    }

    private int getFinalCardBalance(String firstCardBarcode, String secondCardBarcode, String thirdCardBarcode, String fourthCardBarcode, String fifthCardBarcode) {
        int temp = 0;
        String query;
        if (firstCardBarcode != null) {

            query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + firstCardBarcode.trim() + "'";
            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
            data = getData.doInBackground();

            if (data.size() > 0) {
                temp += Integer.valueOf(data.get(0).get("CBALANCE"));
            }

        }

        if (secondCardBarcode != null) {

            query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + secondCardBarcode.trim() + "'";
            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
            data = getData.doInBackground();

            if (data.size() > 0) {
                temp += Integer.valueOf(data.get(0).get("CBALANCE"));
            }

        }
        if (thirdCardBarcode != null) {

            query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + thirdCardBarcode.trim() + "'";
            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
            data = getData.doInBackground();

            if (data.size() > 0) {
                temp += Integer.valueOf(data.get(0).get("CBALANCE"));
            }

        }
        if (fourthCardBarcode != null) {

            query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fourthCardBarcode.trim() + "'";
            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
            data = getData.doInBackground();

            if (data.size() > 0) {
                temp += Integer.valueOf(data.get(0).get("CBALANCE"));
            }

        }
        if (fifthCardBarcode != null) {

            query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fifthCardBarcode.trim() + "'";
            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
            data = getData.doInBackground();

            if (data.size() > 0) {
                temp += Integer.valueOf(data.get(0).get("CBALANCE"));
            }

        }

        return temp;
    }

    private int getTempCardBalance(int[] temp_card_money) {
        int result = 0;
        for (int i = 0; i < temp_card_money.length; i++) {
            result += temp_card_money[i];
        }

        return result;
    }

    private int[] getCardMoney() {
        int[] temp = new int[getCardCount()];

        for (int i = 0; i < getCardCount(); i++) {
            switch (i) {
                case 0:
                    temp[i] = Integer.parseInt(firstCardMoney);
                    break;
                case 1:
                    temp[i] = Integer.parseInt(secondCardMoney);
                    break;
                case 2:
                    temp[i] = Integer.parseInt(thirdCardMoney);
                    break;
                case 3:
                    temp[i] = Integer.parseInt(fourthCardMoney);
                    break;
                case 4:
                    temp[i] = Integer.parseInt(fifthCardMoney);
                    break;
            }
        }

        return temp;
    }

    private int getCardCount() {

        int temp = 0;
        if (firstCardMoney != null) {
            temp++;
        }
        if (secondCardMoney != null) {
            temp++;
        }
        if (thirdCardMoney != null) {
            temp++;
        }
        if (fourthCardMoney != null) {
            temp++;
        }
        if (fifthCardMoney != null) {
            temp++;
        }


        return temp;
    }

    private int getCardBalance() {
        int temp = 0;
        if (firstCardMoney != null) {
            temp += Integer.valueOf(firstCardMoney);
        }
        if (secondCardMoney != null) {
            temp += Integer.valueOf(secondCardMoney);
        }
        if (thirdCardMoney != null) {
            temp += Integer.valueOf(thirdCardMoney);
        }
        if (fourthCardMoney != null) {
            temp += Integer.valueOf(fourthCardMoney);
        }
        if (fifthCardMoney != null) {
            temp += Integer.valueOf(fifthCardMoney);
        }

        return temp;
    }


    private void BarcodeReaderController() {

        disableAll();


        String[] waterStrings = new String[0];

        String query = "select typeid from dbfood..cardtype where TYPEDESCRIPTION like '%โปร 19 ทรูพอยท์%'";
        getData = new GetData(query, "GetTypeTrueFreeWater", AddMoreCardsActivity.this);
        data = getData.doInBackground();
        if (data.size() > 0) {
            waterStrings = new String[data.size()];

            for (int j = 0; j < data.size(); j++) {
                waterStrings[j] = data.get(j).get("TYPEID");
            }
        }

        final String[] finalWaterStrings = waterStrings;
        secondEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                Log.d(TAG, "onKey: " + keyEvent.getKeyCode());


                if (secondEditText.length() >= 11) {


                    if (secondEditText.length() >= 12) {
                        if (secondEditText.getText().charAt(11) == '\n') {
                            secondCardBarcode = secondEditText.getText().toString().replace("\n", "");
                            secondCardBarcode = secondEditText.getText().toString().replace("\r", "");

                            if (!secondCardBarcode.trim().equals(firstCardBarcode.trim())) {

                                secondEditText.setText(secondCardBarcode);

                                String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + secondCardBarcode.trim() + "'";
                                getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    String cused = data.get(0).get("CUSED");
                                    String cused1 = data.get(0).get("CUSED1");
                                    String cbalance = data.get(0).get("CBALANCE");

                                    String typeid = data.get(0).get("TYPEID");
                                    int c = 0;
                                    if (finalWaterStrings.length != 0){
                                        for (int j = 0; j < finalWaterStrings.length; j++) {
                                            if (typeid.equals(finalWaterStrings[j])) {
                                                c++;
                                            }
                                        }
                                    }

                                    if (c!=0) {
                                        //Promotion True แลกน้ำ
                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(secondEditText);
                                            }
                                        });
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {


                                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                            //Card Expired

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(secondEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        } else {
                                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                //Card Status Can Use

                                                secondCardMoney = cbalance;
                                                String tempText = "ยอดเงินในบัตร " + secondCardMoney + " บาท";
                                                secondTextView.setText(tempText);


                                                if (ltotal - Integer.parseInt(cbalance) <= 0) {

                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(String.valueOf(ltotal));
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(secondCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(0));

                                                    dbHelper.addAddCard(addCardModel);
                                                    lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    if (num == 0) {
                                                        PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                        payAsyn.execute();
                                                    }
                                                } else {

                                                    ltotal -= Integer.parseInt(cbalance);

                                                    String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";

                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(cbalance);
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(secondCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                    dbHelper.addAddCard(addCardModel);

                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));


                                                    totalTextView.setText(s);


                                                    secondTextView.setEnabled(false);
                                                    secondEditText.setEnabled(false);
                                                    thirdEditText.setEnabled(true);
                                                    thirdEditText.setFocusable(true);
                                                    thirdEditText.setFocusableInTouchMode(true);
                                                    thirdEditText.requestFocus();
                                                }
                                            } else {
                                                //card status can not used

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(secondEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            }
                                        }
                                    }
                                } else {
                                    // Card Not have data in database

                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(secondEditText);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }


                                return true;
                            } else {
                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        resetCardData(secondEditText);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        } else if (secondEditText.length() >= 13) {
                            if (secondEditText.getText().charAt(12) == '\n') {

                                secondCardBarcode = secondEditText.getText().toString().replace("\n", "");

                                secondCardBarcode = secondEditText.getText().toString().replace("\r", "");

                                if (!secondCardBarcode.trim().equals(firstCardBarcode.trim())) {

                                    secondEditText.setText(secondCardBarcode);

                                    String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + secondCardBarcode.trim() + "'";
                                    getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                    data = getData.doInBackground();

                                    if (data.size() > 0) {
                                        String cused = data.get(0).get("CUSED");
                                        String cused1 = data.get(0).get("CUSED1");
                                        String cbalance = data.get(0).get("CBALANCE");

                                        String typeid = data.get(0).get("TYPEID");
                                        int c = 0;
                                        if (finalWaterStrings.length != 0){
                                            for (int j = 0; j < finalWaterStrings.length; j++) {
                                                if (typeid.equals(finalWaterStrings[j])) {
                                                    c++;
                                                }
                                            }
                                        }

                                        if (c!=0) {
                                            //Promotion True แลกน้ำ
                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                            builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(secondEditText);
                                                }
                                            });
                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        } else {


                                            if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                                //Card Expired

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(secondEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            } else {
                                                if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                    //Card Status Can Use

                                                    secondCardMoney = cbalance;
                                                    String tempText = "ยอดเงินในบัตร " + secondCardMoney + " บาท";
                                                    secondTextView.setText(tempText);


                                                    if (ltotal - Integer.parseInt(cbalance) <= 0) {

                                                        AddCardModel addCardModel = new AddCardModel();
                                                        addCardModel.setSLIPNO(getMRTSlipNo());
                                                        addCardModel.setAMT(String.valueOf(ltotal));
                                                        addCardModel.setCUSED(cused);
                                                        addCardModel.setBARCODE(secondCardBarcode);
                                                        addCardModel.setITEMBALANCE(String.valueOf(0));

                                                        dbHelper.addAddCard(addCardModel);
                                                        lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);


                                                        Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                        if (num == 0) {
                                                            PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                            payAsyn.execute();
                                                        }
                                                    } else {

                                                        ltotal -= Integer.parseInt(cbalance);

                                                        String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";

                                                        AddCardModel addCardModel = new AddCardModel();
                                                        addCardModel.setSLIPNO(getMRTSlipNo());
                                                        addCardModel.setAMT(cbalance);
                                                        addCardModel.setCUSED(cused);
                                                        addCardModel.setBARCODE(secondCardBarcode);
                                                        addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                        dbHelper.addAddCard(addCardModel);

                                                        Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));


                                                        totalTextView.setText(s);


                                                        secondTextView.setEnabled(false);
                                                        secondEditText.setEnabled(false);
                                                        thirdEditText.setEnabled(true);
                                                        thirdEditText.setFocusable(true);
                                                        thirdEditText.setFocusableInTouchMode(true);
                                                        thirdEditText.requestFocus();
                                                    }
                                                } else {
                                                    //card status can not used

                                                    AlertDialog alertDialog;
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                    builder.setCancelable(false);
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            resetCardData(secondEditText);
                                                        }
                                                    });

                                                    alertDialog = builder.create();
                                                    alertDialog.show();

                                                }
                                            }
                                        }
                                    } else {
                                        // Card Not have data in database

                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                        builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(secondEditText);
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    }


                                    return true;
                                } else {
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("Alert");
                                    builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            resetCardData(secondEditText);
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            }
                        }
                    } else if (secondEditText.length() == 12 || secondEditText.length() == 11) {

                        secondCardBarcode = secondEditText.getText().toString().replace("\n", "");
                        secondCardBarcode = secondEditText.getText().toString().replace("\r", "");
                        if (!secondCardBarcode.trim().equals(firstCardBarcode.trim())) {

                            secondEditText.setText(secondCardBarcode);

                            String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + secondCardBarcode.trim() + "'";
                            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                            data = getData.doInBackground();

                            if (data.size() > 0) {
                                String cused = data.get(0).get("CUSED");
                                String cused1 = data.get(0).get("CUSED1");
                                String cbalance = data.get(0).get("CBALANCE");

                                String typeid = data.get(0).get("TYPEID");
                                int c = 0;
                                if (finalWaterStrings.length != 0){
                                    for (int j = 0; j < finalWaterStrings.length; j++) {
                                        if (typeid.equals(finalWaterStrings[j])) {
                                            c++;
                                        }
                                    }
                                }

                                if (c!=0) {
                                    //Promotion True แลกน้ำ
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                    builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(secondEditText);

                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();

                                } else {


                                    if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                        //Card Expired

                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                        builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(secondEditText);
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {
                                        if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                            //Card Status Can Use

                                            secondCardMoney = cbalance;
                                            String tempText = "ยอดเงินในบัตร " + secondCardMoney + " บาท";
                                            secondTextView.setText(tempText);


                                            if (ltotal - Integer.parseInt(cbalance) <= 0) {

                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(String.valueOf(ltotal));
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(secondCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(0));

                                                dbHelper.addAddCard(addCardModel);
                                                lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);


                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                if (num == 0) {
                                                    PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                    payAsyn.execute();
                                                }
                                            } else {

                                                ltotal -= Integer.parseInt(cbalance);

                                                String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";

                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(cbalance);
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(secondCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                dbHelper.addAddCard(addCardModel);

                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));


                                                totalTextView.setText(s);


                                                secondTextView.setEnabled(false);
                                                secondEditText.setEnabled(false);
                                                thirdEditText.setEnabled(true);
                                                thirdEditText.setFocusable(true);
                                                thirdEditText.setFocusableInTouchMode(true);
                                                thirdEditText.requestFocus();
                                            }
                                        } else {
                                            //card status can not used

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");

                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(secondEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        }
                                    }
                                }
                            } else {
                                // Card Not have data in database

                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        resetCardData(secondEditText);
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();

                            }


                            return true;
                        } else {
                            AlertDialog alertDialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    resetCardData(secondEditText);
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    } else if (secondEditText.getText().charAt(11) != ' ') {
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("บัตรไม่สามารถใช้งานได้ กรุณาติดต่อแคชเชียร์");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                resetCardData(secondEditText);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    }

                }
                return false;
            }
        });
        thirdEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {


                if (thirdEditText.length() >= 11) {


                    if (thirdEditText.length() >= 12) {
                        if (thirdEditText.getText().charAt(11) == '\n') {
                            thirdCardBarcode = thirdEditText.getText().toString().replace("\n", "");
                            thirdCardBarcode = thirdEditText.getText().toString().replace("\r", "");
                            if (!thirdCardBarcode.trim().equals(firstCardBarcode.trim()) && !thirdCardBarcode.trim().equals(secondCardBarcode.trim())) {

                                thirdEditText.setText(thirdCardBarcode);

                                String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + thirdCardBarcode.trim() + "'";
                                getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    String cused = data.get(0).get("CUSED");
                                    String cused1 = data.get(0).get("CUSED1");
                                    String cbalance = data.get(0).get("CBALANCE");

                                    String typeid = data.get(0).get("TYPEID");
                                    int c = 0;
                                    if (finalWaterStrings.length != 0){
                                        for (int j = 0; j < finalWaterStrings.length; j++) {
                                            if (typeid.equals(finalWaterStrings[j])) {
                                                c++;
                                            }
                                        }
                                    }

                                    if (c!=0) {
                                        //Promotion True แลกน้ำ
                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(thirdEditText);
                                            }
                                        });
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {


                                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(thirdEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();
                                            //Card Expired
                                        } else {
                                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                //Card Status Can Use

                                                //Card Balance NOT Enough
                                                thirdCardMoney = cbalance;
                                                String tempText = "ยอดเงินในบัตร " + thirdCardMoney + " บาท";
                                                thirdTextView.setText(tempText);

                                                if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(String.valueOf(ltotal));
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(thirdCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(0));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);


                                                    if (num == 0) {
                                                        PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                        payAsyn.execute();
                                                    }

                                                } else {
                                                    ltotal -= Integer.parseInt(cbalance);

                                                    String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(cbalance);
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(thirdCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    totalTextView.setText(s);

                                                    thirdTextView.setEnabled(false);
                                                    thirdEditText.setEnabled(false);
                                                    fourthEditText.setEnabled(true);
                                                    fourthEditText.setFocusable(true);
                                                    fourthEditText.setFocusableInTouchMode(true);
                                                    fourthEditText.requestFocus();
                                                }

                                            } else {
                                                //card status can not used

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(thirdEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            }
                                        }
                                    }
                                } else {
                                    // Card Not have data in database

                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(thirdEditText);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }

                                return true;
                            } else {
                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        resetCardData(thirdEditText);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        } else if (thirdEditText.length() >= 13) {
                            if (thirdEditText.getText().charAt(12) == '\n') {

                                thirdCardBarcode = thirdEditText.getText().toString().replace("\n", "");

                                thirdCardBarcode = thirdEditText.getText().toString().replace("\r", "");

                                if (!thirdCardBarcode.trim().equals(firstCardBarcode.trim()) && !thirdCardBarcode.trim().equals(secondCardBarcode.trim())) {

                                    thirdEditText.setText(thirdCardBarcode);

                                    String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + thirdCardBarcode.trim() + "'";
                                    getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                    data = getData.doInBackground();

                                    if (data.size() > 0) {
                                        String cused = data.get(0).get("CUSED");
                                        String cused1 = data.get(0).get("CUSED1");
                                        String cbalance = data.get(0).get("CBALANCE");

                                        String typeid = data.get(0).get("TYPEID");
                                        int c = 0;
                                        if (finalWaterStrings.length != 0){
                                            for (int j = 0; j < finalWaterStrings.length; j++) {
                                                if (typeid.equals(finalWaterStrings[j])) {
                                                    c++;
                                                }
                                            }
                                        }

                                        if (c!=0) {
                                            //Promotion True แลกน้ำ
                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                            builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(thirdEditText);
                                                }
                                            });
                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        } else {


                                            if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(thirdEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();
                                                //Card Expired
                                            } else {
                                                if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                    //Card Status Can Use

                                                    //Card Balance NOT Enough
                                                    thirdCardMoney = cbalance;
                                                    String tempText = "ยอดเงินในบัตร " + thirdCardMoney + " บาท";
                                                    thirdTextView.setText(tempText);

                                                    if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                        AddCardModel addCardModel = new AddCardModel();
                                                        addCardModel.setSLIPNO(getMRTSlipNo());
                                                        addCardModel.setAMT(String.valueOf(ltotal));
                                                        addCardModel.setCUSED(cused);
                                                        addCardModel.setBARCODE(thirdCardBarcode);
                                                        addCardModel.setITEMBALANCE(String.valueOf(0));

                                                        dbHelper.addAddCard(addCardModel);


                                                        Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                        lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);


                                                        if (num == 0) {
                                                            PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                            payAsyn.execute();
                                                        }

                                                    } else {
                                                        ltotal -= Integer.parseInt(cbalance);

                                                        String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";


                                                        AddCardModel addCardModel = new AddCardModel();
                                                        addCardModel.setSLIPNO(getMRTSlipNo());
                                                        addCardModel.setAMT(cbalance);
                                                        addCardModel.setCUSED(cused);
                                                        addCardModel.setBARCODE(thirdCardBarcode);
                                                        addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                        dbHelper.addAddCard(addCardModel);


                                                        Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                        totalTextView.setText(s);

                                                        thirdTextView.setEnabled(false);
                                                        thirdEditText.setEnabled(false);
                                                        fourthEditText.setEnabled(true);
                                                        fourthEditText.setFocusable(true);
                                                        fourthEditText.setFocusableInTouchMode(true);
                                                        fourthEditText.requestFocus();
                                                    }

                                                } else {
                                                    //card status can not used

                                                    AlertDialog alertDialog;
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                    builder.setCancelable(false);
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            resetCardData(thirdEditText);
                                                        }
                                                    });

                                                    alertDialog = builder.create();
                                                    alertDialog.show();

                                                }
                                            }
                                        }
                                    } else {
                                        // Card Not have data in database

                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                        builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(thirdEditText);
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    }

                                    return true;
                                } else {
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("Alert");
                                    builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            resetCardData(thirdEditText);
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            }
                        }
                    } else if (thirdEditText.length() == 12 || thirdEditText.length() == 11) {
                        thirdCardBarcode = thirdEditText.getText().toString().replace("\n", "");
                        thirdCardBarcode = thirdEditText.getText().toString().replace("\r", "");
                        if (!thirdCardBarcode.trim().equals(firstCardBarcode.trim()) && !thirdCardBarcode.trim().equals(secondCardBarcode.trim())) {


                            thirdEditText.setText(thirdCardBarcode);

                            String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + thirdCardBarcode.trim() + "'";
                            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                            data = getData.doInBackground();

                            if (data.size() > 0) {
                                String cused = data.get(0).get("CUSED");
                                String cused1 = data.get(0).get("CUSED1");
                                String cbalance = data.get(0).get("CBALANCE");

                                String typeid = data.get(0).get("TYPEID");
                                int c = 0;
                                if (finalWaterStrings.length != 0){
                                    for (int j = 0; j < finalWaterStrings.length; j++) {
                                        if (typeid.equals(finalWaterStrings[j])) {
                                            c++;
                                        }
                                    }
                                }

                                if (c!=0) {
                                    //Promotion True แลกน้ำ
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                    builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(thirdEditText);
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();

                                } else {


                                    if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {

                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                        builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(thirdEditText);
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();
                                        //Card Expired
                                    } else {
                                        if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                            //Card Status Can Use

                                            //Card Balance NOT Enough
                                            thirdCardMoney = cbalance;
                                            String tempText = "ยอดเงินในบัตร " + thirdCardMoney + " บาท";
                                            thirdTextView.setText(tempText);

                                            if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(String.valueOf(ltotal));
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(thirdCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(0));

                                                dbHelper.addAddCard(addCardModel);


                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);


                                                if (num == 0) {
                                                    PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                    payAsyn.execute();
                                                }

                                            } else {
                                                ltotal -= Integer.parseInt(cbalance);

                                                String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(cbalance);
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(thirdCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                dbHelper.addAddCard(addCardModel);


                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                totalTextView.setText(s);

                                                thirdTextView.setEnabled(false);
                                                thirdEditText.setEnabled(false);
                                                fourthEditText.setEnabled(true);
                                                fourthEditText.setFocusable(true);
                                                fourthEditText.setFocusableInTouchMode(true);
                                                fourthEditText.requestFocus();
                                            }

                                        } else {
                                            //card status can not used

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(thirdEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        }
                                    }
                                }
                            } else {
                                // Card Not have data in database

                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        resetCardData(thirdEditText);
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();

                            }


                            return true;
                        } else {
                            AlertDialog alertDialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    resetCardData(thirdEditText);
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    } else if (thirdEditText.getText().charAt(11) != ' ') {
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("บัตรไม่สามารถใช้งานได้ กรุณาติดต่อแคชเชียร์");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                resetCardData(thirdEditText);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    }

                }

                return false;
            }
        });
        fourthEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {


                if (fourthEditText.length() >= 11) {


                    if (fourthEditText.length() >= 12) {
                        if (fourthEditText.getText().charAt(11) == '\n') {
                            fourthCardBarcode = fourthEditText.getText().toString().replace("\n", "");
                            fourthCardBarcode = fourthEditText.getText().toString().replace("\r", "");

                            if (!fourthCardBarcode.trim().equals(firstCardBarcode.trim()) && !fourthCardBarcode.trim().equals(secondCardBarcode.trim()) && !fourthCardBarcode.trim().equals(thirdCardBarcode.trim())) {

                                fourthEditText.setText(fourthCardBarcode);

                                String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fourthCardBarcode.trim() + "'";
                                getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    String cused = data.get(0).get("CUSED");
                                    String cused1 = data.get(0).get("CUSED1");
                                    String cbalance = data.get(0).get("CBALANCE");

                                    String typeid = data.get(0).get("TYPEID");
                                    int c = 0;
                                    if (finalWaterStrings.length != 0){
                                        for (int j = 0; j < finalWaterStrings.length; j++) {
                                            if (typeid.equals(finalWaterStrings[j])) {
                                                c++;
                                            }
                                        }
                                    }

                                    if (c!=0) {
                                        //Promotion True แลกน้ำ
                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(fourthEditText);
                                            }
                                        });
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {


                                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                            //Card Expired

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(fourthEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                //Card Status Can Use

                                                //Card Balance NOT Enough
                                                fourthCardMoney = cbalance;
                                                String tempText = "ยอดเงินในบัตร " + fourthCardMoney + " บาท";
                                                fourthTextView.setText(tempText);

                                                if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(String.valueOf(ltotal));
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(fourthCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(0));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);

                                                    if (num == 0) {
                                                        PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                        payAsyn.execute();
                                                    }
                                                } else {
                                                    ltotal -= Integer.parseInt(cbalance);


                                                    String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(cbalance);
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(fourthCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    totalTextView.setText(s);

                                                    fourthTextView.setEnabled(false);
                                                    fourthEditText.setEnabled(false);
                                                    fifthEditText.setEnabled(true);
                                                    fifthEditText.setFocusable(true);
                                                    fifthEditText.setFocusableInTouchMode(true);
                                                    fifthEditText.requestFocus();
                                                }

                                            } else {
                                                //card status can not used

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(fourthEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            }
                                        }
                                    }
                                } else {
                                    // Card Not have data in database

                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(fourthEditText);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }


                                return true;
                            } else {
                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        resetCardData(fourthEditText);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }
                    } else if (fourthEditText.length() >= 13) {
                        if (fourthEditText.getText().charAt(12) == '\n') {
                            fourthCardBarcode = fourthEditText.getText().toString().replace("\n", "");

                            fourthCardBarcode = fourthEditText.getText().toString().replace("\r", "");
                            if (!fourthCardBarcode.trim().equals(firstCardBarcode.trim()) && !fourthCardBarcode.trim().equals(secondCardBarcode.trim()) && !fourthCardBarcode.trim().equals(thirdCardBarcode.trim())) {

                                fourthEditText.setText(fourthCardBarcode);

                                String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fourthCardBarcode.trim() + "'";
                                getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    String cused = data.get(0).get("CUSED");
                                    String cused1 = data.get(0).get("CUSED1");
                                    String cbalance = data.get(0).get("CBALANCE");

                                    String typeid = data.get(0).get("TYPEID");
                                    int c = 0;
                                    if (finalWaterStrings.length != 0){
                                        for (int j = 0; j < finalWaterStrings.length; j++) {
                                            if (typeid.equals(finalWaterStrings[j])) {
                                                c++;
                                            }
                                        }
                                    }

                                    if (c!=0) {
                                        //Promotion True แลกน้ำ
                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(fourthEditText);
                                            }
                                        });
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {


                                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                            //Card Expired

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(fourthEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                //Card Status Can Use

                                                //Card Balance NOT Enough
                                                fourthCardMoney = cbalance;
                                                String tempText = "ยอดเงินในบัตร " + fourthCardMoney + " บาท";
                                                fourthTextView.setText(tempText);

                                                if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(String.valueOf(ltotal));
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(fourthCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(0));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);

                                                    if (num == 0) {
                                                        PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                        payAsyn.execute();
                                                    }
                                                } else {
                                                    ltotal -= Integer.parseInt(cbalance);


                                                    String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(cbalance);
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(fourthCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    totalTextView.setText(s);

                                                    fourthTextView.setEnabled(false);
                                                    fourthEditText.setEnabled(false);
                                                    fifthEditText.setEnabled(true);
                                                    fifthEditText.setFocusable(true);
                                                    fifthEditText.setFocusableInTouchMode(true);
                                                    fifthEditText.requestFocus();
                                                }

                                            } else {
                                                //card status can not used

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(fourthEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            }
                                        }
                                    }
                                } else {
                                    // Card Not have data in database

                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(fourthEditText);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }


                                return true;
                            } else {
                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        resetCardData(fourthEditText);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }


                    } else if (fourthEditText.length() == 12 || fourthEditText.length() == 11) {

                        fourthCardBarcode = fourthEditText.getText().toString().replace("\n", "");

                        fourthCardBarcode = fourthEditText.getText().toString().replace("\r", "");
                        if (!fourthCardBarcode.trim().equals(firstCardBarcode.trim()) && !fourthCardBarcode.trim().equals(secondCardBarcode.trim()) && !fourthCardBarcode.trim().equals(thirdCardBarcode.trim())) {

                            fourthEditText.setText(fourthCardBarcode);

                            String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fourthCardBarcode.trim() + "'";
                            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                            data = getData.doInBackground();

                            if (data.size() > 0) {
                                String cused = data.get(0).get("CUSED");
                                String cused1 = data.get(0).get("CUSED1");
                                String cbalance = data.get(0).get("CBALANCE");
                                String typeid = data.get(0).get("TYPEID");
                                int c = 0;
                                if (finalWaterStrings.length != 0){
                                    for (int j = 0; j < finalWaterStrings.length; j++) {
                                        if (typeid.equals(finalWaterStrings[j])) {
                                            c++;
                                        }
                                    }
                                }

                                if (c!=0) {
                                    //Promotion True แลกน้ำ
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                    builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(fourthEditText);
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();

                                } else {

                                    if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                        //Card Expired

                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                        builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(fourthEditText);
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {
                                        if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                            //Card Status Can Use

                                            //Card Balance NOT Enough
                                            fourthCardMoney = cbalance;
                                            String tempText = "ยอดเงินในบัตร " + fourthCardMoney + " บาท";
                                            fourthTextView.setText(tempText);

                                            if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(String.valueOf(ltotal));
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(fourthCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(0));

                                                dbHelper.addAddCard(addCardModel);


                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);

                                                if (num == 0) {
                                                    PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                    payAsyn.execute();
                                                }
                                            } else {
                                                ltotal -= Integer.parseInt(cbalance);


                                                String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(cbalance);
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(fourthCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                                                dbHelper.addAddCard(addCardModel);


                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                totalTextView.setText(s);

                                                fourthTextView.setEnabled(false);
                                                fourthEditText.setEnabled(false);
                                                fifthEditText.setEnabled(true);
                                                fifthEditText.setFocusable(true);
                                                fifthEditText.setFocusableInTouchMode(true);
                                                fifthEditText.requestFocus();
                                            }

                                        } else {
                                            //card status can not used

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(fourthEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        }
                                    }
                                }
                            } else {
                                // Card Not have data in database

                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        resetCardData(fourthEditText);
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();

                            }


                            return true;
                        } else {
                            AlertDialog alertDialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    resetCardData(fourthEditText);
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    } else if (fourthEditText.getText().charAt(11) != ' ') {
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("บัตรไม่สามารถใช้งานได้ กรุณาติดต่อแคชเชียร์");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                resetCardData(fourthEditText);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    }
                }


                return false;
            }
        });

        fifthEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {


                if (fifthEditText.length() >= 11) {


                    if (fifthEditText.length() >= 12) {
                        if (fifthEditText.getText().charAt(11) == '\n') {
                            fifthCardBarcode = fifthEditText.getText().toString().replace("\n", "");
                            fifthCardBarcode = fifthEditText.getText().toString().replace("\r", "");

                            if (!fifthCardBarcode.trim().equals(firstCardBarcode.trim()) && !fifthCardBarcode.trim().equals(secondCardBarcode.trim()) && !fifthCardBarcode.trim().equals(thirdCardBarcode.trim()) && !fifthCardBarcode.trim().equals(fourthCardBarcode.trim())) {

                                fifthEditText.setText(fifthCardBarcode);

                                String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fifthCardBarcode.trim() + "'";
                                getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    String cused = data.get(0).get("CUSED");
                                    String cused1 = data.get(0).get("CUSED1");
                                    String cbalance = data.get(0).get("CBALANCE");

                                    String typeid = data.get(0).get("TYPEID");
                                    int c = 0;
                                    if (finalWaterStrings.length != 0){
                                        for (int j = 0; j < finalWaterStrings.length; j++) {
                                            if (typeid.equals(finalWaterStrings[j])) {
                                                c++;
                                            }
                                        }
                                    }

                                    if (c!=0) {
                                        //Promotion True แลกน้ำ
                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(fifthEditText);
                                            }
                                        });
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {


                                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                            //Card Expired

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(fifthEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                //Card Status Can Use

                                                //Card Balance NOT Enough
                                                fifthCardMoney = cbalance;
                                                String tempText = "ยอดเงินในบัตร " + fifthCardMoney + " บาท";
                                                fifthTextView.setText(tempText);

                                                if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(String.valueOf(ltotal));
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(fifthCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(0));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);

                                                    if (num == 0) {
                                                        PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                        payAsyn.execute();
                                                    }
                                                } else {
                                                    endPayment();
                                                }

                                            } else {
                                                //card status can not used

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(fifthEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            }
                                        }
                                    }
                                } else {
                                    // Card Not have data in database

                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(fifthEditText);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }


                                return true;
                            } else {
                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        resetCardData(fifthEditText);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }
                    } else if (fifthEditText.length() >= 13) {
                        if (fifthEditText.getText().charAt(12) == '\n') {
                            fifthCardBarcode = fifthEditText.getText().toString().replace("\n", "");

                            fifthCardBarcode = fifthEditText.getText().toString().replace("\r", "");
                            if (!fifthCardBarcode.trim().equals(firstCardBarcode.trim()) && !fifthCardBarcode.trim().equals(secondCardBarcode.trim()) && !fifthCardBarcode.trim().equals(thirdCardBarcode.trim()) && !fifthCardBarcode.trim().equals(fourthCardBarcode.trim())) {
                                fifthEditText.setText(fifthCardBarcode);

                                String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fifthCardBarcode.trim() + "'";
                                getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                                data = getData.doInBackground();

                                if (data.size() > 0) {
                                    String cused = data.get(0).get("CUSED");
                                    String cused1 = data.get(0).get("CUSED1");
                                    String cbalance = data.get(0).get("CBALANCE");

                                    String typeid = data.get(0).get("TYPEID");
                                    int c = 0;
                                    if (finalWaterStrings.length != 0){
                                        for (int j = 0; j < finalWaterStrings.length; j++) {
                                            if (typeid.equals(finalWaterStrings[j])) {
                                                c++;
                                            }
                                        }
                                    }

                                    if (c!=0) {
                                        //Promotion True แลกน้ำ
                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                        builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(fifthEditText);
                                            }
                                        });
                                        alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {


                                        if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                            //Card Expired

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(fifthEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                                //Card Status Can Use

                                                //Card Balance NOT Enough
                                                fifthCardMoney = cbalance;
                                                String tempText = "ยอดเงินในบัตร " + fifthCardMoney + " บาท";
                                                fifthTextView.setText(tempText);

                                                if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                    AddCardModel addCardModel = new AddCardModel();
                                                    addCardModel.setSLIPNO(getMRTSlipNo());
                                                    addCardModel.setAMT(String.valueOf(ltotal));
                                                    addCardModel.setCUSED(cused);
                                                    addCardModel.setBARCODE(fifthCardBarcode);
                                                    addCardModel.setITEMBALANCE(String.valueOf(0));

                                                    dbHelper.addAddCard(addCardModel);


                                                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                    lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);

                                                    if (num == 0) {
                                                        PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                        payAsyn.execute();
                                                    }
                                                } else {
                                                    //Card Not Enough
                                                    endPayment();

                                                }

                                            } else {
                                                //card status can not used

                                                AlertDialog alertDialog;
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        resetCardData(fifthEditText);
                                                    }
                                                });

                                                alertDialog = builder.create();
                                                alertDialog.show();

                                            }
                                        }
                                    }
                                } else {
                                    // Card Not have data in database

                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(fifthEditText);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                }


                                return true;
                            } else {
                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("Alert");
                                builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        resetCardData(fifthEditText);
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }


                    } else if (fifthEditText.length() == 12 || fifthEditText.length() == 11) {

                        fifthCardBarcode = fifthEditText.getText().toString().replace("\n", "");

                        fifthCardBarcode = fifthEditText.getText().toString().replace("\r", "");
                        if (!fifthCardBarcode.trim().equals(firstCardBarcode.trim()) && !fifthCardBarcode.trim().equals(secondCardBarcode.trim()) && !fifthCardBarcode.trim().equals(thirdCardBarcode.trim()) && !fifthCardBarcode.trim().equals(fourthCardBarcode.trim())) {
                            fifthEditText.setText(fifthCardBarcode);

                            String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + fifthCardBarcode.trim() + "'";
                            getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
                            data = getData.doInBackground();

                            if (data.size() > 0) {
                                String cused = data.get(0).get("CUSED");
                                String cused1 = data.get(0).get("CUSED1");
                                String cbalance = data.get(0).get("CBALANCE");
                                String typeid = data.get(0).get("TYPEID");
                                int c = 0;
                                if (finalWaterStrings.length != 0){
                                    for (int j = 0; j < finalWaterStrings.length; j++) {
                                        if (typeid.equals(finalWaterStrings[j])) {
                                            c++;
                                        }
                                    }
                                }

                                if (c!=0) {
                                    //Promotion True แลกน้ำ
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                    builder.setTitle("บัตรไม่สามารถใช้งานได้");
                                    builder.setMessage("บัตรใช้สำหรับแลกน้ำที่ร้านน้ำเท่านั้น");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            resetCardData(fifthEditText);
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();

                                } else {

                                    if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                                        //Card Expired

                                        AlertDialog alertDialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                        builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                        builder.setMessage("บัตรหมดอายุ วันที่ " + data.get(0).get("CEXPIRED"));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                resetCardData(fifthEditText);
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {
                                        if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                                            //Card Status Can Use

                                            //Card Balance NOT Enough
                                            fifthCardMoney = cbalance;
                                            String tempText = "ยอดเงินในบัตร " + fifthCardMoney + " บาท";
                                            fifthTextView.setText(tempText);

                                            if (ltotal - Integer.parseInt(cbalance) <= 0) {


                                                AddCardModel addCardModel = new AddCardModel();
                                                addCardModel.setSLIPNO(getMRTSlipNo());
                                                addCardModel.setAMT(String.valueOf(ltotal));
                                                addCardModel.setCUSED(cused);
                                                addCardModel.setBARCODE(fifthCardBarcode);
                                                addCardModel.setITEMBALANCE(String.valueOf(0));

                                                dbHelper.addAddCard(addCardModel);


                                                Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));

                                                lastCbalanceCard = String.valueOf(Integer.parseInt(cbalance) - ltotal);

                                                if (num == 0) {
                                                    PayAsyn payAsyn = new PayAsyn(AddMoreCardsActivity.this);
                                                    payAsyn.execute();
                                                }
                                            } else {
                                                endPayment();

                                            }

                                        } else {
                                            //card status can not used

                                            AlertDialog alertDialog;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                            builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                            builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    resetCardData(fifthEditText);
                                                }
                                            });

                                            alertDialog = builder.create();
                                            alertDialog.show();

                                        }
                                    }
                                }
                            } else {
                                // Card Not have data in database

                                AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                                builder.setTitle("อ่านบัตรไม่สำเร็จ");
                                builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        resetCardData(fifthEditText);
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();

                            }


                            return true;
                        } else {
                            AlertDialog alertDialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                            builder.setTitle("Alert");
                            builder.setMessage("บัตรได้ใช้งานไปแล้ว กรุณาใช้งานบัตรใบอื่น");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    resetCardData(fifthEditText);
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    } else if (fourthEditText.getText().charAt(11) != ' ') {
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("บัตรไม่สามารถใช้งานได้ กรุณาติดต่อแคชเชียร์");
                        builder.setCancelable(false);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                resetCardData(fifthEditText);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                    }
                }


                return false;
            }
        });
    }


    void doprintwork(String msg, String op) {
        Intent intentService = new Intent(AddMoreCardsActivity.this, PrintBillService.class);
        intentService.putExtra("SPRT", msg);
        intentService.putExtra("OP", op);
        AddMoreCardsActivity.this.startService(intentService);
    }

    private void endPayment() {


        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
        builder.setTitle("จ่ายเงินไม่สำเร็จ");
        builder.setMessage("เงินในบัตรไม่เพียงพอต่อการจ่ายเงิน กรุณาคืนบัตรทั้งหมดให้ลูกค้า");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AddMoreCardsActivity.this, MainActivity.class);
                intent.putExtra("ChooseStore", shopName);
                intent.putExtra("ShopName", shopName);
                intent.putExtra("IP", ip);
                intent.putExtra("SAVE", "SAVE");
                startActivity(intent);
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private String getCharFromIndex(int i) {
        char[] c = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        return String.valueOf(c[i]);
    }


    private void disableAll() {
        waitingLinearLayout.setVisibility(View.GONE);
        secondEditText.setFocusable(true);
        thirdEditText.setEnabled(false);
        fourthEditText.setEnabled(false);
        fifthEditText.setEnabled(false);
    }

    private void BindWidget() {

        textSwitcher = findViewById(R.id.txt_waiting);
        waitingLinearLayout = findViewById(R.id.lin_waiting);
        payButton = findViewById(R.id.btn_pay);
        cancelButton = findViewById(R.id.btn_cancel);
        firstEditText = findViewById(R.id.edt_first);
        secondEditText = findViewById(R.id.edt_second);
        thirdEditText = findViewById(R.id.edt_third);
        fourthEditText = findViewById(R.id.edt_fourth);
        fifthEditText = findViewById(R.id.edt_fifth);
        firstTextView = findViewById(R.id.txt_first);
        secondTextView = findViewById(R.id.txt_second);
        thirdTextView = findViewById(R.id.txt_third);
        fourthTextView = findViewById(R.id.txt_fourth);
        fifthTextView = findViewById(R.id.txt_fifth);
        totalTextView = findViewById(R.id.txt_total);

        firstEditText.setInputType(InputType.TYPE_NULL);
        secondEditText.setInputType(InputType.TYPE_NULL);
        thirdEditText.setInputType(InputType.TYPE_NULL);
        fourthEditText.setInputType(InputType.TYPE_NULL);
        fifthEditText.setInputType(InputType.TYPE_NULL);

    }

    private void SetFirstCardToLayout() {

        String query = "select TOP 1 *,CASE WHEN DATEDIFF(dy, CEXPIRED,GETDATE()) <= 0 THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG,CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG from dbfood..CARDS where BARCODE = '" + firstCardBarcode.trim() + "'";
        getData = new GetData(query, "GetCardDetail", AddMoreCardsActivity.this);
        data = getData.doInBackground();

        Log.d("TAG", "SetFirstCardToLayout: " + data);

        if (data.size() > 0) {
            String cused = data.get(0).get("CUSED");
            String cused1 = data.get(0).get("CUSED1");
            String cbalance = data.get(0).get("CBALANCE");

            if (data.get(0).get("CEXPIRED_FLAG").equals("Y")) {
                //Card Expired
            } else {
                if (data.get(0).get("CSTATUS_FLAG").equals("Y")) {
                    //Card Status Can Use

                    Log.d("TAG", "SetFirstCardToLayout: " + ltotal);

                    firstCardMoney = cbalance;
                    String tempText = "ยอดเงินในบัตร " + firstCardMoney + " บาท";
                    firstTextView.setText(tempText);

                    ltotal = Integer.parseInt(total) - Integer.parseInt(cbalance);

                    String s = "ยอดใช้จ่าย " + total + " บาท ขาดเงินจำนวน " + ltotal + " บาท";

                    AddCardModel addCardModel = new AddCardModel();
                    addCardModel.setSLIPNO(getMRTSlipNo());
                    addCardModel.setAMT(cbalance);
                    addCardModel.setCUSED(cused);
                    addCardModel.setBARCODE(firstCardBarcode);
                    addCardModel.setITEMBALANCE(String.valueOf(ltotal));

                    dbHelper.addAddCard(addCardModel);

                    totalTextView.setText(s);
                    firstEditText.setText(firstCardBarcode);

                    Log.d(TAG, "SetFirstCardToLayout: Test0001 " + Arrays.toString(dbHelper.getAddCard().toArray()));


                    firstTextView.setEnabled(false);
                    firstEditText.setEnabled(false);
                    secondEditText.setFocusable(true);
                    secondEditText.setFocusableInTouchMode(true);
                    secondEditText.requestFocus();

                } else {
                    //card status can not used


//                    AlertDialog alertDialog;
//                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
//                    builder.setTitle("อ่านบัตรไม่สำเร็จ");
//                    builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
//
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            resetCardData(secondEditText);
//                        }
//                    });
//
//                    alertDialog = builder.create();
//                    alertDialog.show();

                }
            }
        } else {
            // Card Not have data in database


//            AlertDialog alertDialog;
//            AlertDialog.Builder builder = new AlertDialog.Builder(AddMoreCardsActivity.this);
//            builder.setTitle("อ่านบัตรไม่สำเร็จ");
//            builder.setMessage("บัตรไม่สามารถใช้งานได้\nกรุณาติดต่อแคชเชียร์");
//
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    resetCardData(secondEditText);
//                }
//            });
//
//            alertDialog = builder.create();
//            alertDialog.show();


        }


    }

    private void resetCardData(EditText edt) {
        edt.setFocusable(true);
        edt.setFocusableInTouchMode(true);
        edt.requestFocus();
        edt.setText("");

    }

    private void GetIntentExtra() {
        shopName = getIntent().getStringExtra("ShopName");
        ip = getIntent().getStringExtra("ip");
        status = getIntent().getStringExtra("Status");
        classss = getIntent().getStringExtra("Class");
        total = getIntent().getStringExtra("amount_remain");
        companyProfile = getIntent().getStringArrayExtra("companyprofile");
        firstCardBarcode = getIntent().getStringExtra("first_card");
    }
}
