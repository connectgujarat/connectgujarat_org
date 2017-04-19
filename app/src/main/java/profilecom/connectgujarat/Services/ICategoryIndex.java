package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import profilecom.connectgujarat.Entities.NewCategoryIndex.GetCategoryIndex;


public interface ICategoryIndex {
    @GET("api/get_category_index/")
    Call<GetCategoryIndex> GetCategoryIndex();
}


//http://connectgujarat.com/api/get_category_index/