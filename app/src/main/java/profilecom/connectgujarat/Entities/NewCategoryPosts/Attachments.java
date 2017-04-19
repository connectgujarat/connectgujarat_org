
package profilecom.connectgujarat.Entities.NewCategoryPosts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Attachments {


    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("title")
    @Expose
    private   String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("caption")
    @Expose
    private  String caption;

    @SerializedName("parent")
    @Expose
    private Integer parent;

    @SerializedName("mime_type")
    @Expose
    private String mime_type;

    @SerializedName("images")
    @Expose
    private transient List<Images> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", caption='" + caption + '\'' +
                ", parent=" + parent +
                ", mime_type='" + mime_type + '\'' +
                ", images=" + images +
                '}';
    }
}
