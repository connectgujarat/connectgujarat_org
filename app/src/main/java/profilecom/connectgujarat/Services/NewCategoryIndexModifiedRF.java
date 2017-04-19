package profilecom.connectgujarat.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.NewCategoryModified.Example;

public class NewCategoryIndexModifiedRF {
    private List<Example> mMediaContent;
    private boolean bExecuted = false;
    ICategoryModified service = null;
    private List<NewApiMoidifiedCategoryListner> listeners
            =
            new ArrayList<NewApiMoidifiedCategoryListner>(0);

    public void addListener(NewApiMoidifiedCategoryListner toAdd) {
        listeners.add(toAdd);
    }


    public NewCategoryIndexModifiedRF(){
        String url =
         "http://connectgujarat.com/";
        //Resources.getSystem().getString(R.string.main_url);


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
              //  .client(new OkHttpClient.Builder().readTimeout(6, TimeUn))
                .build();

         service = retrofit.create(ICategoryModified.class);

        //Log.d("url","url=>"+service.toString());


    }


    public List<Example> getCategoryIndexModified() {

        Call<List<Example>> repoMedia = service.GetCategoryIndexModified();
        Log.d("NewCatIndexService", "NewCatIndexService-url=>"+ service.GetCategoryIndexModified().request().url().toString());
        bExecuted = false;

        repoMedia.enqueue(new Callback<List<Example>>() {

            @Override
            public void onResponse(Call<List<Example>> call,
                                   Response<List<Example>> response) {

                Log.d("NewCatIndexService=>", "NewCatIndexService(actual)"+response+ "");
                if (response.isSuccessful()) {
                  //  Log.d("sucess", String.valueOf(response.body().size()));
                    //Log.d("mediaresp=>", response.body().toString());
                    mMediaContent = response.body();
                    //Log.d("NewCatIndexService=> ","NewCatIndexService(acc)=> "
                     //       + response.body().toString());

                    Gson gson = new Gson();


                    List<Example> gci =
                            gson.fromJson(mMediaContent.toString(), new TypeToken<List<Example>>() {}.getType());
                    Log.d("NewCatIndexService=> ","NewCatIndexService=> "+ gci.toString());

                    listeners.get(0).PostDownloaded(gci);

                    bExecuted = true;

                } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<List<Example>> call, Throwable t) {
                bExecuted = true;
                // Log.d("mediaresp=>", "error");
               // Log.d("mediarespc=>", t. + "");
                // something went completely south (like no internet connection)
                //Log.d("Error", t.getMessage());
            }
        });
        return mMediaContent;
    }
}
