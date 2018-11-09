package com.example.vuphu.newlaundry.Order.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mcontext;

    public CustomInfoWindowAdapter(Context mcontext) {
        this.mWindow = LayoutInflater.from(mcontext).inflate(R.layout.item_info_window, null);
        this.mcontext = mcontext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    private void renderWindowText(Marker marker, View mWindow) {
        TextView branchName = mWindow.findViewById(R.id.info_window_nameBranch);
        branchName.setText(marker.getTitle());
        TextView branchAddress = mWindow.findViewById(R.id.info_window_addressBranch);
        branchAddress.setText(marker.getSnippet());
        TextView totalPrice = mWindow.findViewById(R.id.info_window_totalPrice);
//        totalPrice.setText(marker.getTag().toString());
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
