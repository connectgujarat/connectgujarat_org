package profilecom.connectgujarat.Services;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import profilecom.connectgujarat.Entities.CategoryOld.PostCategory;



public interface ICategories {
    @GET("gu/wp-json/wp/v2/categories?")
    Call<List<PostCategory>> GetCategories(@Query("per_page") int per_page);
}
