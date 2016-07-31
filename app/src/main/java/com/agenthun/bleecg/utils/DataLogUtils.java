package com.agenthun.bleecg.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/7/31 17:26.
 */
public class DataLogUtils {
    private static final String TAG = "DataLogUtils";
    private static final String DATA_LOG_FILE_NAME = "ECGDataLog.txt";
    public static final String RAW_TYPE = "RAW_TYPE";
    public static final String RATE_TYPE = "RATE_TYPE";
    static BufferedOutputStream outputStream;

    public static void logToFileInit() {
        File sdCard = Environment.getExternalStorageDirectory();
        File logFile = new File(sdCard, DATA_LOG_FILE_NAME);
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(logFile));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        }
    }

    public static void logToFileFinish() {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "flush failed or close failed");
            e.printStackTrace();
        }
    }

    public static void logToFile(String tag, int data) {
        if (outputStream == null) {
            return;
        }
        StringBuilder sb = new StringBuilder(tag);
        sb.append(" " + data + "\n");
        try {
            outputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "write failed");
            e.printStackTrace();
        }
    }

    public static byte[] FileToBytes() {
        File sdCard = Environment.getExternalStorageDirectory();
        File logFile = new File(sdCard, DATA_LOG_FILE_NAME);
        byte[] buff = new byte[1024];
        BufferedInputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = new BufferedInputStream(new FileInputStream(logFile));
            int count = 0;
            while ((count = inputStream.read(buff)) != -1) {
                byteArrayOutputStream.write(buff, 0, count);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}