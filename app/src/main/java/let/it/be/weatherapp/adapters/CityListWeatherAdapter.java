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
import let.it.be.weatherapp.models.weather.CityWeatherData;

public class CityListWeatherAdapter extends RecyclerView.Adapter<CityListWeatherAdapter.ViewHolder> {

    private CityWeatherData[] items;
    private LayoutInflater inflater;

    public void setItemsList(CityWeatherData[] items) {
        this.items = items;
    }

    @Override
    public long getItemId(int position) {
        return items[position].id;
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

        View itemView = inflater.inflate(R.layout.list_city_item, parent, false);
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
        private int itemId;
        private String iconUrl;
        private TextView cityName;
        private TextView temperature;
        private ImageView weatherIcon;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cityName = (TextView) itemView.findViewById(R.id.cityName);
            this.temperature = (TextView) itemView.findViewById(R.id.temperature);
            this.weatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
        }

        private void bindView(@NonNull CityWeatherData cityWeatherData) {
            this.itemId = cityWeatherData.id;
            this.iconUrl = cityWeatherData.getWeatherIconUrl();
            this.cityName.setText(cityWeatherData.name);
            this.temperature.setText(cityWeatherData.main.getTempFormated());

            // I don't manually detect and stop process of image display when view holder
            // data is changed, because Universal Image Loading library (UILL for short) checks
            // weather view hash has changed or not before applying loaded bitmap.

            // There is no public method  to stop UILL image loading thread, so it's possible that
            // short living memory leaks will appear, but images should be also cached on disc and
            // there is not that many different ones. So this kind of app should be effected in
            // any way by this.

            // You can check my settings for UILL initialization in #WeatherApp class
            ImageLoader.getInstance().displayImage(iconUrl, weatherIcon);
        }
    }
}
