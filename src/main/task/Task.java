package main.task;

import main.manager.TaskTypes;

import java.time.Instant;
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
    protected Instant startTime;
    protected long duration;

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

    public Task(String description, String taskName, StatusEnum status, Instant startTime, long duration) {
        this.description = description;
        this.taskName = taskName;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getEndTime() {
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime.toEpochMilli() +
                ", endTime=" + getEndTime().toEpochMilli() +
                ", duration=" + duration +
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
