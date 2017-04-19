package profilecom.connectgujarat.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.Post.PostDetail;


public interface IPostOldPosts
{
    @GET("wp-json/wp/v2/posts?orderby=date&order=desc")
    Call<List<PostDetail>> GetPostsOldPosts(
            @Query("categories") int categoryId,
            @Query("posts_per_page") int perPage,
            @Query("page") int pageNumber,
            @Query("before") String beforeDate
            );
}


//http://connectgujarat.com/wp-json/wp/v2/posts?categories=23&filter[posts_per_page]=2&page=1&filter[orderby]=date&filter[order]=desc&before=2016-07-29T15:25:25

//http://connectgujarat.com/wp-json/wp/v2/posts?categories=23&filter[posts_per_page]=1&page=1&filter[orderby]=date&filter[order]=desc&before=2016-07-29T15:25:25