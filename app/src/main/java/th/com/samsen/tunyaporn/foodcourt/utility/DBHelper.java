package th.com.samsen.tunyaporn.foodcourt.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import th.com.samsen.tunyaporn.foodcourt.model.AddCardModel;
import th.com.samsen.tunyaporn.foodcourt.model.Category;
import th.com.samsen.tunyaporn.foodcourt.model.Configuration;
import th.com.samsen.tunyaporn.foodcourt.model.Sell;


public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    String TAG = "TAG";

    public DBHelper(Context context) {
        super(context, Configuration.DATABASE_NAME, null, Configuration.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Table Configuration
        if (!checkDBExist(Configuration.TABLE)) {
            String CREATE_CONFIGURATION_TABLE = String.format("CREATE TABLE %s (%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", Configuration.TABLE, Configuration.Column.ip, Configuration.Column.dbname, Configuration.Column.username, Configuration.Column.password, Configuration.Column.duration);
            db.execSQL(CREATE_CONFIGURATION_TABLE);
        }

        //Create Table Category
        if (!checkDBExist(Category.TABLE)) {
            String CREATE_CATEGORY_TABLE = String.format("CREATE TABLE %s (%s TEXT, %s TEXT)", Category.TABLE, Category.Column.POSID, Category.Column.CategoryName);
            db.execSQL(CREATE_CATEGORY_TABLE);
        }

        if (!checkDBExist(Sell.TABLE)) {
            String CREATE_SELL_TABLE = String.format("CREATE TABLE %s (%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", Sell.TABLE, Sell.Column.FoodName, Sell.Column.MrtAmount, Sell.Column.MRIP, Sell.Column.PosID, Sell.Column.Type, Sell.Column.ID, Sell.Column.BtnIdString, Sell.Column.ShopIdString, Sell.Column.BtnGP, Sell.Column.BtnGP2, Sell.Column.BtnFlag, Sell.Column.BtnGPRule, Sell.Column.BtnGPRule2);
            Log.d(TAG, "createTableSell: " + CREATE_SELL_TABLE);
            db.execSQL(CREATE_SELL_TABLE);
        }

        if (!checkDBExist(AddCardModel.TABLE)) {
            String CREATE_ADD_CARD_TABLE = String.format("CREATE TABLE %s (%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", AddCardModel.TABLE, AddCardModel.Column.BARCODE, AddCardModel.Column.CUSED, AddCardModel.Column.SLIPNO, AddCardModel.Column.AMT, AddCardModel.Column.ITEMBALANCE, AddCardModel.Column.ITEMID);
            db.execSQL(CREATE_ADD_CARD_TABLE);
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Table Configuration
        String DROP_CONFIGURATION_TABLE = String.format("DROP TABLE IF EXISTS %s", Configuration.TABLE);
        db.execSQL(DROP_CONFIGURATION_TABLE);
        onCreate(db);

        //Drop Table Category
        String DROP_CATEGORY_TABLE = String.format("DROP TABLE IF EXISTS %s", Category.TABLE);
        db.execSQL(DROP_CATEGORY_TABLE);
        onCreate(db);

        //Drop Table Sell
        String DROP_SELL_TABLE = String.format("DROP TABLE IF EXISTS %s", Sell.TABLE);
        db.execSQL(DROP_SELL_TABLE);
        onCreate(db);

        //Drop Table Add Card
        String DROP_ADD_CARD_TABLE = String.format("DROP TABLE IF EXISTS %s", AddCardModel.TABLE);
        db.execSQL(DROP_ADD_CARD_TABLE);
        onCreate(db);
    }

    public String getSellId() {
        String id = "0";
        sqLiteDatabase = this.getWritableDatabase();

        if (checkDBExist(Sell.TABLE)) {
            Cursor cursor = sqLiteDatabase.query(Sell.TABLE, new String[]{Sell.Column.ID}, null, null, null, null, Sell.Column.ID + " DESC", "1");
            if (cursor != null) {
                cursor.moveToFirst();
            }

            if (cursor.getCount() > 0) {
                id = String.valueOf(Integer.valueOf(cursor.getString(0)) + 1);
            }
            cursor.close();
        }
        return id;
    }

    public void deleteSellRow(String idString) {
        sqLiteDatabase = this.getWritableDatabase();

        if (checkDBExist(Sell.TABLE)) {
            sqLiteDatabase.delete(Sell.TABLE, Sell.Column.ID + " = " + idString, null);
        }
    }

    public void addAddCard(AddCardModel addCardModel) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(AddCardModel.Column.BARCODE, addCardModel.getBARCODE());
        contentValues.put(AddCardModel.Column.AMT, addCardModel.getAMT());
        contentValues.put(AddCardModel.Column.CUSED, addCardModel.getCUSED());
        contentValues.put(AddCardModel.Column.ITEMBALANCE, addCardModel.getITEMBALANCE());
        contentValues.put(AddCardModel.Column.SLIPNO, addCardModel.getSLIPNO());

        sqLiteDatabase.insert(AddCardModel.TABLE, null, contentValues);

        Cursor cursor = sqLiteDatabase.query(AddCardModel.TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() > 0) {
            Log.d(TAG, "addAddCardModel: " + cursor.getString(0));

        }


//        sqLiteDatabase.close();
        cursor.close();
    }

    public void addSell(Sell sell) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Sell.Column.FoodName, sell.getFoodName());
        contentValues.put(Sell.Column.MRIP, sell.getMRIP());
        contentValues.put(Sell.Column.PosID, sell.getPosID());
        contentValues.put(Sell.Column.MrtAmount, sell.getMrtAmount());
        contentValues.put(Sell.Column.Type, sell.getType());
        contentValues.put(Sell.Column.ID, this.getSellId());
        contentValues.put(Sell.Column.BtnIdString, sell.getBtnIdString());
        contentValues.put(Sell.Column.ShopIdString, sell.getShopIdString());
        contentValues.put(Sell.Column.BtnGP, sell.getBtnGP());
        contentValues.put(Sell.Column.BtnGP2, sell.getBtnGP2());
        contentValues.put(Sell.Column.BtnGPRule, sell.getBtnGPRule());
        contentValues.put(Sell.Column.BtnGPRule2, sell.getBtnGPRule2());
        contentValues.put(Sell.Column.BtnFlag, sell.getBtnFlag());

        Log.d(TAG, "addSell: " + contentValues.getAsString(Sell.Column.ID));

        sqLiteDatabase.insert(Sell.TABLE, null, contentValues);

        Cursor cursor = sqLiteDatabase.query(Sell.TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() > 0) {
            Log.d(TAG, "addSell: " + cursor.getString(0));

        }


