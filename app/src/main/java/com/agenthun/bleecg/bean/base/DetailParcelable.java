
package com.agenthun.bleecg.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailParcelable implements Parcelable {
    public static final String EXTRA_DEVICE = "DetailParcelable";

    private String ContainerId;
    private String ContainerNo;
    private String FreightName;
    private String Origin;
    private String Frequency;
    private String TempThreshold;
    private String HumThreshold;
    private String VibThreshold;
    private String Operationer;
    private String Status;

    private String PositionId;
    private String CreateDatetime;
    private String PositionName;
    private String Coordinate;
    private String ActionType;
    private String SecurityLevel;
    private String Temperature;
    private String Humidity;
    private String Vibration;

    /**
     * @return The ContainerId
     */
    public String getContainerId() {
        return ContainerId;
    }

    /**
     * @param ContainerId The ContainerId
     */
    public void setContainerId(String ContainerId) {
        this.ContainerId = ContainerId;
    }

    /**
     * @return The ContainerNo
     */
    public String getContainerNo() {
        return ContainerNo;
    }

    /**
     * @param ContainerNo The ContainerNo
     */
    public void setContainerNo(String ContainerNo) {
        this.ContainerNo = ContainerNo;
    }

    /**
     * @return The FreightName
     */
    public String getFreightName() {
        return FreightName;
    }

    /**
     * @param FreightName The FreightName
     */
    public void setFreightName(String FreightName) {
        this.FreightName = FreightName;
    }

    /**
     * @return The Origin
     */
    public String getOrigin() {
        return Origin;
    }

    /**
     * @param Origin The Origin
     */
    public void setOrigin(String Origin) {
        this.Origin = Origin;
    }

    /**
     * @return The Frequency
     */
    public String getFrequency() {
        return Frequency;
    }

    /**
     * @param Frequency The Frequency
     */
    public void setFrequency(String Frequency) {
        this.Frequency = Frequency;
    }

    /**
     * @return The TempThreshold
     */
    public String getTempThreshold() {
        return TempThreshold;
    }

    /**
     * @param TempThreshold The TempThreshold
     */
    public void setTempThreshold(String TempThreshold) {
        this.TempThreshold = TempThreshold;
    }

    /**
     * @return The HumThreshold
     */
    public String getHumThreshold() {
        return HumThreshold;
    }

    /**
     * @param HumThreshold The HumThreshold
     */
    public void setHumThreshold(String HumThreshold) {
        this.HumThreshold = HumThreshold;
    }

    /**
     * @return The VibThreshold
     */
    public String getVibThreshold() {
        return VibThreshold;
    }

    /**
     * @param VibThreshold The VibThreshold
     */
    public void setVibThreshold(String VibThreshold) {
        this.VibThreshold = VibThreshold;
    }

    /**
     * @return The Operationer
     */
    public String getOperationer() {
        return Operationer;
    }

    /**
     * @param Operationer The Operationer
     */
    public void setOperationer(String Operationer) {
        this.Operationer = Operationer;
    }

    /**
     * @return The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param Status The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }


    public String getPositionId() {
        return PositionId;
    }

    public void setPositionId(String positionId) {
        PositionId = positionId;
    }

    public String getCreateDatetime() {
        return CreateDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        CreateDatetime = createDatetime;
    }

    public String getPositionName() {
        return PositionName;
    }

    public void setPositionName(String positionName) {
        PositionName = positionName;
    }

    public String getCoordinate() {
        return Coordinate;
    }

    public void setCoordinate(String coordinate) {
        Coordinate = coordinate;
    }

    public String getActionType() {
        return ActionType;
    }

    public void setActionType(String actionType) {
        ActionType = actionType;
    }

    public String getSecurityLevel() {
        return SecurityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        SecurityLevel = securityLevel;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getVibration() {
        return Vibration;
    }

    public void setVibration(String vibration) {
        Vibration = vibration;
    }

    public static Creator<DetailParcelable> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<DetailParcelable> CREATOR = new Creator<DetailParcelable>() {
        @Override
        public DetailParcelable createFromParcel(Parcel in) {
            DetailParcelable detail = new DetailParcelable();

            detail.ContainerId = in.readString();
            detail.ContainerNo = in.readString();
            detail.FreightName = in.readString();
            detail.Origin = in.readString();
            detail.Frequency = in.readString();
            detail.TempThreshold = in.readString();
            detail.HumThreshold = in.readString();
            detail.VibThreshold = in.readString();
            detail.Operationer = in.readString();
            detail.Status = in.readString();

            detail.PositionId = in.readString();
            detail.CreateDatetime = in.readString();
            detail.PositionName = in.readString();
            detail.Coordinate = in.readString();
            detail.ActionType = in.readString();
            detail.SecurityLevel = in.readString();
            detail.Temperature = in.readString();
            detail.Humidity = in.readString();
            detail.Vibration = in.readString();

            return detail;
        }

        @Override
        public DetailParcelable[] newArray(int size) {
            return new DetailParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ContainerId);
        dest.writeString(ContainerNo);
        dest.writeString(FreightName);
        dest.writeString(Origin);
        dest.writeString(Frequency);
        dest.writeString(TempThreshold);
        dest.writeString(HumThreshold);
        dest.writeString(VibThreshold);
        dest.writeString(Operationer);
        dest.writeString(Status);

        dest.writeString(PositionId);
        dest.writeString(CreateDatetime);
        dest.writeString(PositionName);
        dest.writeString(Coordinate);
        dest.writeString(ActionType);
        dest.writeString(SecurityLevel);
        dest.writeString(Temperature);
        dest.writeString(Humidity);
        dest.writeString(Vibration);
    }

    @Override
    public String toString() {
        return "Detail{" +
                "ContainerId='" + ContainerId + '\'' +
                ", ContainerNo='" + ContainerNo + '\'' +
                ", FreightName='" + FreightName + '\'' +
                ", Origin='" + Origin + '\'' +
                ", Frequency='" + Frequency + '\'' +
                ", TempThreshold='" + TempThreshold + '\'' +
                ", HumThreshold='" + HumThreshold + '\'' +
                ", VibThreshold='" + VibThreshold + '\'' +
                ", Operationer='" + Operationer + '\'' +
                ", Status='" + Status + '\'' +
                ", PositionId='" + PositionId + '\'' +
                ", CreateDatetime='" + CreateDatetime + '\'' +
                ", PositionName='" + PositionName + '\'' +
                ", Coordinate='" + Coordinate + '\'' +
                ", ActionType='" + ActionType + '\'' +
                ", SecurityLevel='" + SecurityLevel + '\'' +
                ", Temperature='" + Temperature + '\'' +
                ", Humidity='" + Humidity + '\'' +
                ", Vibration='" + Vibration + '\'' +
                '}';
    }
}
