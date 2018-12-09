package com.example.vuphu.newlaundry.Main.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetReceiptByOrderIdQuery;
import com.example.vuphu.newlaundry.GetReceiptIdByOrderQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.BillActivity;
import com.example.vuphu.newlaundry.Main.InfoOrderDetailActivity;
import com.example.vuphu.newlaundry.Main.OBOrderFragment;
import com.example.vuphu.newlaundry.Main.RatingActivity;
import com.example.vuphu.newlaundry.Main.ReceiptActivity;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.UpdateConfirmMutation;
import com.example.vuphu.newlaundry.UpdateStatusMutation;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.Util;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.vuphu.newlaundry.Utils.StringKey.APPROVED;
import static com.example.vuphu.newlaundry.Utils.StringKey.DATE_SYMBOL;
import static com.example.vuphu.newlaundry.Utils.StringKey.FINISHED;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_BRANCH;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_RECEIPT;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING_DELIVERY;
import static com.example.vuphu.newlaundry.Utils.StringKey.STATUS;
import static com.example.vuphu.newlaundry.Utils.Util.translateStatus;

public class AdapterListOrder extends RecyclerView.Adapter<OrderViewHolder> {
    private Context context;
    private ArrayList<OBOrderFragment> listOrder;

    public AdapterListOrder(Context context, ArrayList<OBOrderFragment> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_your_order,parent, false);
        return new OrderViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OBOrderFragment obOrderFragment = listOrder.get(position);
        holder.status.setChipText(translateStatus(obOrderFragment.getStatus(), context) + " - " + obOrderFragment.getId());
        String strDate = Util.parseDate(obOrderFragment.getDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy");
        holder.date.setText(strDate);
        holder.branchName.setText(obOrderFragment.getBranchName());
        holder.branchAdress.setText(obOrderFragment.getBranchAddress());
        holder.reciever.setText(obOrderFragment.getReciever());
        if(obOrderFragment.getStatus().equals(FINISHED)) {
            holder.view_bill.setVisibility(View.VISIBLE);
            holder.view_bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO get IdReceipt and route BillActivity.
                    getIdReceipt(obOrderFragment.getId());
                }
            });

            if(!obOrderFragment.isConfirm()) {
                holder.confirm_receiver.setVisibility(View.VISIBLE);
                holder.confirm_receiver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setConfirm(obOrderFragment.getId());
                    }
                });
            }

            holder.view_order.setVisibility(View.GONE);
            holder.view_receipt.setVisibility(View.GONE);
        }
        else {
            holder.view_order.setVisibility(View.VISIBLE);
            holder.view_order.setText(R.string.view_orderdetail);
            holder.view_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InfoOrderDetailActivity.class);
                    intent.putExtra(ID_ORDER, obOrderFragment.getId());
                    intent.putExtra(STATUS, obOrderFragment.getStatus());
                    intent.putExtra(ID_BRANCH, obOrderFragment.getIdBranch());
                    context.startActivity(intent);
                }
            });
            if(!obOrderFragment.getStatus().equals(PENDING)) {
                holder.view_receipt.setVisibility(View.VISIBLE);
                holder.view_receipt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ReceiptActivity.class);
                        intent.putExtra(ID_ORDER, obOrderFragment.getId());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    private void setConfirm(String id) {
        String token = PreferenceUtil.getAuthToken(context);
        GraphqlClient.getApolloClient(token, false).mutate(UpdateConfirmMutation.builder().idOrder(id).build())
                .enqueue(new ApolloCall.Callback<UpdateConfirmMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateConfirmMutation.Data> response) {
                        if(response.data().updateCustomerOrderById() != null) {
                            if(response.data().updateCustomerOrderById().customerOrder().confirmByCustomer() != null) {
                                Intent intent = new Intent(context, RatingActivity.class);
                                intent.putExtra(ID_ORDER, id);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    private void getIdReceipt(String id) {
        String token = PreferenceUtil.getAuthToken(context);
        GraphqlClient.getApolloClient(token, false).query(GetReceiptIdByOrderQuery.builder().order(id).build())
                .enqueue(new ApolloCall.Callback<GetReceiptIdByOrderQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetReceiptIdByOrderQuery.Data> response) {
                        if(response.data().allReceipts() != null) {
                            String idReceipt = response.data().allReceipts().nodes().get(0).id();
                            Intent intent = new Intent(context, BillActivity.class);
                            intent.putExtra(ID_RECEIPT, idReceipt);
                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, context.getResources().getString(R.string.err_get_id_receipt), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public void refreshAdapter(ArrayList<OBOrderFragment> list) {
        this.listOrder.clear();
        this.listOrder.addAll(list);
        notifyDataSetChanged();
    }
}
