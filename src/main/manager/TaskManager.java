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

    Integer add(Task task);

    Integer add(Subtask task);

    Integer add(Epic epic);

    void update(Task task);

    void update(Subtask subtask);

    void update(Epic epic);

    List<Task> getHistory();



}
