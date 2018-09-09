package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vuphu.luandry.Categories.OBCategory;
import com.example.vuphu.luandry.Order.ApdapterList.ListChipAdapter;
import com.example.vuphu.luandry.Order.ApdapterList.ListOrderDetailAdapter;
import com.example.vuphu.luandry.Order.OBOrderDetail;
import com.example.vuphu.luandry.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class PrepareOrderActivity extends AppCompatActivity {

    private RecyclerView listPrepareOrder;
    private ListOrderDetailAdapter adapter;
    private List<OBOrderDetail> orderDetailList = new ArrayList<>();
    private List<OBOrderDetail> orderDetailFilterList = new ArrayList<>();
    private List<OBOrderDetail> orderDetailSelectedList = new ArrayList<>();
    private List<OBCategory> categoryList = new ArrayList<>();
    private List<String> tagList = new ArrayList<>();
    private RecyclerView listFilter;
    private ListChipAdapter listChipAdapter;
    private FloatingActionButton floatingActionButton;

    private Toolbar toolbar;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_order);

        initToolbar();
        init();
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Choose your clothes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        listPrepareOrder = findViewById(R.id.prepare_order_list_category);
        listPrepareOrder.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listPrepareOrder.setLayoutManager(linearLayoutManager);
        orderDetailList = new ArrayList<>();
        prepareList();
        orderDetailFilterList = orderDetailList;
        adapter = new ListOrderDetailAdapter(this, orderDetailFilterList);
        listPrepareOrder.setAdapter(adapter);
        listPrepareOrder.invalidate();
        /*floatingActionButton = findViewById(R.id.prepare_order_next);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PrepareOrderAddressActivity.class));
            }
        });*/
        //Tag Filter
        prepareCategory();
        listFilter = findViewById(R.id.list_chip);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listFilter.setLayoutManager(staggeredGridLayoutManager);
        listChipAdapter = new ListChipAdapter(tagList, getApplicationContext());
        listFilter.setAdapter(listChipAdapter);

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


    private void prepareList() {

        for (int i = 0; i < 10; i++) {
            OBOrderDetail detail = new OBOrderDetail();
            detail.setTitle("Product " + i);
            detail.setPricing("200" + i);
            if (i % 2 == 0)
                detail.setCategory("Category Type 1");
            else
                detail.setCategory("Category Type 2");
            orderDetailList.add(detail);
        }
    }


    private void prepareCategory() {
        tagList.add(0, "List All");
        for (int i = 1; i <= 6; i++) {
            OBCategory detail = new OBCategory();
            detail.setName("Category Type " + (i));
            detail.setCode("TYPE_" + (i));
            detail.setId(i + "");
            categoryList.add(detail);
            tagList.add(detail.getName());

        }
    }

    public void checkOut(View view) {
        startActivity(new Intent(getApplicationContext(), PrepareOrderAddressActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search_action);
        searchView.setMenuItem(item);

        return true;
    }
}
