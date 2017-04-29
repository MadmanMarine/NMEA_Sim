/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nmeasimulator;
import java.net.*;
import java.util.*;

/**
 *
 * @author User
 */
public class NetInfo {

    //public String ActiveNetName = null;
    /**
     * @throws java.net.SocketException
     */
    public void GetNetInfo() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        MainWindow.WiFiConnectionFound = false;
        MainWindow.ActiveNetName = "(Not Found)";
        for (NetworkInterface netint : Collections.list(nets)) {
            displayInterfaceInformation(netint);
        }
        //return (ActiveNetName);
    }

    void displayInterfaceInformation(NetworkInterface netint) throws SocketException {

        boolean NetIsUp = (netint.isUp());
        String LastActiveNetName = null;
        InetAddress LastActivePcIpAddress = null;
        String NetName = (netint.getName());
        String NetDispName = (netint.getDisplayName());
        String ActiveIpAddress = null;
        //InetAddress MinIp = InetAddress.getByName("0.0.0.0");
        //InetAddress MaxIp = InetAddress.getByName("255.255.255.255");

        if ((NetName.contains("wlan")) && NetIsUp) {

            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress NetIpAddress : Collections.list(inetAddresses)) {
                //out.printf("InetAddress: %s\n", NetIpAddress);
                if ((NetIpAddress.toString()).length() < 18) {
                    LastActivePcIpAddress = NetIpAddress;
                }
            }
            //System.out.println("Net is Up? " + NetIsUp);//, netint.isUp());
            //out.printf("Loopback? %s\n", netint.isLoopback());
            //out.printf("PointToPoint? %s\n", netint.isPointToPoint());
            //out.printf("Supports multicast? %s\n", netint.supportsMulticast());
            //out.printf("Virtual? %s\n", netint.isVirtual());
            //out.printf("Hardware address: %s\n",
            //Arrays.toString(netint.getHardwareAddress()));
            //out.printf("MTU: %s\n", netint.getMTU());
            //out.printf("\n");
            //LastActiveNetName = NetName;
            MainWindow.ActiveNetName = NetName;
            MainWindow.ActivePcIpAddress = LastActivePcIpAddress;
            MainWindow.ActiveNetDisplayName = NetDispName;
            MainWindow.WiFiConnectionFound = true;
            //System.out.println("Display name: " + NetDispName); //,netint.getDisplayName());
            //System.out.println("Net name: " + NetName); //,netint.getDisplayName());
            //System.out.println("Net is Up? " + NetIsUp);//, netint.isUp());

        } //System.out.println("WLAN Active Net Name:" + LastActiveNetName);

    }

}
