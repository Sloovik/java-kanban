import java.util.*;

/**
 * Класс-менеджер для управления всеми задачами.
 */
public class TaskManager {

    private Integer nextID;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;


    public TaskManager() {
        this.nextID = 1;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    private void updateEpicStatus(Integer epicID) {

        Integer countOfNEW = 0;
        Integer countOfDONE = 0;
        Integer countOfSubtask = epics.get(epicID).getSubtaskIDs().size();

        for (Integer subtaskID : epics.get(epicID).getSubtaskIDs()) {
            if (subtasks.get(subtaskID).getStatus() == StatusEnum.TODO) {
                countOfNEW++;
            } else if (subtasks.get(subtaskID).getStatus() == StatusEnum.DONE) {
                countOfDONE++;
            }
        }

        if (countOfNEW.equals(countOfSubtask) || (countOfSubtask == 0)) {
            Epic epic = epics.get(epicID);
            epic.setStatus(StatusEnum.TODO);
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



    public List<Task> getTaskList() {
        List<Task> list = new ArrayList<>();
        for (Integer task : tasks.keySet()) {
            list.add(tasks.get(task));
        }
        return list;
    }


    public List<Subtask> getSubtaskList() {
        List<Subtask> list = new ArrayList<>();
        for (Integer task : subtasks.keySet()) {
            list.add(subtasks.get(task));
        }
        return list;
    }

    public List<Epic> getEpicList() {
        List<Epic> list = new ArrayList<>();
        for (Integer task : epics.keySet()) {
            list.add(epics.get(task));
        }
        return list;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAll() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    public void remove(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            removeSubtask(id);
        } else if (epics.containsKey(id)) {
            removeEpic(id);
        } else {
            System.out.println("Возможно, задачи с таким идентификатором не существует.");
        }
    }

    public void removeSubtask(Integer id) {
        subtasks.remove(id);
    }

    public void removeEpic(Integer id) {

        for (Integer key : subtasks.keySet()) {
            if (Objects.equals(subtasks.get(key).getEpicID(), id)) {
                subtasks.remove(key);
                if (subtasks.size() <= 1) {
                    subtasks.clear();
                    break;
                }
            }
        }
        epics.remove(id);
    }

    public Task getTaskByID(Integer id) {
        return tasks.get(id);
    }

    public Subtask getSubTaskByID(Integer id) {
        return subtasks.get(id);
    }

    public Epic getEpicByID(Integer id) {
        return epics.get(id);
    }



    public List<Integer> updateSubtasksInEpic(Epic epic) {
        List<Integer> lisOfSubtaskIDs = new ArrayList<>();
        for (Integer subtaskID : epic.getSubtaskIDs()) {
            Subtask subtask = subtasks.get(subtaskID);
            lisOfSubtaskIDs.add(subtask.getId());
        }
        epic.setSubtaskIDs(lisOfSubtaskIDs);

        return epic.getSubtaskIDs();
    }

    public Integer add(Task task) {
        task.setId(nextID++);
        tasks.put(task.getId(), task);
        return nextID - 1;
    }

    public Integer add(Subtask task) {
        task.setId(nextID++);
        subtasks.put(task.getId(), task);
        List<Integer> newSubtaskList = updateSubtasksInEpic(epics.get(task.getEpicID()));
        Epic epic = epics.get(task.getEpicID());
        newSubtaskList.add(task.getId());
        epic.setSubtaskIDs(newSubtaskList);
        epics.put(task.getEpicID(), epic);
        return task.getId();
    }

    public Integer add(Epic epic) {
        epic.setId(nextID++);
        epic.setStatus(StatusEnum.TODO);
        List<Integer> list = updateSubtasksInEpic(epic);
        epic.setSubtaskIDs(list);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(Subtask subtask) {

        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicID());
        updateSubtasksInEpic(epic);
        updateEpicStatus(subtask.getEpicID());
    }


    public void update(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        for (Integer idSubtask : oldEpic.getSubtaskIDs()) {
            remove(idSubtask);
        }

        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    public List<Subtask> getSubtaskListByEpicID(Integer id) {
        List<Subtask> subtaskListInEpic = new ArrayList<>();
        for (Integer currentSubtask : epics.get(id).getSubtaskIDs()) {
            subtaskListInEpic.add(subtasks.get(currentSubtask));
        }
        return subtaskListInEpic;
    }







}
