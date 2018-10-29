package com.example.vuphu.newlaundry.Order;

import android.os.Parcel;
import android.os.Parcelable;

public class OBTimeSchedule implements Parcelable {
    private String id;
    private String timeStart;
    private String timeEnd;
    private boolean isDisplay = true;
    public OBTimeSchedule(){

    }

    public OBTimeSchedule(String id, String timeStart, String timeEnd, boolean isDisplay) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isDisplay = isDisplay;
    }

    public OBTimeSchedule(OBTimeSchedule obTimeSchedule) {
        this.id = obTimeSchedule.getId();
        this.timeStart = obTimeSchedule.getTimeStart();
        this.timeEnd = obTimeSchedule.getTimeEnd();
        this.isDisplay = obTimeSchedule.isDisplay();
    }

    protected OBTimeSchedule(Parcel in) {
        id = in.readString();
        timeStart = in.readString();
        timeEnd = in.readString();
        isDisplay = in.readByte() != 0;
    }

    public static final Creator<OBTimeSchedule> CREATOR = new Creator<OBTimeSchedule>() {
        @Override
        public OBTimeSchedule createFromParcel(Parcel in) {
            return new OBTimeSchedule(in);
        }

        @Override
        public OBTimeSchedule[] newArray(int size) {
            return new OBTimeSchedule[size];
        }
    };

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(timeStart);
        parcel.writeString(timeEnd);
        parcel.writeByte((byte) (isDisplay ? 1 : 0));
    }

//
}
