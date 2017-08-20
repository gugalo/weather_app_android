package let.it.be.weatherapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;

/**
 * This activity has setup in styles 'passive' splash screen that wait for app
 * initialization by system (recommended by android patterns). After 'passive' splash screen it
 * displayes 'active' splash screen that waits for in app settings to load.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private WeatherLoadingFragment workerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getFragmentManager();
        workerFragment =  WeatherLoadingFragment.newInstance(fragmentManager, onSettingsLoaded);
    }

    private ResultListener<CurrentWeatherData> onSettingsLoaded = new ResultListener<CurrentWeatherData>() {
        @Override
        public void onSuccess(CurrentWeatherData result) {
            gotToNextActivity(result, null);
        }

        @Override
        public void onFailed(NetworkException error) {
            gotToNextActivity(null, error);
        }
    };

    private void gotToNextActivity(CurrentWeatherData result, NetworkException error){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if (result != null) intent.putExtra(CurrentWeatherData.TAG, result);
        if (error != null) intent.putExtra(NetworkException.TAG, error);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (workerFragment != null) {
            // this will stop loading process and kill fragment
            workerFragment.destroy();
        }
        super.onBackPressed();
    }
}
