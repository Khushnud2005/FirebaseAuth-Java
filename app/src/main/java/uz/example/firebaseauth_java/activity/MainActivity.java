package uz.example.firebaseauth_java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.manager.AuthManager;

public class MainActivity extends BaseActivity {
    Button signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOut = findViewById(R.id.btn_signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.signOut();
                openSignInActivity(context);
            }
        });
    }
}