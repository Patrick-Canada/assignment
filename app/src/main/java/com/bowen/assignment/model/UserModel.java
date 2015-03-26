package com.bowen.assignment.model;

import com.bowen.assignment.common.MConstant;

import org.json.JSONObject;

/**
 * Created by patrick on 2015-03-26.
 */
public class UserModel extends BaseModel{

    public static final String USER_URL="";

    public UserModel(String url) {
        super(url);
    }

    public static UserModel initUser(String userIcon){

        UserModel userModel=new UserModel(USER_URL);

        userModel.setMethod(MConstant.REST_POST);

        userModel.setAction("initUser");

        userModel.getRequestParams().put("userIcon",userIcon);

        return userModel;
    }


    public static UserModel updateUser(String userId,String userIcon){


        UserModel userModel=new UserModel(USER_URL);

        userModel.setMethod(MConstant.REST_POST);

        userModel.setAction("updateUser");

        userModel.getRequestParams().put("userId",userId);

        userModel.getRequestParams().put("userIcon",userIcon);

        return userModel;

    }


    @Override
    public void parseJSONObject(JSONObject jsonObject){



    }
}
