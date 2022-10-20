package uz.example.firebaseauth_java.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.manager.DatabaseHandler;
import uz.example.firebaseauth_java.manager.DatabaseManager;
import uz.example.firebaseauth_java.model.Post;

public class CreateActivity extends BaseActivity {
    EditText et_title;
    EditText et_body;
    ImageView iv_close;
    Button btn_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
    }
    void initViews(){
        et_title = findViewById(R.id.et_title);
        et_body = findViewById(R.id.et_body);
        iv_close = findViewById(R.id.iv_close);
        btn_create = findViewById(R.id.btn_create);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String body = et_body.getText().toString().trim();
                if (!title.isEmpty() && !body.isEmpty()){
                    Post post = new Post(title,body);
                    storeDatabase(post);
                }else{
                    Toast.makeText(context, "Enter Post Parameters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeDatabase(Post post) {
        showLoading(this);
        DatabaseManager.storePost(post, new DatabaseHandler() {
            @Override
            public void onSuccess() {
                dismissLoading();
                finishIntent();
                Log.d("###","onSuccess()");
            }

            @Override
            public void onError() {
                dismissLoading();
            }
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                Log.d("###","onSuccess(ArrayList<Post> posts)");
            }


        });
    }
    void finishIntent(){
        Intent returnIntent = this.getIntent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}