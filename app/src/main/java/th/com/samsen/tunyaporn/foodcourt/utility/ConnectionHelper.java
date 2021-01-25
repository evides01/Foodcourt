package th.com.samsen.tunyaporn.foodcourt.utility;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import th.com.samsen.tunyaporn.foodcourt.R;
import th.com.samsen.tunyaporn.foodcourt.model.Configuration;


public class ConnectionHelper {
    String TAG = "TAG";
    DBHelper dbHelper;
    Configuration configuration;

    public Connection connection(Context context) {
        dbHelper = new DBHelper(context);

        configuration = dbHelper.getConfigurationDetail();
//        configuration = new Configuration("192.168.102.224", "sa", "12345", "dbfood");
//        configuration = new Configuration("192.168.1.50", "sa", "12345", "dbfood_tops","35");
//        configuration = new Configuration("172.20.10.11", "sa", "12345", "dbfood_amarin","35");


        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Connection connection = null;
        String connectionURL = null;


        try {

            String[] split = configuration.getIp().split("\\\\");
            String server = split[0];
            String instance = "";
            if (split.length > 1) {
                instance = split[1];
            }
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server + ";instance="+instance+";databaseName=" + configuration.getDbname() + ";user=" + configuration.getUsername() + ";password=" + configuration.getPassword() + ";loginTimeout=5;socketTimeout=2";
//            connectionURL = "jdbc:jtds:sqlserver://192.168.102.60;instance=sql2017;database=dbfood;user=sa;password=";
            Log.e(TAG, "connection: " + connectionURL);
            Properties properties = new Properties();
            properties.put("connectTimeout", "2000");

            DriverManager.setLoginTimeout(10);
            connection = DriverManager.getConnection(connectionURL,properties);

        } catch (SQLException se) {
            Log.e("TAG", "error here SQLException : " + se.getMessage());
            Toast.makeText(context, context.getResources().getString(R.string.pls_check_conn) + " " + se + " Line : " + se.getStackTrace()[0].getLineNumber(), Toast.LENGTH_LONG).show();
            return null;
        } catch (ClassNotFoundException e) {
            Log.e("TAG", "error here ClassNotFoundException : " + e.getMessage());
            Toast.makeText(context, context.getResources().getString(R.string.pls_check_conn) + " " + e + " Line : " + e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_LONG).show();
            return null;
        } catch (NetworkOnMainThreadException e) {
            // log this exception
            Log.e("TAG", "error here NetworkOnMainThreadException : " + e.getMessage());
            Toast.makeText(context, context.getResources().getString(R.string.pls_check_conn) + " " + e + " Line : " + e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("TAG", "error here Exception : " + e.getMessage());
            Toast.makeText(context, context.getResources().getString(R.string.pls_check_conn) + " " + e + " Line : " + e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_LONG).show();
            return null;
        }


        return connection;
    }
}
