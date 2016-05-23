package com.agenthun.bleecg.model.protocol;

import com.agenthun.bleecg.model.utils.Crc;
import com.agenthun.bleecg.model.utils.Encrypt;
import com.agenthun.bleecg.model.utils.PositionType;
import com.agenthun.bleecg.model.utils.SensorType;
import com.agenthun.bleecg.model.utils.StateExtraType;
import com.agenthun.bleecg.model.utils.StateType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/11 下午10:18.
 */
public class ESealOperation {
    private static final String TAG = "ESealOperation";

    public static final int ESEALBD_OPERATION_PORT = 0xA002;
    private static final short ESEALBD_OPERATION_CMD_MAX_SIZE = 256;
    public static final short ESEALBD_PROTOCOL_CMD_DATA_OFFSET = 20;
    public static final short ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET = 0;

    public static final short ESEALBD_OPERATION_REQUEST_SIZE_QUERY = (2 + 2 + 2 + 4);
    public static final short ESEALBD_OPERATION_REQUEST_SIZE_CONFIG = (2 + 2 + 2 + 4 + 2 + 1 + 1 + 9);
    public static final short ESEALBD_OPERATION_REQUEST_SIZE_OPERATION = (2 + 2 + 2 + 4 + 1 + 1);
    public static final short ESEALBD_OPERATION_REQUEST_SIZE_WRITE_DATA_WITHOUT_DLEN = (2 + 2 + 2 + 4 + 2);
    public static final short ESEALBD_OPERATION_REQUEST_SIZE_READ_DATA = (2 + 2 + 2 + 4 + 2);
    public static final short ESEALBD_OPERATION_REQUEST_SIZE_CLEAR = (2 + 2 + 2 + 4 + 2);
    public static final short ESEALBD_OPERATION_REQUEST_SIZE_INFO = (2 + 2 + 2 + 4);

    private static final short ESEALBD_OPERATION_TYPE_QUERY = 0x2F00;
    private static final short ESEALBD_OPERATION_TYPE_CONFIG = 0x2F01;
    private static final short ESEALBD_OPERATION_TYPE_OPERATION = 0x2F0C;
    private static final short ESEALBD_OPERATION_TYPE_WRITE_DATA = 0x2FD0;
    private static final short ESEALBD_OPERATION_TYPE_READ_DATA = 0x2FD1;
    private static final short ESEALBD_OPERATION_TYPE_CLEAR = 0x2FD3;
    private static final short ESEALBD_OPERATION_TYPE_INFO = 0x2FD2;

    public static final short ESEALBD_OPERATION_TYPE_REPLAY_ERROR = 0x1F0F;
    public static final short ESEALBD_OPERATION_TYPE_REPLAY_QUERY = 0x1F00;
    public static final short ESEALBD_OPERATION_TYPE_REPLAY_READ_DATA = 0x1FD1;
    public static final short ESEALBD_OPERATION_TYPE_REPLAY_INFO = 0x1FD0;

    public static final byte POWER_OFF = 0;
    public static final byte POWER_ON = 1;
    public static final byte SAFE_LOCK = 0;
    public static final byte SAFE_UNLOCK = 1;

    public static final int PERIOD_DEFAULT = 60;
    public static final short WINDOW_DEFAULT = 30;
    public static final byte CHANNEL_DEFAULT = 1;

    public ESealOperation() {
    }

