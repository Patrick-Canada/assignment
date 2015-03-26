package com.bowen.assignment.model;
import org.json.JSONObject;

/**
 * Created by patrick on 2015-03-26.
 */
public class GPSModel extends BaseModel {

    private final static String GPSURL="";

    public GPSModel(String url) {
        super(url);
    }


    /**
     * save gps info to server side
     * @param userId
     * @param gps
     * @return
     */
    public static GPSModel initGPS(String userId, String gps){

        GPSModel gpsModel=new GPSModel(GPSURL);
        return gpsModel;
    }

    @Override
    public void parseJSONObject(JSONObject jsonObject){


    }

}
