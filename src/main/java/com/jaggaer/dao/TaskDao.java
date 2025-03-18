package com.jaggaer.dao;

import com.jaggaer.model.Task;
import com.jaggaer.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TaskDao {

    private final EntityManager entityManager;

    public TaskDao() {
        this.entityManager = JPAUtil.getEntityManager();
    }

    public void save(Task todo) {
        entityManager.getTransaction().begin();
        entityManager.persist(todo);
        entityManager.getTransaction().commit();
    }

    public List<Task> getAll() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public void delete(Long id) {
        entityManager.getTransaction().begin();
        Task task = entityManager.find(Task.class, id);
        if (task != null) entityManager.remove(task);
        entityManager.getTransaction().commit();
    }
}
