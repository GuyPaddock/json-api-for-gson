package com.rosieapp.jsonapi.v1;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Exclusion strategy that prevents the {@code id} attribute of resources from
 * being serialized into the {@code attributes} of the JSON, since it's included
 * separately as part of the resource identifier.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class IdExclusionStrategy
implements ExclusionStrategy
{
    /**
     * @inheritDoc
     *
     * @return
     *   {@code true} if the field name is {@code id}; {@code false}, otherwise.
     */
    @Override
    public boolean shouldSkipField(final FieldAttributes fieldAttributes)
    {
        return (fieldAttributes.getName().equals("id"));
    }

    /**
     * @inheritDoc
     *
     * @return
     *   {@code false}, always.
     */
    @Override
    public boolean shouldSkipClass(final Class<?> aClass)
    {
        return false;
    }
}