package uni.myosotis.logic;

import uni.myosotis.objects.Keyword;
import uni.myosotis.persistence.KeywordRepository;

import java.util.List;
import java.util.Optional;

public class KeywordLogic {

    final KeywordRepository KeywordRepository;

    /**
     * Creates a new KeywordLogic.
     */
    public KeywordLogic() {
        this.KeywordRepository = new KeywordRepository();
    }

    /**
     * Creates a new Keyword and saves it in the database.
     * If the keyword could not be saved to the database, a
     * IllegalStateException will the thrown.
     *
     * @param name The name of the Keyword.
     */
    public Keyword createKeyword(String name) {
        Keyword keyword = new Keyword(name);
        if (KeywordRepository.saveKeyword(keyword) < 0) {
            throw new IllegalStateException();
        }
        return keyword;
    }

    /**
     * Returns if the Keyword already exists.
     * @param word The word of the Keyword.
     */
    public boolean KeywordIsPresent(String word) {
        return KeywordRepository.getKeywordByName(word).isPresent();
    }

    /**
     * Delegates the exercise to get all Keywords to the KeywordRepository and returns them.
     *
     * @return          All Keywords, could be empty.
     */
    public List<Keyword> getAllKeywords() {
        return KeywordRepository.getAllKeywords();
    }

    /**
     * Return the Keyword with the given name.
     *
     * @param           name The name of the Keyword.
     * @return               The Keyword or null if it does not exist
     */
    public Optional<Keyword> getKeywordByName(String name) {
        return KeywordRepository.getKeywordByName(name);
    }

    /**
     * Deletes an existing Keyword from the database.
     * If the Keyword could not be deleted,
     * a IllegalStateException will be thrown.
     *
     * @param name          Name of the keyword that should be deleted.
     */
    public void deleteKeyword(String name) {
        if (KeywordIsPresent(name)) {
            if (KeywordRepository.deleteKeyword(name) < 0) {
                throw new IllegalStateException();
            }
        }
    }
}
