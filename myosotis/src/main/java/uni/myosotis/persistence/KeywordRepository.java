package uni.myosotis.persistence;

import jakarta.persistence.EntityManager;
import uni.myosotis.objects.Keyword;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is used to access the persistence storage for the object type "Keyword".
 */
public class KeywordRepository {

    private final Logger logger = Logger.getLogger(KeywordRepository.class.getName());
    private final PersistenceManager pm = new PersistenceManager();

    /**
     * This method is used to save an object of type "Keyword" to the
     * persistence storage.
     *
     * @param keyword     The keyword that should be saved to the persistence.
     * @return            Status, -1 means an error has been occurred on save.
     */

    public int saveKeyword(final Keyword keyword) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(keyword);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving keyword: {0}", keyword.getName());
            return -1;
        }
        //logger.log(java.util.logging.Level.INFO, "Successfully saved keyword: {0}", keyword.getName());
        return 0;
    }

    /**
     * This method is used to delete an object of type "Keyword" in the
     * persistence storage.
     *
     * @param name      The name of the keywords.
     * @return          Status, -1 means an error has been occurred on delete.
     */
    public int deleteKeyword(final String name) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.find(Keyword.class, name));
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error deleting keyword: {0}", name);
            return -1;
        }
        //logger.log(java.util.logging.Level.INFO, "Successfully deleted keyword: {0}", name);
        return 0;
    }

    /**
     * This method is used to find an object of type "Keyword" in the
     * persistence storage.
     *
     * @param word      The word of the keyword.
     * @return          The object of type "Keyword" or null if it does not exist.
     */
    public Optional<Keyword> getKeywordByName(final String word) {
        try (final EntityManager em = pm.getEntityManager()) {
            return Optional.ofNullable(em.find(Keyword.class, word));
        }
        catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error getting keyword: {0}", word);
            return Optional.empty();
        }
    }

    /**
     * This method is used to get all objects of type "Keyword" in the
     * persistence storage.
     *
     * @return List of all objects of type "Keyword", could be empty.
     */
    public List<Keyword> getAllKeywords(){
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT k FROM Keyword k", Keyword.class).getResultList();
        }
        catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error getting all keywords");
            return List.of();
        }
    }
}
