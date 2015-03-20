package com.bowen.assignment.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.bowen.assignment.R;
/**
 * Created by gen on 2015-03-14.
 */
public class MGlobal {

    public Context context;

    public static MGlobal global;

    private MGlobal(Context context){

        this.context=context;
    }


    public static void init(Context context){

        if (global==null){
            global=new MGlobal(context.getApplicationContext());
        }
    }

    public static MGlobal getInstance(){

        return global;
    }


    public SharedPreferences getUserConfigPreferences(){
       SharedPreferences preferences= this.context.getSharedPreferences(this.context.
                       getString(R.string.user_config_info),
                       Context.MODE_PRIVATE);
       return preferences;
    }


    public boolean isShowUserIconConfig() {

        return getUserConfigPreferences().
                getBoolean(MConstant.SHOW_USER_CONFIG_KEY,true);

    }

    public void setShowUserIconConfig(boolean showUserIconConfig) {

        getUserConfigPreferences().edit().
                putBoolean(MConstant.SHOW_USER_CONFIG_KEY,showUserIconConfig).commit();
    }
}
