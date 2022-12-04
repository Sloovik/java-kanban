package main.manager;

import main.task.Task;

import java.util.List;

/**
 * Интерфейс для управления историей просмотров.
 */
public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}
