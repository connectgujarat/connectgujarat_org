package profilecom.connectgujarat.Services;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StoryUploadRF {
    private boolean bExecuted = false;
    IUploadStory service = null;

    private List<UploadStoryListener> listeners
            = new ArrayList<UploadStoryListener>(0);

    // private static int val=0;

    public void addListener(UploadStoryListener toAdd) {
        listeners.add(toAdd);
    }


    public StoryUploadRF() {
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IUploadStory.class);

        //Log.d("url","url=>"+service.toString());

    }


    public void UploadANews( UploadStoryBody uploadStoryBody) {

        Call<String> repoRetComments = service.UploadANews( uploadStoryBody);
        Log.d("url", "url(uploadStoryBody)=>" +
                service.UploadANews(uploadStoryBody).request().url().toString());
        bExecuted = false;

        repoRetComments.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {


              //  if (response.isSuccessful()) {



                    listeners.get(0).UploadStorySuccess(200);
                    bExecuted = true;

              /*  } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


                listeners.get(0).UploadStoryFailure(200);
                bExecuted = true;

                // something went completely south (like no internet connection)
               // Log.d("Error", t.getMessage());
                //  listeners.get(0).PostDownloaded(null);
            }
        });
    }

}

