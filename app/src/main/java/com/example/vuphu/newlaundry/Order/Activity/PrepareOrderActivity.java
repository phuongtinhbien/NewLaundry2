package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.Categories.OBCategory;
import com.example.vuphu.newlaundry.Categories.iFCategory;
import com.example.vuphu.newlaundry.GetListUnitPriceMutation;
import com.example.vuphu.newlaundry.GetProductQuery;
import com.example.vuphu.newlaundry.GetProductTypeQuery;
import com.example.vuphu.newlaundry.GetUnitPricesByUnitQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Order.Adapter.ListChipAdapter;
import com.example.vuphu.newlaundry.Order.Adapter.ListOrderDetailAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Order.OBPrice;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.Product.OBProduct;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.type.UnitPriceInput;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.aflak.libraries.OBOrderDetailFilter;
import me.aflak.libraries.OBProductFilter;
import me.aflak.utils.Condition;

import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_SERVICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.NAME_SERVICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_ORDERDETAIL;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.UNIT_NAME_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.UNIT_NAME_KG;

public class PrepareOrderActivity extends AppCompatActivity implements iFCategory, IFOBPrepareOrder{

    private RecyclerView listPrepareOrder;
    private ListOrderDetailAdapter adapter;
    private List<OBOrderDetail> orderDetailList = new ArrayList<>();
    private List<OBOrderDetail> orderDetailFilterList = new ArrayList<>();
    private List<OBCategory> categoryList = new ArrayList<>();
    private RecyclerView listFilter;
    private ListChipAdapter listChipAdapter;
    private Popup popup;

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private Button seeYourBag;

    private String token;
    private String idService;
    private String serviceName;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_order);
        initToolbar();
        init();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.choose_clothes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        linearLayout = findViewById(R.id.total_panel_spml);
        linearLayout.setVisibility(View.GONE);
        Intent intent = getIntent();
        if(intent.hasExtra(ID_SERVICE)) {
            idService = intent.getStringExtra(ID_SERVICE);
        }
        if(intent.hasExtra(NAME_SERVICE)) {
            serviceName = intent.getStringExtra(NAME_SERVICE);
        }

        token = PreferenceUtil.getAuthToken(getApplicationContext());
        listPrepareOrder = findViewById(R.id.prepare_order_list_category);
