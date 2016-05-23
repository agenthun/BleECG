package com.agenthun.bleecg.utils;

import android.os.Parcel;

import com.agenthun.bleecg.bean.base.Detail;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/7 下午9:41.
 */
public class ContainerNoSuggestion implements SearchSuggestion {

    private Detail detail;
    private String mContainerNo;
    private boolean mIsHistory;

    public ContainerNoSuggestion(Detail detail) {
        this.detail = detail;
        this.mContainerNo = detail.getContainerNo();
    }

    public ContainerNoSuggestion(Parcel source) {
        this.mContainerNo = source.readString();
    }

    public Detail getDetail() {
        return detail;
    }

    @Override
    public String getBody() {
        return detail.getContainerNo();
    }

    @Override
    public Creator getCreator() {
        return CREATOR;
    }

    public boolean getIsHistory() {
        return mIsHistory;
    }

    public void setIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }

    public static final Creator<ContainerNoSuggestion> CREATOR = new Creator<ContainerNoSuggestion>() {
        @Override
        public ContainerNoSuggestion createFromParcel(Parcel source) {
            return new ContainerNoSuggestion(source);
        }

        @Override
        public ContainerNoSuggestion[] newArray(int size) {
            return new ContainerNoSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mContainerNo);
    }
}
