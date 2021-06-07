package com.example.social_interest_club.DataStructsAndDAOs;

public class AllDataOperationAccessObject {
    //Singleton pastern is implemented.
    private static AllDataOperationAccessObject instance=null;

    private final ClubMemberStructDAO clubMemberStructDAO=new ClubMemberStructDAO();
    private final ClubStructDAO clubStructDAO =new ClubStructDAO();
    private final IntrestStructDOA intrestStructDOA =new IntrestStructDOA();
    private final MessageStructDAO messageStructDAO =new MessageStructDAO();
    private final QuestionStructDAO questionStructDAO =new QuestionStructDAO();
    private final RequestClubStructDAO requestClubStructDAO =new RequestClubStructDAO();
    private final RequestSubClubStructDAO requestSubClubStructDAO =new RequestSubClubStructDAO();
    private final SubClubMemberStructDAO subClubMemberStructDAO =new SubClubMemberStructDAO();
    private final SubClubStructDAO subClubStructDAO= new SubClubStructDAO();
    private final UserStructDAO userStructDAO =new UserStructDAO();

    private AllDataOperationAccessObject(){
    }

    public static AllDataOperationAccessObject getInstance() {
        if (instance==null){
            return new AllDataOperationAccessObject();
        }
        else {
            return instance;
        }
    }


    public ClubMemberStructDAO getClubMemberStructDAO() {
        return clubMemberStructDAO;
    }


    public ClubStructDAO getClubStructDAO() {
        return clubStructDAO;
    }


    public IntrestStructDOA getIntrestStructDOA() {
        return intrestStructDOA;
    }



    public MessageStructDAO getMessageStructDAO() {
        return messageStructDAO;
    }



    public QuestionStructDAO getQuestionStructDAO() {
        return questionStructDAO;
    }


    public RequestClubStructDAO getRequestClubStructDAO() {
        return requestClubStructDAO;
    }


    public RequestSubClubStructDAO getRequestSubClubStructDAO() {
        return requestSubClubStructDAO;
    }

    public SubClubMemberStructDAO getSubClubMemberStructDAO() {
        return subClubMemberStructDAO;
    }

    public SubClubStructDAO getSubClubStructDAO() {
        return subClubStructDAO;
    }

    public UserStructDAO getUserStructDAO() {
        return userStructDAO;
    }

}
