package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final DbService service;
    private final TaskMapper taskMapper;

    @GetMapping(path = "getTasks")
    public List<TaskDto> getTasks() {
        List<Task> tasks = service.getAllTasks();

        return taskMapper.mapToTaskDtoList(tasks);
    }

    @GetMapping(path = "getTask")
    public TaskDto getTask(@RequestParam(name = "taskId") Long taskId) {
        Task task = service.getTaskById(taskId).orElse(null);

        return taskMapper.mapToTaskDto(task);
    }

    @PostMapping(path = "createTask")
    public void createTask(TaskDto task) {
    }

    @PutMapping(path = "updateTask")
    public TaskDto updateTask(TaskDto task) {
        return new TaskDto(1L, "edited task title", "edited task content");
    }

    @DeleteMapping(path = "deleteTask")
    public void deleteTask(Long taskId) {
    }
}
