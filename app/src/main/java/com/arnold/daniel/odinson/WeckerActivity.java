package com.arnold.daniel.odinson;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.Calendar;

import static com.arnold.daniel.odinson.R.id.timePicker;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;


public class WeckerActivity extends AppCompatActivity {
    private Socket socket_2;
    private static final String SERVER_IP = "192.168.178.22";
    private DrawerLayout mDrawer_layout;
    private ActionBarDrawerToggle mToggle;
    int hour;
    int minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wecker);

        refreshjob refresh_start = new refreshjob();
        refresh_start.execute();

     final  TimePicker Uhr = (TimePicker)findViewById(R.id.timePicker);
         Uhr.setIs24HourView(true);


        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer_layout, R.string.open, R.string.close);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Switch myswitch = (Switch) findViewById(R.id.switch1);
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked == true)
                {
                    Uhr.setVisibility(Uhr.VISIBLE);
                    System.out.println("zeige uhr");
                }
                if(isChecked==false)
                {
                    setausjob setaus = new  setausjob();
                    setaus.execute();
                    Uhr.setVisibility(Uhr.INVISIBLE);

                }

            }
        });

            final Button submit = (Button) findViewById(R.id.submit_button);
            submit.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View z)
                        {
                            submit();



                        }


                });









        NavigationView nv = (NavigationView)findViewById(R.id.nvView);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.nav_first_fragment):
                        Intent intentlicht = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intentlicht);
                        return true;
                    case (R.id.nav_second_fragment) :
                        Intent intent = new Intent(getApplicationContext(),WeckerActivity.class);
                        startActivity(intent);

                        return true;
                    default:
                        return true;


                }

            }

        });
    }
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
                System.out.println("wann klingelt eigentlich der wecker");
                refreshjob re_job = new refreshjob();
                re_job.execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class refreshjob extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] params) {
            String zustand = "0";
            try {
                socket_2 = new Socket(SERVER_IP, 51718);
                String str = "wann";
                PrintWriter out = new PrintWriter(socket_2.getOutputStream(), true);
                out.println(str);

                InputStream rein = socket_2.getInputStream();
                while (rein.available() == 0) {
                    rein = socket_2.getInputStream();
                    //System.out.println("verf\u00FCgbare Bytes: " + rein.available());

                }
                BufferedReader buff = new BufferedReader(new InputStreamReader(rein));
                while (buff.ready()) {
                    zustand = buff.readLine();
                    System.out.println(zustand);
                }

                socket_2.close();


            } catch (UnknownHostException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();
            }
            System.out.println("100ms pause:");
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println(ex);
            }


            return zustand;
        }

        protected void onPostExecute(String wann) {
            System.out.println("update den zustand auf dem Bildschrm");
            System.out.println(wann);
            updatezustand(wann);


        }
    }


   public void updatezustand (String uhrzeit)
    {
        Switch myswitch = (Switch) findViewById(R.id.switch1);
        if(uhrzeit.equals("aus"))
        {
            myswitch.setChecked(false);
            System.out.println("Der Wecker ist aus");
        }
        else {
            myswitch.setChecked(true);
            int time = parseInt(uhrzeit);
            System.out.println(time);
            int hour_update = time / 100;
            int minute_update = time % 100;
            System.out.println(hour_update);
            System.out.println(minute_update);
            final TimePicker Uhr = (TimePicker) findViewById(R.id.timePicker);
            Uhr.setIs24HourView(true);
            Uhr.setMinute(minute_update);
            Uhr.setHour(hour_update);
        }
    }

    private class setausjob extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] params) {
            String zustand = "0";
            try {
                socket_2 = new Socket(SERVER_IP, 51718);
                String str = "setaus";
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
            } catch (Exception ex) {
                System.out.println(ex);
            }


            return zustand;
        }

    }



    private class settimejob extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] params) {
            String zustand = "0";
            try {
                socket_2 = new Socket(SERVER_IP, 51718);

                String str = "set"+hour+":"+minute;
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
            } catch (Exception ex) {
                System.out.println(ex);
            }


            return zustand;
        }

    }


    void submit()
    {

        final  TimePicker Uhr = (TimePicker)findViewById(R.id.timePicker);
        Uhr.setIs24HourView(true);
        System.out.println("Time is:");
        hour = Uhr.getHour();
        minute = Uhr.getMinute();
        settimejob settime = new settimejob();
        settime.execute();
        Toast.makeText(this, "Wecker klingelt um "+hour+":"+minute, Toast.LENGTH_SHORT).show();

        System.out.print(hour);
        System.out.println(":");
        System.out.println(minute);
    }




}
