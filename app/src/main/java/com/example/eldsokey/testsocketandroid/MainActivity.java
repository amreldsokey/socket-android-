package com.example.eldsokey.testsocketandroid;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView messageET , ipaddTv , portTV;

    ServerSocket server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageET = findViewById(R.id.ET_message);
        ipaddTv = findViewById(R.id.TV_ip);
        portTV = findViewById(R.id.TV_port);

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        //-------------- get wifi ip address --------------------//
        @SuppressLint("WifiManagerLeak") WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        Log.e("wifi**" , ipAddress);
        ipaddTv.setText(ipAddress);
        //////////////////////////////////////////////////////////


        //------------------- open ports ------------------------//
//        DatagramSocket ds = new DatagramSocket(port);
    }

  class MyServerThread implements Runnable
  {
      Socket s;
      ServerSocket ss;// = new ServerSocket(7801);
      InputStreamReader isr;
      BufferedReader bufferedReader;
      String message;
      Handler handler = new Handler();


      @Override
      public void run() {

          try {
              ss = new ServerSocket(8000);
              Log.e( "portNum**"  , ""+ss.getLocalPort());
              portTV.setText(""+ss.getLocalPort());
              while ((true))
              {
//                  //s =  new Socket("127.0.0.1" , 8000);
               //   Log.e("amr**" , "before accept");
                  s =  ss.accept();
                 // Log.e("amr**" , "after accept");
                  isr = new InputStreamReader(s.getInputStream());
                  bufferedReader = new BufferedReader(isr);
                  message = bufferedReader.readLine();
                  handler.post(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(getApplicationContext() , message , Toast.LENGTH_LONG).show();
                          Log.e("message**" , message);
                          messageET.setText(message);
                      }
                  });

              }
          }catch (IOException e)
          {
              Log.e("serverError**" , e.getMessage());
          }

      }
  }

  public void send(View v)
  {

  }
}
