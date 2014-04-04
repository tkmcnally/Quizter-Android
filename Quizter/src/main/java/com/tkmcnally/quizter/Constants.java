package com.tkmcnally.quizter;

/**
 * Created by missionary on 12/26/2013.
 */
public class Constants {

    public static String STRING_QUESTION = "question";
    public static String STRING_ANSWER = "answer";
    public static String STRING_DENSITY = "screen_density";
    public static String STRING_UPDATED_QUESTIONS = "updated_questions";
    public static String STRING_URL_PATH = "urlPath";
    public static String STRING_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String STRING_CURRENT_END_INDEX = "current_end_index";
    public static String SEGOE_FONT_PATH = "fonts/segoeui.ttf";
    public static String SEGOE_LIGHT_FONT_PATH = "fonts/segoe_ui_light.ttf";
    public static String STRING_PLAYER_INDEX = "player_index";
    public static String STRING_PLAYER_ID = "player_id";
    public static String STRING_QUESTION_ANSWER = "question_answer";

    public static class API {

        public static String GET_PROFILE_PATH = "http://quizter.tkmcnally.com/api/android/get-profile";

        public static String GET_DEFAULT_QUESTIONS_PATH = "http://quizter.tkmcnally.com/api/android/get-default-questions";

        public static String UPDATE_PROFILE_PATH = "http://quizter.tkmcnally.com/api/android/update-profile";

        public static String GET_LEADERBOARD_PATH = "http://quizter.tkmcnally.com/api/android/get-leaderboard";

        public static String GET_AVAILABLE_PLAYER_PATH = "http://quizter.tkmcnally.com/api/android/get-available-player";

        public static String GET_PLAYER_PROFILE_PATH = "http://quizter.tkmcnally.com/api/android/get-player-profile";

        public static String SUBMIT_QUIZ_PATH = "http://quizter.tkmcnally.com/api/android/submit-quiz";
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

}
