package let.it.be.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import let.it.be.weatherapp.models.CityData;

public class ForecastActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecat);

        ((TextView) findViewById(R.id.cityId)).setText("" + getIntent().getLongExtra(CityData.TAG, 0));
    }
}
