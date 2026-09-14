package com.example.debugexercise;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskServiceTest {

    private final TaskService taskService = new TaskService();

    @Test
    void summaryReportsTotalTaskCount() {
        List<Task> tasks = List.of(
                new Task(1, "a", "LOW", false),
                new Task(2, "b", "LOW", false),
                new Task(3, "c", "LOW", false)
        );

        Summary summary = taskService.getSummary(tasks);

        assertEquals(3, summary.getTotal());
    }

    @Test
    void summaryCountsEveryCompletedTaskNotJustOne() {
        List<Task> tasks = List.of(
                new Task(1, "a", "LOW", true),
                new Task(2, "b", "LOW", true),
                new Task(3, "c", "LOW", true),
                new Task(4, "d", "LOW", false)
        );

        Summary summary = taskService.getSummary(tasks);

        // If this fails, the failure message itself is a big hint: note what
        // value actually came back vs. what was expected, then set a
        // breakpoint inside getSummary()'s loop (see README "Attaching the
        // backend debugger") and step through — watch completedCount on each
        // iteration rather than only reading the code.
        assertEquals(3, summary.getCompletedCount(),
                "expected all 3 completed tasks to be counted, not just the first one");
    }

    @Test
    void summaryCountsZeroWhenNothingIsCompleted() {
        List<Task> tasks = List.of(
                new Task(1, "a", "LOW", false),
                new Task(2, "b", "LOW", false)
        );

        Summary summary = taskService.getSummary(tasks);

        assertEquals(0, summary.getCompletedCount());
    }
}
