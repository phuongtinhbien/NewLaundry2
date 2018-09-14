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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotificationFragment extends Fragment {

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
                Toast.makeText(getContext(), "User refreshes", Toast.LENGTH_SHORT).show();
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

        for (int i=0 ; i<20; i++){
            OBNotification item = new OBNotification();
            item.setContent(getResources().getString(R.string.notification_content));
            item.setTime(Util.getDate().toString());
            list.add(item);
        }
    }

   
}
