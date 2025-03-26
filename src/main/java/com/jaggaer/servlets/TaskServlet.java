package com.jaggaer.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaggaer.dao.TaskDao;
import com.jaggaer.dto.TaskDTO;
import com.jaggaer.factory.TaskServiceFactory;
import com.jaggaer.mapper.TaskMapper;
import com.jaggaer.model.enums.Status;
import com.jaggaer.service.TaskService;
import com.jaggaer.service.interfaces.ITaskService;
import com.jaggaer.utils.ParseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/TodoList/*")
public class TaskServlet extends HttpServlet {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 5;

    private final ITaskService taskService = TaskServiceFactory.getTaskService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int page = Math.max(ParseUtils.parseIntOrDefault(req.getParameter("page"), DEFAULT_PAGE), DEFAULT_PAGE);
            int size = Math.max(ParseUtils.parseIntOrDefault(req.getParameter("size"), DEFAULT_PAGE_SIZE), DEFAULT_PAGE_SIZE);

            List<TaskDTO> tasks = taskService.getTasks(page, size).stream()
                    .map(TaskMapper.INSTANCE::toDTO)
                    .collect(Collectors.toList());

            req.setAttribute("tasks", tasks);
            req.setAttribute("statusOptions", Arrays.asList(Status.values()));
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", taskService.getTotalPages(size));
            req.setAttribute("pageSize", size);
            req.getRequestDispatcher("/tasks.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "An error occurred while loading tasks.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
