package profilecom.connectgujarat.Services;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.NewCategoryIndex.GetCategoryIndex;


public class NewCategoryIndexRF {
    private GetCategoryIndex mMediaContent;
    private boolean bExecuted = false;
    ICategoryIndex service = null;
    private List<NewApiCategoryListner> listeners
            =
            new ArrayList<NewApiCategoryListner>(0);

    public void addListener(NewApiCategoryListner toAdd) {
        listeners.add(toAdd);
    }


    public NewCategoryIndexRF(){
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);



        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
              //  .client(new OkHttpClient.Builder().readTimeout(6, TimeUn))
                .build();

         service = retrofit.create(ICategoryIndex.class);

        //Log.d("url","url=>"+service.toString());


    }


    public GetCategoryIndex getCategoryIndex() {

        Call<GetCategoryIndex> repoMedia = service.GetCategoryIndex();
        // Log.d("NewCatIndexService", "NewCatIndexService-url=>" + service.GetCategoryIndex().request().url().toString());
        bExecuted = false;

        repoMedia.enqueue(new Callback<GetCategoryIndex>() {

            @Override
            public void onResponse(Call<GetCategoryIndex> call,
                                   Response<GetCategoryIndex> response) {

              //  Log.d("NewCatIndexService=>", "NewCatIndexService(actual)"+response+ "");
                if (response.isSuccessful()) {
                  //  Log.d("sucess", String.valueOf(response.body().size()));
                    //Log.d("mediaresp=>", response.body().toString());
                    mMediaContent = response.body();
                    //Log.d("NewCatIndexService=> ","NewCatIndexService(acc)=> "
                     //       + response.body().toString());

                    Gson gson = new Gson();


                    GetCategoryIndex gci =
                            gson.fromJson(mMediaContent.toString(), GetCategoryIndex.class);
                  //  Log.d("NewCatIndexService=> ","NewCatIndexService=> "+ gci);

                    listeners.get(0).PostDownloaded(gci);

                    bExecuted = true;

                } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<GetCategoryIndex> call, Throwable t) {
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
