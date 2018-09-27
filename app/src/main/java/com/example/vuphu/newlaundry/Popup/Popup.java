package com.example.vuphu.newlaundry.Popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.vuphu.newlaundry.R;

import java.util.List;

public class Popup {
    private final static boolean CANCEL_OUTSIDE = false;
    private Activity context;
    private final Dialog dialog;
    private Button dialogButton;
    private TextView text;
    private ProgressBar loading;
    private ListView list;
    private String choosenValue;
    final LinearLayout.LayoutParams lp = new
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    @SuppressLint("ResourceAsColor")
    public Popup(Activity context) {
        this.context = context;
        dialog = new Dialog(this.context, R.style.translucentDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(CANCEL_OUTSIDE);
        dialog.setContentView(R.layout.popup);
        dialogButton = dialog.findViewById(R.id.btn_popup);
        text = dialog.findViewById(R.id.txt_popup_content);
        loading = dialog.findViewById(R.id.progress_popup);
        list = dialog.findViewById(R.id.list_popup);
        list.setVisibility(View.GONE);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void createSuccessDialog(String content, String button){
        dialogButton.setVisibility(View.VISIBLE);
        text.setText(content);
        dialogButton.setText(button);
        dialogButton.setBackground(this.context.getDrawable(R.drawable.btn_bg));
        loading.setVisibility(View.GONE);
    }
    public void createSuccessDialog(int content, int button){

        dialogButton.setVisibility(View.VISIBLE);
        text.setText(content);
        dialogButton.setText(button);
        dialogButton.setBackground(this.context.getDrawable(R.drawable.btn_bg));
        loading.setVisibility(View.GONE);
    }

    public void createFailDialog(String content, String button){

        dialogButton.setVisibility(View.VISIBLE);
        text.setText(content);
        dialogButton.setText(button);
        dialogButton.setBackground(this.context.getDrawable(R.drawable.btn_fail_bg));
        loading.setVisibility(View.GONE);
    }
    public void createFailDialog(int content, int button){

        dialogButton.setVisibility(View.VISIBLE);
        text.setText(content);
        dialogButton.setText(button);
        dialogButton.setBackground(this.context.getDrawable(R.drawable.btn_fail_bg));
        loading.setVisibility(View.GONE);
    }


    public void createLoadingDialog (){
        loading.setVisibility(View.VISIBLE);
        text.setText(R.string.notify_loading);
        dialogButton.setVisibility(View.GONE);
    }
    public void show() {
        dialog.show();
    }

    public void hide(){
        dialog.dismiss();
    }

    public void createListPopup(final List<String> value , String content, final EditText editText){
        text.setText(content);
        dialogButton.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(true);

        list.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, value));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosenValue = value.get(position);
                editText.setText(choosenValue);
                hide();
            }
        });
    }

    public String getChoosenValue() {
        return choosenValue;
    }
}
