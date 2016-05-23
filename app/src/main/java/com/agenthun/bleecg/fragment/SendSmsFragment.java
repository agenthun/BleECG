package com.agenthun.bleecg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.bleecg.R;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/7 上午5:47.
 */
public class SendSmsFragment extends Fragment {

    private static final String TAG = "FreightTrackFragment";
    private static final String ARG_TYPE = "TYPE";
    private static final String ARG_CONTAINER_NO = "CONTAINER_NO";

    private String mType;
    private String mContainerNo;

    public static SendSmsFragment newInstance(String type, String containerNo) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putString(ARG_CONTAINER_NO, containerNo);
        SendSmsFragment fragment = new SendSmsFragment();
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
        View view = inflater.inflate(R.layout.fragment_freight_query, container, false);
        return view;
    }
}
