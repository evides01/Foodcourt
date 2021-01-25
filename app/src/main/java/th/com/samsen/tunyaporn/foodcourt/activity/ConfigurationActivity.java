package th.com.samsen.tunyaporn.foodcourt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import th.com.samsen.tunyaporn.foodcourt.R;
import th.com.samsen.tunyaporn.foodcourt.model.Configuration;
import th.com.samsen.tunyaporn.foodcourt.utility.DBHelper;

public class ConfigurationActivity extends AppCompatActivity {

    Button saveButton, backButton;
    EditText ipEditText, databaseEditText, usernameEditText, passwordEditText, rabbitDurationEditText;
    private String classString, shopNameString, statusString, ipString, companyString, posidString, categoryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        bindWidget();
        GetDataExtra();

        DBHelper dbHelper = new DBHelper(ConfigurationActivity.this);
        if (dbHelper.getConfigurationDetail() != null) {
            Configuration configuration = dbHelper.getConfigurationDetail();
            databaseEditText.setText(configuration.getDbname());
            ipEditText.setText(configuration.getIp());
            usernameEditText.setText(configuration.getUsername());
            passwordEditText.setText(configuration.getPassword());
//            rabbitDurationEditText.setText(configuration.getDuration());
        }

    }

    private void GetDataExtra() {
        classString = getIntent().getStringExtra("Class");

        if (classString.equals("MainActivity")) {
            shopNameString = getIntent().getStringExtra("ShopName");
            statusString = getIntent().getStringExtra("Status");
        } else if (classString.equals("MenuActivity")) {
            shopNameString = getIntent().getStringExtra("ShopName");
            categoryString = getIntent().getStringExtra("Category");
            ipString = getIntent().getStringExtra("IP");
            companyString = getIntent().getStringExtra("Company");
            posidString = getIntent().getStringExtra("POSID");
        }
    }

    @Override
    public void onBackPressed() {
        if (classString.equals("MainActivity")) {
            Intent intent = new Intent(ConfigurationActivity.this, MainActivity.class);
            intent.putExtra("Class", classString);
            intent.putExtra("ShopName", shopNameString);
            intent.putExtra("Status", statusString);
            startActivity(intent);
            finish();

        }
//        else if (classString.equals("MenuActivity")) {
//            Intent intent = new Intent(ConfigurationActivity.this, MenuActivity.class);
//            intent.putExtra("Class", classString);
//            intent.putExtra("Category", categoryString);
//            intent.putExtra("IP", ipString);
//            intent.putExtra("Company", companyString);
//            intent.putExtra("POSID", posidString);
//            intent.putExtra("ShopName", shopNameString);
//            startActivity(intent);
//            finish();
//
//        }

    }


    public void SaveButtonController(View view) {

        DBHelper dbHelper = new DBHelper(ConfigurationActivity.this);
        Configuration configuration = new Configuration();
        configuration.setDbname(databaseEditText.getText().toString());
        configuration.setIp(ipEditText.getText().toString());
        configuration.setPassword(passwordEditText.getText().toString());
        configuration.setUsername(usernameEditText.getText().toString());
        configuration.setDuration("0");
        dbHelper.addConfiguration(configuration);

        if (classString.equals("MainActivity")) {
            Intent intent = new Intent(ConfigurationActivity.this, MainActivity.class);
            intent.putExtra("Class", classString);
            intent.putExtra("ShopName", shopNameString);
            intent.putExtra("Status", statusString);
            startActivity(intent);
            finish();

        }
//        else if (classString.equals("MenuActivity")) {
//            Intent intent = new Intent(ConfigurationActivity.this, MenuActivity.class);
//            intent.putExtra("Class", classString);
//            intent.putExtra("Category", categoryString);
//            intent.putExtra("IP", ipString);
//            intent.putExtra("Company", companyString);
//            intent.putExtra("POSID", posidString);
//            intent.putExtra("ShopName", shopNameString);
////                    try {
////                        serialport2.close();
////                    } catch (RemoteException e) {
////                        e.printStackTrace();
////                    }
//            startActivity(intent);
//            finish();
//
//        }

    }

    public void BackButtonController(View view) {

        if (classString.equals("MainActivity")) {
            Intent intent = new Intent(ConfigurationActivity.this, MainActivity.class);
            intent.putExtra("Class", classString);
            intent.putExtra("ShopName", shopNameString);
            intent.putExtra("Status", statusString);
            startActivity(intent);
            finish();

        }
//        else if (classString.equals("MenuActivity")) {
//            Intent intent = new Intent(ConfigurationActivity.this, MenuActivity.class);
//            intent.putExtra("Class", classString);
//            intent.putExtra("Category", categoryString);
//            intent.putExtra("IP", ipString);
//            intent.putExtra("Company", companyString);
//            intent.putExtra("POSID", posidString);
//            intent.putExtra("ShopName", shopNameString);
////                    try {
////                        serialport2.close();
////                    } catch (RemoteException e) {
////                        e.printStackTrace();
////                    }
//            startActivity(intent);
//            finish();
//
//        }

    }

    private void bindWidget() {

        ipEditText = findViewById(R.id.edtIP);
        databaseEditText = findViewById(R.id.edtDatabase);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        rabbitDurationEditText = findViewById(R.id.edt_duration);

        saveButton = findViewById(R.id.btn_save);
        backButton = findViewById(R.id.btn_back);
    }
}
