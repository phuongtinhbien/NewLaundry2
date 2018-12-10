package com.example.vuphu.newlaundry.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.UpdateRatingAndCommentMutation;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import org.jetbrains.annotations.NotNull;

import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;

public class RatingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RatingBar ratingBar;
    private TextView txt_RatingText;
    private EditText txt_comment;
    private Button btn_send_feedback;
    private String idOrder, token;
    private int rating = 0;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        initToolbar();
        addControls();
        addEvents();
    }

    private void addEvents() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = (int) v;
                setTextRating();
            }
        });

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    comment = txt_comment.getText().toString();
                    GraphqlClient.getApolloClient(token, false).mutate(UpdateRatingAndCommentMutation
                            .builder()
                            .id(idOrder)
                            .rating(rating)
                            .comment(comment)
                            .build()
                    ).enqueue(new ApolloCall.Callback<UpdateRatingAndCommentMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<UpdateRatingAndCommentMutation.Data> response) {
                            if(response.data().updateCustomerOrderById().customerOrder() != null) {
                                RatingActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RatingActivity.this, getResources().getString(R.string.result_send_feedback), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {

                        }
                    });
                }
            }
        });
    }

    private boolean validate() {
        if(rating < 2) {
            Toast.makeText(RatingActivity.this, getResources().getString(R.string.err_rating), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(txt_comment.getText().toString())) {
            Toast.makeText(RatingActivity.this, getResources().getString(R.string.err_comment), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setTextRating() {
        switch ((int) ratingBar.getRating()) {
            case 1:
                txt_RatingText.setText(R.string.rating_1);
                break;
            case 2:
                txt_RatingText.setText(R.string.rating_2);
                break;
            case 3:
                txt_RatingText.setText(R.string.rating_3);
                break;
            case 4:
                txt_RatingText.setText(R.string.rating_4);
                break;
            case 5:
                txt_RatingText.setText(R.string.rating_5);
                break;
            default:
                txt_RatingText.setText("");
        }
    }

    private void addControls() {
        token = PreferenceUtil.getAuthToken(RatingActivity.this);
        Intent intent = getIntent();
        if(intent.hasExtra(ID_ORDER)) {
            idOrder = intent.getStringExtra(ID_ORDER);
        }

        ratingBar = findViewById(R.id.ratingBar);
        txt_RatingText = findViewById(R.id.txt_RatingText);
        txt_comment = findViewById(R.id.txt_comment);
        btn_send_feedback = findViewById(R.id.btn_send_feedback);
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_rating_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            finish();
            return true;
        }
        return false;
    }
}
