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
import android.widget.Toast;

import com.example.vuphu.newlaundry.Order.OBTimeSchedule;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnChipClickListener;
import com.robertlevonyan.views.chip.OnSelectClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class PickupTimeDeliveryDialogFragment extends BottomSheetDialogFragment implements PickupTimeDeliveryListener {

    private static final String PICKUP = "pickup";
    private static final String DELIVERY = "delivery";
    private static final String ARG_LIST_ITEM = "list_item";
    private OBTimeSchedule ObPickup;
    private OBTimeSchedule ObDelivery;
    private RecyclerView recyclerViewPickupTime, recyclerViewDeliveryTime;
    private MaterialButton submit;
    private DatePicker datePickup;
    private DatePicker dateDelivery;
    private String mDatePickup;
    private String mDateDelivery;
    private String strDateFormat = "dd/MM/yyyy";
    private SimpleDateFormat sdf;
    private ArrayList<OBTimeSchedule> listPickup;
    private ArrayList<OBTimeSchedule> listDelivery;
    GetPickupTimeDelivery mListener;

    public static PickupTimeDeliveryDialogFragment newInstance(ArrayList<OBTimeSchedule> list, OBTimeSchedule timePickup, OBTimeSchedule timeDelivery, String datePickup, String dateDelivery) {
        final PickupTimeDeliveryDialogFragment fragment = new PickupTimeDeliveryDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST_ITEM, list);
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
        LinearLayoutManager linearLayoutManagerpickup = new LinearLayoutManager(getContext());
        linearLayoutManagerpickup.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager linearLayoutManagerDelivery = new LinearLayoutManager(getContext());
        linearLayoutManagerDelivery.setOrientation(LinearLayoutManager.HORIZONTAL);
        listPickup = new ArrayList<>();
        listDelivery = new ArrayList<>();
        listPickup.clear();
        listDelivery.clear();
        listPickup.addAll(getArguments().<OBTimeSchedule>getParcelableArrayList(ARG_LIST_ITEM));
        listDelivery.addAll(getArguments().<OBTimeSchedule>getParcelableArrayList(ARG_LIST_ITEM));
        Log.i("listSize", "size: " + listPickup.size());
        recyclerViewPickupTime = view.findViewById(R.id.list_pickupTime);
        recyclerViewPickupTime.setLayoutManager(linearLayoutManagerpickup);
        recyclerViewPickupTime.setAdapter(new PickupTimeDeliveryAdapter(listPickup, PICKUP, this) );
        recyclerViewDeliveryTime = view.findViewById(R.id.list_deliveryTime);
        recyclerViewDeliveryTime.setLayoutManager(linearLayoutManagerDelivery);
        recyclerViewDeliveryTime.setAdapter(new PickupTimeDeliveryAdapter(listDelivery, DELIVERY, this));

        sdf = new SimpleDateFormat(strDateFormat);
        datePickup = view.findViewById(R.id.date_pickup);
        dateDelivery = view.findViewById(R.id.date_delivery);
        setUpDatePickup();
        setUpDateDelivery();

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
                    else if(mDatePickup.equals(mDateDelivery)) {
                        if(!checkTime(ObPickup.getTimeStart(), ObDelivery.getTimeStart())){
                            Toast.makeText(getContext(), getResources().getString(R.string.time_pickup_2h), Toast.LENGTH_LONG).show();
                        }
                        else {
                            mListener.GetTime(ObPickup, ObDelivery, mDatePickup, mDateDelivery);
                            dismiss();
                        }
                    }
                        else if(!checkDate(mDatePickup, mDateDelivery)) {
                            Toast.makeText(getContext(), getResources().getString(R.string.date_delivery_pickup), Toast.LENGTH_LONG).show();
                        }
                            else {
                                mListener.GetTime(ObPickup, ObDelivery, mDatePickup, mDateDelivery);
                                dismiss();
                            }
            }
        });

    }

    private boolean checkTime(String timeStart, String timeStart1) {
        int start = Integer.parseInt(timeStart.substring(0, 2));
        int end = Integer.parseInt(timeStart1.substring(0, 2));
        return (end - start) >= 2;
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


    private void setUpDateDelivery() {
        final Calendar calendar = Calendar.getInstance();
        if(getArguments().get("dateDelivery") != null){
            String date = getArguments().getString("dateDelivery");
            try {
                calendar.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDateDelivery = sdf.format(calendar.getTime());
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        dateDelivery.setMinDate(calendar.getTime().getTime());
        dateDelivery.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                mDateDelivery = sdf.format(calendar.getTime());
                Log.i("date12", "date" + mDateDelivery);
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
        }
        mDatePickup = sdf.format(calendar.getTime());
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        datePickup.setMinDate(calendar.getTime().getTime());
        datePickup.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                mDatePickup = sdf.format(calendar.getTime());
                Log.i("date12", "date" + mDatePickup);
            }
        });
    }


    @Override
    public void onPickupTimeDeliveryClicked(OBTimeSchedule obTimeSchedule, String type) {
        switch (type) {
            case PICKUP: {
                ObPickup = new OBTimeSchedule(obTimeSchedule);
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
