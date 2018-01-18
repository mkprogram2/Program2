package com.mk.admin.payroll.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.mk.admin.payroll.R;

import static android.content.Context.MODE_PRIVATE;
/**
 * Created by admin on 1/4/2018.
 */

public class SharedPreferenceEditor
{
    private static String filename;
    private static String value;

    public static boolean SavePreferences(Context context, String key, String value)
    {
        try
        {
            filename = context.getResources().getString(R.string.preference_file_key);
            SharedPreferences sharedPreferences = context.getSharedPreferences(filename, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static String LoadPreferences(Context context, String key, String defaultValue)
    {
        try
        {
            filename = context.getResources().getString(R.string.preference_file_key);
            SharedPreferences sharedPreferences = context.getSharedPreferences(filename , MODE_PRIVATE);
            value = sharedPreferences.getString(key, defaultValue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }
}
