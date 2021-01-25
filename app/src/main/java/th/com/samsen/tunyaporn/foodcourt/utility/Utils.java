package th.com.samsen.tunyaporn.foodcourt.utility;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.lang.Math.floor;


public class Utils {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    public static String hexToString(String s) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < s.length(); i += 2) {
            String str = s.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return String.valueOf(output);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        Log.d("TAG", "hideKeyboard: I'm Here");
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void readAllData(MifareClassic mifareClassic) throws IOException {
        byte[] dataBytes;
        mifareClassic.connect();
        boolean auth = false;
        String cardData = null;
        int secCount = mifareClassic.getSectorCount();
        int bCount = 0;
        int bIndex = 0;
        for (int j = 0; j < secCount; j++) {
            auth = mifareClassic.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
            if (auth) {
                bCount = mifareClassic.getBlockCountInSector(j);
                bIndex = mifareClassic.sectorToBlock(j);


                for (int i = 0; i < bCount; i++) {
                    dataBytes = mifareClassic.readBlock(bIndex);
                    Log.i("TAG", bytesToHex(dataBytes));
                    bIndex++;
                }

            } else { // Authentication failed - Handle it

            }
        }
    }

    public static String readDataViaSecBlock(MifareClassic mifareClassic, int sector, int block) throws IOException {
        byte[] dataBytes;
        mifareClassic.connect();
        boolean auth = false;
        String cardData = null;
        int bCount = 0;
        int bIndex = 0;
        auth = mifareClassic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        if (auth) {
            bCount = mifareClassic.getBlockCountInSector(sector);
            bIndex = mifareClassic.sectorToBlock(sector);
            bIndex += block;

            dataBytes = mifareClassic.readBlock(bIndex);
            Log.i("TAG", bytesToHex(dataBytes));
            return bytesToHex(dataBytes);
        } else { // Authentication failed - Handle it
            return "";
        }
//        }

    }

    public static String resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mifareClassic = MifareClassic.get(tag);

            try {

                return readDataViaSecBlock(mifareClassic, 1, 0);

            } catch (IOException e) {
                e.printStackTrace();

                Log.e("TAG", "Error --> " + e + " Line: " + e.getStackTrace()[0].getLineNumber());
                return "";
            }
        } else {
            return "";
        }
    }

    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }


    public static String getDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        Log.d("TAG", "getDate: " + currentDateandTime);

        return currentDateandTime;
    }

    public static String GetRunningFormat6Unit(String running) {
        int no_running = Integer.parseInt(running);
        if (floor(no_running / 100000) > 0) {
            return String.valueOf(no_running);
        } else if (floor(no_running / 10000) > 0) {
            return "0" + String.valueOf(no_running);
        } else if (floor(no_running / 1000) > 0) {
            return "00" + String.valueOf(no_running);
        } else if (floor(no_running / 100) > 0) {
            return "000" + String.valueOf(no_running);
        } else if (floor(no_running / 10) > 0) {
            return "0000" + String.valueOf(no_running);
        } else {
            return "00000" + String.valueOf(no_running);
        }
    }

    public static String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }

    public static String getRequestdt() {


//        Calendar calendar = new BuddhistCalendar();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        java.text.SimpleDateFormat zone = new java.text.SimpleDateFormat("Z");
        String zoneTime = zone.format(new Date());

        String temp1 = zoneTime.substring(0, 3);
        String temp2 = zoneTime.substring(3,5);

        currentDateandTime =currentDateandTime + temp1 + ":" + temp2;
//        int offset = calendar.get(Calendar.ZONE_OFFSET) / (1000 * 60 * 60);
//        if (offset > 0) {
//            currentDateandTime += "+";
//        } else {
//            currentDateandTime += "-";
//        }
//
//        currentDateandTime += twoDigit(offset);
//        currentDateandTime += ":00";

        return currentDateandTime;
    }

    public static String twoDigit(int i) {
        if (i >= 0 && i < 10) {
            return "0" + String.valueOf(i);
        }
        return String.valueOf(i);
    }

    public static String getYearOfDate() {

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime.substring(0, 4);
    }


    public static String getMonthOfDate() {

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime.substring(5, 7);
    }


    public static String getDateOfDate() {

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime.substring(8, 10);
    }

    public static String getCurrentDateFromFormat(String s) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(s);
        return sdf.format(new Date());

    }


    public static String getDateFormat(String st) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateTime = "";
        try {
            Date date = new Date();
            dateTime = sdf.format(date);
            System.out.println("Current Date Time : " + dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;

    }

    public static String getDateTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        Log.d("TAG", "getDate: " + currentDateandTime);

        return currentDateandTime;

    }


    public static String getTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        Log.d("TAG", "getDate: " + currentDateandTime);

        return currentDateandTime;

    }


    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

}
