package uz.example.firebaseauth_java.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.ArrayList;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.manager.StorageHandler;
import uz.example.firebaseauth_java.manager.StorageManager;

public class EditActivity extends BaseActivity {
    EditText et_title;
    EditText et_body;
    ImageView iv_close;
    ImageView iv_photo_edit;
    ImageView iv_camera_edit;
    Button btn_edit;
    String id;
    String exten;
    String old_image = null;
    Uri pickedPhoto = null;
    ArrayList<Uri> allPhotos = new ArrayList<Uri>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    void initViews(){
        et_title = findViewById(R.id.et_title_edit);
        et_body = findViewById(R.id.et_body_edit);
        iv_close = findViewById(R.id.iv_close_edit);
        btn_edit = findViewById(R.id.btn_update);
        iv_photo_edit = findViewById(R.id.iv_photo_edit);
        iv_camera_edit = findViewById(R.id.iv_camera_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            et_title.setText(extras.getString("title"));
            et_body.setText(extras.getString("body"));
            if (extras.getString("image") != null){
                old_image = extras.getString("image");

                Glide.with(this).asBitmap().load(old_image).into(iv_photo_edit);
            }
            id = extras.getString("id");
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_camera_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickProfilePhoto();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String body = et_body.getText().toString().trim();
                if (!title.isEmpty() && !body.isEmpty()){
                    Intent intent = new  Intent(context, MainActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("body", body);
                    intent.putExtra("id", id);
                    if (old_image != null){
                        intent.putExtra("image", old_image);
                    }
                    startActivity(intent);
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
                            iv_photo_edit.setImageURI(pickedPhoto);
                            String mimeType = getContentResolver().getType(pickedPhoto);
                            exten = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                            apiStoreStorage(pickedPhoto);
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
    private void apiStoreStorage(Uri pickedPhoto) {
        if (old_image != null){
            StorageManager.deletePhoto(old_image);
        }

        StorageManager.uploadPhoto(pickedPhoto,exten, new StorageHandler() {
            @Override
            public void onSuccess(String imgUri) {
                old_image = imgUri;
            }

            @Override
            public void onError(Exception exception) {
                old_image = null;
            }
        });
    }
}