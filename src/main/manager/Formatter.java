package main.manager;

import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;

import java.time.Instant;
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

//    static List<Integer> fromString(String value) {
//        String[] idsString = value.split(",");
//        List<Integer> tasksIds = new ArrayList<>();
//
//        for (String idString : idsString) {
//            tasksIds.add(Integer.valueOf(idString));
//        }
//        return tasksIds;
//
//    }

    static Task fromString(String value) {
        String[] params = value.split(",");
        int id = Integer.parseInt(params[0]);
        String type = params[1];
        String name = params[2];
        StatusEnum status = StatusEnum.valueOf(params[3].toUpperCase());
        String description = params[4];
        Instant startTime = Instant.parse(params[5]);
        long duration = Long.parseLong(params[6]);
        Integer epicId = type.equals("SUBTASK") ? Integer.parseInt(params[7]) : null;

        if (type.equals("EPIC")) {
            Epic epic = new Epic(description, name, status, startTime, duration);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else if (type.equals("SUBTASK")) {
            Subtask subtask = new Subtask(description, name, status, epicId, startTime, duration);
            subtask.setId(id);
            return subtask;
        } else {
            Task task = new Task(description, name, status, startTime, duration);
            task.setId(id);
            return task;
        }
    }
}
