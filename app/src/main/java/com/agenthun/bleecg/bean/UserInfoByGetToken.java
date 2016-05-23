package com.agenthun.bleecg.bean;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/2 下午4:56.
 */
public class UserInfoByGetToken {
    private String USERID;
    private String USERNAME;
    private String EMAIL;
    private String ENTERPRISE;
    private String FREIGHTOWNER;
    private String REALNAME;
    private String MOBILE;
    private Boolean ISEMAIL;
    private Boolean ISSMS;
    private String IMGURL;
    private String ROLEID;
    private String TOKEN;

    public UserInfoByGetToken() {

    }

    public UserInfoByGetToken(String USERID, String USERNAME, String EMAIL, String ENTERPRISE, String REALNAME, String MOBILE, Boolean ISEMAIL, Boolean ISSMS) {
        this.USERID = USERID;
        this.USERNAME = USERNAME;
        this.EMAIL = EMAIL;
        this.ENTERPRISE = ENTERPRISE;
        this.REALNAME = REALNAME;
        this.MOBILE = MOBILE;
        this.ISEMAIL = ISEMAIL;
        this.ISSMS = ISSMS;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getENTERPRISE() {
        return ENTERPRISE;
    }

    public void setENTERPRISE(String ENTERPRISE) {
        this.ENTERPRISE = ENTERPRISE;
    }

    public String getFREIGHTOWNER() {
        return FREIGHTOWNER;
    }

    public void setFREIGHTOWNER(String FREIGHTOWNER) {
        this.FREIGHTOWNER = FREIGHTOWNER;
    }

    public String getREALNAME() {
        return REALNAME;
    }

    public void setREALNAME(String REALNAME) {
        this.REALNAME = REALNAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public Boolean getISEMAIL() {
        return ISEMAIL;
    }

    public void setISEMAIL(Boolean ISEMAIL) {
        this.ISEMAIL = ISEMAIL;
    }

    public Boolean getISSMS() {
        return ISSMS;
    }

    public void setISSMS(Boolean ISSMS) {
        this.ISSMS = ISSMS;
    }

    public String getIMGURL() {
        return IMGURL;
    }

    public void setIMGURL(String IMGURL) {
        this.IMGURL = IMGURL;
    }

    public String getROLEID() {
        return ROLEID;
    }

    public void setROLEID(String ROLEID) {
        this.ROLEID = ROLEID;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    @Override
    public String toString() {
        return "UserInfoByGetToken{" +
                "USERID='" + USERID + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", ENTERPRISE='" + ENTERPRISE + '\'' +
                ", FREIGHTOWNER='" + FREIGHTOWNER + '\'' +
                ", REALNAME='" + REALNAME + '\'' +
                ", MOBILE='" + MOBILE + '\'' +
                ", ISEMAIL=" + ISEMAIL +
                ", ISSMS=" + ISSMS +
                ", IMGURL='" + IMGURL + '\'' +
                ", ROLEID='" + ROLEID + '\'' +
                ", TOKEN='" + TOKEN + '\'' +
                '}';
    }
}
