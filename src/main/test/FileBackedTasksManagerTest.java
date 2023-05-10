package main.test;

import main.manager.FileBackedTasksManager;
import main.manager.InMemoryTaskManager;
import main.manager.Managers;
import main.manager.TaskTypes;
import main.task.Epic;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static main.task.StatusEnum.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public static final Path path = Path.of("data.csv");
    File file = new File(String.valueOf(path));
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager("data.csv");
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void shouldCorrectlySaveAndLoad() {
        Task task = new Task("Description", "Title", NEW, Instant.now(), 0, TaskTypes.TASK);
        manager.addTask(task);
        Epic epic = new Epic("Description", "Title", NEW, Instant.now(), 0, TaskTypes.EPIC);
        manager.addEpic(epic);
        FileBackedTasksManager fileManager = new FileBackedTasksManager("data.csv");
        fileManager.loadFromFile("data.csv");
        assertEquals(List.of(task), manager.getTaskList());
        assertEquals(List.of(epic), manager.getEpicList());
    } // passed

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory().toString()); //maybe not toString
        fileManager.save();
        fileManager.loadFromFile("data.csv");
        assertEquals(Collections.EMPTY_LIST, manager.getTaskList());
        assertEquals(Collections.EMPTY_LIST, manager.getEpicList());
        assertEquals(Collections.EMPTY_LIST, manager.getSubtaskList());
    } // passed

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory().toString()); //maybe not toString
        fileManager.save();
        fileManager.loadFromFile("data.csv");
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    } // passed
}