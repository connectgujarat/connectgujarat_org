package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface IUploadStory
{
    @POST("send-mail-api/")
    Call<String> UploadANews(
            @Body UploadStoryBody uploadStoryBody
    );
}

//http://connectgujarat.com/wp-json/wp/v2/comments?post=11947