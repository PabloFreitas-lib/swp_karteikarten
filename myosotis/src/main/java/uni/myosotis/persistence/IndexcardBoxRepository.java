package uni.myosotis.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import uni.myosotis.objects.IndexcardBox;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * This class is used to access the persistence storage for the object type "IndexcardBox".
 */
public class IndexcardBoxRepository {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(IndexcardBoxRepository.class.getName());

    private final PersistenceManager pm = new PersistenceManager();

    /**
     * @author Len Thiemann
     * <p>
     * This method is used to save an object of type "indexcardBox" to the persistent
     * persistence storage.
     *
     * @param indexcardBox     The index card that should be saved to the persistence.
     * @return              Status, -1 means an error has been occurred on save.
     */
    public int saveIndexcardBox(final IndexcardBox indexcardBox) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(indexcardBox);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving index card: {0}", indexcardBox.getName());
            return -1;
        }
        //logger.log(Level.INFO, "Successfully saved index card: {0}", indexcardBox.getName());
        return 0;
    }

    /**
     * @author Johannes Neugebauer
     * <p>
     * This method is used to update an object of type "indexcardBox" to the persistent
     * persistence storage. If the Card does not exist at this point it will be created
     * and added to the database. Otherwise, the content of the given card will be updated
     * instead.
     *
     * @param indexcardBox     The index card that should be updated.
     * @return              Status, -1 means an error has been occurred on update.
     */
    public int updateIndexcardBox(final IndexcardBox indexcardBox) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(indexcardBox);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating index card: {0}", indexcardBox.getName());
            return -1;
        }
        //logger.log(Level.INFO, "Successfully updated index card: {0}", indexcardBox.getName());
        return 0;
    }

    /**
     * This method is used to delete an object of type "IndexcardBox" in the persistent
     * persistence storage.
     *
     * @param name      The unique id of the index card.
     * @return          Status, -1 means an error has been occurred on delete.
     */
    public int deleteIndexcardBox(final String name) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.find(IndexcardBox.class, name));
            em.getTransaction().commit();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting index card: {0}", name);
            return -1;
        }
        //logger.log(Level.INFO, "Successfully deleted index card: {0}", name);
        return 0;
    }

    /**
     * This method is used to find an object of type "IndexcardBox" in the persistent
     * persistence storage.
     *
     * @param name      The unique id of the index card.
     * @return          The object of type "IndexcardBox" or null if it does not exist.
     */
    public Optional<IndexcardBox> getIndexcardBoxByName(final String name) {
        try (final EntityManager em = pm.getEntityManager()) {
            return Optional.ofNullable(em.find(IndexcardBox.class, name));
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE,"Error retrieving indexcard box by name: {}",name);
            logger.log(Level.SEVERE,e.getMessage());
            return Optional.empty();
        }
    }




    /**
     * This method is used to get all objects of type "Indexcard" in the persistent
     * persistence storage.
     *
     * @return List of all objects of type "Indexcard", could be empty.
     */
    public List<IndexcardBox> getAllIndexcardBoxes(){
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM IndexcardBox i").getResultList();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving all index cards");
            return null;
        }
    }

    /**
     * This method gets all IndexcardBoxes that contains a part of
     * the given text in their name, by using a LIKE query and
     * UPPER function to make it case insensitiv.
     *
     * @param text The text.
     * @return A list of the IndexcardBoxes that contains a part of the given text in their name.
     */
    public List<IndexcardBox> searchIndexcardBox(String text) {
       try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM IndexcardBox i WHERE UPPER(i.name) LIKE UPPER(:text)").setParameter("text", "%" + text + "%").getResultList();
       }
       catch (Exception e) {
           logger.log(Level.SEVERE, "Error retrieving all index cards");
           return null;
       }
    }
}
