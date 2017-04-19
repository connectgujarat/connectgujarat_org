package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.NewCategoryPosts.GetCategoryPost;



public interface IGetLatestPost
{
    @GET("sanitizehomepage")
    Call<GetCategoryPost> GetLatestPost(
      // @Query("count") int count
    );
}



//http://connectgujarat.com/?json=get_recent_posts&count=1
