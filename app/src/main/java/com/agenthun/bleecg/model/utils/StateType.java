package com.agenthun.bleecg.model.utils;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/12 下午7:53.
 */
public class StateType {
    private short period;
    private boolean power;
    private byte safe;
    private boolean locked;

    public StateType() {
    }

    public short getPeriod() {
        return period;
    }

    public void setPeriod(short period) {
        this.period = period;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
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
