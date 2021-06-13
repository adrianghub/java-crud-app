package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
