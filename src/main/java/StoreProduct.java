import com.rosieapp.jsonapi.v1.Resource;

/**
 * Describe file here.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class StoreProduct
implements Resource
{
    private int id;
    private int productId;
    private Store store;
    private String storeSku;
    private String title;
    private String shortDescription;
    private String longDescription;

    public StoreProduct(int id, int productId, Store store, String storeSku,
                        String title, String shortDescription,
                        String longDescription)
    {
        this.id                 = id;
        this.productId          = productId;
        this.store              = store;
        this.storeSku           = storeSku;
        this.title              = title;
        this.shortDescription   = shortDescription;
        this.longDescription    = longDescription;
    }

    public String getId()
    {
        return Integer.toString(id);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getProductId()
    {
        return this.productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public Store getStore()
    {
        return this.store;
    }

    public void setStore(Store store)
    {
        this.store = store;
    }

    public String getStoreSku()
    {
        return this.storeSku;
    }

    public void setStoreSku(String storeSku)
    {
        this.storeSku = storeSku;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getShortDescription()
    {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription()
    {
        return this.longDescription;
    }

    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }
}
