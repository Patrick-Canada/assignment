package com.bowen.assignment.vo;
import android.net.nsd.NsdServiceInfo;

/**
 * Created by patrick on 2015-03-10.
 */
public class LocalServerVO {

    private String name;

    private String ipAddress;

    private int port;


    private NsdServiceInfo serviceInfo;

    public NsdServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(NsdServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
