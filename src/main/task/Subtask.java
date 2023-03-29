package main.task;

import main.manager.TaskTypes;

import java.time.Instant;
import java.util.Objects;

/**
 * Класс сабтаск (подзадачи эпика).
 */
public class Subtask extends Task {

    protected Integer epicId;



    public Subtask(Integer id, String taskName, String description, StatusEnum status, Integer epicId) {
        super(id, taskName, description, status);
        this.epicId = epicId;


    }

    public Subtask(String taskName, String description, StatusEnum status, Integer epicId) {
        super(epicId, taskName, description, status);
        this.epicId = epicId;
        this.taskType = TaskTypes.SUBTASK;
    }

    public Subtask(String description, String taskName, StatusEnum status, Integer epicId, Instant startTime,
                   long duration) {
        super(description, taskName, status, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicID() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", taskType=" + taskType +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    } // finalize with endTime

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, taskType, taskName, status, description, getEpicID());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subtask otherSubtask = (Subtask) obj;
        return Objects.equals(epicId, otherSubtask.epicId);
    }


}
