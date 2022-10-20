package uz.example.firebaseauth_java.manager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uz.example.firebaseauth_java.model.Post;

public class DatabaseManager {
     static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
     static DatabaseReference referance = database.child("posts");


    public static void storePost(Post post, DatabaseHandler handler){
        String key = referance.push().getKey();
        if (key == null)return ;

        post.setId(key);
        referance.child(key).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError();
            }
        });
    }

    public static void loadPosts(DatabaseHandler handler){
        referance.addValueEventListener(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);
                        posts.add(post);
                    }
                    handler.onSuccess(posts);
                }else{
                    handler.onSuccess(posts = new ArrayList());
                }
            }
            @Override
            public void onCancelled(DatabaseError error ) {
                handler.onError();
            }
        });
    }
    public static void deletePost(String id, DatabaseHandler handler){

        referance.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError();
            }
        });
    }
    public static void editPost(Post post, DatabaseHandler handler){

        referance.child(post.getId()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError();
            }
        });
    }
}
