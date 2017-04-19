package profilecom.connectgujarat.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import profilecom.connectgujarat.Entities.NewCategoryModified.Example;


public interface ICategoryModified {
    @GET("/wp-json/wp/v2/categories?order=asc&orderby=include&per_page=40&include=24,20,23,84,26,13,72,34,19,62,33,35,69,12")
    Call<List<Example>> GetCategoryIndexModified();
}


//http://connectgujarat.com//wp-json/wp/v2/categories?order=desc&orderby=description&per_page=40