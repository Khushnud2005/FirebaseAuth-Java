package uz.example.firebaseauth_java.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.ArrayList;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.manager.DatabaseHandler;
import uz.example.firebaseauth_java.manager.DatabaseManager;
import uz.example.firebaseauth_java.manager.StorageHandler;
import uz.example.firebaseauth_java.manager.StorageManager;
import uz.example.firebaseauth_java.model.Post;

public class CreateActivity extends BaseActivity {
    EditText et_title;
    EditText et_body;
    ImageView iv_close;
    ImageView iv_photo;
    ImageView iv_camera;
    Button btn_create;
    Uri pickedPhoto;
    String exten;
    ArrayList<Uri> allPhotos  = new ArrayList<Uri>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
    }
    void initViews(){
        et_title = findViewById(R.id.et_title);
        et_body = findViewById(R.id.et_body);
        iv_close = findViewById(R.id.iv_close);
        iv_photo = findViewById(R.id.iv_photo);
        iv_camera = findViewById(R.id.iv_camera);
        btn_create = findViewById(R.id.btn_create);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickProfilePhoto();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String body = et_body.getText().toString().trim();
                if (!title.isEmpty() && !body.isEmpty()){
                    Post post = new Post(title,body);
                    apiStoreNewPost(post);
                }else{
                    Toast.makeText(context, "Enter Post Parameters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public ActivityResultLauncher<Intent> photoLuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        if (result.getData() != null){
                            allPhotos = result.getData().getParcelableArrayListExtra(FishBun.INTENT_PATH);
                            pickedPhoto = allPhotos.get(0);
                            iv_photo.setImageURI(pickedPhoto);
                            String mimeType = getContentResolver().getType(pickedPhoto);
                            exten = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                            Log.d("@@@onLauncherExtension", exten);
                        }

                    }
                }
            });
    private void pickProfilePhoto() {
        allPhotos.clear();
        FishBun.with(this).setImageAdapter(new GlideAdapter())
                .setMinCount(1)
                .setMaxCount(1)
                .setCamera(true)
                .setSelectedImages(allPhotos)
                .startAlbumWithActivityResultCallback(photoLuncher);
    }
    private void apiStoreNewPost(Post post) {
        if (pickedPhoto != null){
            apiStoreStorage(post);
        }else{
            storeDatabase(post);
        }
    }

    private void apiStoreStorage(Post post) {
        showLoading(this);
        Log.d("###",pickedPhoto.toString());
        StorageManager.uploadPhoto(pickedPhoto,exten, new StorageHandler() {
            @Override
            public void onSuccess(String imgUri) {
                post.setImage(imgUri);
                storeDatabase(post);
            }

            @Override
            public void onError(Exception exception) {
                storeDatabase(post);
            }
        });
    }

    private void storeDatabase(Post post) {
        showLoading(this);
        DatabaseManager.storePost(post, new DatabaseHandler() {
            @Override
            public void onSuccess() {
                dismissLoading();
                finishIntent();
                Log.d("###","onSuccess()");
            }

            @Override
            public void onError() {
                dismissLoading();
            }
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                Log.d("###","onSuccess(ArrayList<Post> posts)");
            }


        });
    }
    void finishIntent(){
        Intent returnIntent = this.getIntent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}