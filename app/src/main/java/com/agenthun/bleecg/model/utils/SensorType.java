package com.agenthun.bleecg.model.utils;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/11 下午11:48.
 */
public class SensorType {
    private byte temperatureEn;
    private short temperature;
    private byte humidityEn;
    private short humidity;
    private byte shakeEn;
    private short shake;

    public SensorType() {
        this.temperatureEn = 0;
        this.temperature = 0;
        this.humidityEn = 0;
        this.humidity = 0;
        this.shakeEn = 0;
        this.shake = 0;
    }

    public SensorType(byte temperatureEn, short temperature, byte humidityEn, short humidity, byte shakeEn, short shake) {
        this.temperatureEn = temperatureEn;
        this.temperature = temperature;
        this.humidityEn = humidityEn;
        this.humidity = humidity;
        this.shakeEn = shakeEn;
        this.shake = shake;
    }

    public byte getTemperatureEn() {
        return temperatureEn;
    }

    public void setTemperatureEn(byte temperatureEn) {
        this.temperatureEn = temperatureEn;
    }

    public short getTemperature() {
        return temperature;
    }

    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    public byte getHumidityEn() {
        return humidityEn;
    }

    public void setHumidityEn(byte humidityEn) {
        this.humidityEn = humidityEn;
    }

    public short getHumidity() {
        return humidity;
    }

    public void setHumidity(short humidity) {
        this.humidity = humidity;
    }

    public byte getShakeEn() {
        return shakeEn;
    }

    public void setShakeEn(byte shakeEn) {
        this.shakeEn = shakeEn;
    }

    public short getShake() {
        return shake;
    }

    public void setShake(short shake) {
        this.shake = shake;
    }
}
