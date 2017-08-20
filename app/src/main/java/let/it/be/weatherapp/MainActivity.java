package let.it.be.weatherapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import let.it.be.weatherapp.adapters.CityListWeatherAdapter;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;

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

    // views
    private RecyclerView cityList;
    private ViewGroup errorContainer;
    private View retryButton;
    private View retryProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.city_list_title);

        errorContainer = (ViewGroup) findViewById(R.id.errorContainer);
        retryButton = findViewById(R.id.errorRetryButton);
        retryProgress = findViewById(R.id.errorRetryProgress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityListAdapter = new CityListWeatherAdapter();
        cityList = (RecyclerView) findViewById(R.id.cityList);
        cityList.setLayoutManager(layoutManager);
        cityList.setAdapter(cityListAdapter);

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
        FragmentManager fm = getFragmentManager();
        State oldState = State.parse(savedInstanceState.getInt(State.TAG));

        if (oldState == State.IDLE) {
            CurrentWeatherData weatherData = savedInstanceState.getParcelable(CurrentWeatherData.TAG);
            setWeatherInformation(weatherData);
            return;
        }
        showCritErrorMessage(R.string.network_error_message);
        if (oldState == State.ERROR_RETRY) {
            state = State.ERROR_RETRY;
            retryButton.setVisibility(View.GONE);
            retryProgress.setVisibility(View.VISIBLE);
            workerFragment = WeatherLoadingFragment.newInstance(fm, weatherDataListener);
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
        cityList.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
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
//            cityListAdapter.setOnItemClickListener(null);
            cityList.setAdapter(null);
            cityList.setLayoutManager(null);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.errorRetryButton:
                    state = State.ERROR_RETRY;
                    retryButton.setVisibility(View.GONE);
                    retryProgress.setVisibility(View.VISIBLE);
                    if (workerFragment == null) {
                        FragmentManager fm = getFragmentManager();
                        workerFragment = WeatherLoadingFragment.newInstance(fm, weatherDataListener);
                    } else {
                        workerFragment.retry();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void showCritErrorMessage(@StringRes int resid) {
        showCritErrorMessage(getResources().getString(resid));
    }

    private void showCritErrorMessage(String message) {
        ((TextView) errorContainer.findViewById(R.id.errorMessageText)).setText(message);
        retryButton.setOnClickListener(clickListener);
        retryButton.setVisibility(View.VISIBLE);
        cityList.setVisibility(View.GONE);
        retryProgress.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(State.TAG, state.code);
        outState.putParcelable(CurrentWeatherData.TAG, currentWeatherData);
    }
}
