package com.example.vuphu.newlaundry.Promotion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.Chip;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemPromotionListDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_ITEM = "item_count";
    private Listener mListener;
    private List<OBPromotion> promotionList;

    // TODO: Customize parameters
    public static ItemPromotionListDialogFragment newInstance(ArrayList<OBPromotion> itemCount) {
        final ItemPromotionListDialogFragment fragment = new ItemPromotionListDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM,itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_promotion_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_promotion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        promotionList = (List<OBPromotion>) getArguments().getSerializable(ARG_ITEM);
        recyclerView.setAdapter(new ItemPromotionAdapter(promotionList));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemPromotionClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView time;
        final ImageView img;
        final Chip code;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_item_promotion_list_dialog_item, parent, false));
            title =  itemView.findViewById(R.id.item_promotion_title);
            time =  itemView.findViewById(R.id.item_promotion_time);
            img = itemView.findViewById(R.id.img_promotion);
            code = itemView.findViewById(R.id.item_promotion_code);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemPromotionClicked(getAdapterPosition());
                        Toast.makeText(getContext(), "Promotion applied", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });
        }

    }

    private class ItemPromotionAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<OBPromotion> mItemCount;

        ItemPromotionAdapter(List<OBPromotion> mItemCount) {
            this.mItemCount = mItemCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(mItemCount.get(position).getTitle());
            holder.code.setChipText("PROMOTIONECONOMYS");

        }

        @Override
        public int getItemCount() {
            return mItemCount.size();
        }

    }

}
