package com.example.jorge.tadm_ejercicio1unidad3_jorgedanielrubio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity {

    String tiempo="";
    TextView resp;
   
    ListView lista;
    private String[] cord={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resp = findViewById(R.id.txtTemp);
        lista=  findViewById(R.id.listview);
        soli();
        arreglo();

    }

    public void arreglo(){

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cord);
        lista.setAdapter(adaptador);

    }
    public void soli(){

        String url="http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=f984a946569c92d056a2c65fe7a42e3f";
        new ReadJSONFeed().execute(url);
    }

    private class ReadJSONFeed extends AsyncTask<String, String, String> {

        protected void onPreExecute(){}
        @Override

        protected String doInBackground(String... urls){
            HttpClient httpclient=new DefaultHttpClient();
            StringBuilder builder=new StringBuilder();
            HttpPost  httppost= new HttpPost(urls[0]);

            try{

                HttpResponse response= httpclient.execute(httppost);
                StatusLine statusLine= response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode==200){
                    HttpEntity entity= response.getEntity();
                    InputStream content= entity.getContent();
                    BufferedReader reader= new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return builder.toString();

        }

        protected void onPostExecute(String resultado){

            tiempo= "Clima en Tepic es :  \n";
            cord = new String[6];

            try{
                JSONObject jsonObject = new JSONObject(resultado);

                // tiempo+="Codigo: "+ jsonObject.getString("id")+"\n";
                cord[0]="Codigo: "+ jsonObject.getString("id");

                JSONObject jcoordObject = new JSONObject(jsonObject.getString("coord"));

                //tiempo+="Longitud: "+ jcoordObject.getString("lon")+"\n";
                //tiempo+="Latitud: "+ jcoordObject.getString("lat")+"\n";
                cord[1]="Lon: "+ jcoordObject.getString("lon");
                cord[2]="Lat: "+ jcoordObject.getString("lat");

                JSONArray jsweatherObject = new JSONArray(jsonObject.getString("weather"));
                JSONObject jweatherObject= jsweatherObject.getJSONObject(0);
                //tiempo+="Nubes: "+ jweatherObject.getString("description")+"\n";
                //cord[3]="Nubes: "+ jweatherObject.getString("description");

                JSONObject jsmainObject= new JSONObject(jsonObject.getString("main"));
                //tiempo+="Humedad: "+ jsmainObject.getString("humidity")+"%\n";
                //tiempo+="Presion atmosferica: "+ jsmainObject.getString("pressure")+"hpa \n";
                cord[3]="Humidity: "+ jsmainObject.getString("humidity")+"%";
                cord[4]="temp: "+(Float.valueOf(jsmainObject.getString("temp"))-273)+"°";
                cord[5]="Pressure: "+ jsmainObject.getString("pressure")+" hpa";
                cord[6]="speed : "+ jsmainObject.getString("speed")+"mps";


                JSONObject jswindObject = new JSONObject(jsonObject.getString("wind"));
                //tiempo+="velocidad del viento : "+ jswindObject.getString("speed")+"mps \n";
                //cord[6]="velocidad del viento : "+ jswindObject.getString("speed")+"mps";
                JSONObject jstempObject= new JSONObject(jsonObject.getString("main"));
                tiempo+="humidity: "+ jstempObject.getString("humidity")+"%\n";
                tiempo+="temp: "+ jstempObject.getString("temp")+" \n";


            }catch (Exception e){
                e.printStackTrace();
            }
            arreglo();
            if(tiempo.trim().length()>0){
                resp.setText(tiempo);
            }else{
                resp.setText("no se encontro");
            }

        }


    }

}
