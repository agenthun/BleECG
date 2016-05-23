package com.agenthun.bleecg.bean;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/3 下午8:49.
 */
public class MACByOpenCloseContainer {
    private Integer EFFECTIVETOKEN;
    private String ERRORINFO;
    private String MAC;
    private Integer RESULT;

    public Integer getEFFECTIVETOKEN() {
        return EFFECTIVETOKEN;
    }

    public void setEFFECTIVETOKEN(Integer EFFECTIVETOKEN) {
        this.EFFECTIVETOKEN = EFFECTIVETOKEN;
    }

    public String getERRORINFO() {
        return ERRORINFO;
    }

    public void setERRORINFO(String ERRORINFO) {
        this.ERRORINFO = ERRORINFO;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public Integer getRESULT() {
        return RESULT;
    }

    public void setRESULT(Integer RESULT) {
        this.RESULT = RESULT;
    }

    @Override
    public String toString() {
        return "MACByOpenCloseContainer{" +
                "EFFECTIVETOKEN=" + EFFECTIVETOKEN +
                ", ERRORINFO='" + ERRORINFO + '\'' +
                ", MAC='" + MAC + '\'' +
                ", RESULT=" + RESULT +
                '}';
    }
}
