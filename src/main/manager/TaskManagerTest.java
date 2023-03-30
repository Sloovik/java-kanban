package main.manager;

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
        return new Task("Description", "Title", StatusEnum.NEW, Instant.now(), 0);
    }
    protected Epic createEpic() {

        return new Epic("Description", "Title", StatusEnum.NEW, Instant.now(), 0);
    }
    protected Subtask createSubtask(Epic epic) {
        return new Subtask("Description", "Title", StatusEnum.NEW, epic.getId(), Instant.now(), 0);
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask();
        manager.addTask(task);
        List<Task> tasks = manager.getTaskList();
        assertNotNull(task.getStatus());
        assertEquals(StatusEnum.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void shouldCreateEpic() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        List<Epic> epics = manager.getEpicList();
        assertNotNull(epic.getStatus());
        assertEquals(StatusEnum.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtaskIDs());
        assertEquals(List.of(epic), epics);
    }

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
    }

    @Test
    void shouldReturnNullWhenCreateTaskNull() {
        Integer task = manager.addTask(null);
        assertNull(task);
    }

    @Test
    void shouldReturnNullWhenCreateEpicNull() {
        Integer epic = manager.addEpic(null);
        assertNull(epic);
    }

    @Test
    void shouldReturnNullWhenCreateSubtaskNull() {
        Integer subtask = manager.addSubtask(null);
        assertNull(subtask);
    }

    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = createTask();
        manager.addTask(task);
        task.setStatus(StatusEnum.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(StatusEnum.IN_PROGRESS, manager.getTaskByID(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        epic.setStatus(StatusEnum.IN_PROGRESS);
        assertEquals(StatusEnum.IN_PROGRESS, manager.getEpicByID(epic.getId()).getStatus());
    }

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
    }

    @Test
    public void shouldUpdateTaskStatusToInDone() {
        Task task = createTask();
        manager.addTask(task);
        task.setStatus(StatusEnum.DONE);
        manager.updateTask(task);
        assertEquals(StatusEnum.DONE, manager.getTaskByID(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInDone() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        epic.setStatus(StatusEnum.DONE);
        assertEquals(StatusEnum.DONE, manager.getEpicByID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInDone() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        subtask.setStatus(StatusEnum.DONE);
        manager.updateSubtask(subtask);
        assertEquals(StatusEnum.DONE, manager.getSubTaskByID(subtask.getId()).getStatus());
        assertEquals(StatusEnum.DONE, manager.getEpicByID(epic.getId()).getStatus());
    }

    @Test
    public void shouldNotUpdateTaskIfNull() {
        Task task = createTask();
        manager.addTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskByID(task.getId()));
    }

    @Test
    public void shouldNotUpdateEpicIfNull() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.updateEpic(null);
        assertEquals(epic, manager.getEpicByID(epic.getId()));
    }

    @Test
    public void shouldNotUpdateSubtaskIfNull() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.updateSubtask(null);
        assertEquals(subtask, manager.getSubTaskByID(subtask.getId()));
    }

    @Test
    public void shouldDeleteAllTasks() {
        Task task = createTask();
        manager.addTask(task);
        manager.removeAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getTaskList());
    }

    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.removeAllEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getEpicList());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.removeAllSubtasks();
        assertTrue(epic.getSubtaskIDs().isEmpty());
        assertTrue(manager.getSubtaskList().isEmpty());
    }

//    @Test
//    public void shouldDeleteAllSubtasksByEpic() {
//        Epic epic = createEpic();
//        manager.addEpic(epic);
//        Subtask subtask = createSubtask(epic);
//        manager.addSubtask(subtask);
//        manager.(epic);
//        assertTrue(epic.getSubtaskIds().isEmpty());
//        assertTrue(manager.getAllSubtasks().isEmpty());
//    }

    @Test
    public void shouldDeleteTaskById() {
        Task task = createTask();
        manager.addTask(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getTaskList());
    }

    @Test
    public void shouldDeleteEpicById() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.remove(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getEpicList());
    }

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        Task task = createTask();
        manager.addTask(task);
        manager.remove(999);
        assertEquals(List.of(task), manager.getTaskList());
    }

    @Test
    public void shouldNotDeleteEpicIfBadId() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.remove(999);
        assertEquals(List.of(epic), manager.getEpicList());
    }

    @Test
    public void shouldNotDeleteSubtaskIfBadId() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.remove(999);
        assertEquals(List.of(subtask), manager.getSubtaskList());
        assertEquals(List.of(subtask.getId()), manager.getEpicByID(epic.getId()).getSubtaskIDs());
    }

    @Test
    public void shouldDoNothingIfTaskHashMapIsEmpty(){
        manager.removeAllTasks();
        manager.remove(999);
        assertEquals(0, manager.getTaskList().size());
    }

    @Test
    public void shouldDoNothingIfEpicHashMapIsEmpty(){
        manager.removeAllEpics();
        manager.remove(999);
        assertTrue(manager.getEpicList().isEmpty());
    }

    @Test
    public void shouldDoNothingIfSubtaskHashMapIsEmpty(){
        manager.removeAllEpics();
        manager.remove(999);
        assertEquals(0, manager.getSubtaskList().size());
    }

//    @Test
//    void shouldReturnEmptyListWhenGetSubtaskByEpicIdIsEmpty() {
//        Epic epic = createEpic();
//        manager.addEpic(epic);
//        List<Subtask> subtasks = manager.getSubtaskList(epic.getSubtaskIDs().size());
//        assertTrue(subtasks.isEmpty());
//    }

    @Test
    public void shouldReturnEmptyListTasksIfNoTasks() {
        assertTrue(manager.getTaskList().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListEpicsIfNoEpics() {
        assertTrue(manager.getEpicList().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListSubtasksIfNoSubtasks() {
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskByID(999));
    }

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicByID(999));
    }

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubTaskByID(999));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnEmptyHistoryIfTasksNotExist() {
        manager.getTaskByID(999);
        manager.getSubTaskByID(999);
        manager.getEpicByID(999);
        assertTrue(manager.getHistory().isEmpty());
    }

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
    }

}
