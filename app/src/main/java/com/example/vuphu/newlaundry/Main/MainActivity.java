package com.example.vuphu.newlaundry.Main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vuphu.newlaundry.Main.Fragment.AccountFragment;
import com.example.vuphu.newlaundry.Main.Fragment.HomeFragment;
import com.example.vuphu.newlaundry.Main.Fragment.NotificationFragment;
import com.example.vuphu.newlaundry.Main.Fragment.OrderFragment;
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
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                   fragment = HomeFragment.newInstance();
                   break;
                case R.id.navigation_dashboard:
                    setTitle("Your Order");
                    fragment = OrderFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    setTitle("Notification");
                    fragment = NotificationFragment.newInstance();
                    break;
                case R.id.navigation_person:
                    setTitle("Your acount");
                    fragment = AccountFragment.newInstance();
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
        setTitle("Home");
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
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search_action);
        searchView.setMenuItem(item);
        return true;
    }

}
