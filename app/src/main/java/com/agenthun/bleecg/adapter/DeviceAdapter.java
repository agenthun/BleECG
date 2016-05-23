package com.agenthun.bleecg.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agenthun.bleecg.App;
import com.agenthun.bleecg.R;

import java.util.List;
import java.util.Map;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/9 上午1:27.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private static final String TAG = "DeviceAdapter";
    private List<BluetoothDevice> devices;
    private Map<String, Integer> rssiValues;

    public DeviceAdapter(List<BluetoothDevice> devices, Map<String, Integer> rssiValues) {
        this.devices = devices;
        this.rssiValues = rssiValues;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_device, null);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, final int position) {
        int rssi = rssiValues.get(devices.get(position).getAddress());
        holder.deviceSignal.setImageLevel(Math.abs(rssi));
        holder.deviceSignal.setColorFilter(
                App.getContext().getResources().getColor(R.color.colorAccent));
        holder.deviceName.setText(devices.get(position).getName());
        holder.deviceAddress.setText(devices.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public BluetoothDevice getItem(int position) {
        return devices.get(position);
    }

    public void clear() {
        devices.clear();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView deviceSignal;
        private AppCompatTextView deviceName;
        private AppCompatTextView deviceAddress;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            deviceSignal = (ImageView) itemView.findViewById(R.id.device_signal);
            deviceName = (AppCompatTextView) itemView.findViewById(R.id.device_name);
            deviceAddress = (AppCompatTextView) itemView.findViewById(R.id.device_address);
        }
    }

    //itemClick interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
