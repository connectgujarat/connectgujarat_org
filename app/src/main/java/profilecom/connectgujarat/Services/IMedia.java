package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import profilecom.connectgujarat.Entities.Media.MediaContent;


public interface IMedia {
    @GET("/wp-json/wp/v2/media/{mediaId}")
    Call<MediaContent> GetMedia(@Path("mediaId") String mediaId);
}

//http://connectgujarat.com/wp-json/wp/v2/media/11948
//http://connectgujarat.com//wp-json/wp/v2/media/11598