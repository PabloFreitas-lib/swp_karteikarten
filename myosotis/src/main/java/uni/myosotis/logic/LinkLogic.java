package uni.myosotis.logic;

import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.Link;
import uni.myosotis.persistence.LinkRepository;

import java.util.List;

/**
 * The logic for the Links
 */
public class LinkLogic {

    /**
     * The repository for the Links.
     */
    private final LinkRepository linkRepository;

    /**
     * Creates a new LinkLogic.
     */
    public LinkLogic() {
        this.linkRepository = new LinkRepository();
    }

    /**
     * Creates a new Link and saves it in the LinkRepository.
     *
     * @param term      The term the Indexcard is linked to.
     * @param indexcard The Indexcard the term is linked with.
     * @return The created Link.
     */
    public Link createLink(final String term, final Indexcard indexcard) {
        Link link = new Link(term, indexcard);
        linkRepository.save(link);
        return link;
    }

    /**
     * Deletes an existing Link from the persistence storage.
     *
     * @param link The link that should be deleted.
     */
    public void deleteLink(final Link link) {
        linkRepository.delete(link);
    }

    /**
     * Returns a list of all Links saved in the persistence storage.
     *
     * @return A list of all Links saved in the persistence storage, could be empty.
     */
    public List<Link> getAllLinks() {
        return linkRepository.getAllLinks();
    }

    /**
     * Returns a list of all Links saved in the persistence storage that are linked to a specific term.
     *
     * @param term The specific term.
     * @return A list of all Links saved in the persistence storage that are linked to a specific term,
     *         could be empty.
     */
    public List<Link> getLinksByTerm(final String term) {
        return linkRepository.getLinksByTerm(term);
    }

    /**
     * Returns a list of all Links saved in the persistence storage that are linked with a specific Indexcard.
     *
     * @param indexcard The specific Indexcard.
     * @return A list of all Links saved in the persistence storage that are linked with a specific Indexcard,
     *         could be empty.
     */
    public List<Link> getLinksByIndexcard(final Indexcard indexcard) {
        return linkRepository.getLinksByIndexcard(indexcard);
    }

    /**
     * Deletes all Links saved in the persistence storage that are linked to a specific term.
     *
     * @param term The specific term.
     */
    public void deleteLinksByTerm(final String term) {
        for (Link link : getLinksByTerm(term)) {
            deleteLink(link);
        }
    }

    /**
     * Deletes all Links saved in the persistence storage that are linked with a specific Indexcard.
     *
     * @param indexcard The specific Indexcard.
     */
    public void deleteLinksByIndexcard(final Indexcard indexcard) {
        for (Link link : getLinksByIndexcard(indexcard)) {
            deleteLink(link);
        }
    }
}
