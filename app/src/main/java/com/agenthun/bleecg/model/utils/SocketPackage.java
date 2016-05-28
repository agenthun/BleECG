package com.agenthun.bleecg.model.utils;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Queue;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/11 下午6:13.
 */
public class SocketPackage {
    private static final String TAG = "SocketPackage";

    private static final short SOCKET_HEAD_SIZE = 10;
    private static final int SOCKET_LEAD = 0xFFFFFFFF;
    private static final byte SOCKET_LEAD_BYTE = (byte) 0xFF;
    private static final byte SOCKET_LEAD_EXTRA_BYTE = (byte) 0xAA;
    private static final int SOCKET_LEAD_EXTRA_COUNT = 2;
    private static final short SOCKET_HEAD_EXTRA_NO_DATA_SIZE = 4;
    private static final int SOCKET_EXTRA_COUNT = SOCKET_HEAD_EXTRA_NO_DATA_SIZE + 4;

    int flag;
    int count;
    boolean ok;
    byte[] data;
    int byteDataLen;

    public SocketPackage() {
        this.flag = 0;
        this.count = 0;
        this.ok = false;
        this.byteDataLen = 0;
    }

    public SocketPackage(int flag, int count, byte[] data) {
        this.flag = flag;
        this.count = count;
        this.data = data;
    }

    public byte[] packageAddHeader(int port, int len, byte[] pdata) {
        ByteBuffer buffer = ByteBuffer.allocate(SOCKET_HEAD_SIZE + len);
        buffer.putInt(SOCKET_LEAD);
        short crc = Crc.getCRC16((short) (port & 0xffff), (short) (len & 0xffff));
        buffer.putShort(crc);
        buffer.putShort((short) port);
        buffer.putShort((short) len);
        buffer.put(pdata);
        return buffer.array();
    }

    public int packageReceive(SocketPackage socketPackage, byte[] pdata) {
        int res = 0;
        for (int i = 0; i < pdata.length; i++) {
            if ((pdata[i] == SOCKET_LEAD_BYTE) && (socketPackage.getFlag() == 0)) {
                socketPackage.setFlag(1);
                socketPackage.setCount(0);
            } else {
                if ((socketPackage.getFlag() != 0) && (socketPackage.getFlag() != 1)) {
                    socketPackage.setFlag(0);
                }
            }

            if ((socketPackage.getFlag() == 1) && (socketPackage.getCount() == 0)) {
                int len = (int) SOCKET_HEAD_SIZE + (((int) pdata[i + 8]) << 4) + (int) pdata[i + 9];
                socketPackage.data = new byte[len];
            }
            if (socketPackage.getFlag() == 1) {
                socketPackage.setData(socketPackage.getCount(), pdata[i]);
                socketPackage.setCount(socketPackage.getCount() + 1);
            }
        }
        if (socketPackage.getCount() == socketPackage.getData().length) {
            res = 1;
        } else {
            res = 0;
        }
        return res;
    }

    boolean isSigOk = false;

    //AA AA 04 80 02 37 21 25
    //AA AA 12 02 C8 03 BA 84 05 00 F9 00 03 44 08 34 85 03 FF FF E5 08
    public int packageExtraReceive(SocketPackage socketPackage, byte[] pdata, Queue<Short> rawWave) {
        int res = 0;
        for (int i = 0; i < pdata.length; i++) {
            if ((pdata[i] == SOCKET_LEAD_EXTRA_BYTE) && (socketPackage.getFlag() == 0)) {
                socketPackage.setFlag(1);
                socketPackage.setByteDataLen(0);
                socketPackage.setCount(0);
            } else {
                if ((socketPackage.getFlag() != 0) && (socketPackage.getFlag() != 1)) {
                    socketPackage.setFlag(0);
                }
            }

            if ((socketPackage.getFlag() == 1)) {
                if (socketPackage.getCount() == SOCKET_LEAD_EXTRA_COUNT - 1) {
                    if (pdata[i] != SOCKET_LEAD_EXTRA_BYTE) {
                        socketPackage.setFlag(0);
                        continue;
                    }
                } else if (socketPackage.getCount() == SOCKET_LEAD_EXTRA_COUNT) {
                    if ((pdata[i] & 0xff) > 169) {
                        socketPackage.setFlag(0);
                        continue;
                    } else {
                        int len = (pdata[i] & 0xff);
                        socketPackage.setByteDataLen(len);
                        socketPackage.data = new byte[len];
                    }
                } else if ((socketPackage.getCount() > SOCKET_LEAD_EXTRA_COUNT) &&
                        (socketPackage.getCount() < socketPackage.getByteDataLen() + SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1)) {
                    socketPackage.setData(socketPackage.getCount() - (SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1), pdata[i]);
                } else if (socketPackage.getCount() == socketPackage.getByteDataLen() + SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1) {
                    if ((pdata[i] & 0xff) != Crc.simpleSumReverseCRC(socketPackage.getData())) {
                        socketPackage.setFlag(0);
                        continue;
                    } else {
//                        Log.d(TAG, "is OK");
                        byte[] getData = socketPackage.getData();
                        if (getData.length != 4) {

                            StringBuffer sb = new StringBuffer();
                            for (byte b : getData) {
                                if ((b & 0xff) <= 0x0f) {
                                    sb.append("0");
                                }
                                sb.append(Integer.toHexString(b & 0xff) + " ");
                            }
                            Log.d(TAG, "capture ok: " + sb.toString());

                            if (getData.length == 0x12) {
                                if (getData[0] == (byte) 0x02 && getData[1] == (byte) 0xc8) {
                                    isSigOk = true;
                                } else {
                                    isSigOk = false;
                                }
                            }
                        } else {
                            if (isSigOk) {
                                if ((getData[0] == (byte) 0x80) && (getData[1] == (byte) 0x02)) {
                                    int hi = ((int) getData[2]) & 0xff;
                                    int lo = ((int) getData[3]) & 0xff;
                                    rawWave.offer((short) ((hi << 8) | lo));
                                    Log.d(TAG, "packageExtraReceive() returned: " + (short) ((hi << 8) | lo));
                                }
                            } else {
//                                rawWave.offer((short) 0);
                            }
                        }

                    }
                }

                if (socketPackage.getCount() >= socketPackage.getByteDataLen() + SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1) {
                    socketPackage.setFlag(0);
                } else {
                    socketPackage.setCount(socketPackage.getCount() + 1);
                }
            }
        }
        return res;
    }

