
package profilecom.connectgujarat.Entities.NewCategoryPostsModified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExamplePosts {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("count_total")
    @Expose
    private Integer countTotal;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("query")
    @Expose
    private Query query;

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

    public Integer getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", count=" + count +
                ", countTotal=" + countTotal +
                ", pages=" + pages +
                ", posts=" + posts +
                ", query=" + query +
                '}';
    }
}
