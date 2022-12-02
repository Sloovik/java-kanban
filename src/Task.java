/**
 * Класс - родитель сабтаска и эпика.
 */
public class Task {

    private Integer id;

    private String taskName;

    private String description;

    private StatusEnum status;

    public Task(Integer id, String taskName, String description, StatusEnum status) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public Task(String taskName, String description, StatusEnum status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.taskName = name;
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