    //查询状态报文-加密
    public static byte[] operationQuery(int id, int rn, int key) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_QUERY);
        buffer.putShort(ESEALBD_OPERATION_TYPE_QUERY);
        buffer.putShort((short) 4);
        buffer.putInt(id);
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_QUERY - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_QUERY);
        buffer.putShort((short) 4);
        buffer.putInt(id);
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_QUERY);
        return temp;
    }

    /**
     * 配置报文-加密
     * period->上传周期(60-65535 s), (0-59 s)停止上传
     * window->开窗宽度(5-255 s), (0-4 s)一直开窗
     * channel->通道模式 00 自动, 01 GPRS, 02 北斗, 03 GPRS/北斗, 04 D+, 05 GPRS/D+
     * sensorType->温度/湿度/振动传感器-使能及临界值
     */
    public static byte[] operationConfig(int id, int rn, int key, int period, short window, byte channel, SensorType sensorType) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_CONFIG);
        buffer.putShort(ESEALBD_OPERATION_TYPE_CONFIG);
        buffer.putShort((short) 0x11);
        buffer.putInt(id);
        buffer.putShort((short) (period & 0xffff));
        buffer.put((byte) (window & 0xff));
        buffer.put(channel);

        buffer.put(sensorType.getTemperatureEn());
        buffer.putShort(sensorType.getTemperature());
        buffer.put(sensorType.getHumidityEn());
        buffer.putShort(sensorType.getHumidity());
        buffer.put(sensorType.getShakeEn());
        buffer.putShort(sensorType.getShake());
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_CONFIG - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_CONFIG);
        buffer.putShort((short) 0x11);
        buffer.putInt(id);
        buffer.putShort((short) (period & 0xffff));
        buffer.put((byte) (window & 0xff));
        buffer.put(channel);

        buffer.put(sensorType.getTemperatureEn());
        buffer.putShort(sensorType.getTemperature());
        buffer.put(sensorType.getHumidityEn());
        buffer.putShort(sensorType.getHumidity());
        buffer.put(sensorType.getShakeEn());
        buffer.putShort(sensorType.getShake());
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_CONFIG);
        return temp;
    }

    /**
     * 操作报文-加密
     * power->00 关机, 01 开机
     * safe->00 上封, 01 解封
     */
    public static byte[] operationOperation(int id, int rn, int key, byte power, byte safe) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_OPERATION);
        buffer.putShort(ESEALBD_OPERATION_TYPE_OPERATION);
        buffer.putShort((short) 6);
        buffer.putInt(id);
        buffer.put(power);
        buffer.put(safe);
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_OPERATION - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_OPERATION);
        buffer.putShort((short) 6);
        buffer.putInt(id);
        buffer.put(power);
        buffer.put(safe);
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_OPERATION);
        return temp;
    }

    //写数据报文-加密
    public static byte[] operationWriteData(int id, int rn, int key, byte[] writeData, short writeLen) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_WRITE_DATA_WITHOUT_DLEN + writeLen);
        buffer.putShort(ESEALBD_OPERATION_TYPE_WRITE_DATA);
        buffer.putShort((short) (6 + writeLen));
        buffer.putInt(id);
        buffer.putShort(writeLen);
        buffer.put(writeData, 0, writeLen);
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_WRITE_DATA_WITHOUT_DLEN + writeLen - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_WRITE_DATA);
        buffer.putShort((short) (6 + writeLen));
        buffer.putInt(id);
        buffer.putShort(writeLen);
        buffer.put(writeData, 0, writeLen);
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_WRITE_DATA_WITHOUT_DLEN + writeLen);
        return temp;
    }

    //读数据报文-加密
    public static byte[] operationReadData(int id, int rn, int key, short readLen) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_READ_DATA);
        buffer.putShort(ESEALBD_OPERATION_TYPE_READ_DATA);
        buffer.putShort((short) 6);
        buffer.putInt(id);
        buffer.putShort(readLen);
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_READ_DATA - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_READ_DATA);
        buffer.putShort((short) 6);
        buffer.putInt(id);
        buffer.putShort(readLen);
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_READ_DATA);
        return temp;
    }

    //擦除数据报文-加密
    public static byte[] operationClear(int id, int rn, int key) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_CLEAR);
        buffer.putShort(ESEALBD_OPERATION_TYPE_CLEAR);
        buffer.putShort((short) 6);
        buffer.putInt(id);
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_CLEAR - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_CLEAR);
        buffer.putShort((short) 6);
        buffer.putInt(id);
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_CLEAR);
        return temp;
    }

    //请求信息报文-加密
    public static byte[] operationInfo(int id, int rn, int key) {
        ByteBuffer buffer = ByteBuffer.allocate(ESEALBD_OPERATION_REQUEST_SIZE_INFO);
        buffer.putShort(ESEALBD_OPERATION_TYPE_INFO);
        buffer.putShort((short) 4);
        buffer.putInt(id);
        byte[] temp = buffer.array();
        short crc = Crc.getCRC16(temp, ESEALBD_OPERATION_REQUEST_SIZE_INFO - 2);
        buffer.clear();

        buffer.putShort(crc);
        buffer.putShort(ESEALBD_OPERATION_TYPE_INFO);
        buffer.putShort((short) 4);
        buffer.putInt(id);
        temp = buffer.array();

        Encrypt.encrypt(id, rn, key, temp, ESEALBD_OPERATION_REQUEST_SIZE_INFO);
        return temp;
    }

    public static void operationQueryReplay(ByteBuffer buffer, StateType stateType) {
        short period = buffer.getShort(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 10);
        boolean power = ((buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 15) & 0xff) == 0) ? false : true;
        byte safe = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 16);
        boolean locked = ((buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 17) & 0xff) == 0) ? false : true;
        stateType.setPeriod(period);
        stateType.setPower(power);
        stateType.setSafe(safe);
        stateType.setLocked(locked);
    }

    public static void operationInfoReplay(ByteBuffer buffer, PositionType positionType) {

        int year = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 11) + 2000;
        int month = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 12) - 1; //从0计算
        int day = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 13);
        int hour = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 14) + 8; //GPS时间转UTC时间, time_GPS + 8 = time_UTC_china
        int minute = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 15);
        int second = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 16);
        positionType.getCalendar().set(year, month, day, hour, minute, second);

        byte safe = buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 32);
        boolean locked = ((buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 33) & 0xff) == 0) ? false : true;
        positionType.setSafe(safe);
        positionType.setLocked(locked);

        if ((buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 17) & 0xff) == 1) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            StringBuffer sb = new StringBuffer();
            sb.append(buffer.getShort(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 19));
            sb.append('.');
            sb.append(buffer.getInt(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 21));

            float latitude = Float.parseFloat(sb.substring(0, 2))
                    + Float.parseFloat(sb.substring(2)) / 60; //纬度

            sb = new StringBuffer();
            sb.append(buffer.getShort(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 26));
            sb.append('.');
            sb.append(buffer.getInt(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 28));
            float longitude = Float.parseFloat(sb.substring(0, 3))
                    + Float.parseFloat(sb.substring(3)) / 60; //经度

            sb = new StringBuffer();
            sb.append(longitude);
            sb.append(", ");
            sb.append(latitude);
            positionType.setPosition(sb.toString());
