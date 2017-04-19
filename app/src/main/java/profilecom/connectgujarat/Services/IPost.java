package profilecom.connectgujarat.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.Post.PostDetail;

public interface IPost
{
    @GET("wp-json/wp/v2/posts?orderby=date&order=desc")
    Call<List<PostDetail>> GetPosts(

    @Query("categories") int categoryId,
    @Query("posts_per_page") int perPage,
    @Query("page") int pageNumber,
    @Query("after") String after);
}
//http://connectgujarat.com/wp-json/wp/v2/posts?categories=23&filter%5Bposts_per_page%5D=2&page=1&filter%5Borderby%5D=date&filter%5Border%5D=desc&after=2016-07-30T08:52:57










//http://connectgujarat.com/wp-json/wp/v2/posts?categories=23&filter[posts_per_page]=2&page=1&filter[orderby]=date&filter[order]=desc

//http://connectgujarat.com/wp-json/wp/v2/posts?categories=23&filter[posts_per_page]=2&page=1&filter[orderby]=date&filter[order]=desc&before=2016-07-29T15:25:25

