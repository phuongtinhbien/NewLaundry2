package com.example.vuphu.newlaundry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Order.OBTimeSchedule;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnChipClickListener;
import com.robertlevonyan.views.chip.OnSelectClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class PickupTimeDeliveryDialogFragment extends BottomSheetDialogFragment implements PickupTimeDeliveryListener {

    private static final String PICKUP = "pickup";
    private static final String DELIVERY = "delivery";
    private static final String ARG_LIST_ITEM_PICKUP = "list_item_pickup";
    private static final String ARG_LIST_ITEM_DELIVERY = "list_item_delivery";
    private OBTimeSchedule ObPickup;
    private OBTimeSchedule ObDelivery;
    private RecyclerView recyclerViewPickupTime, recyclerViewDeliveryTime;
    private MaterialButton submit;
    private DatePicker datePickup;
    private DatePicker dateDelivery;
    private String mDatePickup;
    private String mDateDelivery;
    private String strDateFormat = "dd/MM/yyyy";
    private String strDateSFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private SimpleDateFormat sdf, sdf1;
    private ArrayList<OBTimeSchedule> listPickup;
    private ArrayList<OBTimeSchedule> listDelivery;
    private PickupTimeDeliveryAdapter adapterPickup, adapterDelivery;
    private  int yearPickup, monthPickup, dayPickup;
    private Calendar currentDate;
    GetPickupTimeDelivery mListener;
    private String token;
    private View v;
    private boolean pickupClick = false;

    public static PickupTimeDeliveryDialogFragment newInstance(ArrayList<OBTimeSchedule> listPick, ArrayList<OBTimeSchedule> listDeli,OBTimeSchedule timePickup, OBTimeSchedule timeDelivery, String datePickup, String dateDelivery, String idBranch) {
        final PickupTimeDeliveryDialogFragment fragment = new PickupTimeDeliveryDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST_ITEM_PICKUP, listPick);
        args.putParcelableArrayList(ARG_LIST_ITEM_DELIVERY, listDeli);
        if(timePickup != null) {
            args.putParcelable("OBTimePickup", timePickup);
        }
        if(timeDelivery != null) {
            args.putParcelable("OBTimeDelivery", timeDelivery);
        }
        if(datePickup != null) {
            args.putString("datePickup", datePickup);
        }
        if(dateDelivery != null) {
            args.putString("dateDelivery", dateDelivery);
        }
        if(idBranch != null) {
            args.putString("idBranch", idBranch);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pickuptimedelivery_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        v = view;
        token = PreferenceUtil.getAuthToken(getActivity());
        sdf = new SimpleDateFormat(strDateFormat);
        sdf1 = new SimpleDateFormat(strDateSFormat);
        currentDate = Calendar.getInstance();
        dateDelivery = view.findViewById(R.id.date_delivery);
        dateDelivery.setVisibility(View.INVISIBLE);
        listPickup = new ArrayList<>();
        listPickup.clear();
        listPickup.addAll(getArguments().<OBTimeSchedule>getParcelableArrayList(ARG_LIST_ITEM_PICKUP));
        listDelivery = new ArrayList<>();
        listDelivery.clear();
        listDelivery.addAll(getArguments().<OBTimeSchedule>getParcelableArrayList(ARG_LIST_ITEM_DELIVERY));
        Log.i("listPickup", listPickup.toString() + "---" + listDelivery.toString());
        initializePickup(view);




        submit = view.findViewById(R.id.input_dialog_confirm);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ObPickup == null) {
                    Toast.makeText(getContext(),getResources().getString(R.string.time_pickup_null) , Toast.LENGTH_LONG).show();
                }
                else if(ObDelivery == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.time_delivery_null), Toast.LENGTH_LONG).show();
                    }
                else {
                mListener.GetTime(ObPickup, ObDelivery, mDatePickup, mDateDelivery);
                dismiss();
                }

            }
        });

    }

    private void getTimeLimit(View view, Calendar calendar) {
        GraphqlClient.getApolloClient(token, false).query(GetTimeLimitDeliveryQuery.
                builder().
                branch(getArguments().getString("idBranch"))
                .build()
        ).enqueue(new ApolloCall.Callback<GetTimeLimitDeliveryQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetTimeLimitDeliveryQuery.Data> response) {
                Log.i("dateAllow", response.data().getMinTimeForHandle() + "sd");
                if(response.data().getMinTimeForHandle() != null) {
                    String dateAllow = response.data().getMinTimeForHandle();
                    Date result = new Date();
                    try {
                         result = sdf1.parse(dateAllow);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.i("dateAllow", dateAllow);
                    Date finalResult = result;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initializeDelivery(finalResult, view, calendar);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("getTimeLimitDelivery", e.getCause() +" - "+e);
            }
        });
    }

    private void initializeDelivery(Date dateAllow, View view, Calendar calendar) {
        long limitTime = 0;
        if(dateAllow.compareTo(calendar.getTime()) > 0 && subTime(dateAllow, calendar) > 5*3600000) {
            limitTime = dateAllow.getTime();
        }
        else {
            limitTime = calendar.getTimeInMillis() + 5*3600000;
        }
        Log.i("limitTime", limitTime + " s");
        LinearLayoutManager linearLayoutManagerDelivery = new LinearLayoutManager(getContext());
        linearLayoutManagerDelivery.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewDeliveryTime = view.findViewById(R.id.list_deliveryTime);
        recyclerViewDeliveryTime.setLayoutManager(linearLayoutManagerDelivery);
        adapterDelivery = new PickupTimeDeliveryAdapter(listDelivery, DELIVERY, this);
        recyclerViewDeliveryTime.setAdapter(adapterDelivery);
        dateDelivery.setVisibility(View.VISIBLE);
        setUpDateDelivery(limitTime);
    }

    private long subTime(Date dateAllow, Calendar cal) {
        long sub =  cal.getTimeInMillis() - dateAllow.getTime();
        return Math.abs(sub);
    }


    private void initializePickup(View view) {
        LinearLayoutManager linearLayoutManagerpickup = new LinearLayoutManager(getContext());
        linearLayoutManagerpickup.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerViewPickupTime = view.findViewById(R.id.list_pickupTime);
        recyclerViewPickupTime.setLayoutManager(linearLayoutManagerpickup);
        adapterPickup = new PickupTimeDeliveryAdapter(listPickup, PICKUP, this);
        recyclerViewPickupTime.setAdapter(adapterPickup);
        datePickup = view.findViewById(R.id.date_pickup);
        setUpDatePickup();
    }

    private boolean checkTime(String timeStart, String timeStart1) {
        int start = Integer.parseInt(timeStart.substring(0, 2));
        int end = Integer.parseInt(timeStart1.substring(0, 2));
        return (end - start) >= 5;
    }



    private boolean checkDate(String mDatePickup, String mDateDelivery) {
        try {
            Date dateEnd = sdf.parse(mDateDelivery);
            Date dateStart = sdf.parse(mDatePickup);
            return dateStart.before(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void setUpDateDelivery(long timeLimit) {
        final Calendar calendar = Calendar.getInstance();
        if(getArguments().get("dateDelivery") != null){
            String date = getArguments().getString("dateDelivery");
            try {
                calendar.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            calendar.setTimeInMillis(timeLimit);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            Log.i("HourDeli", h + " h" + " date: " + calendar.getTime());
            filterListOBDelivery(h, calendar);
        }
        mDateDelivery = sdf.format(calendar.getTime());
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        dateDelivery.setMinDate(calendar.getTime().getTime());
        dateDelivery.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                mDateDelivery = sdf.format(calendar.getTime());
                if(calendar.getTimeInMillis() > timeLimit) {
                    Log.i("LonHon", "Yes");
                    adapterDelivery.refreshAdapter(getArguments().<OBTimeSchedule>getParcelableArrayList(ARG_LIST_ITEM_DELIVERY));
                } else {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(timeLimit);
                    int h = calendar.get(Calendar.HOUR_OF_DAY);
                    filterListOBDelivery(h, calendar);
                }
            }
        });
    }

    private void setUpDatePickup() {
        final Calendar calendar = Calendar.getInstance();

        if(getArguments().get("datePickup") != null){
            String date = getArguments().getString("datePickup");
            try {
                calendar.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            Log.i("HourPick", h + " h");
            filterListOBPickup(h, calendar);
        }
        mDatePickup = sdf.format(calendar.getTime());
        yearPickup = calendar.get(calendar.YEAR);
        monthPickup = calendar.get(calendar.MONTH);
        dayPickup = calendar.get(calendar.DAY_OF_MONTH);
        datePickup.setMinDate(calendar.getTimeInMillis());
        datePickup.init(yearPickup, monthPickup, dayPickup, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                mDatePickup = sdf.format(calendar.getTime());
                if(calendar.getTime().compareTo(currentDate.getTime()) > 0) {
//                    ArrayList<OBTimeSchedule> list = new ArrayList<>();
//                    list.addAll(getArguments().getParcelableArrayList(ARG_LIST_ITEM_DELIVERY));
//                    for (OBTimeSchedule ob : list) {
//                        if(!ob.isDisplay()) {
//                            ob.setDisplay(true);
//                            Log.i("isDisplay", "yes");
//                        }
//                        else if(ob.isDisplay()) {
//                            adapterPickup.setSelected(true, list.indexOf(ob));
//                        }
//                    }
                    adapterPickup.refreshAdapter(getArguments().getParcelableArrayList(ARG_LIST_ITEM_DELIVERY));
                } else if(calendar.getTime().compareTo(currentDate.getTime()) == 0) {
                    int h = Integer.parseInt(ObPickup.getTimeStart().substring(0,2));
                    filterListOBPickup(h, calendar);
                }
                if(pickupClick) {
                    Log.i("CalendarPickup", "date" + calendar.getTime());
                    long time = calendar.getTimeInMillis() + Long.parseLong(ObPickup.getTimeStart().substring(0, 2))*3600000;
                    calendar.setTimeInMillis(time);
                    getTimeLimit(v, calendar);
                }
            }
        });
    }

    private void filterListOBDelivery(int h, Calendar calendar) {
        if(h >= 17 && h <= 23) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            ArrayList<OBTimeSchedule> list = getArguments().getParcelableArrayList(ARG_LIST_ITEM_DELIVERY);
            for (OBTimeSchedule ob : list) {
                if(!ob.isDisplay()) {
                    ob.setDisplay(true);
                }
            }
            adapterDelivery.refreshAdapter(list);

        } else if(h >= 6 && h <= 16) {
            ArrayList<OBTimeSchedule> list = getArguments().getParcelableArrayList(ARG_LIST_ITEM_DELIVERY);
            ArrayList<OBTimeSchedule> listResult = new ArrayList<>();
            for (OBTimeSchedule ob: list) {
                int start = Integer.parseInt(ob.getTimeStart().substring(0, 2));
                if(start >= h) {
                    if(!ob.isDisplay()){
                        ob.setDisplay(true);
                    }
                    listResult.add(ob);
                }
            }
            if(listResult.size() > 0) {
                adapterDelivery.refreshAdapter(listResult);
            }
        }
    }

    private void filterListOBPickup(int h, Calendar calendar) {
        if(h > 17 && h <= 23) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else if(h >= 6 && h<= 16) {
            ArrayList<OBTimeSchedule> list = new ArrayList<>();
            for (OBTimeSchedule ob: listPickup) {
                int start = Integer.parseInt(ob.getTimeStart().substring(0, 2));
                if(start >= h) {
                    list.add(ob);
                }
            }
            if(list.size() > 0) {
                adapterPickup.refreshAdapter(list);
            }
        }
    }


    @Override
    public void onPickupTimeDeliveryClicked(OBTimeSchedule obTimeSchedule, String type) {
        switch (type) {
            case PICKUP: {
                ObPickup = new OBTimeSchedule(obTimeSchedule);
                Calendar calendar = Calendar.getInstance();
                Date d = new Date();
                try {
                    d = sdf.parse(mDatePickup);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long day = d.getTime() + Long.parseLong(ObPickup.getTimeStart().substring(0, 2))*3600000;
                calendar.setTimeInMillis(day);
                pickupClick = true;
                getTimeLimit(v, calendar);
                Log.i("pickup", ObPickup.getTimeEnd());
                break;
            }
            case DELIVERY: {
               ObDelivery = new OBTimeSchedule(obTimeSchedule);
                Log.i("delivery", ObDelivery.getTimeEnd());
                break;
            }
            default: Log.i("default12", "12");
        }
    }

    @Override
    public void onPickupTimeDeliveryUnClicked(String type) {
        switch (type) {
            case PICKUP: {
                ObPickup = null;
                break;
            }
            case DELIVERY: {
                ObDelivery = null;
                break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (GetPickupTimeDelivery) parent;
        } else {
            mListener = (GetPickupTimeDelivery) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private class PickupTimeDeliveryAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final ArrayList<OBTimeSchedule> listTime;
        private final String type;
        private boolean flag = true;
        private boolean isSelected = false;
        private int pos;
        private PickupTimeDeliveryListener pickupTimeDeliveryListener;
        private PickupTimeDeliveryAdapter(ArrayList<OBTimeSchedule> listTime, String type, PickupTimeDeliveryListener pickupTimeDeliveryListener) {
            this.listTime = listTime;
            this.type = type;
            this.pickupTimeDeliveryListener = pickupTimeDeliveryListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pickup_delivery, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final OBTimeSchedule timeOB = listTime.get(position);
            holder.time.setChipText(timeOB.getTimeStart() + " - " + timeOB.getTimeEnd());
            Log.i("abc", "onBindViewHolder: " + timeOB.isDisplay());
            if(getArguments().containsKey("OBTimePickup") && getArguments().containsKey("OBTimeDelivery") && flag){
                switch (type) {
                    case PICKUP: {
                        OBTimeSchedule obTimeSchedule = getArguments().getParcelable("OBTimePickup");
                        if(!timeOB.getId().equals(obTimeSchedule.getId())) {
                           timeOB.setDisplay(false);
                        }
                        else {
                            holder.time.setSelected(true);
                            timeOB.setDisplay(true);
                            pickupTimeDeliveryListener.onPickupTimeDeliveryClicked(timeOB, type);
                        }
                        break;
                    }
                    case DELIVERY: {
                        OBTimeSchedule obTimeSchedule = getArguments().getParcelable("OBTimeDelivery");
                        if(!timeOB.getId().equals(obTimeSchedule.getId())) {
                            timeOB.setDisplay(false);
                        }
                        else {
                            holder.time.setSelected(true);
                            timeOB.setDisplay(true);
                            pickupTimeDeliveryListener.onPickupTimeDeliveryClicked(timeOB, type);
                        }
                        break;
                    }
                }
            }
            if(timeOB.isDisplay()) {
                holder.itemView.setVisibility(View.VISIBLE);
                if(checkSelected(position)) {
                    holder.time.setSelected(true);
                }
            } else {
                holder.itemView.setVisibility(View.GONE);
            }
            holder.time.setOnSelectClickListener(new OnSelectClickListener() {
                @Override
                public void onSelectClick(View v, boolean selected) {
                    Log.i("select", "select: " + selected);
                    if(selected){
                        setDisplayItem(position, !selected);
                        pickupTimeDeliveryListener.onPickupTimeDeliveryClicked(timeOB, type);
                        flag = false;
                    }
                    else {
                        setDisplayItem(position, !selected);
                        flag = false;
                        pickupTimeDeliveryListener.onPickupTimeDeliveryUnClicked(type);
                        pickupClick = false;
                    }

                }
            });
        }

        public void setDisplayItem(int pos, boolean display) {
            for(int i=0; i<listTime.size(); i++){
                if(i != pos){
                    listTime.get(i).setDisplay(display);
                    Log.i("log", "i: " + i + "-" + listTime.get(i).isDisplay());
                    notifyItemChanged(i);
                }
            }

        }

        public void refreshAdapter(List<OBTimeSchedule> list){
            this.listTime.clear();
            this.listTime.addAll(list);
            notifyDataSetChanged();
        }

        public void setSelected(boolean select, int pos) {
            this.isSelected = select;
            this.pos = pos;
        }

        public boolean checkSelected(int position) {
            return this.isSelected && position == this.pos;
        }

        @Override
        public int getItemCount() {
            return listTime.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        Chip time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time_pd);
        }
    }

    public interface GetPickupTimeDelivery {
        void GetTime(OBTimeSchedule pickup, OBTimeSchedule delivery, String datePickup, String dateDelivery);
    }
}
