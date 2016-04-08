package com.rosieapp.jsonapi.v1;

/**
 * An interface for objects that can be used as top-level JSON API resources.
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public interface Resource
{
   /**
    * Gets the identifier for this object.
    *
    * @return
    *   The identifier for this object.
    */
   public String getId();
}
