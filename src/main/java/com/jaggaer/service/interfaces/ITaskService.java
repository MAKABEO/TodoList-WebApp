package com.jaggaer.service.interfaces;

import com.jaggaer.model.Task;

import java.util.List;

public interface ITaskService {
    List<Task> getTasks(int page, int size);
    long getTotalTasks();
    Task getTaskById(Long id);
    void saveTask(Task task);
    void deleteTask(Long id);
    int getTotalPages(int size);
}
