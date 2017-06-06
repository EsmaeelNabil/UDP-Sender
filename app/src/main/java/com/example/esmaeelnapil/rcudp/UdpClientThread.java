package com.example.esmaeelnapil.rcudp;

/**
 * Created by Esmaeel napil on 10/2/2016.
 */
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread{

    String dstAddress;
    String msg;
    int dstPort;
    private boolean running;
    MyControler.UdpClientHandler handler;

    DatagramSocket socket;
    InetAddress address;

    public UdpClientThread(String addr, int port,String mmsg, MyControler.UdpClientHandler handler) {
        super();
        msg=mmsg;
        dstAddress = addr;
        dstPort = port;
        this.handler = handler;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    private void sendState(String state){
        handler.sendMessage(
                Message.obtain(handler,
                        MyControler.UdpClientHandler.UPDATE_STATE, state));
    }

    @Override
    public void run() {
        sendState("connecting...");

        running = true;

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(dstAddress);


            // send request
            byte[] buf = new byte[256];
           // String message="hello";
            buf=msg.getBytes();

            DatagramPacket packet =
                    new DatagramPacket(buf, buf.length, address, dstPort);
            socket.send(packet);

            sendState("Data sent");

            // get response
            packet = new DatagramPacket(buf, buf.length);


            socket.receive(packet);
            String line = new String(packet.getData(), 0, packet.getLength());

            handler.sendMessage(
                    Message.obtain(handler, MyControler.UdpClientHandler.UPDATE_MSG, line));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                handler.sendEmptyMessage(MyControler.UdpClientHandler.UPDATE_END);
            }
        }

    }
}
