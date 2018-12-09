package com.example.vuphu.newlaundry.Main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import com.example.vuphu.newlaundry.Authen.LoginActivity;

import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends FancyWalkthroughActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstOpenApp();

        FancyWalkthroughCard fancyWalkthroughCardLogin = new FancyWalkthroughCard(R.string.wl_login, R.string.wl_login_content, R.drawable.dangnhap );
        fancyWalkthroughCardLogin.setBackgroundColor(R.color.white);
        fancyWalkthroughCardLogin.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardLogin.setTitleColor(R.color.black);
        fancyWalkthroughCardLogin.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChooseService = new FancyWalkthroughCard(R.string.wl_choose_service, R.string.wl_choose_service_content, R.drawable.dichvu );
        fancyWalkthroughCardChooseService.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChooseService.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChooseService.setTitleColor(R.color.black);
        fancyWalkthroughCardChooseService.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChooseClothes = new FancyWalkthroughCard(R.string.wl_choose_clothes, R.string.wl_choose_clothes_Content, R.drawable.chondo);
        fancyWalkthroughCardChooseClothes.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChooseClothes.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChooseClothes.setTitleColor(R.color.black);
        fancyWalkthroughCardChooseClothes.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChooseClothesDetail = new FancyWalkthroughCard(R.string.wl_choose_clothes_detail, R.string.wl_choose_clothes_Content_detail, R.drawable.chitietdo);
        fancyWalkthroughCardChooseClothesDetail.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChooseClothesDetail.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChooseClothesDetail.setTitleColor(R.color.black);
        fancyWalkthroughCardChooseClothesDetail.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardBag = new FancyWalkthroughCard(R.string.wl_bag, R.string.wl_bag_content, R.drawable.giodo);
        fancyWalkthroughCardBag.setBackgroundColor(R.color.white);
        fancyWalkthroughCardBag.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardBag.setTitleColor(R.color.black);
        fancyWalkthroughCardBag.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChooseBranch = new FancyWalkthroughCard(R.string.wl_choose_branch, R.string.wl_choose_branch_content, R.drawable.chonchinhanh);
        fancyWalkthroughCardChooseBranch.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChooseBranch.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChooseBranch.setTitleColor(R.color.black);
        fancyWalkthroughCardChooseBranch.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChooseTime = new FancyWalkthroughCard(R.string.wl_choose_time, R.string.wl_choose_time_content, R.drawable.chonthoigian);
        fancyWalkthroughCardChooseTime.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChooseTime.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChooseTime.setTitleColor(R.color.black);
        fancyWalkthroughCardChooseTime.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChoosePromotion = new FancyWalkthroughCard(R.string.wl_choose_promotion, R.string.wl_choose_promotion_content, R.drawable.khuyenmai);
        fancyWalkthroughCardChoosePromotion.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChoosePromotion.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChoosePromotion.setTitleColor(R.color.black);
        fancyWalkthroughCardChoosePromotion.setDescriptionColor(R.color.black);

        FancyWalkthroughCard fancyWalkthroughCardChooseEditCancel = new FancyWalkthroughCard(R.string.wl_edit_cancel, R.string.wl_edit_cancel_content, R.drawable.thongitndonhang);
        fancyWalkthroughCardChooseEditCancel.setBackgroundColor(R.color.white);
        fancyWalkthroughCardChooseEditCancel.setIconLayoutParams(1000,1000,0,0,0,0);
        fancyWalkthroughCardChooseEditCancel.setTitleColor(R.color.black);
        fancyWalkthroughCardChooseEditCancel.setDescriptionColor(R.color.black);


        List<FancyWalkthroughCard> pages = new ArrayList<>();
        pages.add(fancyWalkthroughCardLogin);
        pages.add(fancyWalkthroughCardChooseService);
        pages.add(fancyWalkthroughCardChooseClothes);
        pages.add(fancyWalkthroughCardChooseClothesDetail);
        pages.add(fancyWalkthroughCardBag);
        pages.add(fancyWalkthroughCardChooseBranch);
        pages.add(fancyWalkthroughCardChooseTime);
        pages.add(fancyWalkthroughCardChoosePromotion);
        pages.add(fancyWalkthroughCardChooseEditCancel);

        setFinishButtonTitle(R.string.wl_start);
        showNavigationControls(true);
        setInactiveIndicatorColor(R.color.grey_600);
        setActiveIndicatorColor(R.color.colorAccent);
        setImageBackground(R.drawable.bg_am);
        setFinishButtonDrawableStyle(getDrawable(R.drawable.btn));
        setOnboardPages(pages);

    }

    private void checkFirstOpenApp() {
        boolean isFistOpen = PreferenceUtil.isFirstOpen(getApplicationContext());
        Log.i("isFistOpen", isFistOpen + "");
        if(isFistOpen) {
            PreferenceUtil.setIsFirstOpen(getApplicationContext(), false);
        }
        else {
            Dialog dialog = new Dialog(WelcomeActivity.this);
            dialog.setTitle(R.string.title_check_fisrt_open);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.welcome_dialog);
            Button btnYes = dialog.findViewById(R.id.btn_yes);
            Button btnNo = dialog.findViewById(R.id.btn_no);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if(hasCapture) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
    }
}
