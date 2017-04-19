package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.NewCategoryPostsModified.ExamplePosts;

public interface ICategoryPaginatePosts
{
    @GET("sanitizejson/get_posts")
    Call<ExamplePosts> GetCategoryPaginatePosts(
            @Query("cat") int catid,
            @Query("date_query[before]") String before,
            @Query("count") int count);
}

//http://connectgujarat.com/api/get_posts/?cat=23&date_query%5Bbefore%5D=2016-08-28T13:10:00&count=1

//http://139.59.48.164/sanitjson?url=http://connectgujarat.com/api/get_posts/&cat=68&date_query[before]=2017-03-02T12:05:02&count=10

//http://connectgujarat.com/api/get_category_posts/?id=23&count=20

//http://139.59.48.164/sanitizejson/get_posts?cat=68&date_query[before]=2017-03-02T12:05:02&count=10