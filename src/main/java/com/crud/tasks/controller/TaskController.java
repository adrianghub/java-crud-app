package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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

    @GetMapping(path = "getTask/{id}")
    public TaskDto getTask(@PathVariable("id") Long taskId) throws TaskNotFoundException {
        Task task = service.getTaskById(taskId).orElseThrow(TaskNotFoundException::new);

        return taskMapper.mapToTaskDto(task);
    }

    @PostMapping(path = "createTask", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        service.saveTask(task);
    }

    @PutMapping(path = "updateTask")
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        Task savedTask = service.saveTask(task);
        return taskMapper.mapToTaskDto(savedTask);
    }

    @DeleteMapping(path = "deleteTask/{id}")
    public void deleteTask(@PathVariable("id") Long taskId) throws TaskNotFoundException {
        Task task = service.getTaskById(taskId).orElseThrow(TaskNotFoundException::new);
        service.deleteTask(task);
    }
}
