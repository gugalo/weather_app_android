package let.it.be.weatherapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import let.it.be.weatherapp.adapters.CityListWeatherAdapter;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView cityList;
    private CityListWeatherAdapter cityListAdapter;
    private WeatherLoadingFragment workerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.city_list_title);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityListAdapter = new CityListWeatherAdapter();
        cityList = (RecyclerView) findViewById(R.id.cityList);
        cityList.setLayoutManager(layoutManager);
        cityList.setAdapter(cityListAdapter);

        CurrentWeatherData weatherData = getIntent().getParcelableExtra(CurrentWeatherData.TAG);
        if (weatherData == null) {
            NetworkException error = getIntent().getParcelableExtra(NetworkException.TAG);
            weatherDataListener.onFailed(error);
            return;
        }

        weatherDataListener.onSuccess(weatherData);
    }

    private ResultListener<CurrentWeatherData> weatherDataListener = new ResultListener<CurrentWeatherData>() {
        @Override
        public void onSuccess(CurrentWeatherData result) {
            Log.e(TAG, "Current weather info loaded successfully");
            cityListAdapter.setItemsList(result.list);
            cityListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailed(NetworkException error) {
            Log.e(TAG, "Error loading current weather", error);
            // TODO: show error screen
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            // this means that this activity is not subject to orientation change but is destroyed
            // so we need to remove our worker thread/fragment here
            workerFragment.destroy();

            // other stuff to clean
//            cityListAdapter.setOnItemClickListener(null);
            cityList.setAdapter(null);
            cityList.setLayoutManager(null);
        }
    }
}
