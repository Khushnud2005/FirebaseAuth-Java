package uz.example.firebaseauth_java.manager;

import androidx.annotation.Nullable;

import java.util.AbstractList;
import java.util.ArrayList;

import uz.example.firebaseauth_java.model.Post;

public interface DatabaseHandler {

    void onSuccess();
    //void onSuccess(@Nullable Post post, ArrayList<Post> posts);
    void onError();

    void onSuccess(ArrayList<Post> posts);
}
