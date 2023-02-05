package uni.myosotis.logic;

import uni.myosotis.objects.Category;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.IndexcardBox;
import uni.myosotis.persistence.CategoryRepository;

import java.util.*;
import java.util.logging.Level;
public class CategoryLogic {

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(CategoryLogic.class.getName());

    /**
     * The repository for the Category's.
     */
    final CategoryRepository categoryRepository;

    final IndexcardLogic indexcardLogic;

    final IndexcardBoxLogic indexcardBoxLogic;



    /**
     * Creates a new CategoryLogic.
     */
    public CategoryLogic () {

        this.indexcardBoxLogic = new IndexcardBoxLogic();
        this.categoryRepository = new CategoryRepository();
        this.indexcardLogic = new IndexcardLogic();
    }

    /**
     * Creates a new Category and saves it in the database.
     * *
     * @param name The name of the Category.
     * @param indexcards The Indexcards that should be in the Category.
     * @param parents The parents of the Category.
     */
    public void createCategory(String name, List<Indexcard> indexcards, List<Category> parents) {
        try {
            // Create new Category and save it to the database
            Category newCategory = new Category(name, new ArrayList<>(), indexcards);
            categoryRepository.saveCategory(newCategory);
            // Add the new Category as a child to the selected parent-categories
            for (Category parent : parents) {
                parent.addChild(newCategory);
                categoryRepository.updateCategory(parent);
            }
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Edits an existing Category and saves it in the database.
     * If there is no Category with the given name, it will throw a IllegalStateException.
     *
     * @param newName The new name of the Category that should be edited.
     * @param newParents The new parents of the Category.
     * @param newIndexcards The new Indexcards of the Category.
     * @param id The id of the Category.
     */
    public void updateCategory(String newName, List<Category> newParents, List<Indexcard> newIndexcards, Long id) {
        try {
            if (categoryRepository.getCategoryById(id).isPresent()) {
                Category category = categoryRepository.getCategoryById(id).get();
                List<Category> oldParents = getParentCategories(category);
                List<Category> notParents = oldParents.stream()
                        .filter(parent -> !newParents.stream().map(Category::getCategoryName).toList().contains(parent.getCategoryName()))
                        .toList();
                // Remove this Category as a child from Categories, that are not parents anymore.
                for (Category parent : notParents) {
                    parent.setChildren(parent.getChildren().stream().filter(child -> !child.getCategoryName().equals(category.getCategoryName())).toList());
                    categoryRepository.updateCategory(parent);
                }
                // Update this Category
                category.setCategoryName(newName);
                category.setIndexcards(newIndexcards);
                categoryRepository.updateCategory(category);
                // Add this Category as a child to new added parents
                List<Category> newAddedParents = newParents.stream().filter(newParent -> !oldParents.stream().map(Category::getCategoryName).toList().contains(newParent.getCategoryName())).toList();
                for (Category newParent : newAddedParents) {
                    newParent.addChild(category);
                    categoryRepository.updateCategory(newParent);
                }
            }
        } catch (final Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Deletes an existing Category from the database.
     *
     * @param category The Category that should be deleted.
     */
    public void deleteCategory(Category category) {
        try {
            // Delete this Category from IndexcardBoxes that contain it
            String categoryName = category.getCategoryName();
            List<IndexcardBox> boxContains = indexcardBoxLogic.getAllIndexcardBoxes();
            for (IndexcardBox boxContain : boxContains) {
                if (Arrays.asList(boxContain.getCategoryNameList()).contains(categoryName)) {
                    List<Category> temp = boxContain.getCategoryList();
                    for (int j = 0; j < temp.size(); j++) {
                        temp.removeIf(s -> s.getCategoryName().equals(categoryName));
                        indexcardBoxLogic.updateIndexcardBox(boxContain.getName(), temp);
                    }
                }
            }
            // Remove this Category as a child from the parents-categories.
            for (Category parent : getParentCategories(category)) {
                List<Category> newChildren = parent.getChildren();
                newChildren.removeIf(children -> children.getCategoryName().equals(category.getCategoryName()));
                parent.setChildren(newChildren);
                categoryRepository.updateCategory(parent);
            }
            // Delete the Category.
            categoryRepository.deleteCategory(category);
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Returns a list of all parents of a Category.
     *
     * @param category The Category from them the parents should be returned.
     * @return A list of all parents of the Category.
     */
    public List<Category> getParentCategories(Category category) {
        return categoryRepository.getAllCategories().stream().filter(c -> c.getChildren().stream().map(Category::getCategoryName).toList().contains(category.getCategoryName())).toList();
    }

    /**
     * Returns a list of all parents of a Category. Includes the parents of the parent-categories.
     *
     * @param category The Category from them the parents should be returned.
     * @return A list of all parents of the Category, including the parents of the parent-categories.
     */
    public List<Category> getAllParentCategories(Category category) {
        List<Category> parents = getParentCategories(category);
        Set<Category> parentList = new HashSet<>(parents);
        for (Category parent : parents) {
            parentList.addAll(getAllParentCategories(parent));
        }
        return new ArrayList<>(parentList);
    }

    /**
     * Returns all Category's.
     *
     * @return All Category's.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    /**
     * Return the category with the given name.
     *
     * @param category The name of the category.
     * @return The category if it exists.
     */
    public Optional<Category> getCategoryByName(String category) {
        return categoryRepository.getCategoryByName(category);
    }

    /**
     * Get all Categories by a category name list.
     */
    public List<Category> getCategoriesByCategoryNameList(List<String> categoryNameList) {
        List<Category> categoryList = new ArrayList<>();
        for (String categoryName : categoryNameList) {
            try {
                Optional<Category> category = getCategoryByName(categoryName);
                if (category.isPresent()) {
                    categoryList.add(category.get());
                } else {
                    log.log(Level.WARNING, "Category not found: {0}", categoryName);
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "Error while retrieving category: {0}", categoryName);
                log.log(Level.SEVERE, "Error", e);
            }
        }
        return categoryList;
    }

    /**
     * Returns all Categories that contain a specific Indexcard to the CategoryLogic.
     *
     * @param indexCard The Indexcard
     * @return A list of all Categories that contain that Indexcard.
     */
    public List<Category> getCategoriesByIndexcard(Indexcard indexCard) {
        return getAllCategories().stream().filter(category -> category.getIndexcards().stream().map(Indexcard::getName).toList().contains(indexCard.getName())).toList();
    }

    /**
     * Find all Indexcards in this category to the CategoryLogic.
     *
     * @param category The Category.
     * @return A list of all Indexcards in this category.
     */
    public List<Indexcard> getIndexcardsByCategory(Category category) {
        return category.getIndexcards();
    }

    /**
     * Searches for Category`s with text in the category repository.
     *
     * @param text The Text.
     * @return A list of Category`s that contains the text.
     */
    public List<Category> searchCategory(String text) {
        return categoryRepository.searchCategory(text);
    }
}
