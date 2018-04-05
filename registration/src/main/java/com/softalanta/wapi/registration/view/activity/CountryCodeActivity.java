package com.softalanta.wapi.registration.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.R2;
import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.view.adapters.CountryListAdapter;
import com.softalanta.wapi.registration.view.viewmodel.CountryCodeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryCodeActivity extends AppCompatActivity {

    private CountryCodeViewModel countryCodeViewModel;

    private CountryListAdapter countryListAdapter;

    private CountryListAdapter.OnItemClickListener itemClickListener;

    private List<Country> countries;

    @BindView(R2.id.country_code_list_view) RecyclerView countryCodeListView;

    @BindView(R2.id.toolbar) Toolbar toolbar;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_code_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        countryCodeListView.setLayoutManager(mLayoutManager);
        //Add list item divider line
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(countryCodeListView.getContext(),DividerItemDecoration.VERTICAL);
        countryCodeListView.addItemDecoration(dividerItemDecoration);
        countryCodeListView.setItemAnimator(new DefaultItemAnimator());

        setItemClickListener();
        this.countries = new ArrayList<>();
        countryListAdapter = new CountryListAdapter(this.countries,this.itemClickListener);
        countryCodeListView.setAdapter(countryListAdapter);

        /**
         * Initialize ViewModel
         */
        countryCodeViewModel = ViewModelProviders.of(this).get(CountryCodeViewModel.class);
        setObservers();
    }

    private void setItemClickListener(){
        this.itemClickListener = new CountryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Country country) {
                if(country != null){
                    setResult(country);
                }
            }
        };
    }

    private void setResult(Country country){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SELECTED_COUNTRY",country);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    private void setObservers(){

        Observer<List<Country>> countryListObserver = new Observer<List<Country>>() {
            @Override
            public void onChanged(@Nullable List<Country> countries) {
            setCountryList(countries);
            }
        };
        countryCodeViewModel.loadCountries(this).observe(this,countryListObserver);
    }

    private void setCountryList(List<Country> countries){
        this.countries.clear();
        this.countries.addAll(countries);
        countryListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                countryListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                countryListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
        }
        super.onBackPressed();
    }
}
