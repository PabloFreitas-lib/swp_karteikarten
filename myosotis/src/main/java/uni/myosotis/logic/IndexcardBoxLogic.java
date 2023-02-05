package uni.myosotis.logic;

import uni.myosotis.objects.Category;
import uni.myosotis.objects.IndexcardBox;
import uni.myosotis.persistence.IndexcardBoxRepository;

import java.util.List;

public class IndexcardBoxLogic {
    /**
     * The repository for the IndexcardBox's.
     */
    final IndexcardBoxRepository indexcardBoxRepository;

    /**
     * Creates a new IndexcardBoxLogic.
     */
    public IndexcardBoxLogic () {
        this.indexcardBoxRepository = new IndexcardBoxRepository();
    }
    public IndexcardBox getIndexcardBoxByName(String name) {
        return indexcardBoxRepository.getIndexcardBoxByName(name).get();
    }

    /**
     * Creates a new IndexcardBox.
     *
     * @param name The name of the IndexcardBox.
     * @param categoryList The Categories in the IndexcardBox.
     */
    public void createIndexcardBox(String name, List<Category>categoryList){
        if (indexcardBoxRepository.getIndexcardBoxByName(name).isPresent()) {
            throw new IllegalStateException();
        } else {
            if (indexcardBoxRepository.saveIndexcardBox(new IndexcardBox(name, categoryList)) < 0) {
                throw new IllegalStateException();
            }
        }
    }

    public List<IndexcardBox> getAllIndexcardBoxes() {
        return indexcardBoxRepository.getAllIndexcardBoxes();
    }

    public Boolean IndexcardBoxIsPresent(String name) {
        return indexcardBoxRepository.getIndexcardBoxByName(name).isPresent();
    }

    public void deleteIndexcardBox(String name){
        if (IndexcardBoxIsPresent(name)) {
            if (indexcardBoxRepository.deleteIndexcardBox(name) < 0) {
                throw new IllegalStateException();
            }
        }
    }

    public void updateIndexcardBox(String indexcardBoxName, List<Category> indexcardBoxList) {
        if (indexcardBoxRepository.getIndexcardBoxByName(indexcardBoxName).isPresent()) {
            IndexcardBox indexcardBox = indexcardBoxRepository.getIndexcardBoxByName(indexcardBoxName).get();
            // Updates all values of the old indexcardBox.
            indexcardBox.setName(indexcardBoxName);
            indexcardBox.setCategoryList(indexcardBoxList);
             // Update in database failed.
            if (indexcardBoxRepository.updateIndexcardBox(indexcardBox) < 0) {
                throw new IllegalStateException();
            }

        }
        // Invalid id, indexcardBox does not exist.
        else {
            throw new IllegalStateException();
        }
    }

    public List<IndexcardBox> searchIndexcardBox(String text) {
        return indexcardBoxRepository.searchIndexcardBox(text);
    }
}
