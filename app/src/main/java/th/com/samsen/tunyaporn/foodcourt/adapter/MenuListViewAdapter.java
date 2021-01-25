package th.com.samsen.tunyaporn.foodcourt.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import th.com.samsen.tunyaporn.foodcourt.R;
import th.com.samsen.tunyaporn.foodcourt.model.Sell;
import th.com.samsen.tunyaporn.foodcourt.utility.DBHelper;
import th.com.samsen.tunyaporn.foodcourt.view.MenuButtonView;

public class MenuListViewAdapter extends BaseAdapter {

    Context context;
    String[] foodNameStrings, priceStrings, price2Strings, gpStrings, gp2Strings,btnFlagStrings,btnGPRuleStrings,btnGPRule2Strings;
    String PosID, MRIP;
    TextView priceTextView;
    Button foodNameButton;
    String TAG = "Menu";
    List<String> foodNameTempStrings, priceTempStrings, price2TempStrings, gpTempStrings, gp2TempStrings,btnFlagTempStrings,gpRuleTempStrings,gpRule2TempStrings;
    int totalIndex;
    MenuListViewHolder menuListViewHolder;
    DBHelper dbHelper;
    OnClickInAdapter onClickInAdapter;
    String shopIdString;
    String[] btnIdStrings;


    public MenuListViewAdapter(Context context, String[] foodNameStrings, String[] priceStrings, String posID, String MRIP, String shopIdString, String[] btnIdStrings, String[] btnPrice2Strings, String[] btnGPStrings, String[] btnGP2Strings, String[] btnGPRuleStrings, String[] btnGPRule2Strings) {
        this.context = context;
        this.foodNameStrings = foodNameStrings;
        this.priceStrings = priceStrings;
        price2Strings = btnPrice2Strings;
        gpStrings = btnGPStrings;
        gp2Strings = btnGP2Strings;
        PosID = posID;
        this.MRIP = MRIP;
        this.shopIdString = shopIdString;
        this.btnIdStrings = btnIdStrings;
        this.btnGPRuleStrings = btnGPRuleStrings;
        this.btnGPRule2Strings = btnGPRule2Strings;


        gpRuleTempStrings = new LinkedList<String>(Arrays.asList(btnGPRuleStrings));
        gpRule2TempStrings = new LinkedList<String>(Arrays.asList(btnGPRule2Strings));
        foodNameTempStrings = new LinkedList<String>(Arrays.asList(foodNameStrings));
        priceTempStrings = new LinkedList<String>(Arrays.asList(priceStrings));
        price2TempStrings = new LinkedList<String>(Arrays.asList(price2Strings));
        gpTempStrings = new LinkedList<String>(Arrays.asList(gpStrings));
        gp2TempStrings = new LinkedList<String>(Arrays.asList(gp2Strings));
        Log.d(TAG, "MenuListViewAdapter: " + foodNameTempStrings);
        totalIndex = 0;
    }


    public MenuListViewAdapter(Context context, String[] foodNameStrings, String[] priceStrings, String posID, String MRIP, String shopIdString, String[] btnIdStrings, String[] btnPrice2Strings, String[] btnGPStrings, String[] btnGP2Strings, String[] btnFlagStrings, String[] btnGPRuleStrings, String[] btnGPRule2Strings) {
        this.context = context;
        this.foodNameStrings = foodNameStrings;
        this.priceStrings = priceStrings;
        price2Strings = btnPrice2Strings;
        gpStrings = btnGPStrings;
        gp2Strings = btnGP2Strings;
        PosID = posID;
        this.MRIP = MRIP;
        this.shopIdString = shopIdString;
        this.btnIdStrings = btnIdStrings;
        this.btnFlagStrings = btnFlagStrings;
        this.btnGPRuleStrings = btnGPRuleStrings;
        this.btnGPRule2Strings = btnGPRule2Strings;


        gpRuleTempStrings = new LinkedList<String>(Arrays.asList(btnGPRuleStrings));
        gpRule2TempStrings = new LinkedList<String>(Arrays.asList(btnGPRule2Strings));
        btnFlagTempStrings = new LinkedList<String> (Arrays.asList(btnFlagStrings));
        foodNameTempStrings = new LinkedList<String>(Arrays.asList(foodNameStrings));
        priceTempStrings = new LinkedList<String>(Arrays.asList(priceStrings));
        price2TempStrings = new LinkedList<String>(Arrays.asList(price2Strings));
        gpTempStrings = new LinkedList<String>(Arrays.asList(gpStrings));
        gp2TempStrings = new LinkedList<String>(Arrays.asList(gp2Strings));
        Log.d(TAG, "MenuListViewAdapter: " + foodNameTempStrings);
        totalIndex = 0;

    }

