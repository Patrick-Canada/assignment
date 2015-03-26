package com.bowen.assignment.fragment;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bowen.assignment.R;
import com.bowen.assignment.vo.LocalServerVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-03-10.
 */
public class SendFragment extends Fragment {

    private final static String TAG="SendFragment";

    private View contentView;

    private List<LocalServerVO> dataSource=new ArrayList<>();

    NsdManager.DiscoveryListener browser;

    NsdManager nsdManager;

    private final static String SERVICE_TYPE="_http._tcp.";

    public void configBonjourBrowser(){

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
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {

                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {

                LocalServerVO serverVO=new LocalServerVO();
                serverVO.setName(serviceInfo.getServiceName());
                serverVO.setIpAddress(serviceInfo.getHost()+" port:"+serviceInfo.getPort());

                dataSource.add(serverVO);
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

                Log.e(TAG, "service lost" + serviceInfo.getServiceName());
            }
        };

        nsdManager = (NsdManager) getActivity().getSystemService(getActivity().NSD_SERVICE);
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, browser);

    }


    public void showUserFragment(){
        UserFragment fragment=new UserFragment();
        FragmentTransaction fragmentTransaction=getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(contentView.getId(),
                fragment, getString(R.string.user_fragment_tag));
        fragmentTransaction.addToBackStack(getString(R.string.user_fragment_tag));
        fragmentTransaction.commit();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_net_work,container,false);
        contentView=view.findViewById(R.id.send_fragment);
        View backgroundView=view.findViewById(R.id.send_background_view);
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getActivity().getSupportFragmentManager().popBackStack();
                return false;
            }
        });
        ListView listView=(ListView) view.findViewById(R.id.server_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUserFragment();
            }
        });

        this.configBonjourBrowser();

        listView.setAdapter(new SendAdapter(this.getActivity(),dataSource));
        return view;
    }

}
