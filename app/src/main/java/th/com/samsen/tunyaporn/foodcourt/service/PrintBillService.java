package th.com.samsen.tunyaporn.foodcourt.service;

import android.app.IntentService;
import android.content.Intent;
import android.device.PrinterManager;

public class PrintBillService extends IntentService {

    private PrinterManager printer;

    public PrintBillService() {
        super("bill");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        printer = new PrinterManager();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        String context = intent.getStringExtra("SPRT");
        String op = intent.getStringExtra("OP");
        String test = intent.getStringExtra("OP");
        if(context== null ) return ;
        printer.prn_open();

        printer.prn_setupPage(384, -1);

        int ret;

        switch (op) {
            case "center_header":
                ret =printer.prn_drawTextEx(context, 85, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 19, 0, 0x0001, 0);
                break;
            case "left_header":
                ret = printer.prn_drawTextEx(context, 5, 0, 370, -1, "/mnt/sdcard/config/font/THSarabun.ttf", 22, 0, 0x0001, 0);
                break;
            case "center_body":
                ret =printer.prn_drawTextEx(context, 125, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 17, 0, 0, 0);
                break;
            case "center_body2":
                ret =printer.prn_drawTextEx(context, 100, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 20, 0, 0, 0);
                break;
            case "center_body3":
                ret =printer.prn_drawTextEx(context, 75, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 20, 0, 0, 0);
                break;
            case "center_body4":
                ret =printer.prn_drawTextEx(context, 50, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 20, 0, 0, 0);
                break;
            case "right_body":
                ret =printer.prn_drawTextEx(context, 300, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 20, 0, 0, 0);
                break;
            default:
                ret =printer.prn_drawTextEx(context, 5, 0,370,-1, "/mnt/sdcard/config/font/THSarabun.ttf", 15, 0, 0, 0);

                break;
        }

        android.util.Log.i("TAG", "ret:" + ret);
        //ret += printer.prn_drawTextEx(context, 5, ret,300,-1, "arial", 25, 0, 0x0001, 1);
        //ret += printer.prn_drawTextEx(context, 5, ret,-1,-1, "arial", 25, 0, 0x0008, 0);
//        ret +=printer.prn_drawTextEx(context, 300, ret,-1,-1, "arial", 25, 1, 0, 0);
        //ret +=printer.prn_drawTextEx(context, 0, ret,-1,-1, "/system/fonts/DroidSans-Bold.ttf", 25, 0, 0, 0);
        //ret +=printer.prn_drawTextEx(context, 0, ret,-1,-1, "/system/fonts/kaishu.ttf", 25, 0, 0x0001, 0);
//        android.util.Log.i("debug", "ret:" + ret);
        //printer.prn_drawTextEx(context, 5, 60,160,-1, "arial", 25, 0, 0x0001 |0x0008, 0);
        //printer.prn_drawTextEx(context, 180, 0,160,-1, "arial", 25, 1, 0x0008, 0);
        //printer.prn_drawTextEx(context, 300, 30,160,-1, "arial", 25, 2, 0x0008, 0);
        //printer.prn_drawTextEx(context, 300, 160,160,-1, "arial", 25, 3, 0x0008, 0);
        //printer.prn_drawTextEx(context, 0, 0,160,-1, "arial", 25, 1, 0x0008, 0);
        //printer.prn_drawTextEx(context, 160, 30,200,-1, "arial", 28, 0, 2,1);
        //printer.prn_drawTextEx(context, 0, 180,-1,-1, "arial", 28, 0, 2,1);
        printer.prn_printPage(0);
        printer.prn_close();
    }

    private void sleep(){
        //延时1秒
        try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}