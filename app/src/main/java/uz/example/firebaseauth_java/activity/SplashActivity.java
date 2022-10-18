package uz.example.firebaseauth_java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.manager.AuthManager;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initViews();
    }
    void initViews(){
        countDownTimer();
    }
    void countDownTimer(){
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (AuthManager.isSignedIn()) {
                    openMainActivity(context);
                }else{
                    openSignInActivity(context);
                }
            }
        }.start();
    }
}