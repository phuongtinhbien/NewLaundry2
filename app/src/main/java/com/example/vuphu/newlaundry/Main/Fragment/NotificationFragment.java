package com.example.vuphu.newlaundry.Main.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetNotificationCustomerQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Notification.ListNotificationAdapter;
import com.example.vuphu.newlaundry.Notification.OBNotification;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.Util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.vuphu.newlaundry.Utils.StringKey.APPROVED;
import static com.example.vuphu.newlaundry.Utils.StringKey.DECLINED;
import static com.example.vuphu.newlaundry.Utils.StringKey.DELIVERIED;
import static com.example.vuphu.newlaundry.Utils.StringKey.FINISHED;
import static com.example.vuphu.newlaundry.Utils.StringKey.FINISHED_SERVING;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING_DELIVERY;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING_SERVING;
import static com.example.vuphu.newlaundry.Utils.StringKey.RECEIVED;
import static com.example.vuphu.newlaundry.Utils.StringKey.SERVING;
import static com.example.vuphu.newlaundry.Utils.StringKey.TASK_CUSTOMER_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.TASK_RECEIPT;


public class NotificationFragment extends Fragment {
    private RecyclerView notificationList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<OBNotification> list;
    ListNotificationAdapter adapter;
    private String token;
    private String customerID;
    public NotificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationList = v.findViewById(R.id.list_notification);
        swipeRefreshLayout = v.findViewById(R.id.swipe_notification);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL, false);
        notificationList.setLayoutManager(layoutManager);
        token = PreferenceUtil.getAuthToken(v.getContext());
        customerID = PreferenceUtil.getIdUser(v.getContext());
        createList(v);
        return v;
    }

    public static NotificationFragment newInstance() {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void createList(View view){
        list = new ArrayList<>();
        GraphqlClient.getApolloClient(token, false).query(GetNotificationCustomerQuery.builder().cusId(customerID).build())
                .enqueue(new ApolloCall.Callback<GetNotificationCustomerQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetNotificationCustomerQuery.Data> response) {
                        if(response.data().getNotificationCustomer().nodes().size() > 0){
                            List<GetNotificationCustomerQuery.Node> nodes = response.data().getNotificationCustomer().nodes();
                            for(GetNotificationCustomerQuery.Node node : nodes) {
                                OBNotification obNotification = new OBNotification();
                                String content = "";
                                if(node.taskType().equals(TASK_CUSTOMER_ORDER)){
                                    content = handleTitle(node.customerOrder(), node.currentStatus());
                                }
                                else if(node.taskType().equals(TASK_RECEIPT)) {
                                    String staffDelivery = "";
                                    String staffpick = "";
                                    if(node.receiptByReceipt().staffByStaffDelivery() != null) {
                                        staffDelivery = node.receiptByReceipt().staffByStaffDelivery().fullName();
                                    }
                                    if(node.receiptByReceipt().staffByStaffPickUp() != null) {
                                        staffpick = node.receiptByReceipt().staffByStaffPickUp().fullName();
                                    }
                                    content = handleTitle(node.receiptByReceipt().orderId(),
                                            node.currentStatus(),
                                            node.receiptByReceipt().deliveryDate(),
                                            node.receiptByReceipt().deliveryTime(),
                                            node.receiptByReceipt().pickUpDate(),
                                            node.receiptByReceipt().pickUpTime(),
                                            staffDelivery,
                                            staffpick
                                    );
                                }
                                String time = "";
                                if(!TextUtils.isEmpty(node.updateDate())) {
                                    time = Util.parseDate(node.updateDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy") + " " + node.updateDate().substring(11, 19);
                                }
                                else {
                                    time = Util.parseDate(node.createDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy") + " " + node.updateDate().substring(11, 19);
                                }
                                if(!TextUtils.isEmpty(content) && !TextUtils.isEmpty(time)) {
                                    obNotification.setContent(content);
                                    obNotification.setTime(time);
                                    list.add(obNotification);
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inittializeView(view);
                                }
                            });
                        }
                        else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inittializeView(view);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });

    }

    private void inittializeView(View view) {
        notificationList.setHasFixedSize(true);
        adapter = new ListNotificationAdapter(view.getContext(),list);
        notificationList.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: Refresh list notification
                refresh();
            }
        });
    }

    private void refresh() {
        GraphqlClient.getApolloClient(token, false).query(GetNotificationCustomerQuery.builder().cusId(customerID).build())
                .enqueue(new ApolloCall.Callback<GetNotificationCustomerQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetNotificationCustomerQuery.Data> response) {
                        if(response.data().getNotificationCustomer().nodes().size() > 0){
                            List<GetNotificationCustomerQuery.Node> nodes = response.data().getNotificationCustomer().nodes();
                            ArrayList<OBNotification> listRefresh = new ArrayList<>();
                            listRefresh.clear();
                            for(GetNotificationCustomerQuery.Node node : nodes) {
                                OBNotification obNotification = new OBNotification();
                                String content = "";
                                if(node.taskType().equals(TASK_CUSTOMER_ORDER)){
                                    content = handleTitle(node.customerOrder(), node.currentStatus());
                                }
                                else if(node.taskType().equals(TASK_RECEIPT)) {
                                    String staffDelivery = "";
                                    String staffpick = "";
                                    if(node.receiptByReceipt().staffByStaffDelivery() != null) {
                                        staffDelivery = node.receiptByReceipt().staffByStaffDelivery().fullName();
                                    }
                                    if(node.receiptByReceipt().staffByStaffPickUp() != null) {
                                        staffpick = node.receiptByReceipt().staffByStaffPickUp().fullName();
                                    }
                                    content = handleTitle(node.receiptByReceipt().orderId(),
                                            node.currentStatus(),
                                            node.receiptByReceipt().deliveryDate(),
                                            node.receiptByReceipt().deliveryTime(),
                                            node.receiptByReceipt().pickUpDate(),
                                            node.receiptByReceipt().pickUpTime(),
                                            staffDelivery,
                                            staffpick
                                    );
                                }
                                String time = "";
                                if(!TextUtils.isEmpty(node.updateDate())) {
                                    time = Util.parseDate(node.updateDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy") + " " + node.updateDate().substring(11, 19);
                                }
                                else {
                                    time = Util.parseDate(node.createDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy") + " " + node.updateDate().substring(11, 19);
                                }
                                if(!TextUtils.isEmpty(content) && !TextUtils.isEmpty(time)) {
                                    obNotification.setContent(content);
                                    obNotification.setTime(time);
                                    listRefresh.add(obNotification);
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("listRefresh", listRefresh.size() + "****");
                                    if(listRefresh.size() > 0){
                                        adapter.refreshAdapter(listRefresh);
                                    }
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });

    }

    private String handleTitle(String orderId, String currentStatus, String deliveryDate, String deliveryTime, String pickUpDate, String pickUpTime, String staffDelivery, String staffPickup) {
        String result = "";
        switch (currentStatus) {
            case PENDING: {
                if(!TextUtils.isEmpty(pickUpTime) && !TextUtils.isEmpty(pickUpDate) && !TextUtils.isEmpty(staffPickup)) {
                    result = getResources().getString(R.string.ORDER_NUMBER) + " "
                            + orderId + " "
                            + getResources().getString(R.string.CONTENT_PENDING_RECEIPT_PICKUP) + " "
                            + getResources().getString(R.string.CONTENT_PENDING_RECEIPT_PICKUP_TIME) + " "
                            + pickUpTime + " - "
                            + Util.parseDate(pickUpDate, "yyyy/MM/dd", "dd/MM/yyyy") + " "
                            + getResources().getString(R.string.CONTENT_PENDING_RECEIPT_PICKUP_STAFF) + " "
                            + staffPickup;
                }
                else {
                    result = getResources().getString(R.string.RECEIPT_OF) + " "
                            + orderId + " "
                            + getResources().getString(R.string.CONTENT_PENDING_RECEIPT);
                }
                return result;
            }
            case RECEIVED: {
                result  = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_RECEIVED);
                return result;
            }
            case PENDING_DELIVERY:{
                if(!TextUtils.isEmpty(deliveryTime) && !TextUtils.isEmpty(deliveryDate) && !TextUtils.isEmpty(staffDelivery)) {
                    result  = getResources().getString(R.string.ORDER_NUMBER) + " "
                            + orderId + " "
                            + getResources().getString(R.string.CONTENT_PENDING_DELIVERY) + " "
                            + deliveryTime + " - "
                            + Util.parseDate(deliveryDate, "yyyy/MM/dd", "dd/MM/yyyy") + " "
                            + getResources().getString(R.string.CONTENT_PENDING_DELIVERY_CONTINUE) + " "
                            + staffDelivery;
                }
                return result;
            }
            case DELIVERIED: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_DELIVERIED);
            }
        }
        return result;
    }

    private String handleTitle(String orderId, String currentStatus) {
        String result = "";
        switch (currentStatus){
            case APPROVED: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_APPROVED);
                return result;
            }
            case PENDING_SERVING: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_PENDING_SERVING);
                return result;
            }
            case SERVING: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId +" "
                        + getResources().getString(R.string.CONTENT_SERVING);
                return result;
            }
            case FINISHED_SERVING: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_FINISHED_SERVING);
                return result;
            }
            case FINISHED: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_FINISHED);
                return result;
            }
            case DECLINED: {
                result = getResources().getString(R.string.ORDER_NUMBER) + " "
                        + orderId + " "
                        + getResources().getString(R.string.CONTENT_DELIVERIED);
                return result;
            }
        }
        return result;
    }


}
