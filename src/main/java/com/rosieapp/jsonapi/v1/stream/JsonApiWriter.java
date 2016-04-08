package com.rosieapp.jsonapi.v1.stream;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Interface for {@link com.google.gson.stream.JsonWriter} instances that
 * provide the additional methods needed for the output to conform to
 * the <a href="http://jsonapi.org">JSON API v1 specification</a>.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public interface JsonApiWriter<T extends JsonWriter & JsonApiWriter>
{
    /**
     * Gets the current object nesting level of the output stream.
     *
     * For example, if {@link com.google.gson.stream.JsonWriter#beginObject()}
     * has been called three times and
     * {@link com.google.gson.stream.JsonWriter#endObject()} has been called
     * once, the current nesting level is {@code 2}.
     *
     * @return
     *   The current nesting level, where {@code 0} indicates that the output
     *   stream is not inside any object, and any level greater than {@code 0}
     *   indicates how many objects the output stream is nested within.
     */
    public int getNestingLevel();

    /**
     * Determines whether or not the current nesting level of the output stream
     * is inside a JSON API model.
     *
     * @return
     *  {@code true} if the current nesting level places the stream inside a
     *  resource object; {@code false} if it does not.
     */
    public boolean outputIsCurrentlyInResource();

    /**
     * Begins encoding the relationship to another object. Each call to this
     * method must be paired with a call to {@link #endRelationship()}.
     *
     * @return
     *   This writer, for chaining calls.
     *
     * @throws IOException
     *   If an error occurs while writing to the underlying writer.
     *
     * @throws IllegalStateException
     *   If it is not an appropriate time to start encoding a relationship.
     */
    public T beginRelationship()
    throws IOException, IllegalStateException;

    /**
     * <p>Ends encoding the current relationship.</p>
     *
     * <p>It is recommended that implementations not immediately write
     * the relationship to the underlying writer. Instead, the caller must
     * ensure that {@link #writeRelationships()} is invoked before calling
     * {@link com.google.gson.stream.JsonWriter#endObject()} for the current
     * nesting level.</p>
     *
     * @return
     *   This writer, for chaining calls.
     *
     * @throws IOException
     *   If an error occurs while writing to the underlying writer.
     *
     * @throws IllegalStateException
     *   If it is not an appropriate time to stop encoding a relationship.
     */
    public T endRelationship()
    throws IOException, IllegalStateException;

    /**
     * Begins encoding a new included object. Each call to this method must be
     * paired with a call to {@link #endRelationship()}.
     *
     * @return
     *   This writer, for chaining calls.
     */
    public T beginIncluded()
    throws IOException, IllegalStateException;

    /**
     * <p>Ends encoding the current included object.</p>
     *
     * <p>It is recommended that implementations not immediately write
     * the included object to the underlying writer. Instead, the caller must
     * ensure that {@link #writeIncluded()} is invoked before calling
     * {@link com.google.gson.stream.JsonWriter#endObject()} for the current
     * nesting level.</p>
     *
     * @return
     *   This writer, for chaining calls.
     *
     * @throws IOException
     *   If an error occurs while writing to the underlying writer.
     *
     * @throws IllegalStateException
     *   If it is not an appropriate time to stop encoding a relationship.
     */
    public T endIncluded()
    throws IOException, IllegalStateException;

    /**
     * <p>Writes out any relationships for the current nesting level to the
     * underlying writer.</p>
     *
     * <p>This must be called before
     * {@link com.google.gson.stream.JsonWriter#endObject()} is called to avoid
     * an {@link IllegalStateException} from being invoked when there are
     * relationships queued-up.</p>
     *
     * @return
     *   This writer, for chaining calls.
     *
     * @throws IOException
     *   If an error occurs while writing to the underlying writer.
     *
     * @throws IllegalStateException
     *   If it is not an appropriate time to stop encoding a relationship.
     */
    public T writeRelationships()
    throws IOException, IllegalStateException;

    /**
     * <p>Writes out any relationships for the current nesting level to the
     * underlying writer.</p>
     *
     * <p>This must be called before
     * {@link com.google.gson.stream.JsonWriter#endObject()} is called to avoid
     * an {@link IllegalStateException} from being invoked when there are
     * relationships queued-up.</p>
     *
     * @return
     *   This writer, for chaining calls.
     *
     * @throws IOException
     *   If an error occurs while writing to the underlying writer.
     *
     * @throws IllegalStateException
     *   If it is not an appropriate time to stop encoding a relationship.
     */
    public T writeIncluded()
    throws IOException, IllegalStateException;
}