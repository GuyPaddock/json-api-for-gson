package com.rosieapp.jsonapi.v1;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.rosieapp.jsonapi.v1.stream.JsonApiWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Type adapter factory used when serializing {@link Resource} objects into and
 * out of JSON that complies with JSON API v1.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 * @see <a href="http://jsonapi.org/format/">JSON API v1 Specification</a>
 */
public class JsonApiTypeAdapterFactory
implements TypeAdapterFactory
{
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken)
    {
        TypeAdapter<?>  result  = null;
        Type            type    = typeToken.getType();

        if (Resource.class.isAssignableFrom(typeToken.getRawType()))
        {
            TypeAdapter<Resource> delegateAdapter =
                (TypeAdapter<Resource>)gson.getDelegateAdapter(this, typeToken);

            result = new JsonApiTypeAdapter(delegateAdapter);
        }

        return (TypeAdapter<T>)result;
    }

    public static class JsonApiTypeAdapter
    extends TypeAdapter<Resource>
    {
        private TypeAdapter<Resource> delegateAdapter;

        public JsonApiTypeAdapter(final TypeAdapter<Resource> delegateAdapter)
        {
            this.delegateAdapter = delegateAdapter;
        }

        protected TypeAdapter<Resource> getDelegateAdapter()
        {
            return this.delegateAdapter;
        }

        @Override
        public void write(final JsonWriter jsonWriter, final Resource resource)
        throws IOException
        {
            if (resource == null)
                jsonWriter.nullValue();

            else
                this.writeResource(jsonWriter, resource);
        }

        @Override
        public Resource read(final JsonReader jsonReader)
        throws IOException
        {
            return null;
        }

        protected void writeResource(final JsonWriter jsonWriter,
                                     final Resource resource)
        throws IOException
        {
            jsonWriter.beginObject();

            this.writeResourceData(jsonWriter, resource);

            jsonWriter.endObject();
        }

        protected void writeResourceData(final JsonWriter jsonWriter,
                                         final Resource resource)
        throws IOException
        {
            if (!(jsonWriter instanceof JsonApiWriter))
            {
                throw new IllegalArgumentException(
                    "jsonWriter must be a JsonApiWriter");
            }

            else
            {
                JsonApiWriter<?> jsonApiWriter = (JsonApiWriter)jsonWriter;

                jsonWriter.name("data");
                jsonWriter.beginObject();

                this.writeResourceHeader(jsonWriter, resource);

                if (!jsonApiWriter.outputIsCurrentlyInResource())
                    this.writeResourceAttributes(jsonWriter, resource);

                jsonWriter.endObject();
            }
        }

        protected void writeResourceHeader(final JsonWriter jsonWriter,
                                           final Resource resource)
        throws IOException
        {
            jsonWriter.name("type").value(getJsonTypeName(resource));
            jsonWriter.name("id").value(resource.getId());
        }

        protected void writeResourceAttributes(final JsonWriter jsonWriter,
                                               final Resource resource)
        throws IOException
        {
            jsonWriter.name("attributes");
            this.getDelegateAdapter().write(jsonWriter, resource);
        }

        protected static String getJsonTypeName(Resource resource)
        {
            String className = resource.getClass().getSimpleName();

            return CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE, className);
        }
    }
}
