package com.softalanta.wapi.registration.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.model.Country;

import java.util.List;

/**
 * Created by chris on 2/6/18.
 */

public class CountryListAdapter extends ArrayAdapter<Country> {

    public CountryListAdapter(@NonNull Context context, @NonNull List<Country> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Country country= getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.country_list_layout, parent, false);
        }
        TextView countryName=(TextView)convertView.findViewById(R.id.country_name);
        ImageView countryIcon =(ImageView)convertView.findViewById(R.id.country_icon);

       // countryIcon.setImageResource(country.getIcon());
        countryName.setText(country.getName());
        return convertView;
    }
}
