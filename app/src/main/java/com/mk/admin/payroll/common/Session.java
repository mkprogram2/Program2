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
}