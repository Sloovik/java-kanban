package main.test;

import main.http.HttpTaskManager;
import main.http.KVServer;
import main.manager.HistoryManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.manager.TaskTypes;
import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest<T extends TaskManagerTest<HttpTaskManager>> {
    private KVServer server;
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            manager = Managers.getDefault(historyManager);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = new Task("description1", "name1", StatusEnum.NEW, Instant.now(), 1);
        Task task2 = new Task("description2", "name2", StatusEnum.NEW, Instant.now(), 2);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTaskByID(task1.getId());
        manager.getTaskByID(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getTaskList(), list);
    }

    @Test
    public void shouldLoadEpics() {
        Epic epic1 = new Epic("description1", "name1", StatusEnum.NEW, Instant.now(), 3, TaskTypes.EPIC);
        Epic epic2 = new Epic("description2", "name2", StatusEnum.NEW, Instant.now(), 4, TaskTypes.EPIC);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.getEpicByID(epic1.getId());
        manager.getEpicByID(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getEpicList(), list);
    }

    @Test
    public void shouldLoadSubtasks() {
        Epic epic1 = new Epic("description1", "name1", StatusEnum.NEW, Instant.now(), 5);
        Subtask subtask1 = new Subtask("description1", "name1", StatusEnum.NEW, epic1.getId()
                , Instant.now(), 6, TaskTypes.SUBTASK);
        Subtask subtask2 = new Subtask("description2", "name2", StatusEnum.NEW, epic1.getId(),
                Instant.now(), 7, TaskTypes.SUBTASK);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getSubTaskByID(subtask1.getId());
        manager.getSubTaskByID(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getSubtaskList(), list);
    }

}