package com.agenthun.bleecg.model.utils;

import java.util.Calendar;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/12 下午7:52.
 */
public class PositionType {
    private Calendar calendar = Calendar.getInstance();
    private String position; //经度, 纬度
    private byte safe;
    private boolean locked;

    public PositionType() {
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public byte getSafe() {
        return safe;
    }

    public void setSafe(byte safe) {
        this.safe = safe;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
