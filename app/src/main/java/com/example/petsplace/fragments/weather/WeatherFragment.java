package com.example.petsplace.fragments.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petsplace.R;
import com.example.petsplace.geolocation.LocListener;
import com.example.petsplace.geolocation.LocListenerInterface;
import com.example.petsplace.service.GetWeatherService;
import com.example.petsplace.json.WeatherInformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherFragment extends Fragment implements LocListenerInterface {

    private static final String keyAPI = "7485f2a78f114b2b959152050222305";
    private static final String aqi = "no";
    private static String cityName = "Kiev";
    private LocationManager locationManager;
    private Location userLocation;
    private LocListener locListener;
    private TextView temperatureInYourRegion;
    private TextView yourCity;
    private Call<WeatherInformation> call;
    private Retrofit retrofit;
    private ProgressBar progressBar;
    private RelativeLayout rela;
    private TextView timeT;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_weather, container, false);
        init();


        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return view;
    }

    private void init(){
        timeT = view.findViewById(R.id.time);
        rela = view.findViewById(R.id.rela);
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        locListener = new LocListener();
        locListener.setLocListenerInterface(this);

        temperatureInYourRegion = view.findViewById(R.id.temperatureInYourRegion);
        yourCity = view.findViewById(R.id.yourCity);

        progressBar = view.findViewById(R.id.progress_bar);

        checkLocationPermission();
    }

    private void checkLocationPermission(){
        if (
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                && (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            //turnGPSOn(getActivity());
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,1000, locListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            checkLocationPermission();
        }
        else{
            Toast.makeText(getActivity(), "Необходимо дать разрешение", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.hasSpeed() || userLocation == null){
            userLocation = location;
            getCity();
        }
    }

    private void getCity(){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 10000);

            if (addresses != null) {
                String returnedAddress = String.valueOf(addresses.get(0));
                int indexStart = returnedAddress.indexOf("locality=");
                returnedAddress = returnedAddress.substring(indexStart+9);
                int indexEnd = returnedAddress.indexOf(",");
                returnedAddress = returnedAddress.substring(0,indexEnd);
                cityName = returnedAddress;
                getWeatherInformation();
            } else {
                yourCity.setText("Нет адреса. Быть может, вас не существует?");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            yourCity.setText("Не могу получить адрес! Проверьте подключение к планете Земля.");
        }
    }

    private void getWeatherInformation(){

        GetWeatherService service = retrofit.create(GetWeatherService.class);
        call = service.getPosts(keyAPI,cityName,aqi);

        call.clone().enqueue(new Callback<WeatherInformation>() {
            @Override
            public void onResponse(Call<WeatherInformation> call, Response<WeatherInformation> response) {

                WeatherInformation weatherInformation = response.body();

                progressBar.setVisibility(View.INVISIBLE);

                String time = weatherInformation.getLocation().getLocaltime();
                int index = time.indexOf(" ");
                time = time.substring(index);
                if (weatherInformation.getCurrent().getCondition().getText().contains("rain")){
                    timeT.setText(time);
                    yourCity.setText("Погода в вашем городе");
                    rela.setBackground(getActivity().getResources().getDrawable(R.drawable.rainy_day));
                    temperatureInYourRegion.setText(weatherInformation.getCurrent().getTemp_c()+"°C"+"\nДождливо");
                }
                else{

                    String[] re = time.split(":");
                    int t = Integer.parseInt(re[0].trim());
                    if (t < 6){
                        timeT.setText(time);
                        yourCity.setText("Погода в вашем городе");
                        rela.setBackground(getActivity().getResources().getDrawable(R.drawable.night));
                    }
                    else if (t > 6 && t < 12){
                        timeT.setText(time);
                        yourCity.setText("Погода в вашем городе");
                        rela.setBackground(getActivity().getResources().getDrawable(R.drawable.morning));
                    }
                    else if (t > 12 && t < 16){
                        timeT.setText(time);
                        yourCity.setText("Погода в вашем городе");
                        rela.setBackground(getActivity().getResources().getDrawable(R.drawable.day));
                    }
                    else if (t > 16 && t < 24){
                        timeT.setText(time);
                        yourCity.setText("Погода в вашем городе");
                        rela.setBackground(getActivity().getResources().getDrawable(R.drawable.evening));
                    }

                    if (weatherInformation.getCurrent().getCondition().getText().contains("cloudy")){
                        temperatureInYourRegion.setText(weatherInformation.getCurrent().getTemp_c()+"°C"+"\nПасмурно");
                    }
                    else{
                        temperatureInYourRegion.setText(weatherInformation.getCurrent().getTemp_c()+"°C"+"\nСолнечно");
                    }
                }
                //yourCity.setText("Погода в вашем городе");
            }

            @Override
            public void onFailure(Call<WeatherInformation> call, Throwable t) {

            }
        });
    }
}