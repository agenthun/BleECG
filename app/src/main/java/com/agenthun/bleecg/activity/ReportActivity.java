package com.agenthun.bleecg.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.agenthun.bleecg.R;
import com.agenthun.bleecg.adapter.HistogramAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onBackPressed()");
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setOnFlingListener(null);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);

        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("0");
        list.add("0");
        list.add("16");
        list.add("12");
        list.add("18");
        list.add("10");
        list.add("11");
        list.add("16");
        list.add("19");
        list.add("8");
        list.add("13");
        list.add("11");
        list.add("16");
        list.add("19");
        list.add("14");
        list.add("17");
        list.add("0");
        list.add("0");
        list.add("0");
        HistogramAdapter histogramAdapter = new HistogramAdapter(list);
        mRecyclerView.setAdapter(histogramAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
