package main.manager;

import main.exceptions.ManagerSaveException;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static main.manager.Formatter.fromString;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;
    private String fileName;


    public FileBackedTasksManager(String filename) {
        fileName = filename;
        file = new File(fileName);
        if (!file.exists()) {
            try {
                Path path = Files.createFile(Paths.get(fileName));
            } catch (IOException e) {
                System.out.println(("Ошибка создания файла."));
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(String fileName) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName);
        String data = "";
        try {
            data = Files.readString(Paths.get(fileName), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println(e);
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        String[] lines = data.split("\n");

        parseEachLine(fileBackedTasksManager, lines);

        return fileBackedTasksManager;
    }

    private static void parseEachLine(FileBackedTasksManager backedManager, String[] lines) {
        int id = 0;

        for (int i = 1; i < lines.length - 1; i++) {
            String line = lines[i];

            if (!line.isEmpty() && !line.equals("\r")) {
                var splittedLine = line.split(",");
                var taskId = Integer.valueOf(splittedLine[0]);
                var taskType = TaskTypes.valueOf(splittedLine[1]);

                switch (taskType) {
                    case EPIC -> {
                        Epic epic = (Epic) fromString(line);
                        id = epic.getId();
                        if (id > backedManager.id) {
                            backedManager.id = id;
                        }

                        backedManager.epics.put(id, epic);
                    }
                    case SUBTASK -> {
                        var subtask = (Subtask) fromString(line);
                        var epics = backedManager.getEpicList();

                        id = subtask.getId();
                        if (id > backedManager.id) {
                            backedManager.id = id;
                        }

                        Epic epic = epics
                                .stream()
                                .filter(e -> Objects.equals(e.getId(), subtask.getEpicID()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Epic not found"));

                        if (!epic.getSubtaskIDs().contains(subtask.getId())) {
                            var index = epics.indexOf(epic);

                            epic.appendSubtaskId(subtask.getId());
                            epics.set(index, epic);
                        }

                        backedManager.subtasks.put(id, subtask);
                    }

                    case TASK -> {
                        Task task = fromString(line);
                        id = task.getId();
                        if (id > backedManager.id) {
                            backedManager.id = id;
                        }
                        backedManager.tasks.put(id, task);
                    }
                }

                backedManager.historyManager.add(getAllTypesOfTasks(taskId, backedManager));
            }
        }
    }

    public static Task getAllTypesOfTasks(int id, InMemoryTaskManager inMemoryTaskManager) {
        Task task = inMemoryTaskManager
                .getTaskList()
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (task != null) {
            return task;
        }

        Task epic = inMemoryTaskManager
                .getEpicList()
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (epic != null) {
            return epic;
        }

        Task subtask = inMemoryTaskManager
                .getSubtaskList()
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        return subtask;
    }

    public void save() {
        HashMap<Integer, String> allTasks = new HashMap<>();
        List<Task> tasks = super.getTaskList();
        List<Epic> epics = super.getEpicList();
        List<Subtask> subtasks = super.getSubtaskList();

        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            int counter = 1;


            for (Task task : tasks) {
                allTasks.put(counter, task.toStringFromFile());
                counter++;
            }

            for (Epic item : epics) {
                allTasks.put(counter, item.toStringFromFile());
                counter++;
            }

            for (Subtask subtask : subtasks) {
                allTasks.put(counter, subtask.toStringFromFile());

                Epic epic = epics
                        .stream()
                        .filter(e -> Objects.equals(e.getId(), subtask.getEpicID()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Epic not found"));

                if (!epic.getSubtaskIDs().contains(subtask.getId())) {
                    var index = epics.indexOf(epic);

                    epic.appendSubtaskId(subtask.getId());
                    epics.set(index, epic);
                }

                counter++;
            }

            for (String value : allTasks.values()) {
                writer.write(String.format("%s\n", value));
            }
            writer.write("\n");
            writer.write(Formatter.toString(this.historyManager));

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Ошибка записи файла.");
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Integer addTask(Task task) {
        if (task == null) return null;
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        if (subtask == null) return null;
        super.addSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public Integer addEpic(Epic epic) {
        if (epic == null) return null;
        super.addEpic(epic);
        save();
        return epic.getId();
    }


    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void removeEpic(Integer id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(Integer id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        save();
    }

    @Override
    public Task getTaskByID(Integer id) {
        return super.getTaskByID(id);
    }

    @Override
    public Subtask getSubTaskByID(Integer id) {
        return super.getSubTaskByID(id);
    }

    @Override
    public Epic getEpicByID(Integer id) {
        return super.getEpicByID(id);
    }


}
