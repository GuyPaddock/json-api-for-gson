package com.rosieapp.jsonapi.v1;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * An exclusion strategy that skips embedded resource objects.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class NestedResourceExclusionStrategy
implements ExclusionStrategy
{
    public boolean shouldSkipClass(Class<?> clazz)
    {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes fieldInfo)
    {
        return Resource.class.isAssignableFrom(fieldInfo.getDeclaredClass());
    }
}
