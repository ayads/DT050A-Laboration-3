package se.miun.distsys.clients;

import java.net.InetAddress;

/**
 * A class that defines each 
 * client in the network.
 */

public class Client {
    public InetAddress ipAddress;
    public int port;
    private int ID;

    public Client(InetAddress ipAddress, int port, int ID) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.ID = ID;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public int getID() {
        return ID;
    }
}