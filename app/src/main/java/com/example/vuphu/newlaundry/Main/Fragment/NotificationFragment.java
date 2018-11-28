package com.example.vuphu.newlaundry.Main.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vuphu.newlaundry.Notification.ListNotificationAdapter;
import com.example.vuphu.newlaundry.Notification.OBNotification;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NotificationFragment extends Fragment {
    private String strFormat = "dd/MM/yyyy HH:mm:ss";
    private SimpleDateFormat sdf;
    private RecyclerView notificationList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<OBNotification> list;
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
        sdf = new SimpleDateFormat(strFormat);
        swipeRefreshLayout = v.findViewById(R.id.swipe_notification);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL, false);
        notificationList.setLayoutManager(layoutManager);
        createList();
        notificationList.setHasFixedSize(true);
        ListNotificationAdapter adapter = new ListNotificationAdapter(v.getContext(),list);
        notificationList.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: Refresh list notification
            }
        });

        return v;
    }

    public static NotificationFragment newInstance() {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void createList(){
        list = new ArrayList<>();
        

        OBNotification item = new OBNotification();
        item.setContent("Bạn đã tạo đơn ngày có mã số 27 vào ngày 23/11/2018.");
        item.setTime("23/11/2018 20:53:00");
        list.add(item);

        OBNotification item1 = new OBNotification();
        item1.setContent("Đơn hàng mã số 27 đã được duyệt thành công.");
        item1.setTime("24/11/2018 07:30:00");
        list.add(item1);

    }

   
}
