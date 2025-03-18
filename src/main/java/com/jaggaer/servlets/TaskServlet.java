package com.jaggaer.servlets;

import com.jaggaer.dao.TaskDao;
import com.jaggaer.model.Task;
import com.jaggaer.model.enums.Status;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/TodoList")
public class TaskServlet extends HttpServlet {

    private TaskDao taskDao = new TaskDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Task> tasks = taskDao.getAll();
        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("/tasks.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        Status status = Status.valueOf(req.getParameter("status"));
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        taskDao.save(task);
        resp.sendRedirect("TodoList");
    }
}
