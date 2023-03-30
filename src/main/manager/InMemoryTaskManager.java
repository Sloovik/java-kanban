package main.manager;

import main.task.Epic;
import main.task.StatusEnum;
import main.task.Subtask;
import main.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �����-�������� ��� ���������� ����� �������� (����� - taskManager).
 */
public class InMemoryTaskManager implements TaskManager {

    protected Integer id;
    private Integer nextID;
    final Map<Integer, Task> tasks;
    final Map<Integer, Subtask> subtasks;
    final Map<Integer, Epic> epics;

    private List<Integer> allTypesOfTasksIds;
    final InMemoryHistoryManager historyManager;


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
    }


    @Override
    public void remove(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else if (subtasks.containsKey(id)) {
            removeSubtask(id);
            historyManager.remove(id);
        } else if (epics.containsKey(id)) {
            removeEpic(id);
            historyManager.remove(id);
        } else {
            System.out.println("��������, ������ � ����� ��������������� �� ����������.");
        }
    }

    @Override
    public void removeSubtask(Integer id) {
        subtasks.remove(id);
        historyManager.remove(id);
        for (Epic epic : getEpicList()) {
            epic.getSubtaskIDs().remove(id);
        }
    }

    @Override
    public void removeEpic(Integer id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            historyManager.remove(id);
            for (Integer subtaskId : epic.getSubtaskIDs()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
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
        return task.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        if (subtask == null) return null;
        subtask.setId(nextID++);
        subtasks.put(subtask.getId(), subtask);
        List<Integer> newSubtaskList = updateSubtasksInEpic(epics.get(subtask.getEpicID()));
        Epic epic = epics.get(subtask.getEpicID());
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

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicID());
    }


    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
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


}
