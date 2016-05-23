package com.agenthun.bleecg.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.bleecg.R;
import com.agenthun.bleecg.activity.DeviceOperationActivity;
import com.agenthun.bleecg.adapter.DeviceAdapter;
import com.agenthun.bleecg.connectivity.ble.ACSUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.gmariotti.recyclerview.itemanimator.SlideScaleInOutRightItemAnimator;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/7 上午5:47.
 */
public class ScanDeviceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ScanDeviceFragment";
    private static final String ARG_TYPE = "TYPE";
    private static final String ARG_CONTAINER_NO = "CONTAINER_NO";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;

    private String mType;
    private String mContainerNo;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DeviceAdapter deviceAdapter;

    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> deviceList;
    Map<String, Integer> deviceRssiValues = new HashMap<>();

    private ACSUtility.blePort mSelectedPort;
    private ACSUtility utility;
    private boolean utilEnable = false;
    private boolean utilIsScan = false;

    public static ScanDeviceFragment newInstance(String type, String containerNo) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putString(ARG_CONTAINER_NO, containerNo);
        ScanDeviceFragment fragment = new ScanDeviceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mContainerNo = getArguments().getString(ARG_CONTAINER_NO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_device, container, false);

        utility = new ACSUtility(getContext(), callback);
        utilEnable = false;

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark,
                R.color.colorAccent, R.color.colorAccentDark);

        deviceList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        deviceAdapter = new DeviceAdapter(deviceList, deviceRssiValues);
        deviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                swipeRefreshLayout.setRefreshing(false);
                if (utilIsScan) {
                    utilIsScan = false;
                    utility.stopEnum();
                }

                BluetoothDevice device = deviceAdapter.getItem(position);
                Log.d(TAG, "onItemClick() returned: " + device.getName());
                mSelectedPort = utility.new blePort(device);
                if (mSelectedPort != null) {
                    utilEnable = false;
                    utility.closeACSUtility();

                    Bundle b = new Bundle();
                    b.putParcelable(BluetoothDevice.EXTRA_DEVICE, mSelectedPort._device);
                    Intent intent = new Intent(getContext(), DeviceOperationActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(deviceAdapter);
        recyclerView.setItemAnimator(new SlideScaleInOutRightItemAnimator(recyclerView));

        return view;
    }

    @Override
    public void onDestroyView() {
        if (utilEnable) {
            utilEnable = false;
            utility.stopEnum();
            utility.closeACSUtility();
        }
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanDevice();
        } else {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            BluetoothManager bluetoothManager = (BluetoothManager) getContext()
                    .getSystemService(getContext().BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(swipeRefreshLayout, getString(R.string.error_ble_not_supported), Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBluetoothAdapter.isEnabled()) {
                        scanDevice();
                    }
                }
            }, 3000);
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            utilIsScan = false;
            swipeRefreshLayout.setRefreshing(false);
            utility.stopEnum();
        }
    }

    private void scanDevice() {
        deviceAdapter.clear();
        deviceAdapter.notifyDataSetChanged();
        utilIsScan = true;
        utility.enumAllPorts(10);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                utilIsScan = false;
                swipeRefreshLayout.setRefreshing(false);
                utility.stopEnum();
            }
        }, SCAN_PERIOD);
    }

    private void addDevice(BluetoothDevice device, int rssi) {
        boolean isFound = false;
        for (BluetoothDevice deviceFound : deviceList) {
            if (deviceFound.getAddress().equals(device.getAddress())) {
                isFound = true;
                break;
            }
        }
        deviceRssiValues.put(device.getAddress(), rssi);
        if (!isFound) {
            deviceList.add(device);
        }
        deviceAdapter.notifyItemInserted(deviceList.size());
    }

    private ACSUtility.IACSUtilityCallback callback = new ACSUtility.IACSUtilityCallback() {
        @Override
        public void utilReadyForUse() {
            utilEnable = true;
        }

        @Override
        public void didFoundPort(final ACSUtility.blePort newPort, final int rssi) {
            final ACSUtility.blePort mNewtPort = newPort;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "didFoundPort() returned: " + mNewtPort._device.getName() + ", " + mNewtPort._device + ", rssi=" + rssi);
                            addDevice(mNewtPort._device, rssi);
                        }
                    });
                }
            });
        }

        @Override
        public void didFinishedEnumPorts() {
            if (deviceList.size() == 0) {
                if (utilEnable) {
                    utilEnable = false;
//                    utility.setUserCallback(null);
                    utility.stopEnum();
                    utility.closeACSUtility();
                }
                return;
            }
        }

        @Override
        public void didOpenPort(final ACSUtility.blePort port, Boolean bSuccess) {
            Log.d(TAG, "didOpenPort() returned: " + bSuccess);
        }

        @Override
        public void didClosePort(ACSUtility.blePort port) {
            Log.d(TAG, "didClosePort() returned: " + port._device.getAddress());
        }

        @Override
        public void didPackageSended(boolean succeed) {
            Log.d(TAG, "didPackageSended() returned: " + succeed);
        }

        @Override
        public void didPackageReceived(ACSUtility.blePort port, byte[] packageToSend) {
        }

        @Override
        public void heartbeatDebug() {

        }
    };
}