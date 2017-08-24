package let.it.be.weatherapp;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import let.it.be.weatherapp.adapters.ForecastListAdapter;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CityWeatherData;
import let.it.be.weatherapp.models.weather.WeatherForecastData;
import let.it.be.weatherapp.network.ForecastLoadingFragment;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.views.ErrorView;

public class ForecastActivity extends AppCompatActivity {

    private static final String TAG = ForecastActivity.class.getSimpleName();

    private ForecastLoadingFragment workerFragment;
    private CityWeatherData cityCurrentWeather;
    private ActionBar actionBar;
    private RecyclerView forecastsList;
    private ForecastListAdapter forecastAdapter;
    private ErrorView errorView;

    private ResultListener<WeatherForecastData> forecastDataListener = new ResultListener<WeatherForecastData>() {
        @Override
        public void onSuccess(WeatherForecastData result) {
            Log.d(TAG, "Forecast info loaded successfully");
            hideProgress();
            errorView.setVisibility(View.GONE);
            setForecastInfo(result);
        }

        @Override
        public void onFailed(NetworkException error) {
            Log.e(TAG, "Error loading forecast data", error);
            hideProgress();
            showCritErrorMessage(R.string.network_error_message);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        errorView = (ErrorView) findViewById(R.id.errorView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        forecastAdapter = new ForecastListAdapter();
        forecastsList = (RecyclerView) findViewById(R.id.forecastView);
        forecastsList.setLayoutManager(layoutManager);
        forecastsList.setAdapter(forecastAdapter);

        cityCurrentWeather = getIntent().getParcelableExtra(CityWeatherData.TAG);
        updateCurrentWeatherInfo(cityCurrentWeather);

        workerFragment = getForecastLoadingFragment(cityCurrentWeather.id);
        if (savedInstanceState == null) {
            workerFragment.startDataLoading();
            showProgress();
        } else {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateCurrentWeatherInfo(CityWeatherData weatherData) {
        getSupportActionBar().setTitle(weatherData.name);
        setTextViewValue(R.id.temp, "%2.1f°", weatherData.main.temp);
        setTextViewValue(R.id.tempMax, R.string.temp_max_value_label, weatherData.main.tempMax);
        setTextViewValue(R.id.tempMin, R.string.temp_min_value_label, weatherData.main.tempMin);
        setTextViewValue(R.id.wind, R.string.wind_value_label, weatherData.wind.deg, weatherData.wind.speed);
        setTextViewValue(R.id.pressure, R.string.pressure_value_label, weatherData.main.pressure);
        setTextViewValue(R.id.humidity, R.string.humidity_value_label, weatherData.main.humidity);

        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        ImageLoader.getInstance().displayImage(weatherData.weather[0].getWeatherIconUrl(), weatherIcon);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (workerFragment.isLoading()) {
            // work in progress, just wait for callbacks to fire
            showProgress();
            return;
        }
        if (workerFragment.getSavedResult() != null) {
            hideProgress();
            setForecastInfo(workerFragment.getSavedResult());
            return;
        }
        if (workerFragment.getSavedError() != null) {
            hideProgress();
            showCritErrorMessage(R.string.network_error_message);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private ForecastLoadingFragment getForecastLoadingFragment(int cityId) {
        ForecastLoadingFragment forecastFragment = ForecastLoadingFragment.findFragment(this);
        if (forecastFragment == null) {
            forecastFragment = new ForecastLoadingFragment(this, cityId);
        }
        forecastFragment.setResultListener(forecastDataListener);
        return forecastFragment;
    }

    private void setForecastInfo(WeatherForecastData result) {
        forecastAdapter.setItemsList(result.list);
        forecastAdapter.notifyDataSetChanged();
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
            forecastsList.setAdapter(null);
            forecastsList.setLayoutManager(null);
        }
    }

    public void showProgress() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        forecastsList.setVisibility(View.GONE);
    }

    public void hideProgress() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        forecastsList.setVisibility(View.VISIBLE);
    }

    public void setTextViewValue(@IdRes int viewId, @StringRes int stringId, Object... args) {
        setTextViewValue(viewId, (stringId > 0 ? getString(stringId) : null), args);
    }

    public void setTextViewValue(@IdRes int viewId, String text, Object... args) {
        if (args != null && text != null) {
            text = String.format(text, args);
        } else if (args != null) {
            text = String.valueOf(args[0]);
        }
        ((TextView) findViewById(viewId)).setText(text);
    }

    private void showCritErrorMessage(@StringRes int resid) {
        showCritErrorMessage(getResources().getString(resid));
    }

    private void showCritErrorMessage(String message) {
        errorView.setRertyButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorView.showProgress();
                workerFragment.retry();
            }
        });
        errorView.setErrorMessage(message);
        errorView.hideProgress();
        errorView.setVisibility(View.VISIBLE);
        forecastsList.setVisibility(View.GONE);
    }
}
