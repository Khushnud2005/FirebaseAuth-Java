package uz.example.firebaseauth_java.manager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AuthManager {

    static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    static FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    public static Boolean isSignedIn(){
        return firebaseUser != null;
    }

    public static void signIn(String email, String password, AuthHandler handler){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                handler.onSuccess();
            }else{
                String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                handler.onError(task.getException(),errorCode);
            }
        }) ;

    }

    public static void signUp(String email, String password, AuthHandler handler){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                handler.onSuccess();
            }else{
                String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                handler.onError(task.getException(),errorCode);
            }
        }) ;

    }
    public static void signOut(){
        firebaseAuth.signOut();
    }
}