    @Override
    public int getCount() {
        return (int) countRow(foodNameStrings.length);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private long countRow(int all) {
        return (long) Math.ceil((long) all / 6.0);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DBHelper(context);
        final Sell sell = new Sell();

        if (view == null) {
            view = layoutInflater.inflate(R.layout.menu_button_list, viewGroup, false);
            menuListViewHolder = new MenuListViewHolder(view);
            view.setTag(menuListViewHolder);
        } else {
            menuListViewHolder = (MenuListViewHolder) view.getTag();
        }


        int dataSize = priceTempStrings.size();



        for (int index = 0; index < 3; index++) {
            final int finalIndex = index;
            switch (index) {
                case 0:
//                    if (foodNameTempStrings.size() > 0) {


                    if ((i * 3 + index) < dataSize) {
                        if (btnFlagTempStrings != null) {
                            if (btnFlagTempStrings.get(i * 3 + finalIndex).equals("2")) {

                                Log.d(TAG, "getView: btnflag 2 " + price2TempStrings.get(i * 3 + finalIndex));
                                menuListViewHolder.oneMenuButtonView.setPrice(price2TempStrings.get(i * 3 + finalIndex));
                            } else {

                                Log.d(TAG, "getView: btnflag " + priceTempStrings.get(i * 3 + finalIndex));
                                menuListViewHolder.oneMenuButtonView.setPrice(priceTempStrings.get(i * 3 + finalIndex));
                            }
                        } else {
                            menuListViewHolder.oneMenuButtonView.setPrice(priceTempStrings.get(i * 3 + finalIndex));

                        }
                        menuListViewHolder.oneMenuButtonView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "getView:  " + foodNameTempStrings.get(i * 3 + finalIndex));
                        menuListViewHolder.oneMenuButtonView.setFoodName(foodNameTempStrings.get(i * 3 + finalIndex));

//                        priceTempStrings.remove(0);
//                        foodNameTempStrings.remove(0);

                        menuListViewHolder.oneMenuButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "onClick: " + dbHelper.countSell());

                                if (dbHelper.countSell() < 20) {

                                    sell.setID(String.valueOf(i * 3 + finalIndex));
                                    sell.setMRIP(MRIP);
                                    sell.setPosID(PosID);
                                    sell.setBtnGPRule(btnGPRuleStrings[i * 3 + finalIndex]);
                                    sell.setBtnGPRule2(btnGPRule2Strings[i * 3 + finalIndex]);

                                    if (btnFlagTempStrings != null) {
                                        if (btnFlagTempStrings.get(i * 3 + finalIndex).equals("2")) {
                                            sell.setBtnFlag("2");
                                            sell.setMrtAmount(price2Strings[i * 3 + finalIndex]);

                                        } else {

                                            sell.setBtnFlag("1");
                                            sell.setMrtAmount(priceStrings[i * 3 + finalIndex]);
                                        }
                                    } else {
                                        sell.setBtnFlag("1");
                                        sell.setMrtAmount(priceStrings[i * 3 + finalIndex]);
                                    }
                                    sell.setFoodName(foodNameStrings[i * 3 + finalIndex]);
                                    sell.setType("Sell");
                                    sell.setBtnIdString(btnIdStrings[i * 3 + finalIndex]);
                                    sell.setShopIdString(shopIdString);
                                    sell.setBtnGP2(gp2Strings[i * 3 + finalIndex]);
                                    sell.setBtnGP(gpStrings[i * 3 + finalIndex]);

                                    Log.d(TAG, "onClick: " + sell.getFoodName());
                                    dbHelper.addSell(sell);

                                    try {
                                        onClickInAdapter = (OnClickInAdapter) context;
                                    } catch (ClassCastException e) {
                                        throw new ClassCastException(context.toString()
                                                + " must implement OnClickInAdapter");
                                    }

                                    onClickInAdapter.onClickInAdapter(String.valueOf(dbHelper.getTotalSell()), dbHelper.getLastSell());
                                } else {
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("ขายสินค้าเกินจำนวนจำกัด");
                                    builder.setMessage("ขออภัย ไม่สามารถขายสินค้าได้เกินจำนวน 20 ชิ้น");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                    } else {
                        menuListViewHolder.oneMenuButtonView.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 1:
//                    if (foodNameTempStrings.size() > 0) {

                    if ((i * 3 + index) < dataSize) {
                        menuListViewHolder.twoMenuButtonView.setVisibility(View.VISIBLE);
                        menuListViewHolder.twoMenuButtonView.setFoodName(foodNameTempStrings.get(i * 3 + finalIndex));

                        if (btnFlagTempStrings != null) {
                            if (btnFlagTempStrings.get(i * 3 + finalIndex).equals("2")) {
                                Log.d(TAG, "getView: btnflag 2 " + price2TempStrings.get(i * 3 + finalIndex));
                                menuListViewHolder.twoMenuButtonView.setPrice(price2TempStrings.get(i * 3 + finalIndex));
                            } else {

                                Log.d(TAG, "getView: btnflag " + priceTempStrings.get(i * 3 + finalIndex));
                                menuListViewHolder.twoMenuButtonView.setPrice(priceTempStrings.get(i * 3 + finalIndex));
                            }
                        } else {
                            menuListViewHolder.twoMenuButtonView.setPrice(priceTempStrings.get(i * 3 + finalIndex));

                        }
//                        priceTempStrings.remove(0);
//                        foodNameTempStrings.remove(0);

                        menuListViewHolder.twoMenuButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                if (dbHelper.countSell() < 20) {
                                    sell.setID(String.valueOf(i * 3 + finalIndex));
                                    sell.setMRIP(MRIP);
                                    sell.setBtnGPRule(btnGPRuleStrings[i * 3 + finalIndex]);
                                    sell.setBtnGPRule2(btnGPRule2Strings[i * 3 + finalIndex]);


                                    if (btnFlagTempStrings != null) {
                                        if (btnFlagTempStrings.get(i * 3 + finalIndex).equals("2")) {
                                            sell.setBtnFlag("2");
                                            sell.setMrtAmount(price2Strings[i * 3 + finalIndex]);

                                        } else {

                                            sell.setBtnFlag("1");
                                            sell.setMrtAmount(priceStrings[i * 3 + finalIndex]);
                                        }
                                    } else {
                                        sell.setBtnFlag("1");
                                        sell.setMrtAmount(priceStrings[i * 3 + finalIndex]);
                                    }
                                    sell.setPosID(PosID);
                                    sell.setFoodName(foodNameStrings[i * 3 + finalIndex]);
                                    sell.setType("Sell");
                                    sell.setBtnIdString(btnIdStrings[i * 3 + finalIndex]);
                                    sell.setShopIdString(shopIdString);
                                    sell.setBtnGP2(gp2Strings[i * 3 + finalIndex]);
                                    sell.setBtnGP(gpStrings[i * 3 + finalIndex]);
                                    Log.d(TAG, "onClick: " + sell.getFoodName());
                                    dbHelper.addSell(sell);

                                    try {
                                        onClickInAdapter = (OnClickInAdapter) context;
                                    } catch (ClassCastException e) {
                                        throw new ClassCastException(context.toString()
                                                + " must implement OnClickInAdapter");
                                    }

                                    onClickInAdapter.onClickInAdapter(String.valueOf(dbHelper.getTotalSell()), dbHelper.getLastSell());

                                } else {
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("ขายสินค้าเกินจำนวนจำกัด");
                                    builder.setMessage("ขออภัย ไม่สามารถขายสินค้าได้เกินจำนวน 20 ชิ้น");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                    } else {
                        menuListViewHolder.twoMenuButtonView.setVisibility(View.INVISIBLE);

                    }
                    break;
                case 2:
//                    if (foodNameTempStrings.size() > 0) {

                    if ((i * 3 + index) < dataSize) {
                        menuListViewHolder.threeMenuButtonView.setVisibility(View.VISIBLE);
                        menuListViewHolder.threeMenuButtonView.setFoodName(foodNameTempStrings.get(i * 3 + finalIndex));


                        if (btnFlagTempStrings != null) {
                            if (btnFlagTempStrings.get(i * 3 + finalIndex).equals("2")) {
                                Log.d(TAG, "getView: btnflag 2 " + price2TempStrings.get(i * 3 + finalIndex));
                                menuListViewHolder.threeMenuButtonView.setPrice(price2TempStrings.get(i * 3 + finalIndex));
                            } else {

                                Log.d(TAG, "getView: btnflag " + priceTempStrings.get(i * 3 + finalIndex));
                                menuListViewHolder.threeMenuButtonView.setPrice(priceTempStrings.get(i * 3 + finalIndex));
                            }
                        } else {
                            menuListViewHolder.threeMenuButtonView.setPrice(priceTempStrings.get(i * 3 + finalIndex));

                        }
//                        priceTempStrings.remove(0);
//                        foodNameTempStrings.remove(0);

                        menuListViewHolder.threeMenuButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                if (dbHelper.countSell() < 20) {
                                    sell.setID(String.valueOf(i * 3 + finalIndex));
                                    sell.setMRIP(MRIP);

                                    sell.setBtnGPRule(btnGPRuleStrings[i * 3 + finalIndex]);
                                    sell.setBtnGPRule2(btnGPRule2Strings[i * 3 + finalIndex]);

                                    if (btnFlagTempStrings != null) {
                                        if (btnFlagTempStrings.get(i * 3 + finalIndex).equals("2")) {
                                            sell.setBtnFlag("2");
                                            sell.setMrtAmount(price2Strings[i * 3 + finalIndex]);

                                        } else {

                                            sell.setBtnFlag("1");
                                            sell.setMrtAmount(priceStrings[i * 3 + finalIndex]);
                                        }
                                    } else {
                                        sell.setBtnFlag("1");
                                        sell.setMrtAmount(priceStrings[i * 3 + finalIndex]);
                                    }

                                    sell.setPosID(PosID);
                                    sell.setFoodName(foodNameStrings[i * 3 + finalIndex]);
                                    sell.setType("Sell");
                                    sell.setBtnIdString(btnIdStrings[i * 3 + finalIndex]);
                                    sell.setShopIdString(shopIdString);
                                    sell.setBtnGP2(gp2Strings[i * 3 + finalIndex]);
                                    sell.setBtnGP(gpStrings[i * 3 + finalIndex]);
                                    Log.d(TAG, "onClick: " + sell.getFoodName());
                                    dbHelper.addSell(sell);

                                    try {
                                        onClickInAdapter = (OnClickInAdapter) context;
                                    } catch (ClassCastException e) {
                                        throw new ClassCastException(context.toString()
                                                + " must implement OnClickInAdapter");
                                    }

                                    onClickInAdapter.onClickInAdapter(String.valueOf(dbHelper.getTotalSell()), dbHelper.getLastSell());
                                } else {
                                    AlertDialog alertDialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("ขายสินค้าเกินจำนวนจำกัด");
                                    builder.setMessage("ขออภัย ไม่สามารถขายสินค้าได้เกินจำนวน 20 ชิ้น");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    alertDialog = builder.create();

                                    alertDialog.show();
                                }
                            }
                        });
                    } else {
                        menuListViewHolder.threeMenuButtonView.setVisibility(View.INVISIBLE);

                    }
                    break;
            }
        }


        return view;
    }

    private class MenuListViewHolder {
        public MenuButtonView oneMenuButtonView, twoMenuButtonView, threeMenuButtonView;

        public MenuListViewHolder(View view) {

            oneMenuButtonView = view.findViewById(R.id.mbtn_1);
            twoMenuButtonView = view.findViewById(R.id.mbtn_2);
            threeMenuButtonView = view.findViewById(R.id.mbtn_3);

        }
    }

    public interface OnClickInAdapter {
        void onClickInAdapter(String totalSell, String lastMenuSell);
    }
}
