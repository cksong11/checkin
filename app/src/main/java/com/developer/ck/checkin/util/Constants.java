package com.developer.ck.checkin.util;

public class Constants {
    public final static String KEY_CHECK_TYPE = "Key_Check_Type";
    public final static String KEY_PAY_TYPE = "Key_Pay_Type";
//    public final static String API_SERVER_URL = "http://192.168.0.109:7001/api/";
    public final static String API_SERVER_URL = "http://cococheckin.com/api/";
    public final static String USER_ID = "User_Id";
    public final static String USER_PASSWORD = "User_Password";
    public final static String BUILD_NAME = "Build_Name";
    public final static String API_USER_ID = "990510";
    public final static String API_USER_PASSWORD = "Dhh2j3qOsrcHgACOdWySECoK";
    public final static String API_USER_CODE = "cin_user";
    public final static String BOOKING_CODE = "Booking_Code";
    public final static String BOOKING_NAME = "Booking_Name";
    public final static String BOOKING_SECOND_NAME = "Booking_Second_Name";
    public final static String BOOKING_COUNT = "Booking_Count";
    public final static String BOOKING_EMAIL = "Booking_Email";
    public final static String BOOKING_PHONE = "Booking_Phone";
    public final static String ROOM_NUMBER = "Room_Number";
    public final static String ROOM_PASSWORD = "Room_Password";

    public final static class Build {
        public final static String BUILD_ID = "Build_Id";
        public final static String BUILD_NAME = "Build_Name";
        public final static String MESSAGE_WELCOME = "Message_Welcome";
        public final static String MESSAGE_PRIVACY = "Message_Privacy";
        public final static String MESSAGE_MONEY_PRIVACY = "Message_Money_Privacy";
        public final static String LANGUAGE_CODE = "Language_Code";
    }

    public final static class Room {
        public final static String ROOM_WELCOME_JP = "Room_Welcome_Jp";
        public final static String ROOM_WELCOME_EN = "Room_Welcome_En";
        public final static String ROOM_WELCOME_KO = "Room_Welcome_Ko";
        public final static String ROOM_WELCOME_CN = "Room_Welcome_Cn";
        public final static String ROOM_KEY_REPLACE_JP = "[鍵番号]";
        public final static String ROOM_KEY_REPLACE_EN = "[Key Number]";
        public final static String ROOM_KEY_REPLACE_CN = "[钥匙号码]";
        public final static String ROOM_KEY_REPLACE_KO = "[키번호]";
    }

    public final static class Error {
        public final static String INVALID_CODE_EXIST = "invalid_code_exist";
        public final static String INVALID_CODE_OUT = "invalid_code_out";
        public final static String INVALID_CODE_IN = "invalid_code_in";
        public final static String INVALID_BUILD = "invalid_build";
        public final static String INVALID_CODE_NOT_IN = "invalid_code_not_in";
        public final static String NOT_EMPTY_ROOM = "not_empty_room";
        public final static String ERROR_PAYMENT = "err_pay";
    }
}
