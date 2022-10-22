package uz.example.firebaseauth_java.manager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
public class StorageManager {

    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference();
    static StorageReference photosRef = storageRef.child("photos");

    public static void uploadPhoto(Uri uri,String ext, StorageHandler handler){
        String fileName = ""+System.currentTimeMillis()+"."+ext;
        UploadTask uploadTask = photosRef.child(fileName).putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                Task<Uri> result = snapshot.getMetadata().getReference().getDownloadUrl();
                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imgUri = uri.toString();
                        handler.onSuccess(imgUri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onError(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });

    }
    public static void deletePhoto(String url){
        StorageReference photoRef = storage.getReferenceFromUrl(url);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("@@@Delete", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("@@@Delete", "onFailure: did not delete file");
            }
        });
    }
}
