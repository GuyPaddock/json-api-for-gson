package com.rosieapp.jsonapi.v1.stream;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * The default implementation of {@link JsonApiWriter}.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class DefaultJsonApiWriter
extends NestingCountWriter
implements JsonApiWriter<DefaultJsonApiWriter>
{
    /**
     * <p>The name for the next array, object, relationship, or included object;
     * deferred until the next call, which then clarifies how the name will be
     * used.</p>
     *
     * <p>{@link JsonWriter} has a similar concept internally, that we mirror
     * here to be able to redirect names into the {@code relationship} and
     * {@code included} sections of the output.</p>
     *
     * @see #flushDeferredName()
     *
     */
    private String deferredName;

    /**
     * <p>The writer being used to construct relationships to other resources,
     * as well as to include other resources in the JSON for the top-level
     * resource.</p>
     *
     * <p>This is expected to be {@code null} whenever the top-level resource
     * object is being serialized, and non-null whenever JSON is being
     * constructed for a {@code relationship} or {@code included} resource.</p>
     *
     * <p>While non-null, all calls on the containing writer are forwarded to
     * the deferred writer until the relationship or included resource is
     * done being constructed.</p>
     */
    private JsonWriter nestedWriter;

    /**
     * <p>A map of the relationships from the top-level resource to other
     * resources.</p>
     *
     * <p>Each key is the name of a relationship, and its value is the raw
     * JSON content that corresponds to that relationship, as constructed by
     * the {@code nestedWriter}.</p>
     */
    private Map<String, String> relationships;

    /**
     * <p>A map of the resources included in the output for the top-level
     * resource.</p>
     *
     * <p>Each key is a unique name that identifies the property of the
     * top-level object to which the included resource corresponds, and its
     * value is the raw JSON content that corresponds to that included resource,
     * as constructed by the {@code nestedWriter}.</p>
     */
    private Map<String, String> includedResources;

    /**
     * <p>Constructor for {@link DefaultJsonApiWriter}.</p>
     *
     * <p>Creates a new instance that writes a JSON-encoded stream to
     * {@code out}.</p>
     *
     * @see JsonWriter#JsonWriter(java.io.Writer)
     */
    public DefaultJsonApiWriter(final Writer out)
    {
        super(out);

        this.setDeferredName(null);
    }

    @Override
    public DefaultJsonApiWriter name(final String name)
    throws IOException
    {
        if (name == null)
        {
            throw new NullPointerException("name == null");
        }

        else if (this.getDeferredName() != null)
        {
            throw new IllegalStateException(
                "A name has already been set for the next object.");
        }

        else
        {
            this.setDeferredName(name);
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean outputIsCurrentlyInResource()
    {
        /* The first nesting level only contains "data:", and we don't want
         * to discourage writing out the top-level resource's attributes.
         */
        return (this.getNestingLevel() > 2);
    }

    @Override
    public DefaultJsonApiWriter beginObject()
    throws IOException
    {
        this.flushDeferredName();
        super.beginObject();

        return this;
    }

    @Override
    public DefaultJsonApiWriter jsonValue(final String value)
    throws IOException
    {
        this.flushDeferredName();
        super.jsonValue(value);

        return this;
    }

    @Override
    public DefaultJsonApiWriter nullValue()
    throws IOException
    {
        this.flushDeferredName();
        super.nullValue();

        return this;
    }

    @Override
    public DefaultJsonApiWriter value(final boolean value)
    throws IOException
    {
        this.flushDeferredName();
        super.value(value);

        return this;
    }

    @Override
    public DefaultJsonApiWriter value(final double value)
    throws IOException
    {
        this.flushDeferredName();
        super.value(value);

        return this;
    }

    @Override
    public DefaultJsonApiWriter value(final long value)
    throws IOException
    {
        this.flushDeferredName();
        super.value(value);

        return this;
    }

    @Override
    public DefaultJsonApiWriter value(final Number value)
    throws IOException
    {
        this.flushDeferredName();
        super.value(value);

        return this;
    }

    @Override
    public DefaultJsonApiWriter value(final String value)
    throws IOException
    {
        this.flushDeferredName();
        super.value(value);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultJsonApiWriter beginRelationship()
    throws IOException, IllegalStateException
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultJsonApiWriter endRelationship()
    throws IOException, IllegalStateException
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultJsonApiWriter beginIncluded()
    throws IOException, IllegalStateException
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultJsonApiWriter endIncluded()
    throws IOException, IllegalStateException
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultJsonApiWriter writeRelationships()
    throws IOException, IllegalStateException
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultJsonApiWriter writeIncluded()
    throws IOException, IllegalStateException
    {
        return this;
    }

    protected String getDeferredName()
    {
        return this.deferredName;
    }

    protected void setDeferredName(final String deferredName)
    {
        this.deferredName = deferredName;
    }

    protected JsonWriter getNestedWriter()
    {
        return this.nestedWriter;
    }

    protected void setNestedWriter(final JsonWriter nestedWriter)
    {
        this.nestedWriter = nestedWriter;
    }

    protected void flushDeferredName()
    throws IOException
    {
        super.name(this.getDeferredName());
        this.setDeferredName(null);
    }

    protected boolean hasNestedWriter()
    {
        return (this.getNestedWriter() != null);
    }

}
