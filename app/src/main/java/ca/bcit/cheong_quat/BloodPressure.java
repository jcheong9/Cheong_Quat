package ca.bcit.cheong_quat;

public class BloodPressure {
    private String bloodPressureID;
    private String userID;
    private String readDate;
    private String readTime;
    private String condition;
    private int systolicRead;
    private int diastolicRead;
    private double systolicAverage;
    private double diastolicAverage;

    public BloodPressure() {
    }

    public BloodPressure(String bloodPressureID, String userID, String readDate, String readTime, int systolicRead, int diastolicRead) {
        this.bloodPressureID = bloodPressureID;
        this.userID = userID;
        this.readDate = readDate;
        this.readTime = readTime;
        this.systolicRead = systolicRead;
        this.diastolicRead = diastolicRead;
    }

    public BloodPressure(String userID, double systolicAverage, double diastolicAverage, String condition) {
        this.userID = userID;
        this.systolicAverage = systolicAverage;
        this.diastolicAverage = diastolicAverage;
        this.condition = condition;
    }

    public String getBloodPressureID() {
        return bloodPressureID;
    }

    public void setBloodPressureID(String bloodPressureID) {
        this.bloodPressureID = bloodPressureID;
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

    public double getSystolicAverage() {
        return systolicAverage;
    }

    public double getDiastolicAverage() {
        return diastolicAverage;
    }

    public String getCondition() {
        return condition;
    }
}
