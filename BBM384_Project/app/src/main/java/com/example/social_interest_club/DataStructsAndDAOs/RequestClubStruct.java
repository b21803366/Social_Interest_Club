package com.example.social_interest_club.DataStructsAndDAOs;

public class RequestClubStruct
{
    private String clubName;
    //private String description;
    private int numberOfRequests;



    // Constructor
    public RequestClubStruct(String clubName, int numberOfRequests) {
        this.clubName = clubName;
        //this.description = description;
        this.numberOfRequests = numberOfRequests;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(int numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }


}