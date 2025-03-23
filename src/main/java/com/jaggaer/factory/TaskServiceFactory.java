package com.jaggaer.factory;

import com.jaggaer.dao.TaskDao;
import com.jaggaer.service.TaskService;
import com.jaggaer.service.interfaces.ITaskService;

public class TaskServiceFactory {
    private static final ITaskService instance = new TaskService(new TaskDao());

    public static ITaskService getTaskService() {
        return instance;
    }
}
