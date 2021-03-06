package let.it.be.weatherapp.network;

import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.models.weather.WeatherForecastData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public final class WeatherProvider {

    public static final String API_ICON_ENDPOINT = "http://openweathermap.org/img/w/%s.png";
    private static final String API_ENDPOINT = "https://api.openweathermap.org/";
    private static final String API_VERSION = "2.5";
    private static final String API_KEY = "35f423c78290666a4c44c24a2015fa7f";
    private static final String API_UNITS = "metric";
    private static final String API_CONST_ARGUMENTS = "units=" + API_UNITS + "&APPID=" + API_KEY;

    private static Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private WeatherProvider() {
    }

    static Call<CurrentWeatherData> requestCurrentWeather(String cityMapBounds) {
        return retrofit.create(CurrentWeatherService.class).getCurrentWeather(cityMapBounds);
    }

    static Call<WeatherForecastData> requestCityForecast(String cityId) {
        return retrofit.create(WeatherForecastService.class).getForecastForCity(cityId);
    }

    private interface WeatherForecastService {
        @GET("data/" + API_VERSION + "/forecast?" + API_CONST_ARGUMENTS)
        Call<WeatherForecastData> getForecastForCity(@Query("id") String cityId);
    }

    private interface CurrentWeatherService {
        @GET("data/" + API_VERSION + "/box/city?" + API_CONST_ARGUMENTS)
        Call<CurrentWeatherData> getCurrentWeather(@Query("bbox") String cityMapBounds);
    }
}
