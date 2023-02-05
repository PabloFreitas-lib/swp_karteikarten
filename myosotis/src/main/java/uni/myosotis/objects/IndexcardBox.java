package uni.myosotis.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class IndexcardBox implements Serializable{

    @Id
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categoryList;

    public IndexcardBox(){

    }
    /**
     * Creates a new indexcard box
     * @param name Name of the box
     * @param categoryList List of categories
     */
    public IndexcardBox(String name, List<Category> categoryList){
        this.name = name;
        this.categoryList = categoryList;

    }
    /**
    * get Name of the box.
    * @return Name of the box.
    * */
    public String getName() {
        return name;
    }
    /**
     * set Name of the box.*/
    public void setName(String name) {
        this.name = name;
    }
    /**
     * get the category list.
    * @return List of Categories*/
    public List<Category> getCategoryList() {
        return categoryList;
    }

    /**
     * Returns a list of all indexcards in the box, without duplicates.
     * By checking if the indexcard is already in the list, duplicates are avoided.
     *
     * @return List of indexcards
     */
    public List<String> getIndexcardList() {
        List<String> indexcardList = new ArrayList<>();
        for (Category category : categoryList) {
            for (String indexcard : category.getIndexcards().stream().map(Indexcard::getName).toList()) {
                if (!indexcardList.contains(indexcard)) {
                    indexcardList.add(indexcard);
                }
            }
        }
        return indexcardList;
    }

    /**
     * Returns a list of all categories names in the box.
     *
     * @return Array of categories names.
     */
    public String[] getCategoryNameList() {
        return categoryList.stream().map(Category::getCategoryName).toList().toArray(new String[0]);
    }

    /**
     * set the category list.
     * @param categoryList
     */
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

}
