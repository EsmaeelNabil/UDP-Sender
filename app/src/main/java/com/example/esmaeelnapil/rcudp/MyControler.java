package com.example.esmaeelnapil.rcudp;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MyControler extends AppCompatActivity {

    boolean isPlay = false;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, startb, stopb;
    ImageButton backbutton,StartStop;
    TextView textViewState, textViewRx,iptxt;

    UdpClientHandler udpClientHandler;
    UdpClientThread udpClientThread;

    private ListView listViewIp;

    ArrayList<String> ipList;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);







        iptxt=(TextView)findViewById(R.id.iptxt);
        iptxt.setText("Your local IP is "+ getipaddress.getLocalIpAddress());
        StartStop=(ImageButton)findViewById(R.id.StartStopimageButton);
        StartStop.setBackgroundResource(R.drawable.off);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        textViewState = (TextView) findViewById(R.id.state);
        textViewRx = (TextView) findViewById(R.id.received);
        backbutton = (ImageButton) findViewById(R.id.imageButton6);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextAddress.setText("");
                editTextAddress.setHint("Insert IP address");
                editTextPort.setText("");
                editTextPort.setHint("Insert Port number");

             //   finish();
            }
        });

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        udpClientHandler = new UdpClientHandler(this);

        startb = (Button) findViewById(R.id.startbutton);
        stopb = (Button) findViewById(R.id.stopbutton);

        startb.setOnClickListener(new View.OnClickListener() { // start order
            @Override
            public void onClick(View v) {
                try {
                    udpClientThread = new UdpClientThread(
                            editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), "On", udpClientHandler);
                    udpClientThread.start();

                    buttonConnect.setEnabled(true);
                   // if ()
                    StartStop.setBackgroundResource(R.drawable.on);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stopb.setOnClickListener(new View.OnClickListener() { // stop order
            @Override
            public void onClick(View v) {
                try {
                    udpClientThread = new UdpClientThread(
                            editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), "Off", udpClientHandler);
                    udpClientThread.start();

                    buttonConnect.setEnabled(true);
                    StartStop.setBackgroundResource(R.drawable.off);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ////////////////
        listViewIp = (ListView) findViewById(R.id.listviewip);
        ipList = new ArrayList();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ipList)
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
            View view =super.getView(position, convertView, parent);

            TextView textView=(TextView) view.findViewById(android.R.id.text1);

                                                                                 /*YOUR CHOICE OF COLOR*/
            textView.setTextColor(Color.parseColor("#ffffff"));

            return view;
        }
        };
        listViewIp.setAdapter(adapter);

        listViewIp.setOnItemClickListener(new AdapterView.OnItemClickListener() { /////////////// ip click .. smile comment
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str=adapter.getItem(position).toString();
                String substr=str.substring(str.indexOf("1"));
                editTextAddress.setText(substr.toString()); // cut the string from specified index to the last .. smile comment
                editTextPort.setText("8889");
                Toast.makeText(getBaseContext(),str,Toast.LENGTH_LONG).show();
            }
        });


        StartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(isPlay){
                    v.setBackgroundResource(R.drawable.on);
                }else{
                    v.setBackgroundResource(R.drawable.off);
                }

                isPlay = !isPlay; // reverse
                */

            }

        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener() {                                // SCAN button

                @Override
                public void onClick(View arg0) {
                    try {

                        new ScanIpTask().execute();                      // Scan method
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            };

    private void updateState(String state) {
        textViewState.setText(state);

    }

    private void updateRxMsg(String rxmsg) {
        textViewRx.setText(rxmsg + "\n");
    }

    private void clientEnd() {
        udpClientThread = null;
        textViewState.setText("Sent .. ");
        buttonConnect.setEnabled(true);

    }

    public static class UdpClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MyControler parent;

        public UdpClientHandler(MyControler parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_STATE:
                    parent.updateState((String) msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String) msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
    private class ScanIpTask extends AsyncTask<Void, String, Void> {

        /*
        Scan IP 192.168.1.1~192.168.1.225
        you should try different timeout for your network/devices
         */
        static final String subnet = "192.168.1.";
        static final int lower = 1;
        static final int upper = 255;
        static final int timeout =8000;

        @Override
        protected void onPreExecute() {
            ipList.clear();
            adapter.notifyDataSetInvalidated();
            Toast.makeText(MyControler.this, "Scaning please Wait .. ", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (int i = lower; i <= upper; i++) {
                String host = subnet + i;

                try {
                    InetAddress inetAddress = InetAddress.getByName(host);
                    if (inetAddress.isReachable(timeout)){
                        publishProgress(inetAddress.toString());
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            ipList.add(values[0]);
            adapter.notifyDataSetInvalidated();
            Toast.makeText(MyControler.this, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MyControler.this, "Done", Toast.LENGTH_SHORT).show();
            buttonConnect.setText("Rescan");
        }
    }

}

