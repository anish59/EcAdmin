package ec.com.ecadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ec.com.ecadmin.adapter.PostsAdapter;
import ec.com.ecadmin.api.Service;
import ec.com.ecadmin.helper.FuntionsHelper;
import ec.com.ecadmin.model.BaseResponse;
import ec.com.ecadmin.model.GetPostRes;
import ec.com.ecadmin.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.RecyclerView rvPosts;
    private PostsAdapter postsAdapter;
    private Context context;
    private List<Post> postList;
    private ProgressDialog progressDialog;
    private android.widget.TextView txtEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        this.txtEmptyView = (TextView) findViewById(R.id.txtEmptyView);
        this.rvPosts = (RecyclerView) findViewById(R.id.rvPosts);

        init();

    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        postList = new ArrayList<>();
        progressDialog.setMessage("Loading..");

        initAdapter();
        callService();
    }

    private void callService() {
        if (!FuntionsHelper.isNetworkAvailable(context)) {
            Toast.makeText(context, "Connect to internet", Toast.LENGTH_SHORT).show();
            return;
        }
        postList = new ArrayList<>();

        progressDialog.show();
        AppApplication.getRetrofit().create(Service.class).getPosts(0).enqueue(new Callback<GetPostRes>() {
            @Override
            public void onResponse(Call<GetPostRes> call, Response<GetPostRes> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body().getStatus() == 1) {
                    if (response.body().getData().size() > 0) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (response.body().getData().get(i).getStatus().equals("0")) {
                                postList.add(response.body().getData().get(i));

                            }
                        }
                        postsAdapter.setPostAdapter(postList);
                        setEmptyView(false);
                    } else {
                        setEmptyView(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPostRes> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(context, "Error while Processing", Toast.LENGTH_SHORT).show();
                Log.e("failure", t.toString());
                setEmptyView(true);
            }
        });
    }

    private void initAdapter() {
        postsAdapter = new PostsAdapter(context, new PostsAdapter.ApproveListener() {
            @Override
            public void onApprove(String postId, String userId) {
                if (!FuntionsHelper.isNetworkAvailable(context)) {
                    Toast.makeText(context, "Connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                AppApplication.getRetrofit().create(Service.class).getApproveStatus(postId, userId).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful() && response.body().getStatus() == 1) {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            callService();
                        } if (response.body() != null && response.body().getMessage() != null) {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Oops there seems to be some issue, please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        progressDialog.dismiss();

                        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postsAdapter);
    }

    private void setEmptyView(boolean isNoData) {
        if (isNoData) {
            txtEmptyView.setVisibility(View.VISIBLE);
            rvPosts.setVisibility(View.GONE);
        } else {
            txtEmptyView.setVisibility(View.GONE);
            rvPosts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callService();
    }
}
