package com.rosieapp.jsonapi.v1.stream;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Base class for {@link JsonWriter}'s that keep track of how deeply-nested
 * the JSON output currently is at any point in time.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class NestingCountWriter
extends JsonWriter
{
    /**
     * The current nesting level.
     */
    private int nestingLevel;

    /**
     * Constructor for {@link NestingCountWriter}.
     *
     * @param output
     *   The writer to which the output of this object will be written.
     */
    public NestingCountWriter(final Writer output)
    {
        super(output);

        this.setNestingLevel(0);
    }

    /**
     * Gets the current nesting level of the output.
     *
     * @return
     *   The nesting level as a positive integer, where {@code 0} indicates that
     *   the output is outside the top level element of the document.
     */
    public int getNestingLevel()
    {
        return this.nestingLevel;
    }

    /**
     * @inheritDoc
     *
     * This increments the nesting level.
     */
    @Override
    public JsonWriter beginObject()
    throws IOException
    {
        this.incrementNestingLevel();
        return super.beginObject();
    }

    /**
     * @inheritDoc
     *
     * This decrements the nesting level.
     */
    @Override
    public JsonWriter endObject()
    throws IOException
    {
        this.decrementNestingLevel();
        return super.endObject();
    }

    /**
     * Sets the nesting level to the specified value.
     *
     * @param nestingLevel
     *   The new nesting level.
     */
    protected void setNestingLevel(final int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }

    /**
     * Increments the nesting level.
     */
    protected void incrementNestingLevel()
    {
        this.setNestingLevel(this.getNestingLevel() + 1);
    }

    /**
     * Decrements the nesting level.
     */
    protected void decrementNestingLevel()
    {
        this.setNestingLevel(this.getNestingLevel() - 1);
    }
}