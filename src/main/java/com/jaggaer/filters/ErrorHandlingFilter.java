package com.jaggaer.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaggaer.exceptions.DatabaseException;
import com.jaggaer.exceptions.TaskNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class ErrorHandlingFilter implements Filter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (TaskNotFoundException e) {
            sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        } catch (Exception e) {
            sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), Map.of("error", message));
    }
}
