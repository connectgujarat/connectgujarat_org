
package profilecom.connectgujarat.Entities.NewCategoryPosts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

import profilecom.connectgujarat.Entities.NewCategoryIndex.Categories;

@Generated("org.jsonschema2pojo")
public class Posts {


    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("title_plain")
    @Expose
    private String title_plain;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("excerpt")
    @Expose
    private String excerpt;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("categories")
    @Expose
    private List<Categories> categories;

    @SerializedName("tags")
    @Expose
    private List<Tags> tags;

    @SerializedName("author")
    @Expose
    private Author author;

    @SerializedName("comments")
    @Expose
    private List<Comments> comments;

    @SerializedName("attachments")
    @Expose
    private List<Attachments> attachments;

    @SerializedName("comment_count")
    @Expose
    private Integer comment_count;

    @SerializedName("comment_status")
    @Expose
    private String comment_status;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("custom_fields")
    @Expose
    private CustomFields custom_fields;

    @SerializedName("thumbnail_size")
    @Expose
    private String thumbnail_size;

    @SerializedName("thumbnail_images")
    @Expose
    private transient List<ThumbnailImages> thumbnail_images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_plain() {
        return title_plain;
    }

    public void setTitle_plain(String title_plain) {
        this.title_plain = title_plain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
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

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public CustomFields getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(CustomFields custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getThumbnail_size() {
        return thumbnail_size;
    }

    public void setThumbnail_size(String thumbnail_size) {
        this.thumbnail_size = thumbnail_size;
    }

    public List<ThumbnailImages> getThumbnail_images() {
        return thumbnail_images;
    }

    public void setThumbnail_images(List<ThumbnailImages> thumbnail_images) {
        this.thumbnail_images = thumbnail_images;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", slug='" + slug + '\'' +
                ", url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", title_plain='" + title_plain + '\'' +
                ", content='" + content + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", date='" + date + '\'' +
                ", modified='" + modified + '\'' +
                ", categories=" + categories +
                ", tags=" + tags +
                ", author=" + author +
                ", comments=" + comments +
                ", attachments=" + attachments +
                ", comment_count=" + comment_count +
                ", comment_status='" + comment_status + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", custom_fields=" + custom_fields +
                ", thumbnail_size='" + thumbnail_size + '\'' +
                ", thumbnail_images=" + thumbnail_images +
                '}';
    }
}
