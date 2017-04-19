package profilecom.connectgujarat.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.CategoryOld.PostCategory;


public class CategoryService {

    public List<PostCategory> getCategories()
    {
        String url = "http://connectgujarat.com"; //Resources.getSystem().getString(R.string.main_url);
        List<PostCategory> categories;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ICategories service = retrofit.create(ICategories.class);
        Call<List<PostCategory>> repos = service.GetCategories(2);


        repos.enqueue(new Callback<List<PostCategory>>() {
            @Override
            public void onResponse(Call<List<PostCategory>> call, Response<List<PostCategory>> response) {
                if (response.isSuccessful()) {
                    //Log.d("sucess", String.valueOf(response.body().size()));


                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<List<PostCategory>> call, Throwable t) {
                // something went completely south (like no internet connection)
                //Log.d("Error", t.getMessage());
            }
        });

        return null;
    }
}
