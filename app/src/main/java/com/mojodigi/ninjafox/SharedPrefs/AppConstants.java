package com.mojodigi.ninjafox.SharedPrefs;

/**
 * Created by Takendra
 */
public class AppConstants {

    public static final String NUM_TABS = "numTab";
    public static final String PREFS_LAST_REQUEST_TIME = "";
    public static String CHECK_REFRESH = "";

    public static final String API_RESPONSE_CODE = "apiResponseCode";


    public static  long AddRequestInterval=60000*7; // 7 minute
    //public static  long AddRequestInterval=1000*10; // 7 minute


    public static final String CUSTOM_ADD_RUNNING="custmAddRunning";
    public static final String LANDING_URL="landingUrl";
    public static final String BANNER_PATH="bannerPath";
    public static final String COUNTRY_CODE="country_code";

        public static final String PREFS_REDIRECT_URL="redirectUrl";
        public static final String PREFS_CAPTURE_STR="captureStr";
    public static final String PREFS_USER_ID = "userId";
    public static final String PREFS_EMAIL = "email";
    public static final String PREFS_TOKEN = "token";
    public static final String PREFS_LOCATION = "location";
    public static final String PREFS_GOOGLE_ADD_ID = "googleAddId";
    public static final String PREFS_LOCATION_STATE = "state";
    public static final String PREFS_LOCATION_CITY = "city";


    public static final int SELECTED_NEWS_CATEGORY_CCOUNT = 20;
    public static final int UN_SELECTED_NEWS_CATEGORY_CCOUNT = 10;

     public static int NOTIFICATION_ID=1;

    // custom  download directory
    public static final String downloadDirectory ="Ninja Downloads";

    public static final String  FaceBookAddProividerId="4";
    /*prefs key*/
    public static final String ADD_PROVIDER_ID="addProvId";
    //sharedPrefKeys

    // if this value comes false from  the server end we will not diaplay adds in app, means will not display adds even using the addIds from local sring.xml file;
    public static final String SHOW_ADD="show_Add";

    // this will contain mainId if  assigned for addproject  from  dashboard of add providerNetWork ;
    public static  final String APP_ID="appID";
    //this will contain bannerId incase of google /PlaceMentId in case of Inmobi /bannerId incase of SMAATO
    public static  final String BANNER_ADD_ID="bannerId";
    //this will contain interestialAddId in case of google/InMobi  and AddSpaceId in case of SMAATOO
    //it shows fullscreenAdss
    public static  final String INTERESTIAL_ADD_ID="interestialId";

    //this will contain videoAddId in case of every add providerNetWork;
    public static  final String VIDEO_ADD_ID="videoAddId";
    /*prefs key*/


    /*
     *
     * Apirequest Code
     * */

    public static final int registerApiRequest = 1;
    public static final int loginApiRequest = 2;
    public static final int userdDevicedetailsApiCode=3;
    public static final int userPrefsApiRequestCode=4;
    public static final int setLanguageRequestCode=5;
    public static final int forgotPasswordApiRequestCode=6;
    public static final int logoutApiRequestCode=7;
    public static final int deleteAccountApiRequestCode=8;
    public static final int addDetailsApiCode=10;
    public static final int customAddsApiRequestCode=11;



    public static String dummyRespose = "{\n" +
            "  \"status\": \"true\",\n" +
            "  \"message\": \"success\",\n" +
            "  \"languages\": [\n" +
            "    {\n" +
            "      \"languageCode\": \"Eng001\",\n" +
            "      \"languageName\": \"English\",\n" +
            "      \"category\": [\n" +
            "        {\n" +
            "          \"categoryCode\": \"eng001\",\n" +
            "           \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"eng001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"eng001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"eng001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "     {\n" +
            "      \"languageCode\": \"Hin001\",\n" +
            "      \"languageName\": \"Hindi\",\n" +
            "      \"category\": [\n" +
            "        {\n" +
            "          \"categoryCode\": \"hin001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"hin001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"hin001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"hin001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "     {\n" +
            "      \"languageCode\": \"PUN001\",\n" +
            "      \"languageName\": \"Punjabi\",\n" +
            "      \"category\": [\n" +
            "        {\n" +
            "          \"categoryCode\": \"pun001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"pun001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"pun001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"categoryCode\": \"epun001\",\n" +
            "          \"categoryName\": \"Entertainment\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}