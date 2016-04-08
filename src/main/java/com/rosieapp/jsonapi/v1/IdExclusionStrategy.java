package com.rosieapp.jsonapi.v1;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class IdExclusionStrategy
implements ExclusionStrategy
{
    @Override
    public boolean shouldSkipField(final FieldAttributes fieldAttributes)
    {
        return (fieldAttributes.getName().equals("id"));
    }

    @Override
    public boolean shouldSkipClass(final Class<?> aClass)
    {
        return false;
    }
}