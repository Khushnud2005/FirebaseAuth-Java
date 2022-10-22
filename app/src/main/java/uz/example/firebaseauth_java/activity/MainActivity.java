package uz.example.firebaseauth_java.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.adapter.PostAdapter;
import uz.example.firebaseauth_java.manager.AuthManager;
import uz.example.firebaseauth_java.manager.DatabaseHandler;
import uz.example.firebaseauth_java.manager.DatabaseManager;
import uz.example.firebaseauth_java.manager.StorageHandler;
import uz.example.firebaseauth_java.manager.StorageManager;
import uz.example.firebaseauth_java.model.Post;

public class MainActivity extends BaseActivity {
    ImageView signOut;
    FloatingActionButton fab_create;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }
    void initViews(){
        signOut = findViewById(R.id.iv_signOut);
        fab_create = findViewById(R.id.fab_create);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( new GridLayoutManager(this,1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        apiLoadPosts();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.signOut();
                openSignInActivity(context);
            }
        });
        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCreateActivity();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String edit_title = extras.getString("title");
            String edit_body = extras.getString("body");
            String edit_id = extras.getString("id");
            if (extras.getString("image") != null){
                String edited_image = extras.getString("image");
                Log.d("###", "image not NULL - "+ edited_image);
                Post post = new Post(edit_id, edit_title, edit_body,edited_image);
                apiEditPost(post);
            }else {
                Log.d("###", "image - null");
                Post post = new Post(edit_id, edit_title, edit_body);
                apiEditPost(post);
            }


        }
    }

    private void refreshAdapter(ArrayList<Post> posts){
        PostAdapter adapter = new PostAdapter(this,posts);
        recyclerView.setAdapter(adapter);
    }
    public void dialogPoster(Post post) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Poster")
                .setMessage("Are you sure you want to delete this poster?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (post.getImage()!=null){
                            apiDeletePhoto(post);
                        }{
                            apiPostDelete(post);
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void apiDeletePhoto(Post post) {
        StorageManager.deletePhoto(post.getImage());
        apiPostDelete(post);
    }


    private void apiLoadPosts() {
        showLoading(this);
        DatabaseManager.loadPosts(new DatabaseHandler() {

            @Override
            public void onSuccess() {}
            @Override
            public void onError() {
                dismissLoading();
            }
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                dismissLoading();
                refreshAdapter(posts);

            }


        });
    }
    private void apiEditPost(Post post) {
        showLoading(this);
        DatabaseManager.editPost(post,new DatabaseHandler(){
            @Override
            public void onSuccess() {
                dismissLoading();
                apiLoadPosts();
            }
            @Override
            public void onError() {
                dismissLoading();
            }

            @Override
            public void onSuccess(ArrayList<Post> posts) {}

        });
    }
    private void apiPostDelete(Post post) {
        showLoading(this);
        DatabaseManager.deletePost(post.getId(),new DatabaseHandler(){
            @Override
            public void onSuccess() {
                dismissLoading();
                apiLoadPosts();
            }
            @Override
            public void onError() {
                dismissLoading();
            }

            @Override
            public void onSuccess(ArrayList<Post> posts) {}

        });

    }
    public ActivityResultLauncher<Intent> resultlauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        apiLoadPosts();
                    }

                }
            });
    private void callCreateActivity() {
        Intent intent = new Intent(context,CreateActivity.class);
        resultlauncher.launch(intent);
    }
}