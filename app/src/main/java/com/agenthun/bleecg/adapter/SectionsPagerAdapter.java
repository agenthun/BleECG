package com.agenthun.bleecg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agenthun.bleecg.App;
import com.agenthun.bleecg.R;
import com.agenthun.bleecg.fragment.SendSmsFragment;
import com.agenthun.bleecg.fragment.ScanDeviceFragment;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/7 上午5:25.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionsPagerAdapter";

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return ScanDeviceFragment.newInstance("1", "1070");
            case 1:
                return SendSmsFragment.newInstance("2", "1070");
/*            case 2:
                Fragment freightTrackFragment = FreightTrackFragment.newInstance("0", "1070");
                ((FreightTrackFragment) freightTrackFragment).setOnItemClickListener(new FreightTrackFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(String containerNo, String containerId) {
                        Log.d(TAG, "get containerNo: " + containerNo + ", containerId: " + containerId);
                        if (mOnDataChangeListener != null) {
                            mOnDataChangeListener.onContainerDataChange(containerNo, containerId);
                        }
                    }
                });
                return freightTrackFragment;*/
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return App.getContext().getString(R.string.page_title_scan_device);
            case 1:
                return App.getContext().getString(R.string.page_title_send_sms);
/*            case 2:
                return App.getContext().getString(R.string.page_title_freight_track_query);*/
        }
        return null;
    }

    //itemClick interface
    public interface OnDataChangeListener {
        void onContainerDataChange(String containerNo, String containerId);
    }

    private OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener mOnDataChangeListener) {
        this.mOnDataChangeListener = mOnDataChangeListener;
    }
}
