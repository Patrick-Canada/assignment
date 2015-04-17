package com.bowen.assignment.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import com.bowen.assignment.R;
/**
 * Created by gen on 2015-03-14.
 */
public class MGlobal {

    public Context context;

    public static MGlobal global;

    private String address;

    private int port;


    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

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


    public String getUserId(){

       return getUserConfigPreferences().getString(MConstant.USER_ID_KEY,"");
    }


    public void saveUserId(String id){

        getUserConfigPreferences().edit().putString(MConstant.USER_ID_KEY,id).commit();
    }

    public String getGPSId(){

        return getUserConfigPreferences().getString(MConstant.GPS_ID_KEY,"");
    }


    public void saveGPSId(String id){

        getUserConfigPreferences().edit().putString(MConstant.GPS_ID_KEY,id).commit();
    }

    public void setShowUserIconConfig(boolean showUserIconConfig) {

        getUserConfigPreferences().edit().
                putBoolean(MConstant.SHOW_USER_CONFIG_KEY,showUserIconConfig).commit();
    }

    public Typeface getNormalFont(){
        Typeface normal= Typeface.createFromAsset(this.context.getAssets(), "HelveticaNeue.ttf");
        return normal;
    }


    public void alert(String message,Context context){

        if (context==null||message==null){
            return;
        }

        TextView textView=new TextView(context);
        textView.setPadding(10,10,10,10);
        textView.setTypeface(getNormalFont());
        textView.setText(message);

        TextView titleView=new TextView(context);
        titleView.setTextSize(20);
        titleView.setText("Message");
        titleView.setPadding(0,25,0,25);
        titleView.setTypeface(getNormalFont());
        titleView.setGravity(Gravity.CENTER);

        AlertDialog dialog= new AlertDialog.Builder(context)
                .setCustomTitle(titleView)
                .setView(textView)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        Button okButton=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setTypeface(getNormalFont());
        okButton.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        okButton.invalidate();
    }
}
