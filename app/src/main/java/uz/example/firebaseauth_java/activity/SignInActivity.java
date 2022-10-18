package uz.example.firebaseauth_java.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.manager.AuthHandler;
import uz.example.firebaseauth_java.manager.AuthManager;

public class SignInActivity extends BaseActivity {
    EditText et_email, et_password;
    TextView tv_signUp;
    Button btn_signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initViews();
    }
    void initViews() {
        et_email = findViewById(R.id.et_emailSi);
        et_password = findViewById(R.id.et_passwordSi);
        tv_signUp = findViewById(R.id.tv_signup);

        btn_signIn = findViewById(R.id.btn_signIn);
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()){
                    firebaseSignIn(email,password);
                }else{
                    String str = "Email or Password is Empty! \nPlease Try Again!";
                    Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
    }

    private void firebaseSignIn(String email, String password) {
        showLoading(this);
        AuthManager.signIn(email,password,new  AuthHandler(){
            @Override
            public void onSuccess(){
                dismissLoading();
                openMainActivity(context);
                Toast.makeText(context,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception exception, String code) {
                dismissLoading();
                switch (code) {

                    case "ERROR_INVALID_EMAIL" :
                        et_email.setError(getString(R.string.error_invalid_email));
                        et_email.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                       Toast.makeText(context,exception.getMessage(),Toast.LENGTH_SHORT).show();
                        break;
                    case "ERROR_WRONG_PASSWORD" :
                        et_password.setError(getString(R.string.error_wrong_password));
                        et_password.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                       Toast.makeText(context,exception.getMessage(),Toast.LENGTH_SHORT).show();
                        break;

                    case "ERROR_USER_NOT_FOUND" :
                        et_email.setError(getString(R.string.error_user_not_found));
                        et_email.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                       Toast.makeText(context,exception.getMessage(),Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d("@@@_error",code+" - "+exception);
                        Toast.makeText(context,exception.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }

        });
    }


    void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}