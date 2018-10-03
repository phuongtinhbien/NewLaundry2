package com.example.vuphu.newlaundry.Authen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Order.Activity.InfoOrderActivity;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.SaveImageMutation;
import com.example.vuphu.newlaundry.UpdateCustomerMutation;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.StringKey;
import com.example.vuphu.newlaundry.Utils.Util;
import com.example.vuphu.newlaundry.type.CustomerPatch;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import android.util.Base64;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;


public class SetUpInfoActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1234;
    private Toolbar toolbar;
    private List<String> genderList;
    private Popup popup;
    private TextInputEditText gender, phone, address;
    private FloatingActionButton setUp;
    private CircleImageView avatar;
    private String token;
    private static CurrentUserQuery.CurrentUser currentUser;
    private static UpdateCustomerMutation.Customer customer;
    private static SaveImageMutation.Post image;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private TextView name, email;
    int REQUEST_CODE_GALLERY = 0;
    Bitmap bitmap =  null;
    private static String urlAvatar = "default";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://luandry-2f439.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_info);
        initToolbar();
        init();
        requestPermission();
        addEvents();
    }
    private void init() {

        popup = new Popup(this);
        token = PreferenceUtil.getAuthToken(getApplicationContext());
        Log.i("token", token);
        name = findViewById(R.id.nav_txt_name);
        email = findViewById(R.id.nav_txt_email);
        setCurrentUser();
        genderList = Arrays.asList(getResources().getStringArray(R.array.arr_gender));
        gender = findViewById(R.id.set_up_gender);
        phone = findViewById(R.id.set_up_phone);
        avatar = findViewById(R.id.nav_img_avatar);
        address = findViewById(R.id.set_up_address);
        setUp = findViewById(R.id.set_up_btn_finish);

        Util.showHideCursor(gender);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popup.createListPopup(genderList, getResources().getString(R.string.input_gender), gender);
               popup.show();
            }
        });

        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    popup.createLoadingDialog();
                    popup.show();
                    excecute();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Set up information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean validate(){
        boolean validate = true;
        if (gender.getText().length()<=0){
            TextInputLayout layout = findViewById(R.id.set_up_gender_layout);
            layout.setError(getResources().getString(R.string.error_field_required));
            validate = false;
        }
        if (phone.getText().length()<=0){
            TextInputLayout layout = findViewById(R.id.set_up_phone_layout);
            layout.setError(getResources().getString(R.string.error_field_required));
            validate = false;
        }
        if (address.getText().length()<=0){
            TextInputLayout layout = findViewById(R.id.set_up_address_layout);
            layout.setError(getResources().getString(R.string.error_field_required));
            validate = false;
        }

        return validate;
    }

    private void setUpInfo (){

        CustomerPatch customerPatch = CustomerPatch.builder()
                .fullName(name.getText().toString())
                .address(address.getText().toString())
                .gender( gender.getText().toString().equals("Female")?true:false)
                .updateDate(Util.getDate().toString())
                .customerAvatar(image.id())
                .phone(phone.getText().toString()).build();

        GraphqlClient.getApolloClient(token, false)
                .mutate(UpdateCustomerMutation.builder()
                        .id(String.valueOf(currentUser.id()))
                        .customerPath(customerPatch).build())
                .enqueue(new ApolloCall.Callback<UpdateCustomerMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateCustomerMutation.Data> response) {
                        customer  = response.data().updateCustomerById().customer();
                        SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (customer != null){
                                    popup.hide();
                                    View.OnClickListener onClickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            popup.hide();
                                            startActivity(new Intent(SetUpInfoActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    };
                                    PreferenceUtil.setSetUpInfo(getApplicationContext(), true);
                                    popup.createSuccessDialog(R.string.notify_successfully_saved, R.string.use_now, onClickListener);
                                    popup.show();
                                }
                                else{
                                    popup.createFailDialog(R.string.notify_fail, R.string.btn_fail);
                                    popup.show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("set_up_info_err", e.getCause() +" - "+e);
                    }
                });

    }

    public void setCurrentUser() {
        GraphqlClient.getApolloClient(token, false).query(CurrentUserQuery.builder().build())
                .enqueue(new ApolloCall.Callback<CurrentUserQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CurrentUserQuery.Data> response) {
                        currentUser = response.data().currentUser();
                        SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(currentUser.lastName()+ " " + currentUser.firstName());
                                getCustomerInfo();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("current_user_err", e.getCause() +" - "+e);


                    }
                });
    }

    public void getCustomerInfo(){
        GraphqlClient.getApolloClient(token, false)
                .query(GetCustomerQuery.builder()
                .id(String.valueOf(currentUser.id())).build())
                .enqueue(new ApolloCall.Callback<GetCustomerQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetCustomerQuery.Data> response) {
                        final String emailStr = response.data().customerById().email();
                        if ( emailStr != null){
                            SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    email.setText(emailStr);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        final String er = e.getMessage();
                        SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                popup.hide();
                                popup.createFailDialog(er, "Fail");
                                popup.show();
                            }
                        });
                    }
                });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            addEvents();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri image = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(image, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(filePath);
            avatar.setImageBitmap(bitmap);
        }

    }

    private void addEvents() {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, REQUEST_CODE_GALLERY);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addEvents();
        }
    }

    private void saveAvatar(String urlAvatar){
        GraphqlClient.getApolloClient(token, false)
            .mutate(SaveImageMutation.builder()
                    .headerImageFile(urlAvatar)
                    .body("avatar") 
                    .headLine(email.getText().toString()).build())
            .enqueue(new ApolloCall.Callback<SaveImageMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<SaveImageMutation.Data> response) {
                    image = response.data().createPost().post();
                    SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpInfo();
                        }
                    });
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    final String er = e.getMessage();
                    SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            popup.hide();
                            popup.createFailDialog(er, "Fail");
                            popup.show();
                        }
                    });
                }
            });


    }

    private void excecute(){
        final StorageReference storageR = storageReference.child("customer/" + email.getText().toString() + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 5, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageR.putBytes(data);
        final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageR.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    urlAvatar = downloadUri.toString();
                    saveAvatar(urlAvatar);
                } else {
                    popup.hide();
                    popup.createFailDialog(R.string.notify_fail, R.string.btn_fail);
                    popup.show();
                }
            }
        });
    }
}
