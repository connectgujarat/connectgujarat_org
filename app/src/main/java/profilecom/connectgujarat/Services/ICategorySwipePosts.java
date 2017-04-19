package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.NewCategoryPosts.GetCategoryPost;



public interface ICategorySwipePosts
{
    @GET("sanitizejson/get_posts")
    Call<GetCategoryPost> GetSwipedPosts(
            @Query("cat") int catid,
            @Query("date_query[after]") String after,
            @Query("count") int count);
}

//http://connectgujarat.com/api/get_posts/?cat=23&date_query%5Bafter%5D=2016-08-28T13:10:00&count=1


//http://139.59.48.164/sanitjson?url=http://connectgujarat.com/api/get_posts/&cat=68&date_query[after]=2017-03-02T12:05:02&count=10

//http://connectgujarat.com/api/get_category_posts/?id=23&count=20
