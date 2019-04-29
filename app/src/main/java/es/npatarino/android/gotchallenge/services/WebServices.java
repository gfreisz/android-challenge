package es.npatarino.android.gotchallenge.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.npatarino.android.gotchallenge.interfaces.IWebServicesEvents;

public class WebServices {
    private final String TAG = "WebServices";
    private final String URL = "https://project-8424324399725905479.firebaseio.com/characters.json?print=pretty";

    private static WebServices mInstance;
    private WebServices(){}
    public static WebServices getInstace(){
        if (mInstance == null){
            mInstance = new WebServices();
        }

        return mInstance;
    }

    public List<JsonObject> getData() throws IOException{
        URL obj = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Type listType = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        List<JsonObject> list = new Gson().fromJson(response.toString(), listType);

        return list;
    }

    public void getDataAsync(final IWebServicesEvents wsCallbacks){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    wsCallbacks.onGetDataFinishCallback(getData());
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    wsCallbacks.onGetDataErrorCallback(e.getLocalizedMessage());
                } catch (Exception ex){
                    Log.e(TAG, ex.getLocalizedMessage());
                    wsCallbacks.onGetDataErrorCallback(ex.getLocalizedMessage());
                }
            }
        }).start();
    }
}
