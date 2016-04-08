import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.rosieapp.jsonapi.v1.IdExclusionStrategy;
import com.rosieapp.jsonapi.v1.JsonApiTypeAdapterFactory;
import com.rosieapp.jsonapi.v1.stream.NestingCountWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Describe file here.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class EncoderTest
{
    protected static Gson buildGson()
    {
        return new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapterFactory(new JsonApiTypeAdapterFactory())
            .addSerializationExclusionStrategy(new IdExclusionStrategy())
//            .addSerializationExclusionStrategy(
//                new com.rosieapp.jsonapi.v1.NestedResourceExclusionStrategy())
            .create();

    }

    public static void main(String[] args)
    throws IOException
    {
        Store           store;
        StoreProduct    product;
        Gson            gson    = buildGson();

        store =
            new Store(
                10,
                "P&C Fresh",
                "The best selection",
                4);

        product =
            new StoreProduct(
                3514512,
                10,
                store,
                "00013000001243",
                "Heinz Tomato Ketchup",
                "Heinz Tomato Ketchup",
                "Heinz Tomato Ketchup. Established in 1869. 57 varieties. Grown not made.");

        try (Writer outWriter = new PrintWriter(System.out))
        {
            try (JsonWriter jsonWriter = new NestingCountWriter(outWriter))
            {
                Type typeOfSrc = new TypeToken<StoreProduct>(){}.getType();

                gson.toJson(product, typeOfSrc, jsonWriter);
            }
        }
    }
}
