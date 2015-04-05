package com.bowen.assignment.model;
/**
 * Created by patrick on 2015-03-26.
 */
public class GPSModel extends BaseModel {

    private final static String SAVE_GPS_METHOD="saveGpsInfo";

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
        super.parseResult(result);
    }
}
