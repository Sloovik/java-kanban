package main.manager;

import main.exceptions.ManagerIntersectionException;
import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;

import java.time.Instant;
import java.util.*;

/**
 * Класс-менеджер для управления всеми задачами (ранее - taskManager).
 */
public class InMemoryTaskManager implements TaskManager {

    protected Integer id;
    private Integer nextID;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    final InMemoryHistoryManager historyManager;

    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);

    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);


    public InMemoryTaskManager() {
        this.id = 0;
        this.nextID = 1;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    public void updateEpicStatus(Integer epicID) {

        Integer countOfNEW = 0;
        Integer countOfDONE = 0;
        Integer countOfSubtask = epics.get(epicID).getSubtaskIDs().size();

        for (Integer subtaskID : epics.get(epicID).getSubtaskIDs()) {
            if (subtasks.get(subtaskID).getStatus() == StatusEnum.NEW) {
                countOfNEW++;
            } else if (subtasks.get(subtaskID).getStatus() == StatusEnum.DONE) {
                countOfDONE++;
            }
        }


        if (countOfNEW.equals(countOfSubtask) || (countOfSubtask == 0)) {
            Epic epic = epics.get(epicID);
            epic.setStatus(StatusEnum.NEW);
            epics.put(epicID, epic);
        } else if (countOfDONE.equals(countOfSubtask)) {
            Epic epic = epics.get(epicID);
            epic.setStatus(StatusEnum.DONE);
            epics.put(epicID, epic);
        } else {
            Epic epic = epics.get(epicID);
            epic.setStatus(StatusEnum.IN_PROGRESS);
            epics.put(epicID, epic);
        }
    }


    @Override
    public List<Task> getTaskList() {
        List<Task> list = new ArrayList<>();
        for (Integer task : tasks.keySet()) {
            list.add(tasks.get(task));
        }
        return list;
    }


    @Override
    public List<Subtask> getSubtaskList() {
        List<Subtask> list = new ArrayList<>();
        for (Integer task : subtasks.keySet()) {
            list.add(subtasks.get(task));
        }
        return list;
    }

    @Override
    public List<Epic> getEpicList() {
        List<Epic> list = new ArrayList<>();
        for (Integer task : epics.keySet()) {
            list.add(epics.get(task));
        }
        return list;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : getEpicList()) {
            epic.getSubtaskIDs().clear();
        }
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        subtasks.clear();
    }

    @Override
    public void removeAllEpics() {

        for (Integer epic : epics.keySet()) {
            historyManager.remove(epic);
        }
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAll() {
        for (Integer epic : epics.keySet()) {
            historyManager.remove(epic);
        }
        for (Integer subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
        }
        subtasks.clear();
        epics.clear();
        tasks.clear();
        prioritizedTasks.clear();
    }


    @Override
    public void remove(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), id));
        } else if (subtasks.containsKey(id)) {
            removeSubtask(id);
            historyManager.remove(id);
            prioritizedTasks.remove(getSubTaskByID(id));
        } else if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            if (epic != null) {
                epic.getSubtaskIDs().forEach(subtaskId -> {
                    prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subtaskId));
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                });
                removeEpic(id);
                historyManager.remove(id);
            } else {
                System.out.println("Возможно, задачи с таким идентификатором не существует.");
            }
        }
    }

    @Override
    public void removeSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicID());
            epic.getSubtaskIDs().remove(subtask.getId());
            updateEpicStatus(epic.getId());
            updateTimeEpic(epic);
            prioritizedTasks.remove(subtask);
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubtaskIDs().forEach(subtaskId -> {
                prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public Task getTaskByID(Integer id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubTaskByID(Integer id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicByID(Integer id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }


    private List<Integer> updateSubtasksInEpic(Epic epic) {
        List<Integer> lisOfSubtaskIDs = new ArrayList<>();
        for (Integer subtaskID : epic.getSubtaskIDs()) {
            Subtask subtask = subtasks.get(subtaskID);
            lisOfSubtaskIDs.add(subtask.getId());
        }
        epic.setSubtaskIDs(lisOfSubtaskIDs);

        return epic.getSubtaskIDs();
    }

    @Override
    public Integer addTask(Task task) {
        if (task == null) return null;
        task.setId(nextID++);
        tasks.put(task.getId(), task);
        addNewPrioritizedTask(task);
        return task.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        if (subtask == null) return null;
        subtask.setId(nextID++);
        subtasks.put(subtask.getId(), subtask);
        List<Integer> newSubtaskList = updateSubtasksInEpic(epics.get(subtask.getEpicID()));
        Epic epic = epics.get(subtask.getEpicID());
        addNewPrioritizedTask(subtask);
        newSubtaskList.add(subtask.getId());
        epic.setSubtaskIDs(newSubtaskList);
        epics.put(subtask.getEpicID(), epic);
        return subtask.getId();
    }

    @Override
    public Integer addEpic(Epic epic) {
        if (epic == null) return null;
        epic.setId(nextID++);
        epic.setStatus(StatusEnum.NEW);
        List<Integer> list = updateSubtasksInEpic(epic);
        epic.setSubtaskIDs(list);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public void updateTimeEpic(Epic epic) {
        List<Subtask> subtasks = getSubtaskListByEpicID(epic.getId());
        Instant startTime = subtasks.get(0).getStartTime();
        Instant endTime = subtasks.get(0).getEndTime();

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        long duration = (endTime.toEpochMilli() - startTime.toEpochMilli());
        epic.setDuration(duration);
    }


    @Override
    public void updateTask(Task task) {
        if (task != null && getTaskList().contains(task)) {
            addNewPrioritizedTask(task);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Task not found((");
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            addNewPrioritizedTask(subtask);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicID());
            updateEpicStatus(subtask.getEpicID());
            updateTimeEpic(epic);
        } else {
            System.out.println("Subtask not found");
        }

    }


    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateTimeEpic(epic);
            updateEpicStatus(epic.getId());
        } else {
            System.out.println("Epic not found!1!");
        }

    }

    public List<Subtask> getSubtaskListByEpicID(Integer id) {
        List<Subtask> subtaskListInEpic = new ArrayList<>();
        for (Integer currentSubtask : epics.get(id).getSubtaskIDs()) {
            subtaskListInEpic.add(subtasks.get(currentSubtask));
        }
        return subtaskListInEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        validateTaskPriority();
    }

    public boolean checkTime(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        int sizeTimeNull = 0;
        if (tasks.size() > 0) {
            for (Task taskSave : tasks) {
                if (taskSave.getStartTime() != null && taskSave.getEndTime() != null) {
                    if (task.getStartTime().isBefore(taskSave.getStartTime())
                            && task.getEndTime().isBefore(taskSave.getStartTime())) {
                        return false;
                    } else if (task.getStartTime().isAfter(taskSave.getEndTime())
                            && task.getEndTime().isAfter(taskSave.getEndTime())) {
                        return false;
                    }
                } else {
                    sizeTimeNull++;
                }

            }
            return sizeTimeNull == tasks.size();
        } else {
            return false;
        }
    }

    private void validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean taskHasIntersections = checkTime(task);

            if (taskHasIntersections) {
                throw new ManagerIntersectionException(
                        "Задачи #" + task.getId() + " и #" + tasks.get(i - 1) + "пересекаются");
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

}
