package com.bowen.assignment.fragment;
import android.app.Activity;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bowen.assignment.R;
import com.bowen.assignment.common.*;
import com.bowen.assignment.common.Error;
import com.bowen.assignment.model.BaseModel;
import com.bowen.assignment.model.GPSModel;
import com.bowen.assignment.model.ModelDelegate;
import com.bowen.assignment.vo.LocalServerVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-03-10.
 */
public class SendFragment extends Fragment implements ModelDelegate{

    private final static String TAG="SendFragment";

    private List<LocalServerVO> dataSource=new ArrayList<>();

    NsdManager.DiscoveryListener browser;

    NsdManager nsdManager;

    boolean discoveryStart;

    private ListView listView;

    private final static String SERVICE_TYPE="_ssh._tcp.";

    public void configBonjourBrowser(){

        final Activity activity=this.getActivity();

        browser=new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery stop failed: Error code:" + errorCode);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "Service discovery started");
                discoveryStart=true;
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {

                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {

                if (discoveryStart){
                    LocalServerVO serverVO=new LocalServerVO();
                    serverVO.setName(serviceInfo.getServiceName());
                    serverVO.setIpAddress(serviceInfo.getHost()+" port:"+serviceInfo.getPort());

                    dataSource.add(serverVO);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshTable();
                        }
                    });
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

                Log.e(TAG, "service lost" + serviceInfo.getServiceName());
            }
        };

        nsdManager = (NsdManager) getActivity().getSystemService(getActivity().NSD_SERVICE);
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, browser);

    }


    private void refreshTable(){

        ((SendAdapter)listView.getAdapter()).notifyDataSetChanged();
    }


    public void send(){

        MGlobal global=MGlobal.getInstance();
        if (global.getUserId().length()==0){

            MGlobal.getInstance().alert("please choice a user icon",this.getActivity());

            return;
        }
        GPSModel gpsModel=GPSModel.initGPS(global.getAddress(),global.getPort(),
                global.getUserId(),getGPSData());
        gpsModel.setDelegate(this);
        gpsModel.startLoad();

    }


    //to do
    public String getGPSData(){

        return "";
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_net_work,container,false);
        View backgroundView=view.findViewById(R.id.send_background_view);
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (discoveryStart){

                    nsdManager.stopServiceDiscovery(browser);
                    discoveryStart=false;
                }
                nsdManager=null;
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return false;
            }
        });
        listView=(ListView) view.findViewById(R.id.server_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              send();
            }
        });

        this.configBonjourBrowser();

        listView.setAdapter(new SendAdapter(this.getActivity(),dataSource));
        return view;
    }

    @Override
    public void didFinishLoad(BaseModel model) {
        MGlobal.getInstance().alert("send gps data success",this.getActivity());
    }

    @Override
    public void didLoadError(BaseModel model) {

        Error error=model.getError();
        MGlobal.getInstance().alert(error.getMessage(),this.getActivity());
    }


    public void onPause(){

        super.onPause();
    }



}
