package com.rosieapp.jsonapi.v1.stream;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Describe file here.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class NestingCountWriter
extends JsonWriter
{
    private int nestingLevel;

    public NestingCountWriter(final Writer out)
    {
        super(out);

        this.setNestingLevel(0);
    }

    public int getNestingLevel()
    {
        return this.nestingLevel;
    }

    @Override
    public JsonWriter beginObject()
    throws IOException
    {
        this.incrementNestingLevel();
        return super.beginObject();
    }

    @Override
    public JsonWriter endObject()
    throws IOException
    {
        this.decrementNestingLevel();
        return super.endObject();
    }

    protected void setNestingLevel(final int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }

    protected void incrementNestingLevel()
    {
        this.setNestingLevel(this.getNestingLevel() + 1);
    }

    protected void decrementNestingLevel()
    {
        this.setNestingLevel(this.getNestingLevel() - 1);
    }
}