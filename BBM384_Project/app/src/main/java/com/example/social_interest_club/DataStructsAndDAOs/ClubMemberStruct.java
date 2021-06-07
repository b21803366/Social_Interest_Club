package com.example.social_interest_club.DataStructsAndDAOs;

class ClubMemberStruct {

    private int ID;
    private String contex;
    private int sender;
    private int reciver;
    private int clubID;

    protected ClubMemberStruct(int ID, String contex, int sender, int reciver, int clubID) {
        this.ID = ID;
        this.contex = contex;
        this.sender = sender;
        this.reciver = reciver;
        this.clubID = clubID;

    }

    protected int getID() {
        return ID;
    }

    protected void setID(int ID) {
        this.ID = ID;
    }

    protected String getContex() {
        return contex;
    }

    protected void setContex(String contex) {
        this.contex = contex;
    }

    protected int getSender() {
        return sender;
    }

    protected void setSender(int sender) {
        this.sender = sender;
    }

    protected int getReciver() {
        return reciver;
    }

    protected void setReciver(int reciver) {
        this.reciver = reciver;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }
}
