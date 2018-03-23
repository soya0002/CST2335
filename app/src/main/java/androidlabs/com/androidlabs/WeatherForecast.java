package androidlabs.com.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast extends Activity {
    private ProgressBar pBWeather;
    private ImageView ivWeather;
    private TextView tvCurrentTemperature,tvMinTemperature,tvMaxTemperature,tvWindSpeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        pBWeather = (ProgressBar) findViewById(R.id.pBWeather);
        ivWeather = (ImageView) findViewById(R.id.ivWeather);
        tvCurrentTemperature = (TextView) findViewById(R.id.tvCurrentTemperature);
        tvMinTemperature = (TextView) findViewById(R.id.tvMinTemperature);
        tvMaxTemperature = (TextView) findViewById(R.id.tvMaxTemperature);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);

        pBWeather.setVisibility(View.VISIBLE);
        new ForecastQuery().execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");

    }
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    public class ForecastQuery extends AsyncTask<String,Integer,String> {
        private String windspeed, min, max, currentTemperature,icon;

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            InputStream stream = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                stream = conn.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(stream, null);
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "current");
                while(parser.next() != XmlPullParser.END_DOCUMENT){
                    try {
                        if(parser.getName()!=null) {
                            String tag = parser.getName();
                            if (tag.equals("temperature") && tag != null) {
                                if(getAttributeValue(parser,"min")) {
                                    min = parser.getAttributeValue(null, "min");
                                    publishProgress(25);
                                    sleep();
                                }
                                if(getAttributeValue(parser,"max")) {
                                    max = parser.getAttributeValue(null, "max");
                                    publishProgress(50);
                                    sleep();
                                }
                                if(getAttributeValue(parser,"value")) {
                                    currentTemperature = parser.getAttributeValue(null, "value");
                                    Log.e("Min", min);
                                    Log.e("Max", max);
                                    Log.e("Temperature", currentTemperature);
                                    publishProgress(75);
                                    sleep();
                                }

                            } else if (tag.equals("speed") && tag != null) {
                                if(getAttributeValue(parser,"value")) {
                                    windspeed = parser.getAttributeValue(null, "value");
                                    Log.e("Speed", windspeed);
                                }
                            }
                            else if(tag.equals("weather") && tag != null){
                                icon = parser.getAttributeValue(null,"icon");
                                Log.e("Icon", icon);
                                HttpURLConnection connection = null;
                                try {
                                    connection = (HttpURLConnection) new URL("http://openweathermap.org/img/w/" + icon + ".png").openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        if(fileExistance( icon + ".png")){
                                            Log.i("File : " + icon + ".png", " Found");
                                            FileInputStream fis = null;
                                            try {    fis = openFileInput(icon + ".png");   }
                                            catch (FileNotFoundException e) {    e.printStackTrace();  }
                                            Bitmap bm = BitmapFactory.decodeStream(fis);
                                            ivWeather.setImageBitmap(bm);
                                        }
                                        else {
                                            Log.i("File : " + icon + ".png", " Not Found");
                                            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                                            FileOutputStream outputStream = openFileOutput( icon + ".png", Context.MODE_PRIVATE);
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                            outputStream.flush();
                                            outputStream.close();
                                            ivWeather.setImageBitmap(bitmap);
                                        }
                                        publishProgress(100);
                                        sleep();
                                    } else
                                        return null;

                                } catch (Exception e) {
                                    return null;
                                } finally {
                                    if (connection != null) {
                                        connection.disconnect();
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            return "";
        }
        public boolean getAttributeValue(XmlPullParser parser,String key){
            return (parser.getAttributeValue(null, key)==null?false:true);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            pBWeather.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            tvCurrentTemperature.setText("Temperature : " + currentTemperature);
            tvMinTemperature.setText("Min : " + min);
            tvMaxTemperature.setText("Max : " + max);
            tvWindSpeed.setText("Wind Speed: " + windspeed);
            pBWeather.setVisibility(View.INVISIBLE);
        }
        public void sleep(){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
