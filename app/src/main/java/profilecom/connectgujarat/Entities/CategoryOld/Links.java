
package profilecom.connectgujarat.Entities.CategoryOld;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Links {

    @SerializedName("self")
    @Expose
    private List<Self> self = new ArrayList<Self>();
    @SerializedName("collection")
    @Expose
    private List<Collection> collection = new ArrayList<Collection>();
    @SerializedName("about")
    @Expose
    private List<About> about = new ArrayList<About>();
    @SerializedName("wp:post_type")
    @Expose
    private List<WpPostType> wpPostType = new ArrayList<WpPostType>();
    @SerializedName("curies")
    @Expose
    private List<Cury> curies = new ArrayList<Cury>();

    /**
     * 
     * @return
     *     The self
     */
    public List<Self> getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(List<Self> self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The collection
     */
    public List<Collection> getCollection() {
        return collection;
    }

    /**
     * 
     * @param collection
     *     The collection
     */
    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    /**
     * 
     * @return
     *     The about
     */
    public List<About> getAbout() {
        return about;
    }

    /**
     * 
     * @param about
     *     The about
     */
    public void setAbout(List<About> about) {
        this.about = about;
    }

    /**
     * 
     * @return
     *     The wpPostType
     */
    public List<WpPostType> getWpPostType() {
        return wpPostType;
    }

    /**
     * 
     * @param wpPostType
     *     The wp:post_type
     */
    public void setWpPostType(List<WpPostType> wpPostType) {
        this.wpPostType = wpPostType;
    }

    /**
     * 
     * @return
     *     The curies
     */
    public List<Cury> getCuries() {
        return curies;
    }

    /**
     * 
     * @param curies
     *     The curies
     */
    public void setCuries(List<Cury> curies) {
        this.curies = curies;
    }

}
