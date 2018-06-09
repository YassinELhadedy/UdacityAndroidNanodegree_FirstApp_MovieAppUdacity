package com.nextbit.yassin.movieappudacity1.domain.confiq;


public class Constants {



    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "movies_tb";
    public static final String TABLE_NAME2 = "movies_tb2";
    public static final String TABLE_NAME3 = "movies_tb3";
    public static final String TABLE_NAME4 = "movies_tb4";




    public static final String KEY_ID = "_id";
    public static final String DATABASE_NAME = "movilistdb";



//attribute of offline mode

    public static final String KEY_NAME = "name";
    public static final String KEY_MOV_IMAGE = "image_movie";
    public static final String KEY_MOV_OV = "ov_movie";
    public static final String KEY_MOV_RAT = "rat_movie";
    public static final String KEY_TRA_ID = "tra_movie";
    public static final String KEY_V_C = "count_movie";
    public static final String KEY_R_D= "date_movie";

    //attribute of favorite mode
    public static final String KEY_NAME2 = "name2";
    public static final String KEY_MOV_IMAGE2 = "image_movie2";
    public static final String KEY_MOV_OV2 = "ov_movie2";
    public static final String KEY_MOV_RAT2 = "rat_movie2";
    public static final String KEY_TRA_ID2 = "tra_movie2";
    public static final String KEY_V_C2 = "count_movie2";
    public static final String KEY_R_D2= "date_movie2";

    //attribute of review mode
    public static final String KEY_AUTHOR = "rev_author";
    public static final String KEY_CONTENT = "rev_content";
    public static final String KEY_URL = "rev_url";
    //attribute of trailer mode
    public static final String KEY_KEY = "tra_key";
    public static final String ID_TRAMOV = "id_tramov";
    public static final String ID_REV_MOV = "id_reviewmovie";

    public static final String KEY_NAMET = "tra_name";


    // Database creation SQL statement
    public static final String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME +
            " TEXT, " + Constants.KEY_MOV_IMAGE + " TEXT, "+Constants.KEY_MOV_OV+" TEXT, " +Constants.KEY_MOV_RAT+" TEXT, "+Constants.KEY_V_C+"  TEXT, "+Constants.KEY_TRA_ID+"  TEXT, "+ Constants.KEY_R_D + " TEXT  );";

    public static final  String CREATE_FAV_TABLE = "CREATE TABLE " + TABLE_NAME2 + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME2 +
            " TEXT, " + Constants.KEY_MOV_IMAGE2 + " TEXT, " + Constants.KEY_MOV_OV2 + " TEXT, " + Constants.KEY_MOV_RAT2 + " TEXT, " + Constants.KEY_V_C2 + "  TEXT, " + KEY_TRA_ID2 + "  TEXT, " + Constants.KEY_R_D2 + " TEXT  );";
    public static final   //create our table
            String CREATE_TRAILER_TABLE = "CREATE TABLE " + TABLE_NAME3 + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "+ID_TRAMOV + " TEXT, " +KEY_KEY + " TEXT, " + KEY_NAMET +
            " TEXT  );";

   public static final String CREATE_Review_TABLE = "CREATE TABLE " + TABLE_NAME4 + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_AUTHOR +
            " TEXT, " + Constants.KEY_CONTENT + "  TEXT, "+ID_REV_MOV + " TEXT, "  + Constants.KEY_URL +  " TEXT  );";




}
