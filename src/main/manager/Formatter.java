package main.manager;

import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Formatter {

    static String toString(HistoryManager manager) {
        List<String> s = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            s.add(String.valueOf(task.getId()));
        }
        return String.join(",", s);
    }

    static List<Integer> fromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> tasksIds = new ArrayList<>();

        for (String idString : idsString) {
            tasksIds.add(Integer.valueOf(idString));
        }
        return tasksIds;

    }

    static Task fromString(String value, TaskTypes taskType, FileBackedTasksManager fileBackedTasksManager) {
        String[] dataOfTask = value.split(",", 6);
        int id = Integer.parseInt(dataOfTask[0]);
        String name = dataOfTask[2];
        StatusEnum status = StatusEnum.valueOf(dataOfTask[3]);
        String description = dataOfTask[4];
        String epicIdString = dataOfTask[5].trim();

        return switch (taskType) {
            case TASK -> new Task(id, name, description, status);
            case EPIC -> new Epic(id, name, status, description);
            case SUBTASK -> new Subtask(id, name, description, status,
                            Integer.valueOf(epicIdString));
        };
    }
}
