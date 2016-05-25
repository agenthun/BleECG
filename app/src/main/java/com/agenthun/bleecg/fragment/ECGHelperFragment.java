package com.agenthun.bleecg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.bleecg.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/5/24 16:39.
 */
public class ECGHelperFragment extends Fragment {
    private static final String TAG = "ECGHelperFragment";

    public static ECGHelperFragment newInstance() {
        Bundle args = new Bundle();
        ECGHelperFragment fragment = new ECGHelperFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ecg_helper, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.card_replay)
    public void onReplayBtnClick() {
        Log.d(TAG, "onReplayBtnClick() returned: ");
    }

    @OnClick(R.id.card_report)
    public void onReportBtnClick() {
        Log.d(TAG, "onReportBtnClick() returned: ");
    }

    @OnClick(R.id.card_tips)
    public void onTipsBtnClick() {
        Log.d(TAG, "onTipsBtnClick() returned: ");
    }
}
