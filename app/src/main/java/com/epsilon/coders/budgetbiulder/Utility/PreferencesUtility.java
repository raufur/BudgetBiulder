package com.epsilon.coders.budgetbiulder.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.epsilon.coders.budgetbiulder.MainActivity;


/**
 * Created by Raufur on 10/6/15.
 */
public class PreferencesUtility {

    public static int getAccount(Context context){
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREF, 0);
        return settings.getInt("account", 1);
    }

    public static void saveAccount(Context context, int account){
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("account", account);
        editor.commit();
    }
}