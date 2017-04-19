package profilecom.connectgujarat.Entities.Comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Comment {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("post")
    @Expose
    private Integer post;

    @SerializedName("parent")
    @Expose
    private Integer parent;

    @SerializedName("author")
    @Expose
    private Integer author;

    @SerializedName("author_name")
    @Expose
    private String author_name;

    @SerializedName("author_url")
    @Expose
    private String author_url;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("date_gmt")
    @Expose
    private String date_gmt;

    @SerializedName("content")
    @Expose
    private Content content;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("type")
    @Expose
    private String type ;

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    @SerializedName("_links")
    @Expose
    private transient ArrayList<Links> links ;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", post=" + post +
                ", parent=" + parent +
                ", author=" + author +
                ", author_name='" + author_name + '\'' +
                ", author_url='" + author_url + '\'' +
                ", date='" + date + '\'' +
                ", date_gmt='" + date_gmt + '\'' +
                ", content=" + content +
                ", link='" + link + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", links=" + links +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_gmt() {
        return date_gmt;
    }

    public void setDate_gmt(String date_gmt) {
        this.date_gmt = date_gmt;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Links> links) {
        this.links = links;
    }
}
