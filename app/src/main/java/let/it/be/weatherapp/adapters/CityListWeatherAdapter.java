package let.it.be.weatherapp.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import let.it.be.weatherapp.R;
import let.it.be.weatherapp.models.weather.CityForecastData;
import let.it.be.weatherapp.models.weather.CityWeatherData;

public class CityListWeatherAdapter implements ListAdapter {

    private CityWeatherData[] items;
    private LayoutInflater inflater;

    public CityListWeatherAdapter(CityWeatherData[] items) {
        this.items = items;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public CityWeatherData getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return items[position].id;
    }

    public View createEmptyLayout(ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return inflater.inflate(R.layout.list_city_item, parent, false);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityWeatherData itemData = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = createEmptyLayout(parent);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (itemData.id != viewHolder.itemId) {
            viewHolder.bindView(itemData);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return items == null || items.length == 0;
    }

    private static class ViewHolder {
        public int itemId;
        public TextView cityName;
        public TextView temperature;
        public ImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {
            this.cityName = (TextView) itemView.findViewById(R.id.cityName);
            this.temperature = (TextView) itemView.findViewById(R.id.temperature);
            this.weatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
        }

        public void bindView(@NonNull CityWeatherData cityWeatherData) {
            this.itemId = cityWeatherData.id;
            this.cityName.setText(cityWeatherData.name);
            this.temperature.setText(cityWeatherData.main.getTempFormated());
        }
    }
}
