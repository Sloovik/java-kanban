/**
 * Класс сабтаск (подзадачи эпика).
 */
public class Subtask extends Task {

    private final Integer epicId;

    public Subtask(Integer id, String taskName, String description, StatusEnum status, Integer epicId) {
        super(id, taskName, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusEnum status, Integer epicId) {
        super(0, name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicID() {
        return epicId;
    }



}
