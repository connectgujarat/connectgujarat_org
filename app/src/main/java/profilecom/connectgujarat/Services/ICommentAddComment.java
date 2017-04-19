package profilecom.connectgujarat.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.Comments.Comment;


public interface ICommentAddComment
{
    @POST("wp-json/wp/v2/comments")
    Call<List<Comment>> AddAComment(
            @Query("post") int postId,
            @Body CommentBody commentBody
       );
}

//http://connectgujarat.com/wp-json/wp/v2/comments?post=11947