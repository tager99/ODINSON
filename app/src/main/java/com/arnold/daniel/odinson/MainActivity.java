package com.arnold.daniel.odinson;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;


import android.widget.TextView;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {

    private static final int SERVERPORT = 8001;
    private static final String SERVER_IP = "192.168.178.22";
    private Socket socket;
    private Socket socket_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button anButton = (Button) findViewById(R.id.button);
        anButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                connectjob job = new connectjob();
                job.execute();
               // refreshjob refresh = new refreshjob();
                //refresh.execute()







            }


        });

        final Button refresh_button = (Button) findViewById(R.id.refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener()
        {
           public void onClick(View v)
           {
               refreshjob refresh = new refreshjob();
               refresh.execute();
           }
        });


    }

    private void updatezustand (String anaus)
    {

            System.out.println("Übermittelter Wert:");
            System.out.println(anaus);
             TextView Anzeige=(TextView) findViewById(R.id.zustand);
            Button anButton = (Button) findViewById(R.id.button);
            int gelb = Color.parseColor("#FFFF00");

            if (anaus.equals("2")==true)
            {
                System.out.println("ändere Text in Licht ist an");
                Anzeige.setText("Das Licht ist an");
                anButton.setBackgroundColor(gelb);
            }
            if (anaus.equals("1")==true)
            {
                System.out.println("ändere Text in Licht ist aus");
                Anzeige.setText("Das Licht ist aus");
                anButton.setBackgroundResource(android.R.drawable.btn_default);

            }


    }

    private void refresh (String test)
    {
        System.out.println("checke ob" );
        System.out.print(test);
        System.out.print(" gleich brefresh");
        if (test=="refresh") {
            System.out.println("aktualisiere");
              refreshjob refresh = new refreshjob();
              refresh.execute();
        }
    }






 private class connectjob extends AsyncTask<String, Void, String> {
     @Override
     protected String doInBackground(String[] params) {
         try {
             socket = new Socket(SERVER_IP, SERVERPORT);
             System.out.println("Verbindung 1 hergestellt");
             String str = "an";
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             out.println(str);
             out.flush();
             socket.close();
             System.out.println("Verbindung 1 getrennt");




         } catch (UnknownHostException e1) {

             e1.printStackTrace();

         }
         catch (IOException e1) {

             e1.printStackTrace();
         }


         return "refresh";
     }
     protected void onPostExecute(String test)
     {
         System.out.println("nach Background job 1 führe aktualisierung aus");
         refresh(test);

     }

 }



   private class refreshjob extends AsyncTask<String, Void, String> {
       @Override
       protected String doInBackground(String[] params) {
           String zustand = "0";
           try {
               socket_2= new Socket(SERVER_IP, 8002);
               String str = "refresh";
               PrintWriter out = new PrintWriter(socket_2.getOutputStream(), true);
               out.println(str);
               socket_2.close();


           } catch (UnknownHostException e1) {

               e1.printStackTrace();

           } catch (IOException e1) {

               e1.printStackTrace();
           }
            System.out.println("100ms pause:");
           try {
               Thread.sleep(200);
           } catch(Exception ex) {
               System.out.println(ex);
           }



            Socket socket_3 = null;
           System.out.println("erstelle neuen socket");
            try {
                socket_3 = new Socket("192.168.178.22", 51717);
                System.out.println("Verbindung 3 hergestellt");


                InputStream rein = socket_3.getInputStream();
                while(rein.available()==0)
                {
                    rein = socket_3.getInputStream();
                    System.out.println("verf\u00FCgbare Bytes: " + rein.available());

                }
                BufferedReader buff = new BufferedReader(new InputStreamReader(rein));
                while (buff.ready()) {
                    zustand = buff.readLine();
                    System.out.println(zustand);
                }









            } catch (UnknownHostException e) {
                System.out.println("Unknown Host...");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOProbleme...");
                e.printStackTrace();
            } finally {
                if (socket_3 != null)
                    try {
                        socket_3.close();
                        System.out.println("Socket geschlossen...");
                    } catch (IOException e) {
                        System.out.println("Socket nicht zu schliessen...");
                        e.printStackTrace();
                    }
            }



            return zustand;
        }
        protected void onPostExecute(String anaus)
        {
            System.out.println("update den zustand auf dem Bildschrm");
            System.out.println(anaus);
            updatezustand(anaus);
        }

//return "zustand"; //
       }
  // }//





}