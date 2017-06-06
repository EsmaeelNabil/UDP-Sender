package com.example.esmaeelnapil.rcudp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private static final int UDP_SERVER_PORT = 8080;
    private static final int MAX_UDP_DATAGRAM_LEN = 8000;
    private TextView textMessage,textMessage2,textMessage3,textMessage4,localip;
    private RunServerInThread runServer = null;
    ImageButton btn,btn2,btn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        textMessage = (TextView) findViewById(R.id.textView1);
        textMessage2 = (TextView) findViewById(R.id.textView2);
        textMessage3= (TextView) findViewById(R.id.textView3);
        textMessage4 = (TextView) findViewById(R.id.textView4);
        btn=(ImageButton) findViewById(R.id.btn1);
        localip = (TextView) findViewById(R.id.localip);
        localip.setText("Local IP : "+getLocalIpAddress());


        btn2=(ImageButton)findViewById(R.id.imageButton4);
        try {
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mainone = new Intent();
                    Intent mainon = mainone.setClass(MainActivity.this, InfoActivity.class);
                    startActivityForResult(mainone, 1);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        btn3 = (ImageButton)findViewById(R.id.imageButton5);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainone = new Intent();
                Intent mainon = mainone.setClass(MainActivity.this, MyControler.class);
                startActivityForResult(mainone, 1);
            }
        });






        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessage.setText("Waiting ..");
                textMessage2.setText(" ");
                textMessage3.setText(" ");
                textMessage4.setText(" ");
                localip.setText("Local IP : "+getLocalIpAddress());
                try {
                    String Workonn;
                    Workonn = runServer.getLastMessage();
                    String[] splitStrr = Workonn.split("\\s+");

                    if (splitStrr[0] != "")
                    {
                        textMessage2.setText(splitStrr[0]+"°C");
                    }else {
                        textMessage2.setText(splitStrr[0]);
                    }

                    textMessage3.setText(splitStrr[1]);
                    textMessage4.setText(splitStrr[2]);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
        });

        runServer = new RunServerInThread();
        runServer.start();
    }


    private Runnable updateTextMessage = new Runnable() {
        public void run() {
            if (runServer == null) return;



            // do your substring here on the runServer.getLastMessage() :)

           try {
               String Workon;

               Workon = runServer.getLastMessage();

               String[] splitStr = Workon.split("\\s+");
               textMessage.setText(runServer.getLastMessage());
               textMessage2.setText(splitStr[0]+"°C");
               textMessage3.setText(splitStr[1]);
               textMessage4.setText(splitStr[2]);




           }catch (Exception e)
           {
               e.printStackTrace();

           }


           //textMessage.setText(runServer.getLastMessage());
        }
    };





    private void runUdpServer() {
        String message;
        byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
        DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(UDP_SERVER_PORT);
            socket.receive(packet);
            message = new String(lmessage, 0, packet.getLength());
            textMessage.setText(message);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

    }



    private class RunServerInThread extends Thread{
        private boolean keepRunning = true;
        private String lastmessage = "";

        @Override
        public void run() {
            String message;
            byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
            DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);

            DatagramSocket socket = null;

            try {
                socket = new DatagramSocket(UDP_SERVER_PORT);

                while(keepRunning) {
                    socket.receive(packet);
                    message = new String(lmessage, 0, packet.getLength());
                    lastmessage = message;
                    runOnUiThread(updateTextMessage);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if (socket != null) {
                socket.close();
            }

        }

        public String getLastMessage() {
            return lastmessage;
        }
    }

    public String getLocalIpAddress()
    {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress= inetAddress.getHostAddress();
                        Log.e("IP address",""+ipAddress);
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