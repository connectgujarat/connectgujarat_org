package profilecom.connectgujarat.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.Comments.Comment;


public interface ICommentRetreiveComments
{
    @GET("/wp-json/wp/v2/comments")
    Call<List<Comment>> GetAllComments(
            @Query("post") int postId,
            @Query("before") String afterDate,
            @Query("per_page") int perPageCount

    );
}
//http://connectgujarat.com/wp-json/wp/v2/comments?post=11947