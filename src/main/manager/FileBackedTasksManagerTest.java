package main.manager;

import main.task.Epic;
import main.task.StatusEnum;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager>{

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
        Task task = new Task("Description", "Title", StatusEnum.NEW, Instant.now(), 0);
        manager.addTask(task);
        Epic epic = new Epic("Description", "Title", StatusEnum.NEW, Instant.now(), 0);
        manager.addEpic(epic);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory().toString()); // maybe not toString
        fileManager.loadFromFile("data.csv");
        assertEquals(List.of(task), manager.getTaskList());
        assertEquals(List.of(epic), manager.getEpicList());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory().toString()); //maybe not toString
        fileManager.save();
        fileManager.loadFromFile("data.csv");
        assertEquals(Collections.EMPTY_LIST, manager.getTaskList());
        assertEquals(Collections.EMPTY_LIST, manager.getEpicList());
        assertEquals(Collections.EMPTY_LIST, manager.getSubtaskList());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory().toString()); //maybe not toString
        fileManager.save();
        fileManager.loadFromFile("data.csv");
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}