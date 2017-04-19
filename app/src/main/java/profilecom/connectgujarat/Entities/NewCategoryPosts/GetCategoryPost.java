
package profilecom.connectgujarat.Entities.NewCategoryPosts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class GetCategoryPost {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("count_total")
    @Expose
    private Integer count_total;

    @SerializedName("pages")
    @Expose
    private Integer pages;

    @SerializedName("posts")
    @Expose
    private List<Posts> posts;

    @SerializedName("category")
    @Expose
    private Category category;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount_totol() {
        return count_total;
    }

    public void setCount_totol(Integer count_totol) {
        this.count_total = count_totol;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "{" +
                " status='" + status + '\'' +
                ", count=" + count +
                ", count_total=" + count_total +
                ", pages=" + pages +
                ", posts=" + posts +
                ", category=" + category +
                '}';
    }
}
