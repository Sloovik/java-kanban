import java.util.ArrayList;
import java.util.List;

/**
 * Класс эпик (важные задачи).
 */
public class Epic extends Task {

    private List<Integer> subtaskIDs;


    public Epic(String name, String description) {
        super(0, name, description);
        subtaskIDs = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
        subtaskIDs = new ArrayList<>();
    }


    public List<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }

    public void setSubtaskIDs(List<Integer> subtaskIDs) {
        this.subtaskIDs = subtaskIDs;
    }

}

