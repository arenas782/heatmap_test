package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HeatDataEntry;
import com.anychart.charts.HeatMap;
import com.anychart.enums.SelectionMode;
import com.example.myapplication.models.heatMap;
import com.example.myapplication.network.ApiInterface;
import com.example.myapplication.network.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private RelativeLayout retryLayout;
    private Button retryButton;
    private ProgressDialog progressDialog;
    private Call<List<heatMap>> call;
    private AnyChartView anyChartView;
    private List<DataEntry> dataentry;
    private         HeatMap riskMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anyChartView = findViewById(R.id.any_chart_view);
        retryLayout = findViewById(R.id.retry_layout);
        apiInterface = RetrofitSingleton.getInstance().getUserService();
        String URL = "http://192.168.1.10:8090/heatmap";
        call = apiInterface.getData(URL);

        retryButton= findViewById(R.id.retry_button);
        progressDialog=new ProgressDialog(this);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        dataentry= new ArrayList<>();






        riskMap = AnyChart.heatMap();
        riskMap.stroke("1 #fff");


        riskMap.interactivity().selectionMode(SelectionMode.NONE);

        riskMap.title().enabled(true);
        riskMap.title()
                .text("Electrical Company HeatMap 2021")
                .padding(0d, 0d, 20d, 0d);


        riskMap.yAxis(0).stroke(null);
        riskMap.yAxis(0).labels().padding(0d, 15d, 0d, 0d);
        riskMap.yAxis(0).ticks(false);
        riskMap.xAxis(0).stroke(null);
        riskMap.xAxis(0).ticks(false);

        anyChartView.setChart(riskMap);


        loadData();






    }

    private void loadData(){
        progressDialog.setTitle("Cargando...");
        progressDialog.setMessage("Por favor espere mientras se consulta la información");
        progressDialog.show();

        anyChartView.setVisibility(View.INVISIBLE);
        retryLayout.setVisibility(View.GONE);
        call.clone().enqueue(new Callback<List<heatMap>>() {
            @Override
            public void onResponse(Call<List<heatMap>> call, Response<List<heatMap>> response) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.isSuccessful()){
                    List<heatMap> data;
                    retryLayout.setVisibility(View.GONE);
                    anyChartView.setVisibility(View.VISIBLE);

                    data=response.body();
                    for (int i = 0; i < data.size(); i++){
                        dataentry.add(new CustomHeatDataEntry(data.get(i).getX(), data.get(i).getY(), data.get(i).getValue()));
                        Log.d("data",""+data.get(i).toString());
                    }
                    riskMap.data(dataentry);
                }
            }

            @Override
            public void onFailure(Call<List<heatMap>> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                retryLayout.setVisibility(View.VISIBLE);
                anyChartView.setVisibility(View.INVISIBLE);
            }
        });

    }


    private class CustomHeatDataEntry extends HeatDataEntry {
        CustomHeatDataEntry(String x, String y, Integer heat) {
            super(x, y, heat);
            String color="";
            if(heat == 0)
                color=String.format("#%06x", ContextCompat.getColor(getApplicationContext(), R.color.blanco) & 0xffffff);
            if(heat >0 && heat <10)
                color=String.format("#%06x", ContextCompat.getColor(getApplicationContext(), R.color.verde) & 0xffffff);
            else if(heat >=10 && heat <30)
                color=String.format("#%06x", ContextCompat.getColor(getApplicationContext(), R.color.amarillo_claro) & 0xffffff);
            else if(heat >=30 && heat <70)
                color=String.format("#%06x", ContextCompat.getColor(getApplicationContext(), R.color.amarillo) & 0xffffff);
            else if (heat >=70)
                color=String.format("#%06x", ContextCompat.getColor(getApplicationContext(), R.color.rojo) & 0xffffff);
            setValue("fill", color);
        }
    }
}