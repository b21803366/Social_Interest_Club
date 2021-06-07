package com.example.social_interest_club;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Globals {
    public static String userName = "Unassigned";
    public static long status = -1;
    public static long banCount = -1;
    public static boolean isBanned = false;
    public static boolean isComplaint = false;
    public static long bannedDay = -1;

    // ALWAYS CHECK THIS BOOLEAN BEFORE USING USER DATA
    public static boolean isUserDataLoaded = false;

    /**
     * STATUS:
     * Admin : 0
     * Sub-club admin : 1
     * Member : 2
     * Unregistered : 3
     */

    static String regUserName = "Unassigned";
    static List<String> questions = new ArrayList<String>();
    static List<String> questClubs = new ArrayList<String>();
    static boolean is_questions_ready = false;
    static int question_index = 0;
    static Map<String, Object> interest_rates = new HashMap<>();

    static boolean passedQuiz = false;
    static int quizResult = 0;
}
