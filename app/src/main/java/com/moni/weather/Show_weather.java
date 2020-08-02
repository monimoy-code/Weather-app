package com.moni.weather;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Show_weather extends AppCompatActivity {
    TextView city,temp,pres,hum,sun_r,sun_s,wind;
    String tempo;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        city = findViewById(R.id.textView4);
        temp =findViewById(R.id.textView5);
        pres = findViewById(R.id.textView11);
        hum = findViewById(R.id.textView12);
        sun_r = findViewById(R.id.textView14);
        sun_s = findViewById(R.id.textView15);
        wind = findViewById(R.id.textView13);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        tempo=intent.getStringExtra("city");
        city.setText(tempo);
        tempo = intent.getStringExtra("type");
        set_icon();
        tempo=intent.getStringExtra("temp");
        temp.setText(tempo+(char) 0x00B0+"C");
        tempo=intent.getStringExtra("pressure");
        pres.setText(tempo+"hPa");
        tempo=intent.getStringExtra("humidity");
        hum.setText(tempo);
        tempo=intent.getStringExtra("speed");
        wind.setText(tempo+"m/s");
        tempo=intent.getStringExtra("temp_max");
        sun_r.setText(tempo+(char) 0x00B0+"C");
        tempo=intent.getStringExtra("temp_min");
        sun_s.setText(tempo+(char) 0x00B0+"C");
    }
    public void set_icon()
    {
        if(tempo.equals("clear sky"))
        {
            imageView.setImageResource(R.drawable.ic_sun);
        }
        if(tempo.equals("few clouds"))
        {
            imageView.setImageResource(R.drawable.ic_suncloud);
        }
        if(tempo.equals("scattered clouds"))
        {
            imageView.setImageResource(R.drawable.ic_cloudy);
        }
        if(tempo.equals("broken clouds")||tempo.equals("overcast clouds"))
        {
            imageView.setImageResource(R.drawable.ic_cloudy);
        }
        if((tempo.equals("rain"))||(tempo.equals("light rain"))||tempo.equals("light intensity drizzle")||tempo.equals("drizzle")||tempo.equals("heavy intensity drizzle")||tempo.equals("light intensity drizzle rain")||tempo.equals("drizzle rain")||tempo.equals("heavy intensity drizzle rain")||tempo.equals("shower rain and drizzle")||tempo.equals("heavy shower rain and drizzle")||tempo.equals("shower drizzle")||tempo.equals("moderate rain"))
        {
            imageView.setImageResource(R.drawable.ic_lightrain);
        }
        if(tempo.equals("shower rain")||tempo.equals("heavy intensity rain")||tempo.equals("very heavy rain")||tempo.equals("extreme rain")||tempo.equals("freezing rain")||tempo.equals("light intensity shower rain")||tempo.equals("heavy intensity shower rain")||tempo.equals("ragged shower rain"))
        {
            imageView.setImageResource(R.drawable.ic_rain);
        }
        if(tempo.equals("thunderstorm")||tempo.equals("thunderstorm with light rain")||tempo.equals("thunderstorm with rain")||tempo.equals("thunderstorm with heavy rain")||tempo.equals("light thunderstorm")||tempo.equals("heavy thunderstorm")||tempo.equals("ragged thunderstorm")||tempo.equals("thunderstorm with light drizzle")||tempo.equals("thunderstorm with drizzle")||tempo.equals("thunderstorm with heavy drizzle"))
        {
            imageView.setImageResource(R.drawable.ic_lightning);
        }
        if(tempo.equals("snow")||tempo.equals("light snow")||tempo.equals("Heavy snow")||tempo.equals("Sleet")||tempo.equals("Light shower sleet")||tempo.equals("Shower sleet")||tempo.equals("Light rain and snow")||tempo.equals("Rain and snow")||tempo.equals("Light shower snow")||tempo.equals("Shower snow")||tempo.equals("Heavy shower snow"))
        {
            imageView.setImageResource(R.drawable.ic_snow);
        }
        if(tempo.equals("mist")||tempo.equals("Smoke")||tempo.equals("Haze")||tempo.equals("sand/ dust whirls")||tempo.equals("fog")||tempo.equals("sand")||tempo.equals("dust")||tempo.equals("volcanic ash")||tempo.equals("squalls")||tempo.equals("tornado"))
        {
            imageView.setImageResource(R.drawable.ic_mist);
        }
    }
}
