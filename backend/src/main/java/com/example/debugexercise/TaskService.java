package com.example.debugexercise;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    public TaskService() {
        tasks.put(1L, new Task(1L, "Review pull request", "HIGH", false));
        tasks.put(2L, new Task(2L, "Write unit tests", "MEDIUM", false));
        tasks.put(3L, new Task(3L, "Update dependencies", "LOW", false));
        tasks.put(4L, new Task(4L, "Fix flaky CI job", "HIGH", false));
    }

    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    public Task toggleCompleted(long id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        task.setCompleted(!task.isCompleted());
        return task;
    }

    /**
     * Builds the summary shown in the UI header.
     *
     * NOTE FOR THE CLASS: this method has an intentional bug. Find it with a
     * debugger breakpoint + step-through, not by reading — that's the point
     * of the exercise. See EXERCISE.md.
     */
    public Summary getSummary(List<Task> currentTasks) {
        int total = currentTasks.size();
        int completedCount = 0;

        for (Task task : currentTasks) {
            if (task.isCompleted()) {
                completedCount = 1; // BUG: should accumulate, not overwrite
            }
        }

        return new Summary(total, completedCount);
    }
}
