package profilecom.connectgujarat.Services;


public class CommentDetails {

    int commentId;
    int postId;
    String authorname;
    String date;
    String commentContent;
    String status;

    public CommentDetails (){

    }

    public CommentDetails (int a, int b, String c, String d, String e, String f){

        this.commentId = a;
        this.postId = b;
        this.authorname = c;
        this.date = d;
        this.commentContent = e;
        this.status = f;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", authorname='" + authorname + '\'' +
                ", date='" + date + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