//        listPrepareOrder.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        popup = new Popup(this);

        prepareList();



        seeYourBag = findViewById(R.id.see_your_bag);
        seeYourBag.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(getApplicationContext(), BagActivity.class));
              }
          }

        );
        //Tag Filter
        prepareCategory();
        listFilter = findViewById(R.id.list_chip);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchListProduct(newText);
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

    private void searchListProduct(final String newText) {
        if(!newText.isEmpty()) {
            List<OBOrderDetail> orderFilterList = OBOrderDetailFilter.builder()
                    .product().matches(OBProductFilter.builder().extraCondition(new Condition<OBProduct>() {
                        @Override
                        public boolean verify(OBProduct ob) {
                            if (ob.getTitle().toLowerCase().contains(newText.toLowerCase()))
                                return true;
                            return false;
                        }
                    })
                            .build(), OBProductFilter.class)
                    .on(orderDetailFilterList);
            adapter.refreshAdapter(orderFilterList);
        }
        else {
            adapter.refreshAdapter(orderDetailFilterList);
        }

    }


    private void prepareList() {
        popup.createLoadingDialog();
        popup.show();
        GraphqlClient.getApolloClient(token, false).query(GetProductQuery.builder().service(idService).build()).
                enqueue(new ApolloCall.Callback<GetProductQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetProductQuery.Data> response) {
                        if(response.data().allServiceProducts().nodes() != null) {
                            List<GetProductQuery.Node> list = response.data().allServiceProducts().nodes();
                            if(list.size() > 0) {
                                for(GetProductQuery.Node node: list) {
                                    OBOrderDetail obOrderDetail = new OBOrderDetail();
                                    OBProduct product = new OBProduct();
                                    product.setAvatar(node.productByProductId().postByProductAvatar().headerImageFile());
                                    product.setTitle(node.productByProductId().productName());
                                    product.setCategory(node.productByProductId().productTypeId());
                                    product.setId(node.productByProductId().id());
                                    obOrderDetail.setProduct(product);
                                    obOrderDetail.setIdService(idService);
                                    obOrderDetail.setServiceName(serviceName);
                                    orderDetailList.add(obOrderDetail);
                                }
                                PrepareOrderActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initListClothes();
                                    }
                                });
                            } else {
                                PrepareOrderActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initListClothes();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getProduct", e.getCause() +" - "+e);
                    }
                });

    }

    private void initListClothes() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listPrepareOrder.setLayoutManager(gridLayoutManager);
        orderDetailFilterList = new ArrayList<>(orderDetailList);
        adapter = new ListOrderDetailAdapter(orderDetailList, PrepareOrderActivity.this, this);
        listPrepareOrder.setAdapter(adapter);
        listPrepareOrder.invalidate();
    }

    private void prepareCategory() {
        GraphqlClient.getApolloClient(token, false).query(GetProductTypeQuery.builder().build()).
                enqueue(new ApolloCall.Callback<GetProductTypeQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetProductTypeQuery.Data> response) {
                        List<GetProductTypeQuery.Node> list = response.data().allProductTypes().nodes();
                        for (GetProductTypeQuery.Node node: list) {
                            categoryList.add(new OBCategory(node.id(), node.productTypeName()));
                        }
                        PrepareOrderActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initListCategory();
                                popup.hide();
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getProductType", e.getCause() +" - "+e);
                    }
                });
    }

    private void initListCategory() {
        StaggeredGridLayoutManager staggeredGridLayoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listFilter.setLayoutManager(staggeredGridLayoutManager1);
        listChipAdapter = new ListChipAdapter(categoryList, getApplicationContext(), this);
        listFilter.setAdapter(listChipAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search_action);
        MenuItem item1 = menu.findItem(R.id.menu_bag_action);
        item1.setVisible(false);
        menu.findItem(R.id.menu_read_action).setVisible(false);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void categoryClick(String id) {
        List<OBOrderDetail> orderFilterList = OBOrderDetailFilter.builder()
                .product().matches(OBProductFilter.builder()
                        .id().equalsTo(id)
                        .build(), OBProductFilter.class)
                .on(orderDetailFilterList);
        adapter.refreshAdapter(orderFilterList);
    }

    @Override
    public void categoryUnclick() {
        adapter.refreshAdapter(orderDetailFilterList);
    }

    @Override
    public void clickClothes(OBOrderDetail obOrderDetail) {
        popup.createLoadingDialog();
        popup.show();
        if(obOrderDetail != null) {
            getUnitPrice(obOrderDetail);
        }
    }

    private void getUnitPrice(OBOrderDetail obOrderDetail) {
        List<UnitPriceInput> list = new ArrayList<>();
        UnitPriceInput unitPriceInputItem = UnitPriceInput.builder()
                .productId(obOrderDetail.getProduct().getId())
                .unitId(ITEM)
                .serviceTypeId(obOrderDetail.getIdService())
                .build();
        list.add(unitPriceInputItem);
        UnitPriceInput unitPriceInputKG = UnitPriceInput.builder()
                .productId(null)
                .unitId(KG)
                .serviceTypeId(obOrderDetail.getIdService())
                .build();
        list.add(unitPriceInputKG);
        GraphqlClient.getApolloClient(token, false).mutate(GetListUnitPriceMutation.builder().list(list).build()).enqueue(new ApolloCall.Callback<GetListUnitPriceMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetListUnitPriceMutation.Data> response) {
                List<GetListUnitPriceMutation.UnitPrice> unitPrices = response.data().getlistproductprice().unitPrices();
                if(unitPrices.size() > 0) {
                    OBPrice obPriceItem = new OBPrice();
                    OBPrice obPriceKG = new OBPrice();
                    for(GetListUnitPriceMutation.UnitPrice unitPrice : unitPrices) {
                        if(unitPrice.unitId().equals(ITEM)) {
                            obPriceItem.setId(unitPrice.id());
                            obPriceItem.setUnitPrice(unitPrice.price());
                        } else if(unitPrice.unitId().equals(KG)) {
                            obPriceKG.setId(unitPrice.id());
                            obPriceKG.setUnitPrice(unitPrice.price());
                        }
                    }
                    Intent intent = new Intent(PrepareOrderActivity.this, DetailPrepareOrderClothesActivity.class);
                    intent.putExtra(OB_ORDERDETAIL, obOrderDetail);
                    intent.putExtra(OB_UNIT_PRICE_ITEM, obPriceItem);
                    intent.putExtra(OB_UNIT_PRICE_KG, obPriceKG);
                    startActivity(intent);
                    popup.hide();
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("getListUnitPrice", e.getCause() +" - "+e);
            }
        });
    }

    @Override
    public void clickDel(int position) {

    }

}
