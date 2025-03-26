package com.jaggaer.service;

import com.jaggaer.dao.TaskDao;
import com.jaggaer.exceptions.TaskNotFoundException;
import com.jaggaer.model.Task;
import com.jaggaer.service.interfaces.ITaskService;

import java.util.List;

public class TaskService implements ITaskService {
    private final TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void saveTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        taskDao.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskDao.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    @Override
    public List<Task> getTasks(int page, int size) {
        return taskDao.findPaginated(page, size);
    }

    @Override
    public long getTotalTasks() {
        return taskDao.countTasks();
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskDao.delete(task);
    }

    @Override
    public int getTotalPages(int size) {
        long totalTasks = getTotalTasks();
        return (int) Math.ceil((double) totalTasks / size);
    }
}
