package com.example.debugexercise;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public TasksResponse getTasks() {
        List<Task> tasks = taskService.getAllTasks();
        Summary summary = taskService.getSummary(tasks);
        return new TasksResponse(tasks, summary);
    }

    @PatchMapping("/{id}/toggle")
    public TasksResponse toggle(@PathVariable long id) {
        taskService.toggleCompleted(id);
        List<Task> tasks = taskService.getAllTasks();
        Summary summary = taskService.getSummary(tasks);
        return new TasksResponse(tasks, summary);
    }
}
