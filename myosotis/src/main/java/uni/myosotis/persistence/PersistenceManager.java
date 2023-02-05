package uni.myosotis.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This class is used to get the Entity-Manager for the persistence.
 */
public class PersistenceManager {

    /** References the "persistence-unit" name from META-INF/persistence.xml */
    private static final String PU_NAME = "myosotisDB";

    /** emFactory contains the Factory of the Entity-Manager */
    private static final EntityManagerFactory emFactory;

    /* Creates the Entity-Manager of the given factory */
    static {
        emFactory = Persistence.createEntityManagerFactory(PU_NAME);
    }

    /**
     * Method to get the Entity-Manager for the persistent persistence.
     *
     * @return      The Entity-Manager.
     */
    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

}