    public int packageExtraReceive(SocketPackage socketPackage, byte[] pdata, Queue<Short> rawWave, Queue<Byte> heartRate) {
        int res = 0;
        for (int i = 0; i < pdata.length; i++) {
            if ((pdata[i] == SOCKET_LEAD_EXTRA_BYTE) && (socketPackage.getFlag() == 0)) {
                socketPackage.setFlag(1);
                socketPackage.setByteDataLen(0);
                socketPackage.setCount(0);
            } else {
                if ((socketPackage.getFlag() != 0) && (socketPackage.getFlag() != 1)) {
                    socketPackage.setFlag(0);
                }
            }

            if ((socketPackage.getFlag() == 1)) {
                if (socketPackage.getCount() == SOCKET_LEAD_EXTRA_COUNT - 1) {
                    if (pdata[i] != SOCKET_LEAD_EXTRA_BYTE) {
                        socketPackage.setFlag(0);
                        continue;
                    }
                } else if (socketPackage.getCount() == SOCKET_LEAD_EXTRA_COUNT) {
                    if ((pdata[i] & 0xff) > 169) {
                        socketPackage.setFlag(0);
                        continue;
                    } else {
                        int len = (pdata[i] & 0xff);
                        socketPackage.setByteDataLen(len);
                        socketPackage.data = new byte[len];
                    }
                } else if ((socketPackage.getCount() > SOCKET_LEAD_EXTRA_COUNT) &&
                        (socketPackage.getCount() < socketPackage.getByteDataLen() + SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1)) {
                    socketPackage.setData(socketPackage.getCount() - (SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1), pdata[i]);
                } else if (socketPackage.getCount() == socketPackage.getByteDataLen() + SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1) {
                    if ((pdata[i] & 0xff) != Crc.simpleSumReverseCRC(socketPackage.getData())) {
                        socketPackage.setFlag(0);
                        continue;
                    } else {
//                        Log.d(TAG, "is OK");
                        byte[] getData = socketPackage.getData();
                        if (getData.length != 4) {

/*                            StringBuffer sb = new StringBuffer();
                            for (byte b : getData) {
                                if ((b & 0xff) <= 0x0f) {
                                    sb.append("0");
                                }
                                sb.append(Integer.toHexString(b & 0xff) + " ");
                            }
                            Log.d(TAG, "capture ok: " + sb.toString());*/

                            if (getData.length == 0x12) {
                                if (getData[0] == (byte) 0x02 && getData[1] == (byte) 0xc8) {
                                    isSigOk = true;
                                } else {
                                    isSigOk = false;
                                }
//                                Log.d(TAG, "isSigOk: " + isSigOk);

                                if (getData[2] == (byte) 0x03) {
                                    heartRate.offer(getData[3]);
//                                    Log.d(TAG, "heartRate: " + getData[3]);
                                }
                            }
                        } else {
                            if (isSigOk) {
                                if ((getData[0] == (byte) 0x80) && (getData[1] == (byte) 0x02)) {
                                    int hi = ((int) getData[2]) & 0xff;
                                    int lo = ((int) getData[3]) & 0xff;
                                    rawWave.offer((short) ((hi << 8) | lo));
//                                    Log.d(TAG, "rawWaveValue: " + (short) ((hi << 8) | lo));
                                }
                            } else {
//                                rawWave.offer((short) 0);
                            }
                        }

                    }
                }

                if (socketPackage.getCount() >= socketPackage.getByteDataLen() + SOCKET_HEAD_EXTRA_NO_DATA_SIZE - 1) {
                    socketPackage.setFlag(0);
                } else {
                    socketPackage.setCount(socketPackage.getCount() + 1);
                }
            }
        }
        return res;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setData(int position, byte pdata) {
        this.data[position] = pdata;
    }

    public int getByteDataLen() {
        return byteDataLen;
    }

    public void setByteDataLen(int byteDataLen) {
        this.byteDataLen = byteDataLen;
    }
}
