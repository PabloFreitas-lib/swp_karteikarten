package uni.myosotis.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import uni.myosotis.objects.Category;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to access the persistence storage for the object type "Category".
 */
public class CategoryRepository {

    private final Logger logger = Logger.getLogger(CategoryRepository.class.getName());

    private final PersistenceManager pm = new PersistenceManager();

    /**
     * This method is used to save an object of type "category" to the
     * persistence storage.
     *
     * @param category The category that should be saved to the persistence.
     */

    public void saveCategory(final Category category) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error while saving a Category to the database: " + e.getMessage());
        }
    }

    /**
     * This method is used to update an object of type "Category" to the persistent
     * persistence storage. If the Category does not exist at this point it will be created
     * and added to the database. Otherwise, the content of the given Category will be updated
     * instead.
     *
     * @param category The category that should be updated.
     */
    public void updateCategory(final Category category) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error while updating Category in the database: " + e.getMessage());
        }
    }

    /**
     * This method is used to delete an object of type "Category" in the
     * persistence storage.
     *
     * @param category The Category that should be deleted.
     */
    public void deleteCategory(final Category category) {
        try (final EntityManager em = pm.getEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.find(Category.class, category.getId()));
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while deleting category with name {0}", category.getCategoryName());
            logger.log(Level.SEVERE,"Error: {0}", e.getMessage());
        }
    }

    /**
     * This method is used to find an object of type "Category" in the persistent
     * persistence storage.
     *
     * @param name      The name of the Category.
     * @return          The object of type "Category" or null if it does not exist.
     */
    public Optional<Category> getCategoryByName(final String name) {
        try (final EntityManager em = pm.getEntityManager()) {
            return Optional.ofNullable(em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class).setParameter("name", name).getSingleResult());
        } catch (NoResultException e) {
            logger.log(Level.WARNING,"No category found with name {0}", name);
            return Optional.empty();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while searching for category by name {0}", name);
            logger.log(Level.SEVERE,"Error: {0}", e.getMessage());
            throw e;
        }
    }

    /**
     * This method is used to find an object of type "Category" in the
     * persistence storage by id.
     *
     * @param id      The id of the Category.
     * @return        The object of type "Category" or null if it does not exist.
     */
    public Optional<Category> getCategoryById(final Long id) {
        try (final EntityManager em = pm.getEntityManager()) {
            return Optional.ofNullable(em.createQuery("SELECT c FROM Category c WHERE c.id = :id", Category.class).setParameter("id", id).getSingleResult());
        } catch (NoResultException e) {
            logger.log(Level.WARNING,"No category found with id {0}", id);
            return Optional.empty();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while searching for category by id {0}", id);
            logger.log(Level.SEVERE,"Error: {0}", e.getMessage());
            throw e;
        }
    }

    /**
     * This method is used to get all objects of type "Category" in the persistent
     * persistence storage.
     *
     * @return List of all objects of type "Category", could be empty.
     */
    public List<Category> getAllCategories(){
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT k FROM Category k", Category.class).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while retrieving all categories", e);
            throw e;
        }
    }

    /**
     * This method is used to search for an object of type "Category" in the persistent
     * persistence storage.
     *
     * @param text      The text that should be searched for.
     * @return          List of all objects of type "Category" that contain the text, could be empty.
     */

    public List<Category> searchCategory(String text) {
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT i FROM Category i WHERE LOWER(i.name) LIKE :text", Category.class)
                    .setParameter("text", "%" + text.toLowerCase() + "%")
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while searching category with text {0}", text);
            logger.log(Level.SEVERE,"Error: {0}", e.getMessage());
            throw e;
        }
    }

    public List<Category> getChildren(Category category){
        try (final EntityManager em = pm.getEntityManager()) {
            return em.createQuery("SELECT k FROM Category k WHERE parent = :category", Category.class)
                    .setParameter("category", category)
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Error occurred while retrieving all categories", e);
            throw e;
        }
    }

}
