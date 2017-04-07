package com.arnold.daniel.odinson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;


import android.widget.TextView;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {

    private static final int SERVERPORT = 8000;
    private static final String SERVER_IP = "192.168.178.22";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   new Thread(new ClientThread()).start();

        final Button anButton = (Button) findViewById(R.id.button);
        anButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                connectjob job = new connectjob();
                job.execute();



            }


        });


    }

 /*class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                socket = new Socket(SERVER_IP, SERVERPORT);



            }
            catch (UnknownHostException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();
            }


        }


    }
*/

 private class connectjob extends AsyncTask<String, Void, String> {
     @Override
     protected String doInBackground(String[] params) {
         try {
             socket = new Socket(SERVER_IP, SERVERPORT);
             String str = "an";
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             out.println(str);
             socket.close();


         } catch (UnknownHostException e1) {

             e1.printStackTrace();

         }
         catch (IOException e1) {

             e1.printStackTrace();
         }


         return "test";
     }
 }


}