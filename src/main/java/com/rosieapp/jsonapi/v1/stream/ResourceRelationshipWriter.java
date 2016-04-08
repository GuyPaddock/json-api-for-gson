package com.rosieapp.jsonapi.v1.stream;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

/**
 * <p>A {@link JsonWriter} that produces JSON API v1 compliant resource
 * relationship objects.</p>
 *
 * <p>A "relationship object" MUST contain at least one of the following:</p>
 * <ul>
 *   <li>{@code links}: a links object containing at least one of the
 *       following:
 *     <ul>
 *       <li>{@code self}: a link for the relationship itself (a "relationship
 *           link"). This link allows the client to directly manipulate the
 *           relationship. For example, removing an {@code author} through an
 *           {@code article}'s relationship URL would disconnect the person from
 *           the {@code article} without deleting the {@code people} resource
 *           itself. When fetched successfully, this link returns the linkage
 *           for the related resources as its primary data.</li>
 *       <li>{@code related}: a related resource link</li>
 *     </ul></li>
 *   <li>{@code data}: resource linkage</li>
 *   <li>{@code meta}: a meta object that contains non-standard meta-information
 *   about the relationship.</li>
 * </ul>
 *
 * @author Guy Paddock (guy@rosieapp.com)
 */
public class ResourceRelationshipWriter
extends JsonWriter
{
    /**
     * The stack of sections into which this writer is currently nested.
     */
    private Deque<RelationshipSection> visitedSections;

    /**
     * The next section into which the writer will traverse.
     */
    private RelationshipSection nextSection;

    /**
     * The stack of the set of elements that have been seen at each nesting
     * level.
     */
    private Deque<Set<String>> seenElementNames;

    /**
     * <p>Constructor for {@link ResourceRelationshipWriter}.</p>
     *
     * <p>Creates a new instance that writes a JSON-encoded resource
     * relationship to {@code out}.</p>
     *
     * @see com.google.gson.stream.JsonWriter#JsonWriter(java.io.Writer)
     */
    public ResourceRelationshipWriter(final Writer out)
    {
        super(out);

        this.visitedSections  = new LinkedList<>();
        this.seenElementNames = new LinkedList<>();

        this.setNextSection(RelationshipSection.TOP_LEVEL_ROOT);
    }

    @Override
    public JsonWriter name(final String name)
    throws IOException
    {
        this.validateAndTrackName(name);
        super.name(name);
        this.queueUpNextSection(name);

        return this;
    }

    @Override
    public JsonWriter beginObject()
    throws IOException
    {
        this.validateCanNestSection();
        super.beginObject();
        this.beginNestedSection();

        return this;
    }

    @Override
    public JsonWriter endObject()
    throws IOException
    {
        super.endObject();
        this.popCurrentSection();

        return this;
    }

    @Override
    public JsonWriter beginArray()
    throws IOException
    {
        this.validateCanNestArray();
        super.beginArray();

        if (!this.isCurrentlyInMetadata())
            this.pushCurrentSection(RelationshipSection.ARRAY);

        return this;
    }

    @Override
    public JsonWriter endArray()
    throws IOException
    {
        super.endArray();

        if (!this.isCurrentlyInMetadata())
            this.popCurrentSection();

        return this;
    }

    protected RelationshipSection getCurrentSection()
    {
        return this.visitedSections.peek();
    }

    protected boolean isCurrentlyInMetadata()
    {
        return (this.getCurrentSection() == RelationshipSection.META);
    }

    protected void pushCurrentSection(final RelationshipSection section)
    {
        if (section == null)
            throw new IllegalArgumentException("section cannot be null");

        this.visitedSections.push(section);
        this.seenElementNames.push(new HashSet<String>());
    }

    protected void popCurrentSection()
    {
        if (this.visitedSections.isEmpty())
        {
            throw new IllegalStateException(
                "already popped outside the top level root section");
        }

        this.visitedSections.pop();
        this.seenElementNames.pop();
    }

    protected RelationshipSection getNextSection()
    {
        return this.nextSection;
    }

    protected void setNextSection(final RelationshipSection nextSection)
    {
        this.nextSection = nextSection;
    }

    protected Set<String> getSeenElementNames()
    {
        return this.seenElementNames.peek();
    }

    /**
     * Validates that the provided name is valid in the current section of the
     * output stream.
     *
     * @param name
     *   The name to validate.
     *
     * @throws IllegalArgumentException
     *   If the name is not valid in this section.
     */
    protected void validateAndTrackName(String name)
    throws IllegalArgumentException
    {
        final RelationshipSection currentSection  = this.getCurrentSection();
        final boolean             namesRestricted = currentSection.hasRestrictedNames();
        final Set<String>         seenNames       = this.getSeenElementNames();
        List<String>              allowedNames    = Collections.emptyList();

        if (namesRestricted)
        {
            allowedNames = Arrays.asList(currentSection.getAllowedNames());

            if (!allowedNames.contains(name))
            {
                throw new IllegalArgumentException(
                    String.format(
                        "`%s` is not allowed here (`%s`). Allowed names at " +
                        "this level are: %s",
                        name,
                        currentSection.getJsonName(),
                        String.join(", ", allowedNames)));
            }
        }

        if (seenNames.contains(name))
        {
            StringBuilder message = new StringBuilder();

            message.append(
                String.format(
                    "`%s` has already appeared at this level (`%s`).",
                    name,
                    currentSection.getJsonName()));

            if (namesRestricted)
            {
                List<String> remainingNames = new ArrayList<>(allowedNames);

                remainingNames.removeAll(seenNames);

                message.append(
                    String.format(
                        " The only names not yet seen at this level are: %s",
                        String.join(", ", remainingNames)));
            }

            throw new IllegalArgumentException(message.toString());
        }

        seenNames.add(name);
    }

    protected void validateCanNestSection()
    {
        final RelationshipSection currentSection = this.getCurrentSection();

        if ((currentSection != null) && !currentSection.allowsNestedObjects())
        {
            throw new IllegalStateException(
                String.format(
                    "the current section (`%s`) cannot contain nested objects",
                    currentSection.getJsonName()));
        }
    }

    protected void validateCanNestArray()
    {
        final RelationshipSection currentSection = this.getCurrentSection();

        if (!currentSection.allowsNestedArrays())
        {
            throw new IllegalStateException(
                String.format(
                    "the current section (`%s`) cannot contain nested arrays",
                    currentSection.getJsonName()));
        }
    }

    protected void queueUpNextSection(String name)
    {
        if (!this.isCurrentlyInMetadata())
        {
            final RelationshipSection nextSection;

            nextSection = RelationshipSection.getByJsonName(name);

            if (nextSection != null)
                this.setNextSection(nextSection);
        }
    }

    protected void beginNestedSection()
    {
        final RelationshipSection currentSection = this.getCurrentSection(),
                                  nextSection    = this.getNextSection();

        /* Inside the metadata section, we keep track of deep nesting just by
         * pushing and popping more META section types as we traverse the
         * metadata. Eventually, we'll pop our way back to the root of the
         * resource object.
         */
        if (!this.isCurrentlyInMetadata())
            this.pushCurrentSection(nextSection);
        else
            this.pushCurrentSection(RelationshipSection.META);
    }

    protected enum RelationshipSection
    {
        //  Restrict Names? | Objects | Arrays | Allowed Names
        //------------------|---------|--------|--------------------------------------------
        TOP_LEVEL_ROOT(true,  true,     false,   new String[] { "links", "data",  "meta" }),
        LINKS(         true,  false,    false,   new String[] { "self",  "related"       }),
        DATA(          true,  false,    true,    new String[] { "type",  "id"            }),
        META(          false, true,     false,   null),
        ARRAY(         false, true,     false,   null);

        private final boolean hasRestrictedNames;
        private final boolean allowsNestedObjects;
        private final boolean allowsNestedArrays;
        private final String[] allowedNames;

        private RelationshipSection(final boolean hasRestrictedNames,
                                    final boolean allowsNestedObjects,
                                    final boolean allowsNestedArrays,
                                    final String[] allowedNames)
        {
            this.hasRestrictedNames  = hasRestrictedNames;
            this.allowsNestedObjects = allowsNestedObjects;
            this.allowsNestedArrays  = allowsNestedArrays;
            this.allowedNames        = allowedNames;
        }

        public boolean hasRestrictedNames()
        {
            return this.hasRestrictedNames;
        }

        public boolean allowsNestedObjects()
        {
            return this.allowsNestedObjects;
        }

        public boolean allowsNestedArrays()
        {
            return this.allowsNestedArrays;
        }

        public String[] getAllowedNames()
        {
            return this.allowedNames;
        }

        public String getJsonName()
        {
            return this.name().toLowerCase();
        }

        public static RelationshipSection getByJsonName(String name)
        {
            RelationshipSection result = null;

            for (RelationshipSection section : RelationshipSection.values())
            {
                if (section.getJsonName().equals(name))
                {
                    result = section;
                    break;
                }
            }

            return result;
        }
    }

    public static void main(String[] args)
    throws IOException
    {
        try (PrintWriter outWriter = new PrintWriter(System.out))
        {
            try (ResourceRelationshipWriter writer =
                new ResourceRelationshipWriter(outWriter))
            {
                writer.beginObject();
                    writer.name("data");
                    writer.beginObject();
                        writer.name("type");
                        writer.value("people");

                        writer.name("id");
                        writer.value(9);
                    writer.endObject();

                    writer.name("links");
                    writer.beginObject();

                writer.name("test");
                writer.value(5);
                        writer.name("self");
                        writer.value("http://example.com/articles/1/relationships/author");

                        writer.name("related");
                        writer.value("http://example.com/articles/1/author");
                    writer.endObject();

                    writer.name("meta");
                    writer.beginObject();
                        writer.name("links");
                        writer.beginObject();
                            writer.name("self");
                            writer.value("http://example.com/articles/1/relationships/author");

                            writer.name("related");
                            writer.value("http://example.com/articles/1/author");
                        writer.endObject();
                    writer.endObject();
                writer.endObject();
            }
        }
    }
}