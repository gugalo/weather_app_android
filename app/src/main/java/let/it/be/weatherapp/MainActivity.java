package let.it.be.weatherapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import let.it.be.weatherapp.adapters.CityListWeatherAdapter;
import let.it.be.weatherapp.models.exceptions.NetworkException;
import let.it.be.weatherapp.models.weather.CurrentWeatherData;
import let.it.be.weatherapp.network.ResultListener;
import let.it.be.weatherapp.network.WeatherLoadingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String WEATHER_LOADING_FRAGMENT_TAG = "MainActivityRetainedFragment";

    private RecyclerView cityList;
    private CityListWeatherAdapter cityListAdapter;
    private WeatherLoadingFragment workerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.city_list_title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cityListAdapter = new CityListWeatherAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityList = (RecyclerView) findViewById(R.id.cityList);
        cityList.setLayoutManager(layoutManager);
        cityList.setAdapter(cityListAdapter);

        FragmentManager fm = getFragmentManager();
        workerFragment = (WeatherLoadingFragment) fm.findFragmentByTag(WEATHER_LOADING_FRAGMENT_TAG);

        if (workerFragment == null) {
            workerFragment = new WeatherLoadingFragment();
            workerFragment.setListener(weatherDataListener);
            workerFragment.loadDataAsync();
            fm.beginTransaction().add(workerFragment, WEATHER_LOADING_FRAGMENT_TAG).commit();
        } else {
            CurrentWeatherData weatherData = workerFragment.getCurrentWeatherData();
            if (weatherData != null) {
                cityListAdapter.setItemsList(weatherData.list);
            } else {
                workerFragment.setListener(weatherDataListener);
            }
        }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isFinishing()) {
            // this means that this activity is not subject to orientation change but is destroyed
            // so we need to remove our worker thread/fragment here
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().remove(workerFragment).commit();

            // other stuff to clean
//            cityListAdapter.setOnItemClickListener(null);
            cityList.setAdapter(null);
            cityList.setLayoutManager(null);
        }
    }
}
