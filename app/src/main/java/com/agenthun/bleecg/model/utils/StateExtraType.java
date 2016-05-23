package com.agenthun.bleecg.model.utils;

import java.util.Calendar;

/**
 * @project BleMonitor
 * @authors agenthun
 * @date 16/4/14 下午6:44.
 */
public class StateExtraType {
    private Calendar calendar = Calendar.getInstance();
    private byte temperature;
    private byte humidity;
    private boolean locked;
    private boolean isTemperatureAlarm;
    private boolean isHumidityAlarm;
    private short shakeX;
    private short shakeY;
    private short shakeZ;

    private String smsMessage;

    public StateExtraType() {
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public byte getTemperature() {
        return temperature;
    }

    public void setTemperature(byte temperature) {
        this.temperature = temperature;
    }

    public byte getHumidity() {
        return humidity;
    }

    public void setHumidity(byte humidity) {
        this.humidity = humidity;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isTemperatureAlarm() {
        return isTemperatureAlarm;
    }

    public void setTemperatureAlarm(boolean temperatureAlarm) {
        isTemperatureAlarm = temperatureAlarm;
    }

    public boolean isHumidityAlarm() {
        return isHumidityAlarm;
    }

    public void setHumidityAlarm(boolean humidityAlarm) {
        isHumidityAlarm = humidityAlarm;
    }

    public short getShakeX() {
        return shakeX;
    }

    public void setShakeX(short shakeX) {
        this.shakeX = shakeX;
    }

    public short getShakeY() {
        return shakeY;
    }

    public void setShakeY(short shakeY) {
        this.shakeY = shakeY;
    }

    public short getShakeZ() {
        return shakeZ;
    }

    public void setShakeZ(short shakeZ) {
        this.shakeZ = shakeZ;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }
}
