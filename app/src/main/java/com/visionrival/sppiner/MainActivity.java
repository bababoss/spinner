package com.visionrival.sppiner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONObject;
import android.os.AsyncTask;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url="http://nextverp.herokuapp.com/spinner/";
        String question="Who is great man";
        String answer="The suresh is great man";
        new FetchUserInfo().execute(url, question,answer);

    }


    @Override
    public void onStart(){
        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first


    }


    @Override
    public void onPause(){

        super.onPause();

    }


    @Override
    public void onStop(){
        super.onStop();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public static class FetchUserInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed com the finally block.


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder result = new StringBuilder();

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at


                URL url = new URL(params[0]);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(7200000);
                urlConnection.setReadTimeout(7200000);


                urlConnection.setRequestMethod("POST");

                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.setUseCaches(false);
                //urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

//                //send data
                DataOutputStream wr = new DataOutputStream(
                        urlConnection.getOutputStream());

                String data = new JSONObject()
                        .put("question", params[1])
                        .put("answer",params[2]).toString();

                byte[] postData = data.getBytes();
                wr.write(postData);

                wr.flush();
                wr.close();


                int status = urlConnection.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                }

//


                forecastJsonStr = Integer.toString(status);
                 Log.d("SENDDATAAPP", "The response is: " + forecastJsonStr);

                return result.toString();


            } catch (Exception e) {
                e.printStackTrace();
                return null;
                // If the code didn't successfully get the weather data, there's no point com attemping// to parse it.
            }
//            } catch (JSONException e1) {
//                Log.e("JSON Error", "Error ", e1);
//                return null;
//            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        }


        @Override
        protected void onPostExecute(String s) {
            try {
                super.onPostExecute(s);



                 Log.i("json", s);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }



}
