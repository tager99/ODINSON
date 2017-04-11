package com.arnold.daniel.odinson;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Menu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

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


    private DrawerLayout mDrawer_layout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer_layout, R.string.open, R.string.close);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);










        final Button anButton = (Button) findViewById(R.id.button);
        anButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                connectjob job = new connectjob();
                job.execute();
               // refreshjob refresh = new refreshjob();
                //refresh.execute()







            }


        });




         NavigationView nv = (NavigationView)findViewById(R.id.nvView);
         nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 switch (item.getItemId()) {
                     case (R.id.nav_first_fragment):
                         return true;
                     case (R.id.nav_second_fragment) :
                         return true;
                     default:
                         return true;


                 }

             }

         });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(mToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshjob refresh = new refreshjob();
                refresh.execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                anButton.setText("Licht AN");
            }
            if (anaus.equals("1")==true)
            {
                System.out.println("ändere Text in Licht ist aus");
                Anzeige.setText("Das Licht ist aus");
                anButton.setBackgroundResource(android.R.drawable.btn_default);
                anButton.setText("Licht AUS");

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
               Thread.sleep(100);
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