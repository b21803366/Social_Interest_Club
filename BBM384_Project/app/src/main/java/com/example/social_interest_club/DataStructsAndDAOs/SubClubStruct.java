package com.example.social_interest_club.DataStructsAndDAOs;

public class SubClubStruct {
    private String parentClubName;
    private String subClubName;
    private String description;
    private String adminEmail;

    public String q1;
    public String q2;
    public String q3;


    public SubClubStruct(String parentClubName, String clubName, String description) {
        this.parentClubName = parentClubName;
        this.subClubName = clubName;
        this.description = description;
        this.adminEmail = null;
    }

    public String getParentClubName() {
        return parentClubName;
    }

    public void setParentClubName(String parentClubName) {
        this.parentClubName = parentClubName;
    }

    public String getSubClubName() {
        return subClubName;
    }

    public void setSubClubName(String clubName) {
        this.subClubName = clubName;
    }

    public String getClubDescription() {
        return description;
    }

    public void setClubDescription(String clubDescription) {
        this.description = clubDescription;
    }

    public String getClubAdminEmail() {
        return adminEmail;
    }

    public void setClubAdminEmail(String subClubAdminEmail) {
        this.adminEmail = subClubAdminEmail;
    }

}
