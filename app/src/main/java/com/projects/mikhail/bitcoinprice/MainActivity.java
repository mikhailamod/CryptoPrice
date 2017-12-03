package com.projects.mikhail.bitcoinprice;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Attributes
    ProgressBar progressBar;
    TextView textview_price;
    TextView textview_coin;
    TextView textview_graph_title;
    TextView textview_graph_info;
    Spinner spinner;

    Button button_refresh;
    Button button_one_week;
    Button button_two_weeks;
    Button button_one_month;
    Button button_two_months;
    Button button_select_currency;

    static String SOURCE_URL = "https://api.coinmarketcap.com/v1/ticker/";
    static String WEEK_URL = "https://min-api.cryptocompare.com/data/histoday?";
    boolean rand;
    LineChart mainGraph;
    ArrayList<Entry> graphEntries;//List of entries for graph
    ImageView logo;

    Cryptocurrency[] cryptocurrencyList = {new Cryptocurrency("Bitcoin", "bitcoin", "btc"), new Cryptocurrency("Ethereum", "ethereum", "eth"), new Cryptocurrency("OmiseGO", "omisego", "omg"),
                                            new Cryptocurrency("Bitcoin Cash", "bitcoin-cash", "bch"), new Cryptocurrency("Power Ledger", "power-ledger", "powr")};
    Cryptocurrency activeCryptocurrency;//the currently selected crypto
    int numberOfDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rand = false;
        numberOfDays = 7;//the number of days for the graph to display

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        spinner = (Spinner)findViewById(R.id.spinner);

        button_refresh = (Button)findViewById(R.id.button_refresh);
        button_select_currency = (Button)findViewById(R.id.button_currency);
        button_one_week = (Button)findViewById(R.id.day_seven);
        button_two_weeks = (Button)findViewById(R.id.days_fourteen);
        button_one_month= (Button)findViewById(R.id.days_thirty);
        button_two_months = (Button)findViewById(R.id.days_sixty);
        button_one_week.setBackgroundColor(Color.parseColor("#b5ffff"));

        textview_coin = (TextView)findViewById(R.id.textview_coin);
        textview_coin.setText("");
        textview_price = (TextView)findViewById(R.id.textview_price);
        textview_price.setText("");
        textview_graph_title = (TextView)findViewById(R.id.graph_title);
        textview_graph_title.setText("");
        textview_graph_info = (TextView)findViewById(R.id.graph_info);
        textview_graph_info.setText("Last 7 Days");

        //Set on click listeners for buttons
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performTask(false);
            }
        });
        button_select_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rand)
                {
                    button_select_currency.setBackgroundColor(Color.parseColor("#b5ffff"));
                    button_select_currency.setTextColor(Color.BLACK);
                    rand = true;
                    performTask(false);
                }
                else
                {
                    button_select_currency.setTextColor(Color.WHITE);
                    button_select_currency.setBackgroundColor(Color.parseColor("#42a5f5"));
                    rand = false;
                    performTask(false);
                }
            }
        });
        button_one_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_one_week.setBackgroundColor(Color.parseColor("#b5ffff"));
                button_two_weeks.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_one_month.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_two_months.setBackgroundColor(Color.parseColor("#80d6ff"));
                textview_graph_info.setText("Last 7 Days");
                numberOfDays = 7;
                new RetrieveHistory(numberOfDays).execute(activeCryptocurrency);
            }
        });
        button_two_weeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_two_weeks.setBackgroundColor(Color.parseColor("#b5ffff"));
                button_one_week.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_one_month.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_two_months.setBackgroundColor(Color.parseColor("#80d6ff"));
                textview_graph_info.setText("Last 14 Days");
                numberOfDays = 14;
                new RetrieveHistory(numberOfDays).execute(activeCryptocurrency);
            }
        });
        button_one_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_one_month.setBackgroundColor(Color.parseColor("#b5ffff"));
                button_two_weeks.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_one_week.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_two_months.setBackgroundColor(Color.parseColor("#80d6ff"));
                textview_graph_info.setText("Last 30 Days");
                numberOfDays = 30;
                new RetrieveHistory(numberOfDays).execute(activeCryptocurrency);
            }
        });
        button_two_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_two_months.setBackgroundColor(Color.parseColor("#b5ffff"));
                button_two_weeks.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_one_month.setBackgroundColor(Color.parseColor("#80d6ff"));
                button_one_week.setBackgroundColor(Color.parseColor("#80d6ff"));
                textview_graph_info.setText("Last 60 Days");
                numberOfDays = 60;
                new RetrieveHistory(numberOfDays).execute(activeCryptocurrency);
            }
        });

        mainGraph = (LineChart) findViewById(R.id.main_graph);
        graphEntries = new ArrayList<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cryptos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        activeCryptocurrency = cryptocurrencyList[0];

        logo = (ImageView)findViewById(R.id.imageView_logo);
        logo.setImageResource(getLogoID(activeCryptocurrency));

        spinner.setOnItemSelectedListener(this);
    }

    //convert cryptocurrency id into the string format of the logo files, then return int of id
    public int getLogoID(Cryptocurrency cryptocurrency)
    {
        String logoID = "logo_" + cryptocurrency.getId();
        logoID = logoID.replace("-", "_");
        int resID = getResources().getIdentifier(logoID, "drawable", getPackageName());
        return resID;
    }

    //Refresh price, and grahp is parameter is true
    public void performTask(boolean graph)
    {
        textview_coin.setText("");
        textview_price.setText("");
        new RetrieveCurrentPrice().execute(activeCryptocurrency);
        logo.setImageResource(getLogoID(activeCryptocurrency));
        if(graph) {
            new RetrieveHistory(numberOfDays).execute(activeCryptocurrency);
        }
    }

    //given a set of values and timestamps, populate the LineChart
    public void populateGraph(float[] values, long[] timestamps)
    {
        String[] xAxisValues = new String[numberOfDays];//The array to hold the axis labels

        GregorianCalendar gc = new GregorianCalendar();//Calendar needed to convert epoch timestamps to date
        graphEntries = new ArrayList<>();

        //Add entries to mainGraph
        for(int i=0;i<values.length;i++)
        {
            gc.setTimeInMillis(timestamps[i]*1000);// multiply by 1000 bc Java uses milliseconds, API uses seconds
            int day = gc.get(Calendar.DAY_OF_MONTH);
            int month =  gc.get(Calendar.MONTH) + 1;
            xAxisValues[i] = day + "/" + month;//Add date to list
            graphEntries.add(new Entry(i, values[i]));
        }

        XAxisFormatter formatter = new XAxisFormatter(xAxisValues);
        //Set axis values and format
        XAxis xAxis = mainGraph.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        YAxis leftYAxis = mainGraph.getAxisLeft();
        YAxis rightYAxis = mainGraph.getAxisRight();
        leftYAxis.setTextColor(Color.WHITE);
        rightYAxis.setTextColor(Color.WHITE);

        LineDataSet dataSet = new LineDataSet(graphEntries, null);
        dataSet.setLineWidth(2);
        dataSet.setColor(Color.parseColor("#80d6ff"));
        dataSet.setValueTextSize(8);
        dataSet.setCircleRadius(5);
        dataSet.setCircleColor(Color.parseColor("#26c6da"));

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);
        mainGraph.setData(lineData);
        mainGraph.setDescription(null);

        Legend legend = mainGraph.getLegend();
        legend.setEnabled(false);

        mainGraph.setAutoScaleMinMaxEnabled(true);
        //mainGraph.setMaxVisibleValueCount(7);

        //Update graph
        mainGraph.animateXY(1000,1000);
        mainGraph.notifyDataSetChanged();
        mainGraph.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString().toLowerCase();
        label = label.replaceAll(" ", "-");
        //System.out.println("DEBUG----------label-: " + label);
        for(int i = 0; i< cryptocurrencyList.length; i++)
        {
            if(label.equals(cryptocurrencyList[i].getId()))
            {
                activeCryptocurrency = cryptocurrencyList[i];
                //System.out.println("DEBUG----------asas-: " + activeCryptocurrency.getId());
            }
        }
        performTask(true);//set graph to chosen crypto
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class RetrieveCurrentPrice extends AsyncTask<Cryptocurrency, Void, String>
    {

        private Exception exception;

        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            textview_price.setText("");
        }

        @Override
        protected String doInBackground(Cryptocurrency... params) {
            String coin = params[0].getId();
            String urlString = SOURCE_URL + coin + "?convert=ZAR";
            try{
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line=br.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    br.close();
                    return stringBuilder.toString();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR", e.getMessage(),e);
                return null;
            }
        }//end doInBackground

        protected void onPostExecute(String response)
        {
            if(response == null)
            {
                response = "MAJOR ERROR";
            }
            CryptoCurrentPrice ccp = new CryptoCurrentPrice(response);
            String price;
            if(rand) { price = "R" + ccp.getPriceZAR();}
            else { price = "$" + ccp.getPrice();}
            progressBar.setVisibility(View.GONE);
            textview_coin.setText(ccp.getName());
            textview_price.setText(price);
        }
    }//end inner class

    class RetrieveHistory extends AsyncTask<Cryptocurrency, Void, String>
    {
        private Exception exception;
        private Cryptocurrency cryptocurrency;
        private int numDays;

        public RetrieveHistory(int numDays)
        {
            this.numDays = numDays;
        }

        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            textview_graph_title.setText(activeCryptocurrency.getName() + " Price Graph");
        }

        @Override
        protected String doInBackground(Cryptocurrency... params) {
            cryptocurrency = params[0];
            String coinSymbol = "fsym=" + cryptocurrency.getSymbol().toUpperCase();
            String toCurrency = "tsym=USD";
            long now = System.currentTimeMillis();
            String time = "toTs=" + now;
            String urlString = WEEK_URL + coinSymbol + "&" + toCurrency + "&limit=60" + "&" + time;
            try{
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line=br.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    br.close();
                    return stringBuilder.toString();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR", e.getMessage(),e);
                return null;
            }
        }//end doInBackground

        protected void onPostExecute(String response)
        {
            CryptoPriceHistory ch = new CryptoPriceHistory(response, cryptocurrency, 60);
            float[] values = ch.getPrices(numDays);
            long[] timestamps = ch.getTimestamps(numDays);
            progressBar.setVisibility(View.GONE);
            textview_graph_title.setText(ch.getCryptocurrency().getName() + " Price Graph");
            populateGraph(values, timestamps);
        }
    }
}
