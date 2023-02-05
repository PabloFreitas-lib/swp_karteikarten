package uni.myosotis.objects;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an index card.
 */
@Entity
public class Indexcard implements Serializable {

    private String name;

    private String question;

    @Column(length = Integer.MAX_VALUE)
    private String answer;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Keyword> keywords;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Link> links;

    /**
     * Id of the index card, needs to be unique within the persistence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Creates a new Indexcard with the given name, question, answer and keywords.
     *
     * @param name     The name of the Indexcard.
     * @param question The question of the Indexcard.
     * @param answer   The answer of the Indexcard.
     * @param keywords The list keywords of the Indexcard.
     */
    public Indexcard(final String name, final String question, final String answer, final List<Keyword> keywords) {

        this.name = name;
        this.question = question;
        this.answer = answer;
        this.keywords = keywords;

    }
    /**
     * Creates a new Indexcard with the given name, question, answer and keywords.
     *
     * @param name     The name of the Indexcard.
     * @param question The question of the Indexcard.
     * @param answer   The answer of the Indexcard.
     * @param keywords The keywords of the Indexcard.
     * @param links The links of the Indexcard.
     */
    public Indexcard(final String name, final String question, final String answer, final List<Keyword> keywords, final List<Link> links) {

        this.name = name;
        this.question = question;
        this.answer = answer;
        this.keywords = keywords;
        this.links = links;

    }
    /** Creates a new Indexcard with the given name, question, answer and keywords.
     * default constructor.
     */
    public Indexcard () {

    }

    /**
     * Returns the name of the Indexcard.
     *
     * @return The name of the Indexcard.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the question of the Indexcard.
     *
     * @return The question of the Indexcard.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the answer of the Indexcard.
     *
     * @return The answer of the Indexcard.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Returns the keywords of the Indexcard.
     *
     * @return The keywords of the Indexcard.
     */
    public List<Keyword> getKeywords() {
        return keywords;
    }

    /**
     * Returns the names of the keywords of the Indexcard.
     *
     * @return The names of the keywords of the Indexcard.
     */
    public List<String> getKeywordNames() {
        return keywords.stream().
                map(Keyword::getName).toList();
    }

    /**
     * Returns the id of the Indexcard.
     *
     * @return The id of the Indexcard.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the name of the Indexcard to the given name.
     *
     * @param name The name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the question of the Indexcard to the given name.
     *
     * @param question The question.
     */
    public void setQuestion(final String question) {
        this.question = question;
    }

    /**
     * Sets the answer of the Indexcard to the given name.
     *
     * @param answer The answer.
     */
    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    /**
     * Sets the name of the Indexcard to the given name.
     *
     * @param keywords The Keywords.
     */
    public void setKeywords(final List<Keyword> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns the links of the Indexcard.
     *
     * @return A list of all Links of the Indexcard.
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Sets the links of the Indexcard.
     *
     * @param links The Links.
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
