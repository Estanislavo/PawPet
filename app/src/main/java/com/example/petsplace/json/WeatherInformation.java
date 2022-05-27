package com.example.petsplace.json;

public class WeatherInformation {
    Location location;
    Current current;

    String wind_mph;
    String wind_kph;
    String wind_degree;
    String wind_dir;
    String pressure_mb;
    String pressure_in;
    String precip_mm;
    String precip_in;
    String humidity;
    String cloud;
    float feelslike_c;
    float feelslike_f;
    String vis_km;
    String vis_miles;
    String uv;
    String gust_mph;
    String gust_kph;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public String getWind_mph() {
        return wind_mph;
    }

    public String getWind_kph() {
        return wind_kph;
    }

    public String getWind_degree() {
        return wind_degree;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public String getPressure_mb() {
        return pressure_mb;
    }

    public String getPressure_in() {
        return pressure_in;
    }

    public String getPrecip_mm() {
        return precip_mm;
    }

    public String getPrecip_in() {
        return precip_in;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getCloud() {
        return cloud;
    }

    public float getFeelslike_c() {
        return feelslike_c;
    }

    public float getFeelslike_f() {
        return feelslike_f;
    }

    public String getVis_km() {
        return vis_km;
    }

    public String getVis_miles() {
        return vis_miles;
    }

    public String getUv() {
        return uv;
    }

    public String getGust_mph() {
        return gust_mph;
    }

    public String getGust_kph() {
        return gust_kph;
    }
}
