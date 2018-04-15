package ec.com.ecadmin.api;

import ec.com.ecadmin.model.BaseResponse;
import ec.com.ecadmin.model.GetPostRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Anish on 4/15/2018.
 */

public interface Service {
    @GET("GetPosts.php")
    Call<GetPostRes> getPosts(@Query("Type") int type);

    @GET("ApprovePost.php")
    Call<BaseResponse> getApproveStatus(@Query("PostId") String postId, @Query("UserId") String userId);

}
