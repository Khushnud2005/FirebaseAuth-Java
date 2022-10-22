package uz.example.firebaseauth_java.manager;

public interface StorageHandler {
    public void onSuccess(String imgUri);
    public void onError(Exception exception);
}
