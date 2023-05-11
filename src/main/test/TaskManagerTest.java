package main.test;

import main.manager.TaskManager;
import main.manager.TaskTypes;
import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TaskManagerTest <T extends TaskManager> {

    protected T manager;
    protected Task createTask() {
        return new Task("Description", "Title", StatusEnum.NEW, Instant.now().minusSeconds(100000000L), 0, TaskTypes.TASK);
    }
    protected Task createSecondTask() {
        return new Task("Description", "Title2", StatusEnum.NEW, Instant.now().plusSeconds(100000000000L), 0, TaskTypes.TASK);
    }
    protected Task createThirdTask() {
        return new Task("Description", "Title3", StatusEnum.NEW, Instant.now(), 0, TaskTypes.TASK);
    }
    protected Epic createEpic() {
        return new Epic("Description", "Title", StatusEnum.NEW, Instant.now(), 0, TaskTypes.EPIC);
    }
    protected Subtask createSubtask(Epic epic) {
        return new Subtask("Description", "Title", StatusEnum.NEW, epic.getId(), Instant.now(), 0, TaskTypes.SUBTASK);
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask();
        manager.addTask(task);
        List<Task> tasks = manager.getTaskList();
        assertNotNull(task.getStatus());
        assertEquals(StatusEnum.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    } // passed

    @Test
    public void shouldCreateEpic() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        List<Epic> epics = manager.getEpicList();
        assertNotNull(epic.getStatus());
        assertEquals(StatusEnum.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtaskIDs());
        assertEquals(List.of(epic), epics);
    } // passed

    @Test
    public void shouldCreateSubtask() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        List<Subtask> subtasks = manager.getSubtaskList();
        assertNotNull(subtask.getStatus());
        assertEquals(epic.getId(), subtask.getEpicID());
        assertEquals(StatusEnum.NEW, subtask.getStatus());
        assertEquals(List.of(subtask), subtasks);
        assertEquals(List.of(subtask.getId()), epic.getSubtaskIDs());
    } // passed

    @Test
    void shouldReturnNullWhenCreateTaskNull() {
        Integer task = manager.addTask(null);
        assertNull(task);
    } // passed

    @Test
    void shouldReturnNullWhenCreateEpicNull() {
        Integer epic = manager.addEpic(null);
        assertNull(epic);
    } // passed

    @Test
    void shouldReturnNullWhenCreateSubtaskNull() {
        Integer subtask = manager.addSubtask(null);
        assertNull(subtask);
    } // passed

    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = createTask();
        manager.addTask(task);
        task.setStatus(StatusEnum.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(StatusEnum.IN_PROGRESS, manager.getTaskByID(task.getId()).getStatus());
    } // passed

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        epic.setStatus(StatusEnum.IN_PROGRESS);
        assertEquals(StatusEnum.IN_PROGRESS, manager.getEpicByID(epic.getId()).getStatus());
    } // passed

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        subtask.setStatus(StatusEnum.IN_PROGRESS);
        manager.updateSubtask(subtask);
        assertEquals(StatusEnum.IN_PROGRESS, manager.getSubTaskByID(subtask.getId()).getStatus());
        assertEquals(StatusEnum.IN_PROGRESS, manager.getEpicByID(epic.getId()).getStatus());
    } // passed

    @Test
    public void shouldUpdateTaskStatusToDone() {
        Task task = createTask();
        manager.addTask(task);
        task.setStatus(StatusEnum.DONE);
        manager.updateTask(task);
        assertEquals(StatusEnum.DONE, manager.getTaskByID(task.getId()).getStatus());
    } // passed

    @Test
    public void shouldUpdateEpicStatusToDone() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        epic.setStatus(StatusEnum.DONE);
        assertEquals(StatusEnum.DONE, manager.getEpicByID(epic.getId()).getStatus());
    } // passed

    @Test
    public void shouldUpdateSubtaskStatusToDone() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        subtask.setStatus(StatusEnum.DONE);
        manager.updateSubtask(subtask);
        assertEquals(StatusEnum.DONE, manager.getSubTaskByID(subtask.getId()).getStatus());
        assertEquals(StatusEnum.DONE, manager.getEpicByID(epic.getId()).getStatus());
    } // passed

    @Test
    public void shouldNotUpdateTaskIfNull() {
        Task task = createTask();
        manager.addTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskByID(task.getId()));
    } // passed

    @Test
    public void shouldNotUpdateEpicIfNull() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.updateEpic(null);
        assertEquals(epic, manager.getEpicByID(epic.getId()));
    } // passed

    @Test
    public void shouldNotUpdateSubtaskIfNull() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.updateSubtask(null);
        assertEquals(subtask, manager.getSubTaskByID(subtask.getId()));
    } // passed

    @Test
    public void shouldDeleteAllTasks() {
        Task task = createTask();
        manager.addTask(task);
        manager.removeAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getTaskList());
    } // passed

    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.removeAllEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getEpicList());
    } // passed

    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.removeAllSubtasks();
        assertTrue(epic.getSubtaskIDs().isEmpty());
        assertTrue(manager.getSubtaskList().isEmpty());
    } // passed


    @Test
    public void shouldDeleteTaskById() {
        Task task = createTask();
        manager.addTask(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getTaskList());
    } // passed

    @Test
    public void shouldDeleteEpicById() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.remove(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getEpicList());
    } // passed

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        Task task = createTask();
        manager.addTask(task);
        manager.remove(999);
        assertEquals(List.of(task), manager.getTaskList());
    } // passed

    @Test
    public void shouldNotDeleteEpicIfBadId() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.remove(999);
        assertEquals(List.of(epic), manager.getEpicList());
    } // passed

    @Test
    public void shouldNotDeleteSubtaskIfBadId() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.remove(999);
        assertEquals(List.of(subtask), manager.getSubtaskList());
        assertEquals(List.of(subtask.getId()), manager.getEpicByID(epic.getId()).getSubtaskIDs());
    } // passed

    @Test
    public void shouldDoNothingIfTaskHashMapIsEmpty(){
        manager.removeAllTasks();
        manager.remove(999);
        assertEquals(0, manager.getTaskList().size());
    } // passed

    @Test
    public void shouldDoNothingIfEpicHashMapIsEmpty(){
        manager.removeAllEpics();
        manager.remove(999);
        assertTrue(manager.getEpicList().isEmpty());
    } // passed

    @Test
    public void shouldDoNothingIfSubtaskHashMapIsEmpty(){
        manager.removeAllEpics();
        manager.remove(999);
        assertEquals(0, manager.getSubtaskList().size());
    } // passed


    @Test
    public void shouldReturnEmptyListTasksIfNoTasks() {
        assertTrue(manager.getTaskList().isEmpty());
    } // passed

    @Test
    public void shouldReturnEmptyListEpicsIfNoEpics() {
        assertTrue(manager.getEpicList().isEmpty());
    } // passed

    @Test
    public void shouldReturnEmptyListSubtasksIfNoSubtasks() {
        assertTrue(manager.getSubtaskList().isEmpty());
    } // passed

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskByID(999));
    } // passed

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicByID(999));
    } // passed

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubTaskByID(999));
    } // passed

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    } // passed

    @Test
    public void shouldReturnEmptyHistoryIfTasksNotExist() {
        manager.getTaskByID(999);
        manager.getSubTaskByID(999);
        manager.getEpicByID(999);
        assertTrue(manager.getHistory().isEmpty());
    } // passed

    @Test
    public void shouldReturnHistoryWithTasks() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.getEpicByID(epic.getId());
        manager.getSubTaskByID(subtask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(subtask));
        assertTrue(list.contains(epic));
    } // passed

}
