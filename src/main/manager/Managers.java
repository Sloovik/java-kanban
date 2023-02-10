package main.manager;


/**
 * ����������� ����� ��� ��������� �����.
 */
public class Managers {


    public static TaskManager getDefault(String file) {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
