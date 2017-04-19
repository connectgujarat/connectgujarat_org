package profilecom.connectgujarat.Services;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.Comments.Comment;


public class CommentAddRF {
    private boolean bExecuted = false;
    ICommentAddComment service = null;

    private List<CommentListener> listeners
            = new ArrayList<CommentListener>(0);

    // private static int val=0;

    public void addListener(CommentListener toAdd) {
        listeners.add(toAdd);
    }


    public CommentAddRF() {
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ICommentAddComment.class);

        //Log.d("url","url=>"+service.toString());

    }


    public void AddAComment(final int postId, CommentBody commentBody) {

        Call<List<Comment>> repoRetComments = service.AddAComment(postId, commentBody);
        Log.d("url", "url(comments)=>" +
                service.AddAComment(postId, commentBody).request().url().toString());
        bExecuted = false;

        repoRetComments.enqueue(new Callback<List<Comment>>() {

            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {


              //  if (response.isSuccessful()) {



                    listeners.get(0).CommentSuccess(new ArrayList<CommentDetails>());
                    bExecuted = true;

              /*  } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }*/
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {


              //  listeners.get(0).CommentFailure(null);
                bExecuted = true;

                // something went completely south (like no internet connection)
               // Log.d("Error", t.getMessage());
                //  listeners.get(0).PostDownloaded(null);
            }
        });
    }

}

