package uni.myosotis.objects;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class represents a category.
 */
@Entity
public class Category implements Serializable {

    /**
     * The id of the Category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the Category.
     */
    private String name;

    /**
     * The children of the Category.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> children;

    /**
     * The Indexcards in the Category.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Indexcard> indexcards;

    /**
     * Constructor for the Category.
     */
    public Category() {

    }

    /**
     * Creates a new Category.
     *
     * @param name The name of the Category.
     * @param children The children of the Category.
     * @param indexcards The Indexcards in the Category.
     */
    public Category(String name, List<Category> children, List<Indexcard> indexcards) {
        this.name = name;
        this.children = children;
        this.indexcards = indexcards;
    }

    /**
     * Returns the name of the Category.
     *
     * @return The name of the Category.
     */
    public String getCategoryName(){
        return name;
    }

    /**
     * Sets the name of the Category.
     *
     * @param name The new name of the Category.
     */
     public void setCategoryName(String name) {
         this.name = name;
     }

    /**
     * Returns the children of the Category.
     *
     * @return A list of the children of the Category.
     */
    public List<Category> getChildren() {
        return children;
    }

    /**
     * Returns the children of the Category. Includes the children of the children of the Category.
     *
     * @return A list of all children of the Category, including the children of the children.
     */
    public List<Category> getAllChildren() {
        Set<Category> childrenList = new HashSet<>(this.children);
        for (Category child : this.children) {
            childrenList.addAll(child.getAllChildren());
        }
        return new ArrayList<>(childrenList);
    }

    /**
     * Adds a new child to the Category.
     *
     * @param child The Category that should be added to the Category as a child.
     */
    public void addChild(Category child) {
        children.add(child);
    }

    /**
     * Sets the children of the Category.
     *
     * @param children The new children of the Category.
     */
    public void setChildren(List<Category> children) {
        this.children = children;
    }

    /**
     * Returns a list of the Indexcards in the Category.
     *
     * @return The Indexcards of the Category
     */
    public List<Indexcard> getIndexcards() {
        return indexcards;
    }

    /**
     * Returns a list of the Indexcards in the Category. Includes the Indexcards in the children of the Category.
     *
     * @return The Indexcard of the Category, including the Indexcards of the children of the Category.
     */
    public List<Indexcard> getAllIndexcards() {
        Set<Indexcard> indexcardList = new HashSet<>(this.indexcards);
        for (Category child : this.children) {
            indexcardList.addAll(child.getAllIndexcards());
        }
        return new ArrayList<>(indexcardList);
    }

    /**
     * Adds an Indexcard to the Category.
     *
     * @param indexcard The Indexcard which should be added.
     */
    public void addIndexcard(Indexcard indexcard){
        indexcards.add(indexcard);
    }

    /**
     * Sets the Indexcards of the Category.
     *
     * @param indexcards The new Indexcards in the Category.
     */
    public void setIndexcards(List<Indexcard> indexcards) {
        this.indexcards = indexcards;
    }

    /**
     * Returns the id of the Category.
     *
     * @return The id of the Category.
     */
    public Long getId() {
        return id;
    }
}
