package main.test;

import main.manager.HistoryManager;
import main.manager.InMemoryHistoryManager;
import main.manager.TaskTypes;
import main.task.StatusEnum;
import main.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    HistoryManager manager;
    private int id = 0;

    public int generateId() {
        return ++id;
    }

    protected Task addTask() {
        return new Task("Description", "Title", StatusEnum.NEW, Instant.now(), 0, TaskTypes.TASK);
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = addTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    } // passed

    @Test
    public void shouldRemoveTask() {
        Task task1 = addTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.getId());
        assertEquals(List.of(task1, task3), manager.getHistory());
    } // passed

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task = addTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    } // passed

    @Test
    public void shouldHistoryIsEmpty() {
        Task task1 = addTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = addTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = addTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    } // passed

    @Test
    public void shouldNotRemoveTaskWithBadId() {
        Task task = addTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(0);
        assertEquals(List.of(task), manager.getHistory());
    } // passed
}
