package com.agenthun.bleecg.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.bleecg.R;
import com.agenthun.bleecg.view.RoundHistogram;

import java.util.List;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/9/11 21:59.
 */
public class HistogramAdapter extends RecyclerView.Adapter<HistogramAdapter.ViewHolder> {
    private List<String> roundHistograms;

    public HistogramAdapter(List<String> roundHistograms) {
        this.roundHistograms = roundHistograms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_histogram, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String roundHistogram = roundHistograms.get(position);
        holder.histogram.invalidate();
        holder.histogram.setValue(Float.valueOf(roundHistogram));
        holder.histogram.invalidate();
        holder.value.setText(roundHistogram);
    }

    @Override
    public int getItemCount() {
        return roundHistograms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundHistogram histogram;
        private AppCompatTextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            histogram = (RoundHistogram) itemView.findViewById(R.id.histogram);
            value = (AppCompatTextView) itemView.findViewById(R.id.histogram_value);
        }
    }
}
