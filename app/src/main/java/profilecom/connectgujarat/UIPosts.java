package profilecom.connectgujarat;

public class UIPosts {

    int id;
    String type;
    String title;
    String status;
    String content;
    String date;
    String modified;
    String authorName;
    String attUrl;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", modified='" + modified + '\'' +
                ", authorName='" + authorName + '\'' +
                ", attUrl='" + attUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAttUrl() {
        return attUrl;
    }

    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    /*       private static final String KEY_ID = "id";
            private static final String KEY_type = "type";
            private static final String KEY_title = "title";
            private static final String KEY_status = "status";
            private static final String KEY_cotent = "content";
            private static final String KEY_date = "date";
            private static final String KEY_modified = "modified";
            private static final String KEY_author_name = "authorname";
            private static final String KEY_attachment_url = "atturl";*/}
