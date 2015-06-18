package de.submit_ev.vendingapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Igor on 11.06.2015.
 */
public class Preferences {

    SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences("de.submit_ev.vendingapp", Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }
}
