package let.it.be.weatherapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import let.it.be.weatherapp.R;
import let.it.be.weatherapp.models.weather.CityForecastData;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder> {

    private CityForecastData[] items;
    private LayoutInflater inflater;

    public void setItemsList(CityForecastData[] items) {
        this.items = items;
    }

    @Override
    public long getItemId(int position) {
        return RecyclerView.NO_ID;
    }

    public CityForecastData getItem(int position) {
        return items[position];
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        View itemView = inflater.inflate(R.layout.list_forecast_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(items[position]);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayOfWeekLabel;
        private TextView dateLabel;
        private ImageView weatherIcon;
        private TextView descriptionLabel;
        private TextView temperatureLabel;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dayOfWeekLabel = (TextView) itemView.findViewById(R.id.dayOfWeek);
            this.dateLabel = (TextView) itemView.findViewById(R.id.time);
            this.weatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
            this.descriptionLabel = (TextView) itemView.findViewById(R.id.description);
            this.temperatureLabel = (TextView) itemView.findViewById(R.id.temperature);
        }

        private void bindView(@NonNull CityForecastData forecastData) {
            this.dayOfWeekLabel.setText(String.format("%ta", forecastData.dt * 1000));
            this.dateLabel.setText(String.format("%tR", forecastData.dt * 1000));
            this.descriptionLabel.setText(forecastData.weather[0].description);
            this.temperatureLabel.setText(String.format("%.0f°/%.0f°", forecastData.main.tempMin, forecastData.main.tempMax));

            // I don't manually detect and stop process of image display when view holder
            // data is changed, because Universal Image Loading library (UILL for short) checks
            // weather view hash has changed or not before applying loaded bitmap.

            // There is no public method  to stop UILL image loading thread, so it's possible that
            // short living memory leaks will appear, but images should be also cached on disc and
            // there is not that many different ones. So this kind of app should be effected in
            // any way by this.

            // You can check my settings for UILL initialization in #WeatherApp class
            String iconUrl = forecastData.weather[0].getWeatherIconUrl();
            ImageLoader.getInstance().displayImage(iconUrl, weatherIcon);
        }
    }
}
