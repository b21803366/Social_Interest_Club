package com.example.social_interest_club.DataStructsAndDAOs;

public class  ClubStruct
{
    private String clubName;
    private String description;

    public String q1;
    public String q2;
    public String q3;

    // Constructor
    public ClubStruct(String clubName, String description) {
        this.clubName = clubName;
        this.description = description;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return description;
    }

    public void setClubDescription(String clubDescription) {
        this.description = clubDescription;
    }

}
