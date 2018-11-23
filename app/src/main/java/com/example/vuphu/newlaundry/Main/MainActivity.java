package com.example.vuphu.newlaundry.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Graphql.Services;
import com.example.vuphu.newlaundry.Main.Fragment.AccountFragment;
import com.example.vuphu.newlaundry.Main.Fragment.HomeFragment;
import com.example.vuphu.newlaundry.Main.Fragment.NotificationFragment;
import com.example.vuphu.newlaundry.Main.Fragment.OrderFragment;
import com.example.vuphu.newlaundry.Order.Activity.BagActivity;
import com.example.vuphu.newlaundry.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity {



    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            toolbar.getMenu().findItem(R.id.menu_read_action).setVisible(true);
            toolbar.getMenu().findItem(R.id.menu_search_action).setVisible(true);
            toolbar.getMenu().findItem(R.id.menu_bag_action).setVisible(true);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle(R.string.title_home);
                    toolbar.getMenu().findItem(R.id.menu_read_action).setVisible(false);
                   fragment = HomeFragment.newInstance();
                   break;
                case R.id.navigation_dashboard:
                    setTitle(R.string.yout_order);
                    toolbar.getMenu().findItem(R.id.menu_read_action).setVisible(false);
                    fragment = OrderFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    setTitle(R.string.notification);
                    toolbar.getMenu().findItem(R.id.menu_search_action).setVisible(false);
                    toolbar.getMenu().findItem(R.id.menu_bag_action).setVisible(false);
                    fragment = NotificationFragment.newInstance();
                    break;
                case R.id.navigation_person:
                    setTitle(R.string.title_person);
                    fragment = AccountFragment.newInstance();
                    toolbar.getMenu().findItem(R.id.menu_read_action).setVisible(false);
                    toolbar.getMenu().findItem(R.id.menu_search_action).setVisible(false);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.message,fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.message,HomeFragment.newInstance()).commit();
        setTitle(R.string.title_home);
        initToolbar();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        Log.i("PRE_TOKEN",PreferenceUtil.getAuthToken(getApplicationContext()));


    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search_action);
        menu.findItem(R.id.menu_read_action).setVisible(false);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_bag_action){
            startActivity(new Intent(getApplicationContext(), BagActivity.class));
        }
        return true;
    }
}
