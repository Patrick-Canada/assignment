package com.bowen.assignment.model;

import android.util.Log;

import com.bowen.assignment.common.MGlobal;
import com.bowen.assignment.entity.Result;
import com.google.gson.Gson;

/**
 * Created by patrick on 2015-03-26.
 */
public class UserModel extends BaseModel{

    private final static String UPDATE_USER_METHOD="updateUser";

    private final static String ADD_USER_METHOD="addUser";

    private final static String TAG="UserModel";

    public UserModel(String address,int port,String message) {
        super(address,port,message);
    }

    public static UserModel initUser(String address,int port, String userIcon){

        UserModel userModel=new UserModel(address,port,ADD_USER_METHOD);

        userModel.addParam("userIcon",userIcon);

        userModel.setAction(ADD_USER_METHOD);

        return userModel;
    }


    public static UserModel updateUser(String address,int port,String userId,String userIcon){

        UserModel userModel=new UserModel(address,port,UPDATE_USER_METHOD);

        userModel.addParam("userId",userId);

        userModel.addParam("userIcon",userIcon);

        userModel.setAction(UPDATE_USER_METHOD);

        return userModel;
    }


    @Override
    public void parseResult(String result){

        Log.d(TAG,result);

        Gson gson=new Gson();
        Result r= gson.fromJson(result, Result.class);
        if (r.getSuccess().equals("true")){
            MGlobal.getInstance().saveUserId(r.getResult());
            Log.d(TAG,"success");
        }
    }
}
