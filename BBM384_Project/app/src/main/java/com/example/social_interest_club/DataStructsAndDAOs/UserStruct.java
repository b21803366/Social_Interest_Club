package com.example.social_interest_club.DataStructsAndDAOs;

public class UserStruct
{
    public String userName;
    public int status;
    public int banCount;
    public boolean isBanned;
    public boolean isComplaint;
    public int bannedDay;

    // DON'T DELETE THIS CONSTRUCTOR
    public UserStruct()
    {

    }

    // Constructor
    public UserStruct(String _userName, int _status, int _banCount,
                        boolean _isBanned, boolean _isComplaint, int _bannedDay)
    {
        this.userName = _userName;
        this.banCount = _banCount;
        this.status = _status;
        this.isBanned = _isBanned;
        this.isComplaint = _isComplaint;
        this.bannedDay = _bannedDay;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBanCount(int banCount) {
        this.banCount = banCount;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setComplaint(boolean complaint) {
        isComplaint = complaint;
    }

    public void setBannedDay(int bannedDay) {
        this.bannedDay = bannedDay;
    }

    public String getUserName() {
        return userName;
    }
/*
    public String getPassword() {
        return password;
    }
*/
    public int getStatus() {
        return status;
    }

    public int getBanCount() {
        return banCount;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public boolean isComplaint() {
        return isComplaint;
    }

    public int getBannedDay() {
        return bannedDay;
    }
}
