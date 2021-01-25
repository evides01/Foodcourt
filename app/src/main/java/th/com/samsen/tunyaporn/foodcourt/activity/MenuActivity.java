package th.com.samsen.tunyaporn.foodcourt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import th.com.samsen.tunyaporn.foodcourt.R;
import th.com.samsen.tunyaporn.foodcourt.adapter.MenuListViewAdapter;
import th.com.samsen.tunyaporn.foodcourt.model.Sell;
import th.com.samsen.tunyaporn.foodcourt.utility.DBHelper;
import th.com.samsen.tunyaporn.foodcourt.utility.GetData;

public class MenuActivity extends AppCompatActivity implements MenuListViewAdapter.OnClickInAdapter {

    Button pay;
    ListView foodListView;

    TextView nameTextView, priceTextView;

    private DBHelper dbHelper;
    private GetData getData;


    private String chooseStore = "";
    private String[] companyProfile,btnGPStrings,btnGP2Strings,btnPriceStrings,btnPrice2Strings,btnNameStrings,btnIDStrings,btnFlagStrings,btnGPRuleStrings,btnGPRule2Strings;
    private String ip,category;
    private LinearLayout bodyLinearLayout;

    private boolean happyHourABoolean = false;
    private List<Map<String, String>> data = null;
    private List<Map<String, String>> happyHour = null;

    private List<Map<String, String>> merge = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bindWidget();

        getIntentFromMain();




        dbHelper = new DBHelper(MenuActivity.this);

        if (dbHelper.countSell() == 0) {
            happyHourABoolean = checkHappyHour();

            queryButtonAndroid();

        } else {

            queryButtonAndroid();

        }

