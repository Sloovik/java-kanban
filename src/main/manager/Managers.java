package main.manager;


import main.http.HttpTaskManager;
import main.http.KVServer;

import java.io.IOException;

/**
 * Утилитарный класс для менеджера задач.
 */
public class Managers {


//    public static TaskManager getDefault(String file) {
//        return new FileBackedTasksManager(file);
//    }

    public static HttpTaskManager getDefault(HistoryManager historyManager) throws IOException, InterruptedException {
        return new HttpTaskManager(historyManager, "http://localhost:" + KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
