package com.example.social_interest_club.DataStructsAndDAOs;

import java.util.ArrayList;

public class ClubMemberStructDAO {
    static ArrayList<ClubMemberStruct> clubMemberStructs=new ArrayList<ClubMemberStruct>();


    protected ClubMemberStruct getClubMemberStructByID(int ID){
        for(ClubMemberStruct clubMemberStruct:clubMemberStructs){
            if (clubMemberStruct.getClubID()==ID){
                return clubMemberStruct;
            }
        }
        return null;
    }

    public void setClubMemberStructByID(int ID, String contex, int sender, int reciver, int clubID){
        ClubMemberStruct clubMemberStruct=new ClubMemberStruct(ID,contex,sender,reciver,clubID);
        clubMemberStructs.add(clubMemberStruct);
    }

}
