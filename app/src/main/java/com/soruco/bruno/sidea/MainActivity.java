package com.soruco.bruno.sidea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakLineChart;
import com.macroyau.thingspeakandroid.model.ChannelFeed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;

import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Direccion del tutorial para hacer el ViewPager:
    // https://www.youtube.com/watch?v=Rf7CMSPTReU&t=336s
    // Adaptador de secciones para el ViewPager
    private SectionsPagerAdapter mSectionsPagerAdapter;
    // Variable de tipo ViewPager para el deslizamiento
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Con esto le devuelvo el tema original al mainactivity,
        // le habia sacado para el splash screen
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicio Servicio MQTT
        startService(new Intent(this,ServiceMQTT.class));
        //finish();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Se crea el adaptador de secciones que devolverá un fragmento para cada
        // una de las tres secciones principales de la actividad.
        // Enlace al adaptador
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Se configura el ViewPager con el adaptador de secciones.
        // Enlace al ViewPager
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static void GraficoThingSpeak(int channelId, int fieldId, View view){
        ThingSpeakChannel tsChannel;
        ThingSpeakLineChart tsChart;
        final LineChartView chartView;
        // Connect to ThinkSpeak Channel channelId de la cuenta nanocrax
        tsChannel = new ThingSpeakChannel(channelId);
        // Set listener for Channel feed update events
        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
            @Override
            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
                // Show Channel ID and name on the Action Bar
//                getSupportActionBar().setTitle(channelName);
//                getSupportActionBar().setSubtitle("Channel " + channelId);
                // Notify last update time of the Channel feed through a Toast message
//                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
//                Toast.makeText(MainActivity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
            }
        });
        // Fetch the specific Channel feed
        tsChannel.loadChannelFeed();

        // Crear un objeto de calendario
        Calendar calendar = Calendar.getInstance(); // fecha actual
//        calendar.add(Calendar.DAY_OF_YEAR, -1); // le resto dias
        calendar.add(Calendar.MINUTE, -5);
        // Muestro en un toast el inicio de fecha del grafico
//        Toast.makeText(MainActivity.this, "Inicio: "+ calendar.getTime().toString(), Toast.LENGTH_LONG).show();

        // Configure LineChartView
        chartView = view.findViewById(R.id.chart);
        //chartView.setZoomEnabled(false);
        chartView.setZoomEnabled(true);
        chartView.setScrollEnabled(true);
        chartView.setValueSelectionEnabled(true);

        // Create a line chart from Field1 of ThinkSpeak Channel channelId
        tsChart = new ThingSpeakLineChart(channelId, fieldId);
        // Get 200 entries at maximum - Obtenga 200 entradas como máximo
        tsChart.setNumberOfEntries(200);
        // Set value axis labels on 10-unit interval
        // Establecer etiquetas de eje Y, cada cuantas unidades me mostrara una etiqueta
        //tsChart.setValueAxisLabelInterval((float) 0.1);
        tsChart.setValueAxisLabelInterval(5);
        // Set date axis labels on 5-minute interval
        // Establecer etiquetas de eje X, cada cuantas unidades me mostrara una etiqueta
        //tsChart.setDateAxisLabelInterval(15);
        tsChart.setDateAxisLabelInterval(1);
        // Show the line as a cubic spline
        tsChart.useSpline(true);
        // Set the line color
        tsChart.setLineColor(Color.parseColor("#D32F2F"));
        // Set the axis color
        tsChart.setAxisColor(Color.parseColor("#455a64"));
        // Establezca la fecha de inicio (establecida en calendar) para la vista predeterminada del gráfico
        tsChart.setChartStartDate(calendar.getTime());
        //calendar.add(Calendar.DAY_OF_YEAR, 1);
        // Establezca la fecha de fin del grafico
        //tsChart.setChartEndDate(calendar.getTime());
        // Set listener for chart data update
        tsChart.setXAxisName("Tiempo");
        tsChart.setYAxisName("PPM");
        tsChart.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData, Viewport maxViewport, Viewport initialViewport) {
                // Set chart data to the LineChartView
                chartView.setLineChartData(lineChartData);
                // Set scrolling bounds of the chart
                chartView.setMaximumViewport(maxViewport);
                // Set the initial chart bounds
                chartView.setCurrentViewport(initialViewport);
            }
        });
        // Load chart data asynchronously
        tsChart.loadChartData();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_emergencia) {
            Intent intent = new Intent(MainActivity.this, ListaContactos.class);
            startActivity(intent);

        } else if (id == R.id.nav_utiles) {
            Intent intent = new Intent(MainActivity.this, ListaContactosUtiles.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(MainActivity.this, Info.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Aqui construyo los fragmentos para cada vista del Viewpager para cada sensor
     */
    public static class FragmentMQ2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public FragmentMQ2 () {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentMQ2 newInstance(int sectionNumber) {
            FragmentMQ2 fragment = new FragmentMQ2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mq2, container, false);

            // fieldId 2 le corresponde a MQ2
            GraficoThingSpeak(195472, 2, rootView);
            return rootView;
        }
    }

    public static class FragmentMQ5 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public FragmentMQ5 () {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentMQ5 newInstance(int sectionNumber) {
            FragmentMQ5 fragment = new FragmentMQ5 ();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mq5, container, false);

            // fieldId 3 le corresponde a MQ5
            GraficoThingSpeak(195472, 3, rootView);
            return rootView;
        }
    }

    public static class FragmentMQ7 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public FragmentMQ7 () {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentMQ7 newInstance(int sectionNumber) {
            FragmentMQ7 fragment = new FragmentMQ7();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mq7, container, false);

            // fieldId 4 le corresponde a MQ7
            GraficoThingSpeak(195472, 4, rootView);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *  ADAPATADOR PARA EL VIEWPAGER
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        // Constructor por defecto
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
                return FragmentMQ2.newInstance(position + 1);
            }

            if (position == 1) {
                return FragmentMQ5.newInstance(position + 1);
            }else {
                return FragmentMQ7.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public  CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "MQ 2";
                case 1:
                    return "MQ 5";
                case 2:
                    return "MQ 7";
            }
            return null;
        }
    }
}
