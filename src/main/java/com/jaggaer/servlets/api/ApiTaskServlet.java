package com.jaggaer.servlets.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaggaer.dto.TaskDTO;
import com.jaggaer.exceptions.TaskNotFoundException;
import com.jaggaer.factory.TaskServiceFactory;
import com.jaggaer.mapper.TaskMapper;
import com.jaggaer.model.Task;
import com.jaggaer.model.enums.Status;
import com.jaggaer.service.interfaces.ITaskService;
import com.jaggaer.utils.ParseUtils;
import com.jaggaer.utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/TodoList")
public class ApiTaskServlet extends HttpServlet {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 5;

    private final ITaskService taskService = TaskServiceFactory.getTaskService();

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equals(req.getMethod())) {
            doPatch(req, resp);
        }
        else if ("PUT".equals(req.getMethod())) {
            doPut(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Math.max(ParseUtils.parseIntOrDefault(req.getParameter("page"), DEFAULT_PAGE), DEFAULT_PAGE);
        int size = Math.max(ParseUtils.parseIntOrDefault(req.getParameter("size"), DEFAULT_PAGE_SIZE), DEFAULT_PAGE_SIZE);

        List<TaskDTO> tasks = taskService.getTasks(page, size).stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = Map.of(
                "tasks", tasks,
                "currentPage", page,
                "totalPages", taskService.getTotalPages(size)
        );

        sendJsonResponse(resp, HttpServletResponse.SC_OK, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TaskDTO taskDTO = objectMapper.readValue(req.getInputStream(), TaskDTO.class);

            Map<String, String> errors = ValidationUtils.validateTask(taskDTO);
            if (!errors.isEmpty()) {
                sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, errors);
                return;
            }

            Task task = createOrUpdateTask(taskDTO);
            taskService.saveTask(task);
            sendJsonResponse(resp, HttpServletResponse.SC_CREATED, TaskMapper.INSTANCE.toDTO(task));
        } catch (Exception e) {
            handleException(resp, e, "Invalid request data");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TaskDTO taskDTO = objectMapper.readValue(req.getInputStream(), TaskDTO.class);

            Map<String, String> errors = ValidationUtils.validateTask(taskDTO);
            if (!errors.isEmpty()) {
                sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, errors);
                return;
            }
            if (taskDTO.getId() == null) {
                sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("error", "Task ID is required"));
                return;
            }

            Task task = createOrUpdateTask(taskDTO);
            taskService.saveTask(task);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, TaskMapper.INSTANCE.toDTO(task));
        } catch (Exception e) {
            handleException(resp, e, "Invalid request data");
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = ParseUtils.parseLong(req.getParameter("id"));
            Task task = taskService.getTaskById(id);
            task.setStatus(Status.COMPLETE);
            taskService.saveTask(task);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, TaskMapper.INSTANCE.toDTO(task));
        } catch (Exception e) {
            handleException(resp, e, "Task not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = ParseUtils.parseLong(req.getParameter("id"));
            taskService.deleteTask(id);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, Map.of("message", "Task deleted"));
        } catch (Exception e) {
            handleException(resp, e, "Task not found");
        }
    }

    private void sendJsonResponse(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(resp.getWriter(), data);
    }

    private void handleException(HttpServletResponse resp, Exception e, String defaultMessage) throws IOException {
        int status = e instanceof TaskNotFoundException ? HttpServletResponse.SC_NOT_FOUND : HttpServletResponse.SC_BAD_REQUEST;
        sendJsonResponse(resp, status, Map.of("error", e.getMessage() != null ? e.getMessage() : defaultMessage));
    }

    private Task createOrUpdateTask(TaskDTO taskDTO) {
        Task task = taskDTO.getId() != null ? taskService.getTaskById(ParseUtils.parseLong(taskDTO.getId())) : new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setStartDate(taskDTO.getStartDate());
        task.setTargetDate(taskDTO.getTargetDate());
        return task;
    }
}
