package main.task;

import main.manager.TaskTypes;

import java.util.Objects;

/**
 * Класс - родитель сабтаска и эпика.
 */
public class Task {

    protected Integer id;

    protected TaskTypes taskType;

    protected String taskName;

    protected String description;

    protected StatusEnum status;

    public Task(Integer id, String taskName, String description, StatusEnum status) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.taskType = TaskTypes.TASK;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "main.task.Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, taskType, taskName, status, description, "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(id, otherTask.id) && Objects.equals(description, otherTask.description)
                && Objects.equals(taskName, otherTask.taskName) && Objects.equals(taskType, otherTask.taskType)
                && Objects.equals(status, otherTask.status);
    }

}
