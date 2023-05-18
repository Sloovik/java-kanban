package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.adapter.InstantAdapter;
import main.http.KVServer;
import main.manager.*;
import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;

import java.time.Instant;

import static main.task.StatusEnum.NEW;

/**
 * Проверяем методы работы со списками дел.
 */

public class Main {

    public static void main(String[] args) {
        KVServer server;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            TaskManager httpTaskManager = Managers.getDefault(historyManager);

            Task task1 = new Task(
                    "Get a job", "Employment",
                    StatusEnum.NEW,
                    Instant.now(),
                    1
            );
            httpTaskManager.addTask(task1);

            Epic epic1 = new Epic(
                    "Eat, sleep, code, repeat",
                    "Lifestyle",
                    StatusEnum.NEW,
                    Instant.now(),
                    2,
                    TaskTypes.EPIC
            );
            httpTaskManager.addEpic(epic1);

            Subtask subtask1 = new Subtask(
                    "Buy 3 new cars",
                    "Pocket money",
                    StatusEnum.NEW,
                    epic1.getId(),
                    Instant.now(),
                    3,
                    TaskTypes.SUBTASK
            );
            httpTaskManager.addSubtask(subtask1);


            httpTaskManager.getTaskByID(task1.getId());
            httpTaskManager.getEpicByID(epic1.getId());
            httpTaskManager.getSubTaskByID(subtask1.getId());

            System.out.println("Printing all tasks");
            System.out.println(gson.toJson(httpTaskManager.getTaskList()));
            System.out.println("Printing all epics");
            System.out.println(gson.toJson(httpTaskManager.getEpicList()));
            System.out.println("Printing all subtasks");
            System.out.println(gson.toJson(httpTaskManager.getSubtaskList()));
            System.out.println("Loaded manager");
            System.out.println(httpTaskManager);
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        TaskManager taskManager = Managers.getDefault("data/data.csv");
//
//        Integer task1 = taskManager.addTask(new Task(1, "Первый таск", "Убраться", NEW));
//        Integer task2 = taskManager.addTask(new Task(2, "Второй таск", "Уехать за город", NEW));
//
//        Integer epic1 = taskManager.addEpic(new Epic(3, "Мой первый эпик", "Поступить в школу"));
//
//        Integer subtask1 = taskManager.addSubtask(
//                new Subtask("Первый сабтаск первого эпика", "Выучить язык", NEW, 3));
//
//        Integer subtask2 = taskManager.addSubtask(
//                new Subtask("Второй сабтаск первого эпика", "Помыться", NEW, epic1));
//
//        Integer epic2 = taskManager.addEpic(new Epic(6, "Второй эпик", "Переехать к подруге"));
//
//        Integer subtask3 = taskManager.addSubtask(
//                new Subtask("Первый сабтаск второго эпика", "Побриться", NEW, epic2));
//
//        taskManager.getTaskByID(task1);
//        taskManager.getTaskByID(task2);
//        taskManager.getEpicByID(epic1);
//        taskManager.getEpicByID(epic2);
//        taskManager.getSubTaskByID(subtask1);
//        taskManager.getSubTaskByID(subtask2);
//        taskManager.getSubTaskByID(subtask3);
//        taskManager.getHistory().forEach(s -> System.out.println(s.getId()));
//
//        System.out.println("-----");
//        System.out.println(taskManager.getSubtaskList());
//        taskManager.removeEpic(epic1);
//        taskManager.getHistory().forEach(s -> System.out.println(s.getId()));
//
//        System.out.println("-----");
//
//        System.out.println(taskManager.getSubtaskList());
//        taskManager.removeEpic(epic2);
//        System.out.println(taskManager.getSubtaskList());
//        taskManager.getHistory().forEach(s -> System.out.println(s.getId()));
//
//        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile("data/data.csv");
//
//        if (!fileBackedTasksManager.getHistory().equals(taskManager.getHistory())) {
//            System.out.println("Histories not equal");
//            throw new IllegalArgumentException("Histories not equal");
//        } else {
//            System.out.println("Histories are equal! <3");
//        }
    }

}
