
package profilecom.connectgujarat.Entities.NewCategoryPostsModified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("slug")
    @Expose
    private transient String slug;
    @SerializedName("title")
    @Expose
    private transient String title;
    @SerializedName("description")
    @Expose
    private transient String description;
    @SerializedName("caption")
    @Expose
    private transient String caption;
    @SerializedName("parent")
    @Expose
    private transient Integer parent;
    @SerializedName("mime_type")
    @Expose
    private transient String mimeType;
    @SerializedName("images")
    @Expose
    private transient Images images;

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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
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
                ", mimeType='" + mimeType + '\'' +
                ", images=" + images +
                '}';
    }
}
