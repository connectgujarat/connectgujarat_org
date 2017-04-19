package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Collections;

import profilecom.connectgujarat.NewsDetailsActivity;

public class CommentRetreiveService extends Service implements CommentListener {
    CommentRetrieveRF postService = new CommentRetrieveRF();

    public CommentRetreiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        postService.addListener(this);

        // Log.d("onStartCommand", "onStartCommand called");

        int postId = intent.getExtras().getInt("post", 1);
        String afterDate = intent.getExtras().getString("after", "--");
        int perPage = intent.getExtras().getInt("per_page");

        postService.
                getRetreivedComments(postId, afterDate.replace(" ", "T"), perPage);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void CommentSuccess(ArrayList<CommentDetails> commentDetails) {


        Collections.reverse(commentDetails);

        NewsDetailsActivity.allCommentsFromServer = commentDetails;

        Intent intent = new Intent();

        intent.setAction(NewsDetailsActivity.MY_LOCAL_ACTION);

        intent.putExtra("comments", 100);

        sendBroadcast(intent);

        stopSelf();
    }

    @Override
    public void CommentFailure(ArrayList<CommentDetails> commentDetails) {



        Intent intent = new Intent();

        intent.setAction(NewsDetailsActivity.MY_LOCAL_ACTION);

        intent.putExtra("comments", 1000);

        sendBroadcast(intent);

        stopSelf();
    }
}
