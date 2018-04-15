package ec.com.ecadmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ec.com.ecadmin.R;
import ec.com.ecadmin.helper.AppConstants;
import ec.com.ecadmin.model.Post;

/**
 * Created by Anish on 4/15/2018.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    private Context context;
    private List<Post> postList;
    private TextView txtUserName;
    private TextView txtTitle;
    private TextView txtDisc;
    private android.widget.Button btnApprove;
    private ApproveListener approveListener;

    public PostsAdapter(Context context, ApproveListener approveListener) {
        this.approveListener = approveListener;
        this.context = context;
    }

    public void setPostAdapter(List<Post> postList) {
        this.postList = new ArrayList<>();
        this.postList = postList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.txtName.setText(postList.get(position).get);
        holder.txtDisc.setText(postList.get(position).getTitle());
        holder.txtTitle.setText(postList.get(position).getTitle());
//        holder.txtName.setText(postList.get(position).getTitle());
        Log.e("url",AppConstants.GLIDE_BASE_URL + postList.get(position).getImage());
        Glide.with(context).load(AppConstants.GLIDE_BASE_URL + postList.get(position).getImage()).into(holder.imgPost);
        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveListener.onApprove(postList.get(holder.getAdapterPosition()).getId(), postList.get(holder.getAdapterPosition()).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (postList != null && postList.size() > 0 ? postList.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDisc, txtTitle, txtUserName;
        private ImageView imgPost;
        private Button btnApprove;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.btnApprove = (Button) itemView.findViewById(R.id.btnApprove);
            this.txtDisc = (TextView) itemView.findViewById(R.id.txtDisc);
            this.txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            this.imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
//            this.txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
        }
    }

    public interface ApproveListener {
        void onApprove(String postId, String userId);
    }

}
