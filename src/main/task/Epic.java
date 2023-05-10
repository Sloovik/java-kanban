package main.task;

import main.manager.TaskTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс эпик (важные задачи).
 */
public class Epic extends Task {

    private List<Integer> subtaskIDs;

    protected Instant endTime;

//    public Epic(Integer id, String name, String description) {
//        super(id, name, description);
//        subtaskIDs = new ArrayList<>();
//        this.taskType = TaskTypes.EPIC;
//    }

//    public Epic(int id, String name, StatusEnum status, String description) {
//        super(id, name, description);
//        this.subtaskIDs = new ArrayList<>();
//        this.taskType = TaskTypes.EPIC;
//        this.status = status;
//    }

    public Epic(String description, String taskName, StatusEnum status, Instant startTime, long duration, TaskTypes taskType) {
        super(description, taskName, status, startTime, duration, taskType);
        this.endTime = super.getEndTime();
        this.subtaskIDs = new ArrayList<>();
    }


    public List<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }

    public void setSubtaskIDs(List<Integer> subtaskIDs) {
        this.subtaskIDs = subtaskIDs;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void appendSubtaskId(Integer subtaskId) {
        this.subtaskIDs.add(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", taskType=" + taskType +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime().toEpochMilli() +
                ", endTime=" + getEndTime().toEpochMilli() +
                ", duration=" + getDuration() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(subtaskIDs, otherEpic.subtaskIDs);
    }
}

