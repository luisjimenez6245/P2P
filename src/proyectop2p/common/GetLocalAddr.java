/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectop2p.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author luis
 */
public class GetLocalAddr {

    public static String getAddr(String networkInterfaceName) throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
        Enumeration<InetAddress> interfaces = networkInterface.getInetAddresses();
        while (interfaces.hasMoreElements()) {
            InetAddress i = (InetAddress) interfaces.nextElement();
            String addr = i.getHostName();
            if (addr != null && addr.contains("192")) {
                return i.getHostName();
            }
        }
        return null;
    }
}
