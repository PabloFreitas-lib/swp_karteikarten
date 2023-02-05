package uni.myosotis.objects;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * This class represents a LeitnerLearnSystem.
 */
@Entity
public class LeitnerLearnSystem {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LeitnerLearnSystem.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    @OneToMany
    private List<Box> boxes;

    private List<String> indexcardList;
    private int progress;

    private int numberOfBoxes;

    private String sortType;

    private List<String> boxList;

    private boolean started;

    public LeitnerLearnSystem() {
    }

    /**
     * Creates a new LeitnerLearnSystem.
     * All indexcards are places in the first box.
     * @param indexcardList The list of indexcards that should be learned.
     */
    public LeitnerLearnSystem(String name, List<String> indexcardList, int numberOfBoxes,String sortType) {
        this.name = name;
        this.indexcardList = indexcardList;
        this.progress = 0;
        this.numberOfBoxes = numberOfBoxes;
        this.boxes = new ArrayList<>();
        this.boxList = null;
        this.started = false;
        setSortType(sortType);
        for (int i = 0; i < numberOfBoxes; i++) {
            this.boxes.add(new Box("Box " + (i + 1)));
        }
        // add all indexcards to the first box
        this.boxes.get(0).setIndexcardNames(indexcardList);
        logger.log(Level.INFO, name + "created");
    }

    /**
     * Get ID of the LeitnerLearnSystem.
     * @return
     */
    public Long getId() {
        return id;
    }


    /**
     * This method is called when the user answers a question correctly.
     * The indexcard is moved to the next box.
     *
     * @param indexcard The Indexcard that should be moved.
     */
    public void correctAnswer(Indexcard indexcard) {
        logger.log(Level.INFO, "correctAnswer for indexcard: " + indexcard.getName());
        moveIndexcardToNextBox(indexcard.getName());
        logger.log(Level.INFO, "indexcard is moved to the next box");
    }

    /**
     * This method is called when the user answers a question incorrectly.
     * The indexcard is moved to the first box.
     *
     * @param indexcard The Indexcard that should be moved.
     */
    public void wrongAnswer(Indexcard indexcard) {
        logger.log(Level.INFO, "wrongAnswer for indexcard: " + indexcard.getName());
        moveIndexcardToPreviousBox(indexcard.getName());
        logger.log(Level.INFO, "indexcard is moved to the previously box");
    }


    /**
     * This methoed is called to get the learn system name.
     * @return
     */

    public String getName(){
        return this.name;
    }

    /**
     * This method is called to get the indexcard list from a box.
     * @return
     */
    public List<String> getIndexcardFromBox(int boxNumber) {
        return this.boxes.get(boxNumber).getIndexcardNames();
    }


    /**
     * this method return the indexcard that should be learned next.
     * The order inside the indexcard list is the same as the order in the boxes.
     * @return The indexcards that should be learned next.
     */
    public List<String> getNextIndexcardNames() {
        List<String> nextIndexcardNames = new ArrayList<>();
        for (int i = 0; i < this.numberOfBoxes; i++) {
            nextIndexcardNames.addAll(this.boxes.get(i).getIndexcardNames());
        }
        return nextIndexcardNames;
    }

    /**
     * This method is called to increase the progress.
     */
    public void increaseProgress() {
        if (indexcardList.size() > this.progress)
            this.progress++;
    }

    /**
     * This method is called to decrease the progress.
     */
    public void decreaseProgress() {
        if (this.progress > 0)
            this.progress--;
    }
    /**
     * This method is called to get the progress.
     * @return
     */
    public int getProgress() {
        return progress;
    }

    /**
     * This method is called to set the progress.
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }
    /**
     * This method is called to get boxes.
     * @return
     */
    public List<Box> getBoxes() {
        return boxes;
    }

    /**
     * This method is called to move an indexcard to the next box.
     * @param indexcardName The name of the indexcard that should be moved.
     */
    public void moveIndexcardToNextBox(String indexcardName) {
        for (int i = 0; i < this.numberOfBoxes; i++) {
            if (this.boxes.get(i).getIndexcardNames().contains(indexcardName)) {
                if (i < this.numberOfBoxes - 1) {
                    this.boxes.get(i).removeIndexcard(indexcardName);
                    this.boxes.get(i + 1).addIndexcard(indexcardName);
                }
                break;
            }
        }
    }

    /**
     * This method is called to move an indexcard to the previous box.
     * @param indexcardName The name of the indexcard that should be moved.
     */
    public void moveIndexcardToPreviousBox(String indexcardName) {
        for (int i = 0; i < this.numberOfBoxes; i++) {
            if (this.boxes.get(i).getIndexcardNames().contains(indexcardName)) {
                if (i > 0) {
                    this.boxes.get(i).removeIndexcard(indexcardName);
                    this.boxes.get(i - 1).addIndexcard(indexcardName);
                }
                break;
            }
        }
    }

    /*public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * This method return the box with the given name.
     * The Boxes are numbered from 1 to numberOfBoxes.
     * The name is set: Box 1, Box 2, ...
     */
    public Box getIndexcardBox(String boxName){
        for (Box box : this.boxes) {
            if (box.getName().equals(boxName)) {
                return box;
            }
        }
        return null;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        if(sortType!=null)
            this.sortType = sortType;
        else this.sortType = "Random";
    }

    public void setStarted(boolean start){
        this.started = start;
    }

    public boolean getStarted(){
        return this.started;
    }

    /*public void setBoxList (List<String> boxes){
        this.boxList = boxes;
    }

    public List<String> getBoxList (){
        return this.boxList;
    }*/
}
