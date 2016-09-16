package com.agenthun.bleecg.bean;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/9/16 19:21.
 */
public class HeartRate {
    private float value;
    private String date;
    private int healthCondition;

    public HeartRate() {
    }

    public HeartRate(float value, String date) {
        this.value = value;
        this.date = date;
    }

    public HeartRate(float value, String date, int healthCondition) {
        this.value = value;
        this.date = date;
        this.healthCondition = healthCondition;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(int healthCondition) {
        this.healthCondition = healthCondition;
    }
}
