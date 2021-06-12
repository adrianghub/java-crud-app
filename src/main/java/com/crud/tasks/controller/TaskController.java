package com.crud.tasks.controller;

import com.crud.tasks.domain.TaskDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/task")
public class TaskController {

    @GetMapping("getTasks")
    public List<TaskDto> getTasks() {
        return new ArrayList<>();
    }

    @GetMapping("getTask")
    public TaskDto getTask(Long taskId) {
        return new TaskDto(1L, "task title", "task content");
    }

    @PostMapping( "createTask")
    public void createTask(TaskDto task) {
    }

    @PutMapping("updateTask")
    public TaskDto updateTask(TaskDto task) {
        return new TaskDto(1L, "edited task title", "edited task content");
    }

    @DeleteMapping(value = "deleteTask")
    public void deleteTask(Long taskId) {
    }
}
