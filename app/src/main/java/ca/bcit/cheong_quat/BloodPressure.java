package ca.bcit.cheong_quat;

public class BloodPressure {
    private String userID;
    private String readDate;
    private String readTime;
    private int systolicRead;
    private int diastolicRead;

    public BloodPressure() {
    }

    public BloodPressure(String userID, String readDate, String readTime, int systolicRead, int diastolicRead) {
        this.userID = userID;
        this.readDate = readDate;
        this.readTime = readTime;
        this.systolicRead = systolicRead;
        this.diastolicRead = diastolicRead;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public int getSystolicRead() {
        return systolicRead;
    }

    public void setSystolicRead(int systolicRead) {
        this.systolicRead = systolicRead;
    }

    public int getDiastolicRead() {
        return diastolicRead;
    }

    public void setDiastolicRead(int diastolicRead) {
        this.diastolicRead = diastolicRead;
    }
}
