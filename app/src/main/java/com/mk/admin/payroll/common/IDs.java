package com.mk.admin.payroll.common;

/**
 * Created by admin on 1/5/2018.
 */

public class IDs
{
    private static String loginUsername, idUser;

    public static String getLoginUser()
    {
        return loginUsername;
    }

    public static void setLoginUser(String loginUsername) { IDs.loginUsername = loginUsername; }

    public static String getIdUser()
    {
        return idUser;
    }

    public static void setIdUser(String idUser) { IDs.idUser = idUser; }
}
