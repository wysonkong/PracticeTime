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

        assertEquals(3, summary.getCompletedCount());
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
