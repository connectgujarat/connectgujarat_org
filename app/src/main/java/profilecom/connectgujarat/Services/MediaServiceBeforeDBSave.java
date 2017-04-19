package profilecom.connectgujarat.Services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.Media.MediaContent;


public class MediaServiceBeforeDBSave {
    private MediaContent mMediaContent;
    private boolean bExecuted = false;
    IMedia service = null;
    String imagelink;

    public MediaServiceBeforeDBSave(){
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         service = retrofit.create(IMedia.class);
        //Log.d("url","url=>"+service.toString());


    }


    public String getMedia(String mediaID) {



        Call<MediaContent> repoMedia = service.GetMedia(mediaID);
        //Log.d("url", "url=>"+ service.GetMedia(mediaID).request().url().toString());
        bExecuted = false;

//        try {
//            Call<List<MediaContent>> repos2 = repos.clone();
//            mMediaContent = repos2.execute().body();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

       // Log.d("mediaresp=>","ediaService");

        repoMedia.enqueue(new Callback<MediaContent>() {

            @Override
            public void onResponse(Call<MediaContent> call,
                                   Response<MediaContent> response) {

                //Log.d("mediarespc=>", response.code() + "");
                if (response.isSuccessful()) {
                  //  Log.d("sucess", String.valueOf(response.body().size()));
                    //Log.d("mediaresp=>", response.body().toString());
                    mMediaContent = response.body();
                    //Log.d("mediacontent",mMediaContent.toString());
                    imagelink = mMediaContent.getGuid().getRendered();
                   bExecuted = true;
                   /* if(newsImageView!=null) {
                        Picasso.with(mContext)
                                .load(mMediaContent.getGuid().getRendered())
                                        .placeholder(R.drawable.placeholder)
                                .into(newsImageView);


                    }

                    else
                        Log.d("newsImageView","newsImageView is null");*/
                } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<MediaContent> call, Throwable t) {
                bExecuted = true;
                // Log.d("mediaresp=>", "error");
               // Log.d("mediarespc=>", t. + "");
                // something went completely south (like no internet connection)
                //Log.d("Error", t.getMessage());
            }
        });
        return  imagelink;
    }
}
