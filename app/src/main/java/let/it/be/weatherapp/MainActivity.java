package let.it.be.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import let.it.be.weatherapp.adapters.CityListWeatherAdapter;
import let.it.be.weatherapp.adapters.RecycleViewItemClickedListener;
import let.it.be.weatherapp.models.CityData;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CityWeatherData;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;
import let.it.be.weatherapp.views.ErrorView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private enum State {
        IDLE(1), ERROR(2), ERROR_RETRY(3), UNKNOWN(0);
        public static final String TAG = State.class.getSimpleName();
        private final int code;

        State(int code) {
            this.code = code;
        }

        public static State parse(int code) {
            for (State state : State.values()) {
                if (state.code == code) return state;
            }
            return UNKNOWN;
        }
    }

    // states
    private State state = State.IDLE;
    private CurrentWeatherData currentWeatherData;
    private CityListWeatherAdapter cityListAdapter;
    private WeatherLoadingFragment workerFragment;
    private int favoriteCity;

    // views
    private RecyclerView cityList;
    private ErrorView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.city_list_title);

        errorView = (ErrorView) findViewById(R.id.errorView);
        favoriteCity = getIntent().getIntExtra(CityData.TAG, -1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityListAdapter = new CityListWeatherAdapter();
        cityList = (RecyclerView) findViewById(R.id.cityList);
        cityList.setLayoutManager(layoutManager);
        cityList.setAdapter(cityListAdapter);
        cityList.addOnItemTouchListener(createItemListener());

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
            return;
        }
        if (getIntent().hasExtra(NetworkException.TAG)) {
            NetworkException error = (NetworkException) getIntent().getSerializableExtra(NetworkException.TAG);
            weatherDataListener.onFailed(error);
            return;
        }

        CurrentWeatherData weatherData = getIntent().getParcelableExtra(CurrentWeatherData.TAG);
        weatherDataListener.onSuccess(weatherData);
    }

    private void restoreState(Bundle savedInstanceState) {
        State oldState = State.parse(savedInstanceState.getInt(State.TAG));
        favoriteCity = savedInstanceState.getInt(CityData.TAG);

        if (oldState == State.IDLE) {
            CurrentWeatherData weatherData = savedInstanceState.getParcelable(CurrentWeatherData.TAG);
            setWeatherInformation(weatherData);
            return;
        }
        showCritErrorMessage(R.string.network_error_message);
        if (oldState == State.ERROR_RETRY) {
            state = State.ERROR_RETRY;
            errorView.showProgress();
            workerFragment = getWeatherLoadingFragment();
        }
    }

    private ResultListener<CurrentWeatherData> weatherDataListener = new ResultListener<CurrentWeatherData>() {
        @Override
        public void onSuccess(CurrentWeatherData result) {
            Log.d(TAG, "Current weather info loaded successfully");
            setWeatherInformation(result);
        }

        @Override
        public void onFailed(NetworkException error) {
            Log.e(TAG, "Error loading current weather", error);
            state = State.ERROR;
            showCritErrorMessage(R.string.network_error_message);
        }
    };

    private void setWeatherInformation(CurrentWeatherData result) {
        currentWeatherData = result;
        if (result == null) {
            showCritErrorMessage(R.string.network_error_message);
            return;
        }
        state = State.IDLE;
        cityList.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        cityListAdapter.setItemsList(result.list);
        cityListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            // this means that this activity is not subject to orientation change but is destroyed
            // so we need to remove our worker thread/fragment here
            if (workerFragment != null) {
                workerFragment.destroy();
            }

            // other stuff to clean
            cityList.setAdapter(null);
            cityList.setLayoutManager(null);
        }

        // best place to save info is pause... =)
        getSharedPreferences(WeatherApp.TAG, Context.MODE_PRIVATE).edit()
                .putInt(CityData.TAG, favoriteCity)
                .apply();
    }

    private RecycleViewItemClickedListener createItemListener() {
        return new RecycleViewItemClickedListener(this, new RecycleViewItemClickedListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (cityListAdapter == null) return;
                CityWeatherData selectedItem = cityListAdapter.getItem(position);
                openForecastForCity(selectedItem);
                favoriteCity = selectedItem.id;
            }
        });
    }

    private void openForecastForCity(CityWeatherData cityWeatherDta) {
        Intent intent = new Intent(this, ForecastActivity.class);
        intent.putExtra(CityWeatherData.TAG, cityWeatherDta);
        startActivity(intent);
    }

    private WeatherLoadingFragment getWeatherLoadingFragment() {
        WeatherLoadingFragment weatherFragment = WeatherLoadingFragment.findFragment(this);
        if (weatherFragment == null) {
            weatherFragment = new WeatherLoadingFragment(this);
        }
        weatherFragment.setResultListener(weatherDataListener);
        return weatherFragment;
    }

    private void showCritErrorMessage(@StringRes int resid) {
        showCritErrorMessage(getResources().getString(resid));
    }

    private void showCritErrorMessage(String message) {
        errorView.setErrorMessage(message);
        errorView.setRertyButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = State.ERROR_RETRY;
                errorView.showProgress();
                if (workerFragment == null) {
                    workerFragment = getWeatherLoadingFragment();
                    workerFragment.startDataLoading();
                } else {
                    workerFragment.retry();
                }
            }
        });
        errorView.hideProgress();
        errorView.setVisibility(View.VISIBLE);
        cityList.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(State.TAG, state.code);
        outState.putParcelable(CurrentWeatherData.TAG, currentWeatherData);
        outState.putInt(CityData.TAG, favoriteCity);
    }
}