        queryItemSell();
    }

    private boolean checkHappyHour() {

        String query = "select h.*,hd.btnid,hd.btnflg,hd.shopid, CONVERT(varchar, getdate(), 23) as curDate, convert(varchar, getdate(), 8) as curTime from dbfoodbackup..happyhour h join dbfoodbackup..happyhourdt hd on h.happyhour_id = hd.happyhour_id where SHOPID = '"+companyProfile[10]+"'";
        getData = new GetData(query, "GetHappyHour", MenuActivity.this);
        data = getData.doInBackground();


        Log.d("TAG", "checkHappyHour: " + data);

        if (data.size() > 0) {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat tf = new SimpleDateFormat("HH:mm");


            Date date = null;
            Date hpStDate = null;
            Date hpEndDate = null;

            Date time = null;
            Date hpStTime = null;
            Date hpEndTime = null;

            try {
                date = df.parse(data.get(0).get("curDate"));
                time = tf.parse(data.get(0).get("curTime"));


                hpStDate = df.parse(data.get(0).get("datestart"));
                hpStTime = tf.parse(data.get(0).get("timestart"));
                hpEndDate = df.parse(data.get(0).get("dateend"));
                hpEndTime = tf.parse(data.get(0).get("timeend"));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean b = (date.compareTo(hpStDate) > 0 || date.compareTo(hpStDate) == 0) && (date.compareTo(hpEndDate) < 0 || date.compareTo(hpEndDate) == 0);

            if (b) {
                boolean c = (time.compareTo(hpStTime) > 0 || time.compareTo(hpStTime) == 0 )&&(time.compareTo(hpEndTime) < 0 || time.compareTo(hpEndTime) == 0);
                if (c) {

                    happyHour = data;
                    happyHourABoolean = true;

                    return happyHourABoolean;
                }

            }

        }

        happyHourABoolean = false;

        return happyHourABoolean;
    }




    private void queryButtonAndroid() {


        if (happyHourABoolean) {

            String query = "select s.*,hd.btnflg,hd.btnid from dbfood..SHOPBUTTONS s inner join dbfoodbackup..happyhourdt hd on s.BTNID = hd.btnid and s.SHOPID = hd.shopid  where s.SHOPID = '"+companyProfile[10]+"' and BTNGROUP = '"+category+"'";
            getData = new GetData(query, "GetStoreButtonJoinHappyHour", MenuActivity.this);
            data = getData.doInBackground();

        } else {

            String query = "select * from dbfood..SHOPBUTTONS where SHOPID = '"+companyProfile[10]+"' and BTNGROUP = '"+category+"'";
            getData = new GetData(query, "GetStoreButton", MenuActivity.this);
            data = getData.doInBackground();
        }



        Log.d("TAG", "queryButtonAndroid: " + data);


        if (data.size() > 0) {



            btnNameStrings = new String[data.size()];
            btnPriceStrings = new String[data.size()];
            btnGP2Strings = new String[data.size()];
            btnGPStrings = new String[data.size()];
            btnPrice2Strings = new String[data.size()];
            btnIDStrings = new String[data.size()];
            btnGPRuleStrings = new String[data.size()];
            btnGPRule2Strings = new String[data.size()];
            if (happyHourABoolean) {
                btnFlagStrings = new String[data.size()];
            }

            for (int i = 0; i < data.size(); i++) {
                btnIDStrings[i] = data.get(i).get("BTNID");
                btnNameStrings[i] = data.get(i).get("BTNNAME");
                btnPrice2Strings[i] = data.get(i).get("BTNPRICE2");
                btnGPStrings[i] = data.get(i).get("BTNGP");
                btnGP2Strings[i] = data.get(i).get("BTNGP2");
                btnPriceStrings[i] = data.get(i).get("BTNPRICE");
                btnGPRuleStrings[i] = data.get(i).get("BTNGPRULE");
                btnGPRule2Strings[i] = data.get(i).get("BTNGPRULE2");
                if (happyHourABoolean) {
                    btnFlagStrings[i] = data.get(i).get("btnflg");
                }
            }

            if (happyHourABoolean) {
                MenuListViewAdapter menuListViewAdapter = new MenuListViewAdapter(MenuActivity.this, btnNameStrings, btnPriceStrings, companyProfile[8], ip, companyProfile[10], btnIDStrings, btnPrice2Strings, btnGPStrings, btnGP2Strings, btnFlagStrings ,btnGPRuleStrings,btnGPRule2Strings);
                foodListView.setAdapter(menuListViewAdapter);
            } else {
                MenuListViewAdapter menuListViewAdapter = new MenuListViewAdapter(MenuActivity.this, btnNameStrings, btnPriceStrings, companyProfile[8], ip, companyProfile[10], btnIDStrings, btnPrice2Strings, btnGPStrings, btnGP2Strings,btnGPRuleStrings,btnGPRule2Strings);
                foodListView.setAdapter(menuListViewAdapter);

            }
        }

    }

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
                View view = LayoutInflater.from(MenuActivity.this).inflate(R.layout.lis_item_show_header, null);
                TextView itemTextView = view.findViewById(R.id.txt_price);
                TextView qtyTextView = view.findViewById(R.id.txt_qty);
                TextView descTextView = view.findViewById(R.id.txt_desc);

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


    private void setData(int count) {
        btnGP2Strings = new String[count];
        btnGPStrings = new String[count];
        btnIDStrings = new String[count];
        btnNameStrings = new String[count];
        btnPriceStrings = new String[count];
        btnPrice2Strings = new String[count];
    }

    private void getIntentFromMain() {
        chooseStore = getIntent().getStringExtra("ChooseStore");
        companyProfile = getIntent().getStringArrayExtra("CompanyProfile");
        ip = getIntent().getStringExtra("ip");
        category = getIntent().getStringExtra("Category");

    }

    private void bindWidget() {
        bodyLinearLayout = findViewById(R.id.lin_body);


        foodListView = findViewById(R.id.lis_button);
        nameTextView = findViewById(R.id.txt_item_name);
        priceTextView = findViewById(R.id.txt_price);
        pay = findViewById(R.id.btn_pay);

    }



    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.btn_20:
//                sell = new Sell();
//                sell.setBtnGP(btnGPStrings[19]);
//                sell.setBtnGP2(btnGP2Strings[19]);
//                sell.setBtnIdString(btnIDStrings[19]);
//                sell.setFoodName(btnNameStrings[19]);
//                sell.setMRIP(ip);
//                sell.setMrtAmount(btnPriceStrings[19]);
//                sell.setShopIdString(companyProfile[10]);
//                sell.setPosID(companyProfile[8]);
//                sell.setType("S");
//
//                dbHelper.addSell(sell);
//                nameTextView.setText(sell.getFoodName());
//                priceTextView.setText(sell.getMrtAmount());
//                break;
            case R.id.btn_pay:
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("ChooseStore", chooseStore);
                intent.putExtra("ShopName", chooseStore);
                intent.putExtra("IP", ip);
                intent.putExtra("SAVE", "SAVE");
                startActivity(intent);

                finish();
        }

    }

    @Override
    public void onClickInAdapter(String totalSell, String lastMenuSell) {

        priceTextView.setText(totalSell);
        nameTextView.setText(lastMenuSell);

        queryItemSell();
    }
}
