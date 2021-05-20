package com.example.spp_client;

import java.io.Serializable;

public class Member implements Serializable {
    private String mBirth;
    private String mPhone;
    private String mID;
    private String mPW;
    private String mCarNo;
    private String mCardNo;
    private String mValidDate;
    private String mCVC;
    private int mNo;

    //constructor
    public Member(){
        mID="";
        mPW="";
        mBirth="";
        mPhone="";
        mCarNo="";
        mCardNo="";
        mValidDate="";
        mCVC="";
        mNo=0;
    }

    public Member(String id, String pw, String birth, String phone){
        mID = id;
        mPW = pw;
        mBirth = birth;
        mPhone = phone;
        mCarNo="";
        mCardNo="";
        mValidDate="";
        mCVC="";
        mNo=0;
    }

    //getter/setter
    public String getBirth() {
        return mBirth;
    }

    public void setBirth(String mBirth) {
        this.mBirth = mBirth;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getPW() {
        return mPW;
    }

    public void setPW(String mPW) {
        this.mPW = mPW;
    }

    public String getCarNo() {
        return mCarNo;
    }

    public void setCarNo(String mCarNo) {
        this.mCarNo = mCarNo;
    }

    public String getCardNo() {
        return mCardNo;
    }

    public void setCardNo(String mCardNo) {
        this.mCardNo = mCardNo;
    }

    public String getValidDate() {
        return mValidDate;
    }

    public void setValidDate(String mValidDate) {
        this.mValidDate = mValidDate;
    }

    public String getCVC() {
        return mCVC;
    }

    public void setCVC(String mCVC) {
        this.mCVC = mCVC;
    }

    public int getNo() {
        return mNo;
    }

    public void setNo(int mNo) {
        this.mNo = mNo;
    }
}
