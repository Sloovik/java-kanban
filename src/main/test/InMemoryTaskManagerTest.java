package main.test;

import main.manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldCorrectlyGetPrioritizedTasks() {
        manager.addTask(createTask());
        manager.addTask(createSecondTask());
        manager.addTask(createThirdTask());
        assertNotNull(manager.getPrioritizedTasks());
        assertEquals(manager.getPrioritizedTasks().get(0).getTaskName(),"Title");
        assertEquals(manager.getPrioritizedTasks().get(1).getTaskName(),"Title3");
        assertEquals(manager.getPrioritizedTasks().get(2).getTaskName(),"Title2");
    }
}