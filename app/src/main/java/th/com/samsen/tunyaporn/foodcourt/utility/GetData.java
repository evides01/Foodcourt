package th.com.samsen.tunyaporn.foodcourt.utility;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetData extends Application {

    Connection connection;
    String connectionResultString = "";
    Boolean isSuccessABoolean = false;
    String query, op;
    Context context;
    Statement statement;
    ResultSet resultSet;

    public GetData(String query, String op, Context context) {
        this.query = query;
        this.op = op;
        this.context = context;
    }

    public boolean doInBackgroundUpdate() {
        try {
            Log.d("TAG", "doInBackgroundUpdate: query --> " + query);
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connection(context);

            // query :INSERT INTO CARDTRANS (CUSED,BARCODE,SHOPID,MRTAMOUNT,MRTSTATUS,MRTSLIPNO,MRTLUPDATE,LUPDATE,BACKUPFLG,CHKFLG,CUSED1,FOODNAME,SHOPENDFLG,GP,GPAMOUNT,RENTID,INVNO,POSID) VALUES ('<CUSED>','<BARCODE>','<ShopId>','"
            //                                    + cardTransList.get(i).getMRTAMOUNT() + "','" + cardTransList.get(i).getMRTSTATUS() + "','" + cardTransList.get(i).getMRTSLIPNO()
            //                                    + "','" + cardTransList.get(i).getMRTLUPDATE() + "','" + cardTransList.get(i).getLUPDATE() + "','" + cardTransList.get(i).getBACKUPFLG()
            //                                    + "','" + cardTransList.get(i).getCHKFLG() + "','" + cardTransList.get(i).getCUSED1() + "','" + cardTransList.get(i).getFOODNAME() + "','"
            //                                    + cardTransList.get(i).getSHOPENDFLG() + "','" + cardTransList.get(i).getGP() + "','" + cardTransList.get(i).getGPAMOUNT() + "','"
            //                                    + cardTransList.get(i).getRENTID() + "','" + cardTransList.get(i).getINVNO() + "','"
            //                                    + cardTransList.get(i).getPOSID() + "')
            // query :
            // query :
            // query :
            if (connection == null) {
                connectionResultString = "Check your internet access";
            } else {
                statement = connection.createStatement();
                statement.setQueryTimeout(10);
                statement.executeUpdate(query);
                return true;

            }
        } catch (SQLTimeoutException e) {
            e.printStackTrace();


            String message = Arrays.toString(new String[]{e.getMessage()}) + " Line: " + e.getStackTrace()[0].getLineNumber();
            rabbitExceptionWriteToFile(message);

            Log.e("TAG", "doInBackgroundUpdate: " + e);

            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert!!!");
            builder.setMessage("Update Not Success. TRY AGAIN!!!");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog = builder.create();
            alertDialog.show();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG", "doInBackgroundUpdate: " + e);
            if (!e.toString().equals("java.sql.SQLException: The executeUpdate method must not return a result set.")) {



                String message = Arrays.toString(new String[]{e.getMessage()}) + " Line: " + e.getStackTrace()[0].getLineNumber();
                rabbitExceptionWriteToFile(message);
                
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert!!!");
                builder.setMessage("Update Not Success. TRY AGAIN!!!");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "doInBackgroundUpdate: " + e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("TAG", "doInBackgroundUpdate: " + e);
                }
            }
            return true;
        }
    }

    private void rabbitExceptionWriteToFile(String message) {
        try {

            String path = "/mnt/sdcard/amarin";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            path += "/";
            String thisDate = Utils.getDate();
            String year = thisDate.substring(0, 4);
            String month = thisDate.substring(5, 7);
            String date = thisDate.substring(8, 10);
            path += (year + "-" + month + "-" + date);
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }

            path += "/exceptionLog.cd.txt";

            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, true); //True = Append to file, false = Overwrite
                writer.close();

            }
            FileWriter writer = new FileWriter(file, true); //True = Append to file, false = Overwrite
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public List<Map<String, String>> doInBackground() {
        List<Map<String, String>> dataMapsList = null;

        dataMapsList = new ArrayList<Map<String, String>>();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connection(context);
            if (connection == null) {
                connectionResultString = "Check your internet access";
            } else {
                Log.d("TAG", "doInBackground: Connected");
                Log.d("TAG", "doInBackground: query --> " + query);
                Log.d("TAG", "doInBackground: op --> " + op);
                statement = connection.createStatement();
                statement.setQueryTimeout(10);
                resultSet = statement.executeQuery(query);

                Map<String, String> resultMap;

                if (op.equals("GetStore")) {
                    // query : select s.shoptaxid,vs.* from VSHOPS vs join SHOPS s on s.SHOPID = vs.SHOPID where vs.MRIP = '<IP>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        Log.d("TAG", "doInBackground: POSID " + resultSet.getString("POSID"));
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("SHOPID", resultSet.getString("SHOPID"));
                        resultMap.put("SHOPNAME", resultSet.getString("SHOPNAME"));
                        resultMap.put("OWNER", resultSet.getString("OWNER"));
                        resultMap.put("SLIPNO", resultSet.getString("SLIPNO"));
                        resultMap.put("GPRULE", resultSet.getString("GPRULE"));
//                        resultMap.put("VENDORCODE", resultSet.getString("VENDORCODE"));
                        resultMap.put("SHAREPERCENT", resultSet.getString("SHAREPERCENT"));
                        resultMap.put("SHAREPERCENTFLAG", resultSet.getString("SHAREPERCENTFLAG"));
//                        resultMap.put("GP_PROMO", resultSet.getString("GP_PROMO"));
//                        resultMap.put("TAXID", resultSet.getString("TAXID"));
//                        resultMap.put("SH_ID", resultSet.getString("SH_ID"));
//                        resultMap.put("TAXPOSID", resultSet.getString("TAXPOSID"));
                        resultMap.put("MRIP", resultSet.getString("MRIP"));
                        dataMapsList.add(resultMap);
                    }
                }else if (op.equals("GetStoreAndTerminal")) {
                    // query : select s.shoptaxid,vs.* from VSHOPS vs join SHOPS s on s.SHOPID = vs.SHOPID where vs.MRIP = '<IP>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        Log.d("TAG", "doInBackground: POSID " + resultSet.getString("POSID"));
                        resultMap.put("SHOPID", resultSet.getString("SHOPID"));
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("SHOPNAME", resultSet.getString("SHOPNAME"));
                        resultMap.put("OWNER", resultSet.getString("OWNER"));
                        resultMap.put("SLIPNO", resultSet.getString("SLIPNO"));
                        resultMap.put("GPRULE", resultSet.getString("GPRULE"));
                        resultMap.put("MRIP", resultSet.getString("MRIP"));
                        resultMap.put("TTAXID", resultSet.getString("TTAXID"));
                        resultMap.put("TERMINALID", resultSet.getString("TERMINALID"));
                        resultMap.put("TID", resultSet.getString("TID"));
                        resultMap.put("DESCRIPTION", resultSet.getString("DESCRIPTION"));
                        resultMap.put("INVOICE", resultSet.getString("INVOICE"));
                        resultMap.put("CREDITNOTE", resultSet.getString("CREDITNOTE"));

                        dataMapsList.add(resultMap);
                    }

                }else if (op.equals("GetTendor")) {
                    // query : select s.shoptaxid,vs.* from VSHOPS vs join SHOPS s on s.SHOPID = vs.SHOPID where vs.MRIP = '<IP>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("TID", resultSet.getString("TID"));
                        resultMap.put("CODE", resultSet.getString("CODE"));
                        resultMap.put("NAME", resultSet.getString("NAME"));
                        resultMap.put("VENDERCODE", resultSet.getString("VENDERCODE"));
                        resultMap.put("VENDERNAME", resultSet.getString("VENDERNAME"));
                        resultMap.put("CERRENCY", resultSet.getString("CERRENCY"));
                        resultMap.put("PAYMENT_METHOD", resultSet.getString("PAYMENT_METHOD"));
                        resultMap.put("VALUE_CONVERT", resultSet.getString("VALUE_CONVERT"));
                        resultMap.put("Server", resultSet.getString("Server"));
                        resultMap.put("SEQ", resultSet.getString("SEQ"));
                        resultMap.put("TERMINALID", resultSet.getString("TERMINALID"));
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("MERCHAND_ID", resultSet.getString("MERCHAND_ID"));
                        resultMap.put("APP_ID", resultSet.getString("APP_ID"));
                        resultMap.put("CLIENT_KEY", resultSet.getString("CLIENT_KEY"));
                        resultMap.put("CLIENT_SECRET", resultSet.getString("CLIENT_SECRET"));
                        resultMap.put("PREFIX_BARCODE", resultSet.getString("PREFIX_BARCODE"));
                        resultMap.put("REMARK1", resultSet.getString("REMARK1"));
                        resultMap.put("REMARK2", resultSet.getString("REMARK2"));
                        resultMap.put("REMARK3", resultSet.getString("REMARK3"));
                        resultMap.put("SCAN_TYPE", resultSet.getString("SCAN_TYPE"));
                        resultMap.put("VENDER_SHOP_ID", resultSet.getString("VENDER_SHOP_ID"));

                        dataMapsList.add(resultMap);
                    }

                }else if (op.equals("GetHappyHour")) {
                    // query : select s.shoptaxid,vs.* from VSHOPS vs join SHOPS s on s.SHOPID = vs.SHOPID where vs.MRIP = '<IP>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("happyhour_id", resultSet.getString("happyhour_id"));
                        resultMap.put("happyhour_desc", resultSet.getString("happyhour_desc"));
                        resultMap.put("datestart", resultSet.getString("datestart"));
                        resultMap.put("dateend", resultSet.getString("dateend"));
                        resultMap.put("timestart", resultSet.getString("timestart"));
                        resultMap.put("timeend", resultSet.getString("timeend"));
                        resultMap.put("shopid", resultSet.getString("shopid"));
                        resultMap.put("btnid", resultSet.getString("btnid"));
                        resultMap.put("btnflg", resultSet.getString("btnflg"));
                        resultMap.put("curDate", resultSet.getString("curDate"));
                        resultMap.put("curTime", resultSet.getString("curTime"));

                        dataMapsList.add(resultMap);
                    }
                }else if (op.equals("GetTypeTrueFreeWater")){
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("TYPEID", resultSet.getString("TYPEID"));

                        dataMapsList.add(resultMap);

                    }
                }else if (op.equals("GetDayEndData")) {
                    // query : select * from VSHOPBTNANDROID where MRIP = '<IP>' and BTNGROUP = '<Category>' and SHOPNAME = '<ShopName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("SUMAMT", resultSet.getString("SUMAMT"));
                        resultMap.put("SUMVOID", resultSet.getString("SUMVOID"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetWorkingDate")) {
                    // query : select * from VSHOPBTNANDROID where MRIP = '<IP>' and BTNGROUP = '<Category>' and SHOPNAME = '<ShopName>'

                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("date_flag", resultSet.getString("date_flag"));

                        dataMapsList.add(resultMap);

                    }
                } else if (op.equals("getTerminal")) {
                    // query : select * from VSHOPBTNANDROID where MRIP = '<IP>' and BTNGROUP = '<Category>' and SHOPNAME = '<ShopName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("TID", resultSet.getString("TID"));
                        resultMap.put("TSTATUS", resultSet.getString("TSTATUS"));
                        resultMap.put("SLIPDATE", resultSet.getString("SLIPDATE"));
                        resultMap.put("DESCRIPTION", resultSet.getString("DESCRIPTION"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("getTerminalThaiQRUsed")) {
                    // query : select * from VSHOPBTNANDROID where MRIP = '<IP>' and BTNGROUP = '<Category>' and SHOPNAME = '<ShopName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("TID", resultSet.getString("TID"));
                        resultMap.put("TERMINALID", resultSet.getString("TERMINALID"));
                        resultMap.put("TSTATUS", resultSet.getString("TSTATUS"));
                        resultMap.put("SLIPDATE", resultSet.getString("SLIPDATE"));
                        resultMap.put("TTAXID", resultSet.getString("TTAXID"));
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("RONAME", resultSet.getString("RONAME"));

                        resultMap.put("DESCRIPTION", resultSet.getString("DESCRIPTION"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetStoreButton")) {
                    // query : select * from VSHOPBTNANDROID where MRIP = '<IP>' and BTNGROUP = '<Category>' and SHOPNAME = '<ShopName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("BTNGROUP", resultSet.getString("BTNGROUP"));
                        resultMap.put("BTNNAME", resultSet.getString("BTNNAME"));
                        resultMap.put("BTNID", resultSet.getString("BTNID"));
                        resultMap.put("BTNPRICE", resultSet.getString("BTNPRICE"));
                        resultMap.put("BTNGP", resultSet.getString("BTNGP"));
                        resultMap.put("BTNPRICE2", resultSet.getString("BTNPRICE2"));
                        resultMap.put("BTNGP2", resultSet.getString("BTNGP2"));
                        resultMap.put("BTNGPRULE", resultSet.getString("BTNGPRULE"));
                        resultMap.put("BTNGPRULE2", resultSet.getString("BTNGPRULE2"));
//                        resultMap.put("SHOPNAME", resultSet.getString("SHOPNAME"));
//                        resultMap.put("MRIP", resultSet.getString("MRIP"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetStoreButtonJoinHappyHour")) {
                    // query : select * from VSHOPBTNANDROID where MRIP = '<IP>' and BTNGROUP = '<Category>' and SHOPNAME = '<ShopName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("BTNGROUP", resultSet.getString("BTNGROUP"));
                        resultMap.put("BTNNAME", resultSet.getString("BTNNAME"));
                        resultMap.put("BTNID", resultSet.getString("BTNID"));
                        resultMap.put("BTNPRICE", resultSet.getString("BTNPRICE"));
                        resultMap.put("BTNGP", resultSet.getString("BTNGP"));
                        resultMap.put("BTNPRICE2", resultSet.getString("BTNPRICE2"));
                        resultMap.put("BTNGP2", resultSet.getString("BTNGP2"));
                        resultMap.put("btnflg", resultSet.getString("btnflg"));
                        resultMap.put("BTNGPRULE", resultSet.getString("BTNGPRULE"));
                        resultMap.put("BTNGPRULE2", resultSet.getString("BTNGPRULE2"));


//                        resultMap.put("SHOPNAME", resultSet.getString("SHOPNAME"));
//                        resultMap.put("MRIP", resultSet.getString("MRIP"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetCardDetail")) {
                    // query : select TOP 1 BARCODE,CASE WHEN CEXPIRED > GETDATE() THEN 'N' ELSE 'Y' END AS CEXPIRED_FLAG, CEXPIRED,CSTATUS, CASE WHEN CSTATUS = 'A' THEN 'Y' ELSE 'N' END AS CSTATUS_FLAG,CBALANCE,CLSLIPNO from VCARDSANDROID where BARCODE = '<BARCODE>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                            resultMap.put("CUSED", resultSet.getString("CUSED"));
                        resultMap.put("CUSED1", resultSet.getString("CUSED1"));
                        resultMap.put("BARCODE", resultSet.getString("BARCODE"));
                        resultMap.put("CEXPIRED", resultSet.getString("CEXPIRED"));
                        resultMap.put("CEXPIRED_FLAG", resultSet.getString("CEXPIRED_FLAG"));
                        resultMap.put("TYPEID", resultSet.getString("TYPEID"));
                        resultMap.put("CSTATUS", resultSet.getString("CSTATUS"));
                        resultMap.put("CSTATUS_FLAG", resultSet.getString("CSTATUS_FLAG"));
                        resultMap.put("CBALANCE", resultSet.getString("CBALANCE"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetCardData")) {
                    // query : select top 1 CUSED,CUSED1 from CARDS where BARCODE = '<BARCODE>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("CUSED", resultSet.getString("CUSED"));
                        resultMap.put("CUSED1", resultSet.getString("CUSED1"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetCardTrans")) {
                    // query : select * from VCARDTRANS where MRTSLIPNO like '<INV>%'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("FOODNAME", resultSet.getString("FOODNAME"));
                        resultMap.put("MRTAMOUNT", resultSet.getString("MRTAMOUNT"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetSum")) {
                    // query : select SUM(MRTAMOUNT) as SUMAMT from VCARDTRANS where MRTSLIPNO like '<INV>%'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("SUMAMT", resultSet.getString("SUMAMT"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetSumAmt")) {
                    // query : select ISNULL(CAST(SUM(MRTAMOUNT) AS VARCHAR(10)), '0') as SUMAMT from CARDTRANS where DATEADD(dd, 0, DATEDIFF(dd, 0, MRTLUPDATE)) = '<Date>' and SHOPID = '<ShopId>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("SUMAMT", resultSet.getString("SUMAMT"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetShopData")) {
                    // query : select top 1 s.SHOPID,s.POSID,s.SHOPNAME,s.SLIPNO,s.GPRULE,sb.BTNPRICE,sb.BTNGROUP,sb.BTNNAME, GETDATE() as GDATE from SHOPS s inner join SHOPBUTTONS sb on sb.SHOPID = s.SHOPID where SHOPNAME = '<ShopName>' and BTNNAME = '<BtnName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("SHOPID", resultSet.getString("SHOPID"));
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("SHOPNAME", resultSet.getString("SHOPNAME"));
                        resultMap.put("GDATE", resultSet.getString("GDATE"));
                        resultMap.put("SLIPNO", resultSet.getString("SLIPNO"));
                        resultMap.put("SHAREPERCENT", resultSet.getString("SHAREPERCENT"));
//                        resultMap.put("SHAREPERCENTFLAG", resultSet.getString("SHAREPERCENTFLAG"));
//                        resultMap.put("GPRULE", resultSet.getString("GPRULE"));
//                        resultMap.put("BTNPRICE2", resultSet.getString("BTNPRICE2"));
//                        resultMap.put("BTNGP", resultSet.getString("BTNGP"));
//                        resultMap.put("BTNGP2", resultSet.getString("BTNGP2"));
//                        resultMap.put("BTNGPRULE", resultSet.getString("BTNGPRULE"));
//                        resultMap.put("BTNGPRULE2", resultSet.getString("BTNGPRULE2"));
                        resultMap.put("BTNPRICE", resultSet.getString("BTNPRICE"));
                        resultMap.put("BTNGROUP", resultSet.getString("BTNGROUP"));
                        resultMap.put("BTNNAME", resultSet.getString("BTNNAME"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetCategory")) {
                    // query : select distinct POSID,BTNGROUP from VBUTTONSHOP where POSID = '<POSID>' and SHOPNAME = '<ShopName>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("BTNGROUP", resultSet.getString("BTNGROUP"));
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetShopCardEnd")) {
                    // query : select SCEID,TOTALCARD from SHOPCARDEND where POSID = '<POSID>' and DATEADD(dd, 0, DATEDIFF(dd, 0, CARDENDDATE)) = '<Date>' and SHOPID = '<ShopId>'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("SCEID", resultSet.getString("SCEID"));
                        resultMap.put("TOTALCARD", resultSet.getString("TOTALCARD"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetCompanyProfile")) {
                    // query : select * from COMPANYPROFILE
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("BRNNAMETHAI", resultSet.getString("BRNNAMETHAI"));
                        resultMap.put("COMID", resultSet.getString("COMID"));
                        resultMap.put("BRNID", resultSet.getString("BRNID"));
                        resultMap.put("BRNNAMEENG", resultSet.getString("BRNNAMEENG"));
                        resultMap.put("BRNTAXID", resultSet.getString("BRNTAXID"));
                        resultMap.put("BRNVAT", resultSet.getString("BRNVAT"));
                        resultMap.put("COMPNAME", resultSet.getString("COMPNAME"));
                        resultMap.put("ADDRESS1", resultSet.getString("ADDRESS1"));
                        resultMap.put("ADDRESS2", resultSet.getString("ADDRESS2"));
                        resultMap.put("REMARK", resultSet.getString("REMARK"));
//                        resultMap.put("QrLotNo", resultSet.getString("QrLotNo"));
//                        resultMap.put("QrRunNo", resultSet.getString("QrRunNo"));
//                        resultMap.put("FullTaxNo", resultSet.getString("FullTaxNo"));
                        dataMapsList.add(resultMap);
                    }
                } else if (op.equals("GetPOSData")) {
                    // query : select distinct MRIP,POSID,SHOPNAME,OWNER,SHOPID from VSHOPS where MRIP = '<IP>' and SHOPNAME like '%<StoreName>%'
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("SHOPNAME", resultSet.getString("SHOPNAME"));
                        resultMap.put("OWNER", resultSet.getString("OWNER"));
                        resultMap.put("MRIP", resultSet.getString("MRIP"));
                        resultMap.put("SHOPID", resultSet.getString("SHOPID"));
                        dataMapsList.add(resultMap);
                    }
                }else  if(op.equals("GetShopForSlip")){
                    while (resultSet.next()) {
                        resultMap = new HashMap<String, String>();
                        resultMap.put("POSID", resultSet.getString("POSID"));
                        resultMap.put("SLIPNO", resultSet.getString("SLIPNO"));
                        resultMap.put("GDATE", resultSet.getString("GDATE"));
                        dataMapsList.add(resultMap);
                    }
                }else  if(op.equals("GetRabbitDiscount")){
                    while (resultSet.next()){
                        resultMap = new HashMap<String, String>();
                        resultMap.put("BUYAMOUNT", resultSet.getString("BUYAMOUNT"));
                        resultMap.put("DISCOUNT", resultSet.getString("DISCOUNT"));
                        dataMapsList.add(resultMap);
                    }
                }else  if(op.equals("GetTerminalData")){
                    while (resultSet.next()){
                        resultMap = new HashMap<String, String>();
                        resultMap.put("TTAXID", resultSet.getString("TTAXID"));
                        resultMap.put("INVOICE", resultSet.getString("INVOICE"));
                        dataMapsList.add(resultMap);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("TAG", "doInBackground: " + e + " Line : " + e.getStackTrace()[0].getLineNumber());

        }

        return dataMapsList;
    }

}
