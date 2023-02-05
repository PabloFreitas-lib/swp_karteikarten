package uni.myosotis.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a box of indexcards.
 */
@Entity
public class Box {

    // attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    List<String> indexcardNames = new ArrayList<>();


    /**
     * Creates a new Box.
     */
    public Box() {
    }
    // Contructors
    /**
     * Creates a new Box.
     * @param name The name of the box.
     */
    public Box(String name) {
        this.name = name;
    }

    // Methods
    /**
     * Returns the id of the box.
     * @return The id of the box.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the indexcards names of the box.
     * @return The indexcards names of the box.
     */
    public List<String> getIndexcardNames() {
        return indexcardNames;
    }
    /**
     * Set the indexcard names in the box.
     * @param indexcardNames Index card names.
     */
    public void setIndexcardNames(List<String> indexcardNames) {
        this.indexcardNames = indexcardNames;
    }

    /**
     * Adds an indexcard to the box.
     * @param indexcardName The name of the indexcard.
     */
    public void addIndexcard(String indexcardName) {
        this.indexcardNames.add(indexcardName);
    }

    /**
     * Removes an indexcard from the box.
     * @param indexcardName The name of the indexcard.
     */

    public void removeIndexcard(String indexcardName) {
        this.indexcardNames.remove(indexcardName);
    }

    /**
     * Returns the name of the box.
     * @return The name of the box.
     */

    public String getName() {
        return name;
    }
}