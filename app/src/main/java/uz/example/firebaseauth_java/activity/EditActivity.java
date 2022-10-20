package uz.example.firebaseauth_java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import uz.example.firebaseauth_java.R;

public class EditActivity extends BaseActivity {
    EditText et_title;
    EditText et_body;
    ImageView iv_close;
    Button btn_edit;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    void initViews(){
        et_title = findViewById(R.id.et_title_edit);
        et_body = findViewById(R.id.et_body_edit);
        iv_close = findViewById(R.id.iv_close_edit);
        btn_edit = findViewById(R.id.btn_update);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            et_title.setText(extras.getString("title"));
            et_body.setText(extras.getString("body"));
            id = extras.getString("id");
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String body = et_body.getText().toString().trim();

                if (!title.isEmpty() && !body.isEmpty()){
                    Intent intent = new  Intent(context, MainActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("body", body);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "Enter Post Parameters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}