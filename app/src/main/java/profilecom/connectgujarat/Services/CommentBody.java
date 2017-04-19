package profilecom.connectgujarat.Services;


public class CommentBody {

    String author_name;
    String author_email;
    String content;

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                "author_name='" + author_name + '\'' +
                ", author_email='" + author_email + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
