package com.agenthun.bleecg.activity;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.agenthun.bleecg.R;
import com.agenthun.bleecg.connectivity.ble.ACSUtility;
import com.agenthun.bleecg.model.utils.SocketPackage;
import com.txusballesteros.SnakeView;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/9 下午7:22.
 */
public class DeviceOperationActivity extends AppCompatActivity {
    private static final String TAG = "DeviceOperationActivity";

    private static final int DEVICE_SETTING = 1;
    private static final long TIME_OUT = 30000;

    private ACSUtility.blePort mCurrentPort;
    private ACSUtility utility;
    private boolean utilEnable = false;
    private boolean isPortOpen = false;

    private AppCompatDialog mProgressDialog;

    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @Bind(R.id.current_heart_rate)
    AppCompatTextView textCurrentHeartRate;

    @Bind(R.id.snake)
    SnakeView snakeView;

    private boolean isShow = false;

    private int id = 0x12345678;
    private int rn = 0xABABABAB;
    private int key = 0x87654321;

    private static final byte extraPackageBegin = 0x02;
    private static final byte extraPackageDataLength = 0x31;
    private static final byte extraPackageCommand = 0x40;
    private static final byte extraPackageTotalLength = 36 + 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_operation);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        BluetoothDevice device = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);
        Log.d(TAG, "onCreate() returned: " + device.getAddress());

        utility = new ACSUtility(this, callback);
        mCurrentPort = utility.new blePort(device);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(device.getAddress());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getProgressDialog().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (utilEnable) {
            utilEnable = false;
            utility.closePort();
            isPortOpen = false;
            utility.closeACSUtility();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                inputStream = null;
            }
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    InputStream inputStream = null;

    @Override
    protected void onStart() {
        super.onStart();

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputStream = getResources().openRawResource(R.raw.bmd_101_7min);
                new MsgThread().start();
            }
        }, 10000);*/

        gen();
    }

    private void gen() {
        int data;
        if (!rawWaveQueue.isEmpty()) {
            data = rawWaveQueue.poll();
            updateWaveView(data);
        }

        if (!heartRateQueue.isEmpty()) {
            int heartRate = (heartRateQueue.poll() & 0xff);
            textCurrentHeartRate.setText(Integer.toString(heartRate));
            if (heartRate >= 100 && isShow == false) {
                isShow = true;
                new AlertDialog.Builder(DeviceOperationActivity.this)
                        .setTitle("心率提示")
                        .setMessage(R.string.warning_heart_rate_high)
                        .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                onBackPressed();
                                isShow = false;
                            }
                        }).show();
            }
            if (heartRate <= 50 && isShow == false) {
                isShow = true;
                new AlertDialog.Builder(DeviceOperationActivity.this)
                        .setTitle("心率提示")
                        .setMessage(R.string.warning_heart_rate_low)
                        .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                onBackPressed();
                                isShow = false;
                            }
                        }).show();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gen();
            }
        }, 1);
    }

    private ACSUtility.IACSUtilityCallback callback = new ACSUtility.IACSUtilityCallback() {
        @Override
        public void utilReadyForUse() {
            Log.d(TAG, "utilReadyForUse() returned:");
            utilEnable = true;
            utility.openPort(mCurrentPort);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isPortOpen) {
                        getProgressDialog().cancel();
                        new AlertDialog.Builder(DeviceOperationActivity.this)
                                .setTitle(mCurrentPort._device.getName())
                                .setMessage(R.string.time_out_device_connection)
                                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                }).show();
                    }
                }
            }, TIME_OUT);
        }

        @Override
        public void didFoundPort(final ACSUtility.blePort newPort, final int rssi) {

        }

        @Override
        public void didFinishedEnumPorts() {
        }

        @Override
        public void didOpenPort(final ACSUtility.blePort port, Boolean bSuccess) {
            Log.d(TAG, "didOpenPort() returned: " + bSuccess);
            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceOperationActivity.this);
            isPortOpen = bSuccess;
            if (bSuccess) {
                getProgressDialog().cancel();
                builder.setTitle(port._device.getName())
                        .setMessage(R.string.success_device_connection)
                        .setPositiveButton(R.string.text_ok, null).show();
            } else {
                getProgressDialog().cancel();
                builder.setTitle(port._device.getName())
                        .setMessage(R.string.fail_device_connection)
                        .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        }).show();
            }
        }

        @Override
        public void didClosePort(ACSUtility.blePort port) {
            Log.d(TAG, "didClosePort() returned: " + port._device.getAddress());
        }

        @Override
        public void didPackageSended(boolean succeed) {
            Log.d(TAG, "didPackageSended() returned: " + succeed);
            if (succeed) {
/*                Snackbar.make(nestedScrollView, getString(R.string.success_device_send_data), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();*/
            } else {
                Snackbar.make(nestedScrollView, getString(R.string.fail_device_send_data), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }

        @Override
        public void didPackageReceived(ACSUtility.blePort port, byte[] packageToSend) {
/*            StringBuffer sb = new StringBuffer();
            for (byte b : packageToSend) {
                if ((b & 0xff) <= 0x0f) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(b & 0xff) + " ");
            }
            Log.d(TAG, "ecg data: " + sb.toString());*/

            socketPackageReceived.packageExtraReceive(socketPackageReceived, packageToSend, rawWaveQueue, heartRateQueue);
        }

        @Override
        public void heartbeatDebug() {

        }
    };

    private SocketPackage socketPackageReceived = new SocketPackage();
    private Queue<Short> rawWaveQueue = new LinkedList<>();
    private Queue<Byte> heartRateQueue = new LinkedList<>();

    private AppCompatDialog getProgressDialog() {
        if (mProgressDialog != null) {
            return mProgressDialog;
        }
        mProgressDialog = new AppCompatDialog(DeviceOperationActivity.this, AppCompatDelegate.MODE_NIGHT_AUTO);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setContentView(R.layout.dialog_device_connecting);
        mProgressDialog.setTitle(getString(R.string.device_connecting));
        return mProgressDialog;
    }

    private void sendData(byte[] data) {
        utility.writePort(data);
    }

    public void updateWaveView(int data) {
        Log.d(TAG, "updateWaveView: " + data);

        float point = (float) (data * 2048.0 / 32768.0);
        if (point > 512) point = 512;
        if (point < -512) point = -512;
        snakeView.addValue(point);
        Log.d(TAG, "float point: " + point);
    }

    class MsgThread extends Thread {
        @Override
        public void run() {
            final byte[] byteData = new byte[20];
            try {
                int len;
                while ((len = inputStream.read(byteData)) != -1) {
                    socketPackageReceived.packageExtraReceive(socketPackageReceived, byteData, rawWaveQueue, heartRateQueue);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
