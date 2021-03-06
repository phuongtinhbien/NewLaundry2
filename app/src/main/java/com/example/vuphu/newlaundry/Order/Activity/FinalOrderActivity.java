package com.example.vuphu.newlaundry.Order.Activity;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.Bundle;

import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.io.IOException;


import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;



public class FinalOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialButton back_to_home;
    private ImageView img_qrCode;
    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);
        initToolbar();
        back_to_home = findViewById(R.id.btn_backtohome);
        img_qrCode = findViewById(R.id.Img_qrCode);
        Intent intent = getIntent();
        if(intent.hasExtra(ID_ORDER)) {
            orderID = intent.getStringExtra(ID_ORDER);
        }
        try {
            generateQRCodeImage(orderID, 700, 700);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.order_result));
    }

    private void generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        height = bitMatrix.getHeight();
        width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        img_qrCode.setImageBitmap(bmp);
    }

}
