package uz.example.firebaseauth_java.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import uz.example.firebaseauth_java.R;
import uz.example.firebaseauth_java.activity.EditActivity;
import uz.example.firebaseauth_java.activity.MainActivity;
import uz.example.firebaseauth_java.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private MainActivity activity;
    private ArrayList<Post> items;

    public PostAdapter(MainActivity activity, ArrayList<Post> items) {
        this.activity = activity;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posts,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post item = items.get(position);
        SwipeLayout sl_swipe = holder.sl_swipe;
        sl_swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        sl_swipe.addDrag(SwipeLayout.DragEdge.Left, holder.linear_left);
        sl_swipe.addDrag(SwipeLayout.DragEdge.Right, holder.linear_right);


        holder.tv_title.setText(item.getTitle());
        holder.tv_body.setText(item.getBody());
        if (item.getImage() != null){
            holder.ll_image_layer.setVisibility(View.VISIBLE);
            Glide.with(activity).asBitmap().load(item.getImage()).into(holder.iv_photo_post);
        }

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.dialogPoster(item);
            }
        });

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), EditActivity.class);
                intent.putExtra("id",item.getId());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("body", item.getBody());
                if (item.getImage() != null){
                    intent.putExtra("image", item.getImage());
                }
                //activity.setResult(88,intent);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_body;
        LinearLayout linear_left;
        LinearLayout linear_right;
        LinearLayout ll_image_layer;
        SwipeLayout sl_swipe;
        TextView tv_delete;
        TextView tv_edit;
        ImageView iv_photo_post;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_item);
            tv_body = itemView.findViewById(R.id.tv_body_item);
            linear_right = itemView.findViewById(R.id.ll_linear_right);
            linear_left = itemView.findViewById(R.id.ll_linear_left);
            ll_image_layer = itemView.findViewById(R.id.ll_image_layer);
            sl_swipe = itemView.findViewById(R.id.sl_swipe);
            tv_delete = itemView.findViewById(R.id.tv_delete);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            iv_photo_post = itemView.findViewById(R.id.iv_photo_post);
        }
    }
}
