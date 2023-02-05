package uni.myosotis.persistence;

import jakarta.persistence.EntityManager;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.IndexcardBox;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;


/**
 * This class is used to access the persistence storage for the object type "Indexcard".
 */
public class IndexcardRepository {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(IndexcardRepository.class.getName());

    private final PersistenceManager pm = new PersistenceManager();



    /**
     * @author Len Thiemann
     * <p>
     * This method is used to save an object of type "Indexcard" to the persistent
     * persistence storage.
     *
     * @param indexcard     The index card that should be saved to the persistence.
     * @return              Status, -1 means an error has been occurred on save.
     */
    public int saveIndexcard(final Indexcard indexcard) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(indexcard);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving index card: {0}", indexcard.getName());
            return -1;
        }
        //logger.log(Level.INFO, "Successfully saved index card: {0}", indexcard.getName());
        return 0;
    }

    /**
     * This method is used to update an object of type "Indexcard" to the persistent
     * persistence storage. If the Card does not exist at this point it will be created
     * and added to the database. Otherwise, the content of the given card will be updated
     * instead.
     *
     * @param indexcard     The index card that should be updated.
     * @return              Status, -1 means an error has been occurred on update.
     */
    public int updateIndexcard(final Indexcard indexcard) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(indexcard);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating index card: {0}", indexcard.getName());
            return -1;
        }
        //logger.log(Level.INFO, "Successfully updated index card: {0}", indexcard.getName());
        return 0;
    }

    /**
     * This method is used to delete an object of type "Indexcard" in the persistent
     * persistence storage.
     *
     * @param id      The unique id of the index card.
     * @return          Status, -1 means an error has been occurred on delete.
     */
    public int deleteIndexcard(final Long id) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.find(Indexcard.class, id));
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting index card with id: {0}", id);
            return -1;
        }
        //logger.log(Level.INFO, "Successfully deleted index card with id: {0}", id);
        return 0;
    }

    /**
     * This method is used to get all objects of type "Indexcard" in the persistent
     * persistence storage.
     *
     * @return List of all objects of type "Indexcard", could be empty.
     */
    public List<Indexcard> getAllIndexcards(){
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM Indexcard i").getResultList();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while fetching all index cards");
            throw e;
        }
    }

    /**
     * This method is used to get all objects of type "Indexcard" in the persistent
     * persistence storage.
     *
     * @return List of all objects of type "Indexcard", could be empty.
     */
    public List<Indexcard> getAllIndexcards(List<String> names){
        try (EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM Indexcard i WHERE i.name IN :names").setParameter("names", names).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while fetching all index cards");
            throw e;
        }
    }

    /**
     * This method is used to find an object of type "Indexcard" in the persistent
     * persistence storage.
     *
     * @param name      The unique id of the index card.
     * @return          The object of type "Indexcard" or null if it does not exist.
     */
    public Optional<Indexcard> getIndexcardByName(final String name) {
        try (final EntityManager em = pm.getEntityManager()) {
            final List<Indexcard> indexcards = em.createQuery("SELECT i FROM Indexcard i WHERE i.name = :name").setParameter("name", name).getResultList();
            if (indexcards.size() == 1) {
                return Optional.of(indexcards.get(0));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
           logger.log(Level.SEVERE,"Error occurred while fetching indexcard by name: " + name);
            throw e;
        }
    }


    /**
     * This method is used to find an object of type "Indexcard" in the persistent
     * persistence storage.
     *
     * @param name      The unique id of the index card.
     * @return          The object of type "Indexcard" or null if it does not exist.
     */
    public Optional<IndexcardBox> getIndexcardBoxByName(final String name) {
        try (final EntityManager em = pm.getEntityManager()) {
            final List<IndexcardBox> indexcardBoxList = em.createQuery("SELECT i FROM IndexcardBox i WHERE i.name = :name").setParameter("name", name).getResultList();
            if (indexcardBoxList.size() == 1) {
                return Optional.of(indexcardBoxList.get(0));
            } else {
                logger.log(Level.SEVERE,"No indexcard box found with name {0}", name);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while searching indexcard box with name {0}", name);
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public Optional<Indexcard> getIndexcardById(Long id) {
        try (final EntityManager em = pm.getEntityManager()) {
            return Optional.ofNullable(em.find(Indexcard.class, id));
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while fetching index card with id: {0}", id);
            throw e;
        }
    }

    /**
     * This method is used to get all objects of type "Category" in the persistent
     * persistence storage.
     *
     * @return List of all objects of type "Category", could be empty.
     */
    public List<Indexcard> getAllIndexcardByCategories(String categoryName){
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM Indexcard i JOIN Category c WHERE name = :name", Indexcard.class).setParameter("name", categoryName).getResultList();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while fetching all index cards");
            throw e;
        }
    }

    /**
     * This method is used to search the index card by name.
     *
     * @param text The text.
     * @return A list of the Indexcards that contains the text in their name.
     */
    public List<Indexcard> searchIndexcard(String text) {
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM Indexcard i WHERE LOWER(i.name) LIKE :text", Indexcard.class)
                    .setParameter("text", "%" + text.toLowerCase() + "%")
                    .getResultList();
        }
    }

    /**
     * Get all indexcards from an indexcard name list.
     *
     * @param indexcardNameList The names of the Indexcards.
     * @return A list the Indexcards with these names.
     */
    public List<Indexcard> getIndexcardsFromNameList(List<String> indexcardNameList) {
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM Indexcard i WHERE i.name IN :indexcardNameList", Indexcard.class)
                    .setParameter("indexcardNameList", indexcardNameList)
                    .getResultList();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while fetching all index cards");
            throw e;
        }
    }
}
