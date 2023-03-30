package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.util.List;


/**
 * Интерфейс со списком методов объекта-менеджера.
 */
public interface TaskManager {

    List<Task> getTaskList();

    List<Subtask> getSubtaskList();

    List<Epic> getEpicList();

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    void removeAll();

    void remove(Integer id);

    void removeEpic(Integer id);

    void removeSubtask(Integer id);

    Task getTaskByID(Integer id);

    Subtask getSubTaskByID(Integer id);

    Epic getEpicByID(Integer id);

    Integer addTask(Task task);

    Integer addSubtask(Subtask task);

    Integer addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Task> getHistory();



}