//        sqLiteDatabase.close();
        cursor.close();
    }

    public Boolean checkDBExist(String dbName) {
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + dbName + "'";

        if (sqLiteDatabase != null) {
            Log.d(TAG, "checkDBExist: " + query);
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {

                    cursor.close();
                    return true;
                }
                cursor.close();
            }

        }

        return false;
    }

    public List<AddCardModel> getAddCard() {

        sqLiteDatabase = this.getWritableDatabase();

        AddCardModel addCardModel;
        List<AddCardModel> addCardModels = new ArrayList<>();

        if (checkDBExist(AddCardModel.TABLE)) {

            Cursor cursor = sqLiteDatabase.query(AddCardModel.TABLE, null, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    addCardModel = new AddCardModel();
                    addCardModel.setBARCODE(cursor.getString(0));
                    addCardModel.setCUSED(cursor.getString(1));
                    addCardModel.setSLIPNO(cursor.getString(2));
                    addCardModel.setAMT(cursor.getString(3));
                    addCardModel.setITEMBALANCE(cursor.getString(4));
                    addCardModels.add(addCardModel);
                }

//                sqLiteDatabase.close();
                cursor.close();
                return addCardModels;
            } else {

//                sqLiteDatabase.close();
                cursor.close();
                return null;
            }


        } else {
//            sqLiteDatabase.close();
            return null;
        }
    }

    public List<Sell> getSellWithCount() {
        sqLiteDatabase = this.getWritableDatabase();

        Sell sell;
        List<Sell> sells = new ArrayList<>();
        if (checkDBExist(Sell.TABLE)) {
            Cursor cursor = sqLiteDatabase.query(Sell.TABLE,new String[]{"*","COUNT(*) as count"}, null, null, Sell.Column.BtnIdString, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);

                    Log.d(TAG, "getCategoryDetail: data ---> " + cursor.getString(13));


                    sell = new Sell();
                    sell.setBtnFlag(cursor.getString(Sell.ColumnIndex.BtnFlag));
                    sell.setBtnGP(cursor.getString(Sell.ColumnIndex.BtnGP));
                    sell.setBtnGP2(cursor.getString(Sell.ColumnIndex.BtnGP2));
                    sell.setBtnIdString(cursor.getString(Sell.ColumnIndex.BtnIdString));
                    sell.setFoodName(cursor.getString(Sell.ColumnIndex.FoodName));
                    sell.setID(cursor.getString(Sell.ColumnIndex.ID));
                    sell.setMRIP(cursor.getString(Sell.ColumnIndex.MRIP));
                    sell.setMrtAmount(cursor.getString(Sell.ColumnIndex.MrtAmount));
                    sell.setPosID(cursor.getString(Sell.ColumnIndex.PosID));
                    sell.setShopIdString(cursor.getString(Sell.ColumnIndex.ShopIdString));
                    sell.setType(cursor.getString(Sell.ColumnIndex.Type));
                    sell.setBtnGPRule(cursor.getString(Sell.ColumnIndex.BtnGPRule));
                    sell.setBtnGPRule2(cursor.getString(Sell.ColumnIndex.BtnGPRule2));
                    sell.setCount(cursor.getString(13));

                    sells.add(sell);
                }

                cursor.close();
                return sells;
            } else {

                Log.i(TAG, "getCategoryDetail: Not Have Data");
                sqLiteDatabase.close();
                sell = new Sell();
                sell.setFoodName("No Data");
                sell.setMrtAmount("No Data");
                sell.setPosID("No Data");
                sell.setMRIP("No Data");
                sell.setType("No Data");
                sell.setID("No Data");

                sells.add(sell);
                return sells;
            }
        } else {
            onCreate(sqLiteDatabase);
            sell = new Sell();
            sell.setFoodName("Error");
            sell.setMrtAmount("Error");
            sell.setPosID("Error");
            sell.setMRIP("Error");
            sell.setType("Error");
            sell.setID("Error");

            sells.add(sell);
            return sells;
        }

    }

    public List<Sell> getSell() {
        sqLiteDatabase = this.getWritableDatabase();

        Sell sell;
        List<Sell> sells = new ArrayList<>();
        if (checkDBExist(Sell.TABLE)) {


            Cursor cursor = sqLiteDatabase.query(Sell.TABLE, null, null, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//            }
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {

                    cursor.moveToPosition(i);

//                    Log.d(TAG, "getCategoryDetail: data ---> " + cursor.getString(10));


                    sell = new Sell();
                    sell.setBtnFlag(cursor.getString(Sell.ColumnIndex.BtnFlag));
                    sell.setBtnGP(cursor.getString(Sell.ColumnIndex.BtnGP));
                    sell.setBtnGP2(cursor.getString(Sell.ColumnIndex.BtnGP2));
                    sell.setBtnIdString(cursor.getString(Sell.ColumnIndex.BtnIdString));
                    sell.setFoodName(cursor.getString(Sell.ColumnIndex.FoodName));
                    sell.setID(cursor.getString(Sell.ColumnIndex.ID));
                    sell.setMRIP(cursor.getString(Sell.ColumnIndex.MRIP));
                    sell.setMrtAmount(cursor.getString(Sell.ColumnIndex.MrtAmount));
                    sell.setPosID(cursor.getString(Sell.ColumnIndex.PosID));
                    sell.setShopIdString(cursor.getString(Sell.ColumnIndex.ShopIdString));
                    sell.setType(cursor.getString(Sell.ColumnIndex.Type));
                    sell.setBtnGPRule(cursor.getString(Sell.ColumnIndex.BtnGPRule));
                    sell.setBtnGPRule2(cursor.getString(Sell.ColumnIndex.BtnGPRule2));
                    sells.add(sell);
                }
//                sqLiteDatabase.close();
                cursor.close();
                return sells;
            } else {


                Log.i(TAG, "getCategoryDetail: Not Have Data");
                sqLiteDatabase.close();
                sell = new Sell();
                sell.setFoodName("No Data");
                sell.setMrtAmount("No Data");
                sell.setPosID("No Data");
                sell.setMRIP("No Data");
                sell.setType("No Data");
                sell.setID("No Data");

                sells.add(sell);
                return sells;
            }
        } else {
            onCreate(sqLiteDatabase);
            sell = new Sell();
            sell.setFoodName("Error");
            sell.setMrtAmount("Error");
            sell.setPosID("Error");
            sell.setMRIP("Error");
            sell.setType("Error");
            sell.setID("Error");

            sells.add(sell);
            return sells;
        }

    }


    public String getLastItemBalance() {
        sqLiteDatabase = getWritableDatabase();

        String result = "";


        if (checkDBExist(AddCardModel.TABLE)) {

            Cursor cursor = sqLiteDatabase.query(AddCardModel.TABLE, null, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToLast();

                result = cursor.getString(4);

            }

            cursor.close();
        }

//        sqLiteDatabase.close();
        return result;
    }

    public int getTotalSell() {

        List<Sell> sells = new ArrayList<>();

        sells = this.getSell();

        int total = 0;
        if (sells.size() > 0) {
            if (!sells.get(0).getFoodName().equals("No Data") && !sells.get(0).getFoodName().equals("Error")) {
                for (int i = 0; i < sells.size(); i++) {

                    total += Integer.valueOf(sells.get(i).getMrtAmount());
                }
            }
        }
        Log.d(TAG, "getTotalSell: " + total);

        return total;
    }

    public void clearSell() {
        sqLiteDatabase = this.getWritableDatabase();
        if (checkDBExist(Sell.TABLE)) {

            sqLiteDatabase.delete(Sell.TABLE, null, null);
        }
//        sqLiteDatabase.close();
    }

    public void clearAddCard() {
        sqLiteDatabase = this.getWritableDatabase();
        if (checkDBExist(AddCardModel.TABLE)) {
            sqLiteDatabase.delete(AddCardModel.TABLE, null, null);
        }
//        sqLiteDatabase.close();
    }

    public Configuration getConfigurationDetail() {
        sqLiteDatabase = this.getWritableDatabase();

        Configuration configuration = new Configuration();

        Cursor cursor = sqLiteDatabase.query(Configuration.TABLE, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (cursor.getCount() > 0) {
            configuration.setIp(cursor.getString(0));
            configuration.setDbname(cursor.getString(1));
            configuration.setUsername(cursor.getString(2));
            configuration.setPassword(cursor.getString(3));
            configuration.setDuration(cursor.getString(4));

            cursor.close();
//            sqLiteDatabase.close();
            return configuration;
        } else {
            Log.i(TAG, "getConfigurationDetail: Not Have Data");

            cursor.close();
//            sqLiteDatabase.close();
            return null;
        }
    }

    public List<Category> getCategory(String s) {
        sqLiteDatabase = this.getWritableDatabase();

        Category category;
        List<Category> categories = new ArrayList<>();

        Log.d(TAG, "getCategory: " + s);

        Cursor cursor = sqLiteDatabase.query(Category.TABLE, null, Category.Column.POSID + " = ? ",
                new String[]{s}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                category = new Category();
                category.setPOSID(cursor.getString(0));
                category.setCategoryName(cursor.getString(1));
                categories.add(category);
            }
            cursor.close();
//            sqLiteDatabase.close();
            return categories;
        } else {


            Log.i(TAG, "getCategoryDetail: Not Have Data");

            cursor.close();

//            sqLiteDatabase.close();
            return null;
        }
    }

    public void clearCategory() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Category.TABLE, "1", null);
        sqLiteDatabase.close();
    }

    public String getLastSell() {
        String lastSell = "";

        sqLiteDatabase = this.getWritableDatabase();

        if (checkDBExist(Sell.TABLE)) {

            Cursor cursor = sqLiteDatabase.query(Sell.TABLE, new String[]{Sell.Column.FoodName}, null, null, null, null, Sell.Column.ID + " DESC", "1");

            if (cursor != null) {
                cursor.moveToFirst();
            }

            if (cursor.getCount() > 0) {
                lastSell = cursor.getString(0);
            }

            cursor.close();
        }


//        sqLiteDatabase.close();
        return lastSell;
    }

    public int countSell() {
        sqLiteDatabase = this.getWritableDatabase();

        int count = 0;
        if (checkDBExist(Sell.TABLE)) {

            Cursor cursor = sqLiteDatabase.query(Sell.TABLE, null, null, null, null, null, null);

            if (cursor != null) {
                count = cursor.getCount();
            }

            cursor.close();
        }


        return count;
    }


    public void addConfiguration(Configuration configuration) {
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(Configuration.TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            sqLiteDatabase.delete(Configuration.TABLE, "1", null);
        }

        cursor.close();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Configuration.Column.ip, configuration.getIp());
        contentValues.put(Configuration.Column.dbname, configuration.getDbname());
        contentValues.put(Configuration.Column.username, configuration.getUsername());
        contentValues.put(Configuration.Column.password, configuration.getPassword());
        contentValues.put(Configuration.Column.duration, configuration.getDuration());

        sqLiteDatabase.insert(Configuration.TABLE, null, contentValues);

//        sqLiteDatabase.close();
    }

    public void addCategory(Category category) {
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(Category.TABLE, null, Category.Column.POSID + " = ? AND " + Category.Column
                .CategoryName + " = ?", new String[]{category.getPOSID(), category.getCategoryName()}, null, null, null);
        if (cursor != null) {
            sqLiteDatabase.delete(Category.TABLE, Category.Column.POSID + " = ? AND " + Category.Column
                    .CategoryName + " = ?", new String[]{category.getPOSID(), category.getCategoryName()});
        }

        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Category.Column.POSID, category.getPOSID());
        contentValues.put(Category.Column.CategoryName, category.getCategoryName());

        Log.d(TAG, "addCategory: " + category.getPOSID());
        Log.d(TAG, "addCategory: " + category.getCategoryName());

        sqLiteDatabase.insert(Category.TABLE, null, contentValues);

//        sqLiteDatabase.close();
    }
}
