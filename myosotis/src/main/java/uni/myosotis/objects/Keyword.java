package uni.myosotis.objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

/**
 * This class represents a keyword.
 */
@Entity
@Embeddable
public class Keyword implements Serializable {

    @Id
    String name;

    public Keyword() {
        // default constructor
    }

    /**
     * Constructor of Keyword.
     *
     * @param keyword      The name of the Keyword.
     */
    public Keyword(String keyword) {
        this.name = keyword;
    }

    /**
     * Method to obtain the name of a keyword.
     *
     * @return      The name of the keyword.
     */
    public String getName() {
        return name;
    }

    // END OF CLASS
}
