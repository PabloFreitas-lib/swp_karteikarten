package uni.myosotis.logic;

import uni.myosotis.objects.Category;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.Keyword;
import uni.myosotis.objects.Link;
import uni.myosotis.persistence.CategoryRepository;
import uni.myosotis.persistence.IndexcardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IndexcardLogic {

    /**
     * The repository for the Indexcards.
     */
    final IndexcardRepository indexcardRepository;

    /**
     * The repository for the Category's.
     */
    final CategoryRepository categoryRepository;

    /**
     * Creates a new IndexcardLogic.
     */
    public IndexcardLogic () {

        this.indexcardRepository = new IndexcardRepository();
        this.categoryRepository = new CategoryRepository();

    }

    /**
     * Creates a new Indexcard and saves it in the database.
     * If already an indexcard with the same name exists, it will throw a IllegalStateException.
     *
     * @param name The Name of the Indexcard.
     * @param question  The Question of the Indexcard.
     * @param answer The Answer of the Indexcard.
     * @param keywords Keywords which could be added to the Indexcard.
     * @param links Links which should be added to the Indexcard.
     */
    public void createIndexcard(String name, String question, String answer, List<Keyword> keywords, List<Link> links) {
        if (indexcardRepository.getIndexcardByName(name).isPresent()) {
            throw new IllegalStateException();
        } else {
            if (indexcardRepository.saveIndexcard(new Indexcard(name, question, answer, keywords, links)) < 0) {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * Edits an existing Indexcard and saves it in the database.
     * If there is no indexcard with the given name, it will throw a IllegalStateException.
     *
     * @param name The Name of the Indexcard.
     * @param question  The Question of the Indexcard.
     * @param answer The Answer of the Indexcard.
     * @param keywords The keywords of the Indexcard.
     * @param links The Links of the Indexcard.
     * @param id The id of the Indexcard.
     */
    public void updateIndexcard(String name, String question, String answer, List<Keyword> keywords, List<Link> links, Long id) {
        if (indexcardRepository.getIndexcardById(id).isPresent()) {
            Indexcard indexcard = indexcardRepository.getIndexcardById(id).get();

            // Updates all values of the old indexcard.
            indexcard.setName(name);
            indexcard.setQuestion(question);
            indexcard.setAnswer(answer);
            indexcard.setKeywords(keywords);
            indexcard.setLinks(links);

            // Update in database failed.
            if (indexcardRepository.updateIndexcard(indexcard) < 0) {
                throw new IllegalStateException();
            }

        }
        // Invalid id, indexcard does not exist.
        else {
            throw new IllegalStateException();
        }
    }

    /**
     * Deletes an existing Indexcard and saves it in the database.
     * If there is no indexcard with the given id, it will throw a IllegalStateException.
     *
     * @param id The id of the Indexcard.
     */
    public void deleteIndexcard(Long id) {
        if (indexcardRepository.getIndexcardById(id).isPresent()) {
            Indexcard indexcard2delete = indexcardRepository.getIndexcardById(id).get();
            List<Category> categoryContains = categoryRepository.getAllCategories();
            for (Category categoryContain : categoryContains) {
                List<String> temp = new ArrayList<>(categoryContain.getIndexcards().stream().map(Indexcard::getName).toList());
                if (temp.contains(indexcard2delete.getName())) {
                    temp.removeIf(s -> s.equals(indexcard2delete.getName()));
                    categoryContain.setIndexcards(getIndexcardsByIndexcardNameList(temp));
                    categoryRepository.updateCategory(categoryContain);
                }
            }
            if (indexcardRepository.deleteIndexcard(id) < 0) {
                throw new IllegalStateException();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns all Indexcards.
     *
     * @return          A list of all indexcards.
     */
    public List<Indexcard> getAllIndexcards() {
        return indexcardRepository.getAllIndexcards();
    }

    /**
     * Returns all Indexcards.
     *
     * @return          A list of all indexcards.
     */
    public List<Indexcard> getAllIndexcards(List<String> indexcardNames) {
        List<Indexcard> indexcards = new ArrayList<>();
        for (String indexcardName : indexcardNames) {
            if (getIndexcardByName(indexcardName).isPresent()) {
                indexcards.add(getIndexcardByName(indexcardName).get());
            }
        }
        return indexcards;
    }

    /**
     * Returns the Indexcard with the given id.
     *
     * @param id The id.
     * @return The Indexcard with this id.
     */
    public Indexcard getIndexcardById(Long id) {
        Optional<Indexcard> indexcard = indexcardRepository.getIndexcardById(id);
        if (indexcard.isPresent()) {
            return indexcard.get();
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Return the Indexcard with the given name.
     *
     * @param indexcard The name of the Indexcard.
     * @return The Indexcard if it exists.
     */
    public Optional<Indexcard> getIndexcardByName(String indexcard) {
        return indexcardRepository.getIndexcardByName(indexcard);
    }

    /**
     * Returns all Indexcards that have the given Keyword.
     *
     * @param           keyword The Keyword.
     * @return          All Indexcards with that Keyword.
     */
    public List<Indexcard> getIndexcardsByKeyword(String keyword) {

        List<Indexcard> indexcards = indexcardRepository.getAllIndexcards();

        // Remove the Indexcards that do not have the given keyword
        indexcards.removeIf(indexcard -> !indexcard.getKeywords().stream()
                .map(Keyword::getName)
                .toList()
                .contains(keyword));

        return indexcards;
    }

    public List<Indexcard> searchIndexcard(String text) {
        return indexcardRepository.searchIndexcard(text);
    }

    /**
     * Create a function which converts a list of indexcard names to a list of indexcards.
     * @param indexcardNames The list of indexcard names.
     *                       The list of indexcards.
     */
    public List<Indexcard> getIndexcardsByIndexcardNameList(List<String> indexcardNames) {
        List<Indexcard> indexcards = new ArrayList<>();
        for (String indexcardName : indexcardNames) {
            if (indexcardRepository.getIndexcardByName(indexcardName).isPresent()) {
                indexcards.add(indexcardRepository.getIndexcardByName(indexcardName).get());
            }
        }
        return indexcards;
    }
}
