package com.bowen.assignment.model;

import android.util.Log;
import com.bowen.assignment.entity.Result;
import com.google.gson.Gson;

/**
 * Created by patrick on 2015-03-26.
 */
public class GPSModel extends BaseModel {

    private final static String TAG="GPSModel";

    private final static String SAVE_GPS_METHOD="saveGPSInfo";

    public GPSModel(String address, int port, String method){

        super(address,port,method);
    }

    /**
     * save gps info to server side
     * @param userId
     * @param gps
     * @return
     */
    public static GPSModel initGPS(String address,int port,String userId, String gps){

        GPSModel gpsModel=new GPSModel(address,port,SAVE_GPS_METHOD);
        gpsModel.addParam("userId",userId);
        gpsModel.addParam("gps",gps);
        gpsModel.setAction(SAVE_GPS_METHOD);

        return gpsModel;
    }

    @Override
    public void parseResult(String result){

        Log.d(TAG, result);
        Gson gson=new Gson();
        Result r= gson.fromJson(result, Result.class);
        if (this.getAction().equals(SAVE_GPS_METHOD)){
            if (r.getSuccess().equals("true")){
                Log.d(TAG,"save gps success");
            }
        }
    }
}
