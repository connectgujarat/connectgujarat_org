package profilecom.connectgujarat.Services;

import java.util.ArrayList;

public interface CommentListener {
    void CommentSuccess(ArrayList<CommentDetails> commentDetailses);
    void CommentFailure(ArrayList<CommentDetails> commentDetailses);

}
