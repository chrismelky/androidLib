package com.softalanta.wapi.registration.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.model.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2/6/18.
 */

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>
    implements Filterable{

    private List<Country> countryList;

    private List<Country> filteredCountryList;

    private OnItemClickListener onItemClickListener;

    public CountryListAdapter(List<Country> countries,OnItemClickListener onItemClickListener){
        this.countryList = countries;
        this.filteredCountryList = countries;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.country_list_layout, null);
       final CountryViewHolder viewHolder= new CountryViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(filteredCountryList.get(viewHolder.getAdapterPosition()));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = filteredCountryList.get(position);
        holder.countryName.setText(country.getName());
        holder.countryCode.setText("+".concat(Integer.toString(country.getCallingCode())));
    }

    @Override
    public int getItemCount() {
        return filteredCountryList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString().toLowerCase();
                if(searchString.isEmpty()){
                    filteredCountryList = countryList;
                }else {
                    List<Country> filtered = new ArrayList<>();
                    for (Country country : countryList) {
                        if(country.getName().toLowerCase().contains(searchString)){
                            filtered.add(country);
                        }
                    }
                    filteredCountryList = filtered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCountryList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCountryList = (ArrayList<Country>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder{
        public TextView countryName;
        public TextView countryCode;
        public CountryViewHolder(View v) {
            super(v);
            this.countryName = (TextView) v.findViewById(R.id.country_name);
            this.countryCode = (TextView) v.findViewById(R.id.country_code);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Country country);
    }

}
