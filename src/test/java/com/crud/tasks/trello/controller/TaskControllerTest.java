package com.crud.tasks.trello.controller;

import com.crud.tasks.controller.TaskController;
import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void shouldFetchEmptyTaskList() throws Exception {

        // Given
        List<Task> taskList = List.of(new Task(1L, "first task", "this is first task"));
        List<TaskDto> taskListDto = List.of(new TaskDto(1L, "first task dto", "this is first task dto"));

        when(dbService.getAllTasks()).thenReturn(List.of());
        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskListDto);

        // When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/task/getTasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

    }

    @Test
    void shouldFetchTaskList() throws Exception {

        // Given
        List<Task> taskList = List.of(new Task(1L, "second task", "this is first task"));
        List<TaskDto> taskListDto = List.of(new TaskDto(1L, "second task dto", "this is first task dto"));

        when(dbService.getAllTasks()).thenReturn(taskList);
        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskListDto);

        // When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/task/getTasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("second task dto")));

    }

    @Test
    void shouldFetchTaskById() throws Exception {

        // Given
        Task task = new Task(123L, "second task", "this is first task");
        TaskDto taskDto = new TaskDto(123L, "second task", "this is first task");

        when(dbService.getTaskById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        // When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/task/getTask/{id}", 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title", Matchers.is("second task")));

    }


    @Test
    void shouldCreateTask() throws Exception {

        // Given
        Task task = new Task(5L, "third task", "this is third task");
        TaskDto taskDto = new TaskDto(5L, "third task dto", "this is third task dto");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        when(dbService.saveTask(task)).thenReturn(task);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        // When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/task/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTask() throws Exception {

        // Given
        Task task = new Task(5L, "third task", "this is third task");
        TaskDto taskDto = new TaskDto(5L, "third task dto", "this is third task dto");

        when(dbService.getTaskById(task.getId())).thenReturn(Optional.of(task));
        when(dbService.saveTask(task)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(task);

        // When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/task/updateTask/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(5)))
                .andExpect(jsonPath("title", Matchers.is("third task dto")))
                .andExpect(jsonPath("content", Matchers.is("this is third task dto")));

    }

    @Test
    void shouldDeleteTask() throws Exception {

        // Given
        Task task = new Task(5L, "third task", "this is third task");

        when(dbService.getTaskById(task.getId())).thenReturn(Optional.of(task));

        // When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/task/deleteTask/{id}", 5))
                .andExpect(status().isOk());

    }
}