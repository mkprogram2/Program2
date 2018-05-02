package com.mk.admin.payroll.common;

import android.util.Base64;

/**
 * Created by admin on 1/3/2018.
 */

public class RestVariable
{
    //public static final String SERVER_URL = "http://54.179.195.87:8083/";
    //public static final String SERVER_LOGIN = "http://54.179.195.87:8084/";

    /*public static final String SERVER_LOGIN = "http://192.168.71.120:6776/api/auth/";
    public static final String SERVER_URL = "http://192.168.71.120:6776/api/hr/";*/

    public static final String SERVER_LOGIN = "http://192.168.70.41:6776/api/auth/";
    public static final String SERVER_URL = "http://192.168.70.41:6776/api/hr/";

    /*public static final String SERVER_LOGIN = "http://192.168.71.89:6776/api/auth/";
    public static final String SERVER_URL = "http://192.168.71.89:6776/api/hr/";*/

    /*public static final String SERVER_LOGIN = "http://192.168.70.41:6776/api/auth/";
    public static final String SERVER_URL = "http://192.168.70.41:6776/api/hr/";*/

    public static final String APP_ID = "app-payroll";
    public static final String SECRET = "f647a550-0a41-11e8-ae94-d73daeaee534";
    public static final String Authorization = "Basic " + Base64.encodeToString((APP_ID + ":" + SECRET).getBytes(), Base64.NO_WRAP);
    public static final String PASSWORD = "password";
}