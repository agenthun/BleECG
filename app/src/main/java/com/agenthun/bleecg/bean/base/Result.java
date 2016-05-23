
package com.agenthun.bleecg.bean.base;

public class Result {

    private Integer RESULT;
    private Integer EFFECTIVETOKEN;
    private String ERRORINFO;
    private Integer TOTAL;
    private Integer COUNTPAGES;

    private String MAC;

    /**
     * @return The RESULT
     */
    public Integer getRESULT() {
        return RESULT;
    }

    /**
     * @param RESULT The RESULT
     */
    public void setRESULT(Integer RESULT) {
        this.RESULT = RESULT;
    }

    /**
     * @return The EFFECTIVETOKEN
     */
    public Integer getEFFECTIVETOKEN() {
        return EFFECTIVETOKEN;
    }

    /**
     * @param EFFECTIVETOKEN The EFFECTIVETOKEN
     */
    public void setEFFECTIVETOKEN(Integer EFFECTIVETOKEN) {
        this.EFFECTIVETOKEN = EFFECTIVETOKEN;
    }

    /**
     * @return The ERRORINFO
     */
    public String getERRORINFO() {
        return ERRORINFO;
    }

    /**
     * @param ERRORINFO The ERRORINFO
     */
    public void setERRORINFO(String ERRORINFO) {
        this.ERRORINFO = ERRORINFO;
    }

    /**
     * @return The TOTAL
     */
    public Integer getTOTAL() {
        return TOTAL;
    }

    /**
     * @param TOTAL The TOTAL
     */
    public void setTOTAL(Integer TOTAL) {
        this.TOTAL = TOTAL;
    }

    /**
     * @return The COUNTPAGES
     */
    public Integer getCOUNTPAGES() {
        return COUNTPAGES;
    }

    /**
     * @param COUNTPAGES The COUNTPAGES
     */
    public void setCOUNTPAGES(Integer COUNTPAGES) {
        this.COUNTPAGES = COUNTPAGES;
    }


    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    @Override
    public String toString() {
        return "Result{" +
                "RESULT=" + RESULT +
                ", EFFECTIVETOKEN=" + EFFECTIVETOKEN +
                ", ERRORINFO='" + ERRORINFO + '\'' +
                ", TOTAL=" + TOTAL +
                ", COUNTPAGES=" + COUNTPAGES +
                ", MAC='" + MAC + '\'' +
                '}';
    }
}
