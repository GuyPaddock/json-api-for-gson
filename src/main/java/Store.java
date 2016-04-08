import com.rosieapp.jsonapi.v1.Resource;

/**
 * Describe file here.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class Store
implements Resource
{
    private int id;
    private String name;
    private String description;
    private int retailerId;

    public Store(int id, String name, String description, int retailerId)
    {
        this.id             = id;
        this.name           = name;
        this.description    = description;
        this.retailerId     = retailerId;
    }

    public String getId()
    {
        return Integer.toString(id);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getRetailerId()
    {
        return retailerId;
    }

    public void setRetailerId(int retailerId)
    {
        this.retailerId = retailerId;
    }
}
