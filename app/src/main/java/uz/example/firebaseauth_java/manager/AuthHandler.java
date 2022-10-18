package uz.example.firebaseauth_java.manager;

public interface AuthHandler {
    public void onSuccess();
    public void onError(Exception exception,String code);
}
