package com.agenthun.bleecg.model.utils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/11 下午9:08.
 */
public class Encrypt {
    private static final String ALGORITHM_SHA1 = "SHA1";

    public Encrypt() {
    }

    public static void encrypt(int id, int rn, int key, byte[] pdata, int len) {
        byte[] secretData = new byte[256];
        byte[] mac = getMAC(id, rn, key);
        if (len <= 20) {
            for (int i = 0; i < len; i++) {
                secretData[i] = (byte) (pdata[i] ^ mac[i]);
            }
        } else {
            int j;
            for (j = 0; j < (len / 20); j++) {
                for (int i = 0; i < 20; i++) {
                    secretData[i + j * 20] = (byte) (pdata[i + j * 20] ^ mac[i]);
                }
            }
            for (int i = 0; i < (len % 20); i++) {
                secretData[i + j * 20] = (byte) (pdata[i + j * 20] ^ mac[i]);
            }
        }
        for (int i = 0; i < len; i++) {
            pdata[i] = secretData[i];
        }
    }

    public static void decrypt(int id, int rn, int key, byte[] pdata, int len) {
        encrypt(id, rn, key, pdata, len);
    }

    public static void decrypt(int id, int rn, int key, byte[] pdata, int pdataOffset, int len) {
        byte[] secretData = new byte[256];
        byte[] mac = getMAC(id, rn, key);
        if (len <= 20) {
            for (int i = 0; i < len; i++) {
                secretData[i] = (byte) (pdata[i + pdataOffset] ^ mac[i]);
            }
        } else {
            int j;
            for (j = 0; j < (len / 20); j++) {
                for (int i = 0; i < 20; i++) {
                    secretData[i + j * 20] = (byte) (pdata[i + j * 20 + pdataOffset] ^ mac[i]);
                }
            }
            for (int i = 0; i < (len % 20); i++) {
                secretData[i + j * 20] = (byte) (pdata[i + j * 20 + pdataOffset] ^ mac[i]);
            }
        }
        for (int i = 0; i < len; i++) {
            pdata[i + pdataOffset] = secretData[i];
        }
    }

    private static byte[] encodeMessageDigest(String algorithm, byte[] pdata) {
        if (pdata == null || pdata.length == 0) return null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(pdata);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getMAC(int id, int rn, int key) {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(id & 0xffffffff);
        buffer.putInt(rn & 0xffffffff);
        buffer.putInt(key & 0xffffffff);
        return encodeMessageDigest(ALGORITHM_SHA1, buffer.array());
    }
}
