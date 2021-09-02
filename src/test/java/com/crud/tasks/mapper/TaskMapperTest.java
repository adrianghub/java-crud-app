package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
public class TaskMapperTest {

    @Mock
    private TaskMapper taskMapper;

    @Test
    void testMapDtoToTask() {
        //Given
        TaskDto taskDto = new TaskDto(1L, "task dto title", "task dto content");
        Task task = new Task(1L, "task title", "task content");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);

        //When
        Task mappedTask = taskMapper.mapToTask(taskDto);

        //Then
        assertEquals("task title", mappedTask.getTitle());
    }


    @Test
    void testMapTaskToDto() {
        //Given
        TaskDto taskDto = new TaskDto(1L, "task dto title", "task dto content");
        Task task = new Task(1L, "task title", "task content");

        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        //When
        TaskDto mappedTaskDto = taskMapper.mapToTaskDto(task);

        //Then
        assertEquals("task dto title", mappedTaskDto.getTitle());
    }


    @Test
    void testMapTaskListToDto() {
        //Given
        List<Task> taskList = List.of(new Task(1L, "second task", "this is first task"));
        List<TaskDto> taskListDto = List.of(new TaskDto(1L, "second task dto", "this is first task dto"));

        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskListDto);

        //When
        List<TaskDto> mappedTaskDtoList = taskMapper.mapToTaskDtoList(taskList);
        List<TaskDto> filteredDtoTasks = mappedTaskDtoList.stream().filter(task -> task.getTitle() == "second task dto").collect(Collectors.toList());

        //Then
        assertEquals(filteredDtoTasks.size(), 1);
    }
}
