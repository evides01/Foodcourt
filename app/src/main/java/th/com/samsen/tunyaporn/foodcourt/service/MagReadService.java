package th.com.samsen.tunyaporn.foodcourt.service;

import android.content.Context;
import android.device.MagManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.FileDescriptor;
import java.io.FileInputStream;

public class MagReadService {
    
    public final static int MESSAGE_READ_MAG = 0;
    public final static String CARD_TRACK1 = "track1";
    public final static String CARD_NUMBER = "number";
    public final static String CARD_TRACK2 = "track2";
    public final static String CARD_TRACK3 = "track3";
    public final static String CARD_VALIDTIME = "validtime";
    
    private Context mContext;
    private Handler mHandler;
    private FileInputStream magFis;
    private FileDescriptor magFd;
    private MagManager magManager;
    private MagReaderThread magReaderThread;
    private static final int DEFAULT_TAG =1;
    private byte[] magBuffer = new byte[1024];
    
    public MagReadService(Context context, Handler handler) {
        mHandler = handler;
        mContext = context;
        magManager = new MagManager();
        magFd = magManager.open(DEFAULT_TAG);
        if (magFd == null) {
            //Toast.makeText(mContext, "打开读卡器内容失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        magFis = new FileInputStream(magFd);
    }
    
    // 从字节数组到十六进制字符串转换
    public static String Bytes2HexString(byte[] b) {
        String ret = "";

        String hex = "";
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            // ret.append(hex.toUpperCase());
            ret += hex.toUpperCase();
        }

        return ret;
    }
    
    public synchronized void start() {
        
        if(magReaderThread != null) {
            magReaderThread.stopMagReader();
            magReaderThread = null;
        }
        magReaderThread = new MagReaderThread("reader--" + DEFAULT_TAG);
        magReaderThread.start();
    }
    
    public synchronized void stop() {
        if(magReaderThread != null) {
            magReaderThread.stopMagReader();
            magReaderThread = null;
        }
        magManager = null;
    }
    
    private class MagReaderThread extends Thread {
        private boolean running = true;
        private boolean isValid;

        public MagReaderThread(String name) {
            super(name);
            running = true;
        }
        
        public void stopMagReader() {
            running = false;
        }

        public void run() {

            while (running) {
                int size = 0;
                if (magFis == null)
                    return;
                try {
                    size = magFis.read(magBuffer);
                } catch (java.io.IOException e) {
                }
                isValid = false;
                StringBuffer trackOne = new StringBuffer();
                byte[] tmp = new byte[1];
                for(int i = 1;i<511;i++) {
                    if (magBuffer[i] != 00) {
                        tmp[0] = magBuffer[i];
                        trackOne.append(new String(tmp));
                    }
                    
                }
                tmp = new byte[1];
                if (magBuffer[512] == (byte) 40 && magBuffer[662] == (byte) 40) {
                    int a40 = 0;
                    int a41 = 0;
                    String cardNum = "";
                    StringBuffer trackTwo = new StringBuffer();
                    StringBuffer trackThree = new StringBuffer();
                    
                    for (int i = 512; i < size; i++) {
                        if (magBuffer[i] == (byte) 41) {
                            a41++;
                            i++;
                        }
                        if (magBuffer[i] == (byte) 40) {
                            a40++;
                            i++;
                        }
                        isValid = true;

                        if (magBuffer[i] != 00) {
                            
                            if (a40 == 1 && a41 == 0) {
                                
                                if (magBuffer[i] == '=')
                                    cardNum = trackTwo.toString();
                                tmp[0] = magBuffer[i];
                                trackTwo.append(new String(tmp));

                            }
                            if (a40 == 2 && a41 == 1) {
                                tmp[0] = magBuffer[i];
                                trackThree.append(new String(tmp));
                            }
                        }
                    }
                    if (isValid == true && a40 > 1 && a41 >= 1) {

                        if (a41 == 1) {
                            int index = trackTwo.indexOf("=");
                            String validTime = (index != -1 ? trackTwo.substring(index + 1, index + 5) : "");
                            Message msg = mHandler.obtainMessage(MESSAGE_READ_MAG);
                            Bundle bundle = new Bundle();
                            bundle.putString(CARD_TRACK1, trackOne.toString());
                            bundle.putString(CARD_TRACK2, trackTwo.toString());
                            bundle.putString(CARD_TRACK3, trackThree.toString());
                            bundle.putString(CARD_NUMBER, cardNum);
                            bundle.putString(CARD_VALIDTIME, validTime);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                        if (a41 == 2) {
                            int index = trackTwo.indexOf("=");
                            String validTime = (index != -1 ? trackTwo.substring(index + 1, index + 5) : "");
                            Message msg = mHandler.obtainMessage(MESSAGE_READ_MAG);
                            Bundle bundle = new Bundle();
                            bundle.putString(CARD_TRACK1, trackOne.toString());
                            bundle.putString(CARD_TRACK2, trackTwo.toString());
                            bundle.putString(CARD_TRACK3, trackThree.toString());
                            bundle.putString(CARD_NUMBER, cardNum);
                            bundle.putString(CARD_VALIDTIME, validTime);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                        size = 0;
                    }
                    trackTwo = null;
                }  
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
            }
        }
    }
}
