package com.mk.admin.payroll.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 2/1/2018.
 */

public class Session
{
    private SharedPreferences prefs;

    public Session(Context context)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setId (String id)
    {
        prefs.edit().putString("id", id).commit();
    }

    public String getId()
    {
        String id = prefs.getString("id","");
        return id;
    }

    public void setAccesstoken (String access_token)
    {
        prefs.edit().putString("access_token", access_token).commit();
    }

    public String getAccesstoken ()
    {
        String access_token = prefs.getString("access_token", "");
        return access_token;
    }

    public void SetUsername (String username)
    {
        prefs.edit().putString("username", username).commit();
    }

    public String GetUsername ()
    {
        String username = prefs.getString("username", "");
        return username;
    }

    public void SetPassword (String password)
    {
        prefs.edit().putString("password", password).commit();
    }

    public String GetPassword ()
    {
        String password = prefs.getString("password", "");
        return password;
    }

    public void SetMerchantCode (String merchantcode)
    {
        prefs.edit().putString("merchantcode", merchantcode).commit();
    }

    public String GetMerchantCode ()
    {
        String merchantcode = prefs.getString("merchantcode", "");
        return merchantcode;
    }

    public  void SetRememberMe (Integer rememberme)
    {
        prefs.edit().putInt("rememberme", rememberme).commit();
    }

    public Integer GetRememberMe ()
    {
        Integer rememberme = prefs.getInt("rememberme", 0);
        return rememberme;
    }

    public void SetRole (Integer role)
    {
        prefs.edit().putInt("role", role).commit();
    }

    public Integer GetRole ()
    {
        Integer role = prefs.getInt("role", 0);
        return role;
    }
}