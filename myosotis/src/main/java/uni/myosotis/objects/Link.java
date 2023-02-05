package uni.myosotis.objects;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Linking between an Indexcard and a term.
 */

@Entity
public class Link implements Serializable {

    /**
     * The id of the Link.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The term the Indexcard is linked to.
     */
    private String term;

    /**
     * The Indexcard the term is linked with. Multiple Links can link the same Indexcard.
     */
    @ManyToOne
    private Indexcard indexcard;

    /**
     * Creates a new Link between a term and an Indexcard.
     */
    public Link() {

    }

    /**
     * Creates a new Link between a term and an Indexcard.
     *
     * @param term The term the Indexcard is linked to.
     * @param indexcard The Indexcard the term is linked with.
     */
    public Link (final String term, final Indexcard indexcard) {
        this.term = term;
        this.indexcard = indexcard;
    }

    /**
     * Returns the term the Indexcard is linked to.
     *
     * @return The term the Indexcard is linked to.
     */
    public String getTerm() {
        return term;
    }

    /**
     * Returns the Indexcard the term is linked with.
     *
     * @return The Indexcard the term is linked with.
     */
    public Indexcard getIndexcard() {
        return indexcard;
    }

    /**
     * Returns the id of the Link.
     *
     * @return The id of the Link.
     */
    public Long getId() {
        return id;
    }
}