/*            if ((buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 25) & 0xff) == 0x45) {
                //E 东经
                Log.d(TAG, "E 东经");
            } else {
                //W 西经
                Log.d(TAG, "W 西经");
            }

            if ((buffer.get(ESEALBD_PROTOCOL_CMD_DATA_OFFSET + 18) & 0xff) == 0x4e) {
                //N 北纬
                Log.d(TAG, "N 北纬");
            } else {
                //S 南纬
                Log.d(TAG, "S 南纬");
            }*/
        }
    }

    //北斗主板与操作面板通信协议
    public static void operationGetStateExtraData(ByteBuffer buffer, StateExtraType stateExtraType) {
        int year = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 0);
        year = (year >> 4) * 10 + (year & 0x0f) + 2000;
        int month = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 1);
        month = (month >> 4) * 10 + (month & 0x0f) - 1; //从0计算
        int day = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 2);
        day = (day >> 4) * 10 + (day & 0x0f);
        int hour = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 3); //GPS时间转UTC时间, time_GPS + 8 = time_UTC_china
        hour = (hour >> 4) * 10 + (hour & 0x0f) + 8;
        int minute = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 4);
        minute = (minute >> 4) * 10 + (minute & 0x0f);
        int second = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 5);
        second = (second >> 4) * 10 + (second & 0x0f);
        stateExtraType.getCalendar().set(year, month, day, hour, minute, second);

        byte temperature = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 6);
        byte humidity = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 7);
        byte state = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 8);
        boolean locked = (state & 0x01) == 1 ? false : true;
        boolean isTemperatureAlarm = (state & 0x02) == 0 ? false : true;
        boolean isHumidityAlarm = (state & 0x04) == 0 ? false : true;
        short shakeX = (short) (buffer.getShort(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 9) >> 0);
        short shakeY = (short) (buffer.getShort(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 11) >> 0);
        short shakeZ = (short) (buffer.getShort(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 13) >> 0);

        byte[] message = new byte[32];
        for (int i = 0; i < 32 && i < (buffer.capacity() - 15); i++) {
            message[i] = buffer.get(ESEALBD_PROTOCOL_CMD_EXTRA_DATA_OFFSET + 15 + i);
        }

        try {
            String smsMessage = new String(message, "unicode");
//            String smsMessage = new String(message, "gbk");
            stateExtraType.setSmsMessage(smsMessage);
        } catch (Exception e) {
            stateExtraType.setSmsMessage("msg error");
        }

        stateExtraType.setTemperature(temperature);
        stateExtraType.setHumidity(humidity);
        stateExtraType.setLocked(locked);
        stateExtraType.setTemperatureAlarm(isTemperatureAlarm);
        stateExtraType.setHumidityAlarm(isHumidityAlarm);
        stateExtraType.setShakeX(shakeX);
        stateExtraType.setShakeY(shakeY);
        stateExtraType.setShakeZ(shakeZ);
    }
}
