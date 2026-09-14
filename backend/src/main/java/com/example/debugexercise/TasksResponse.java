package com.example.debugexercise;

import java.util.List;

public class TasksResponse {
    private final List<Task> tasks;
    private final Summary summary;

    public TasksResponse(List<Task> tasks, Summary summary) {
        this.tasks = tasks;
        this.summary = summary;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Summary getSummary() {
        return summary;
    }
}
