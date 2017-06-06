package com.example.esmaeelnapil.rcudp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class getipaddress{ 
	
	public getipaddress(){
		getLocalIpAddress();
	}
public static String getLocalIpAddress()
	    {
	        try {
	            for (Enumeration<?> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = (NetworkInterface) en.nextElement();
	                for (Enumeration<?> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
	                        String ipAddress= inetAddress.getHostAddress();
	                        //System.out.println(ipAddress);
	                        return ipAddress;
	                    }
	                }
	            }
	        } catch (SocketException ex) {
	            ex.printStackTrace();
	        }
	        return null;
	    }
}