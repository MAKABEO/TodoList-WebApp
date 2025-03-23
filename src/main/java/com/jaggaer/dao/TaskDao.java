package com.jaggaer.dao;

import com.jaggaer.exceptions.DatabaseException;
import com.jaggaer.model.Task;
import com.jaggaer.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class TaskDao {

    public void save(Task task) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Task managedTask = entityManager.merge(task);
                transaction.commit();
                task.setId(managedTask.getId());
            } catch (Exception e) {
                transaction.rollback();
                throw new DatabaseException("Error saving task", e);
            }
        }
    }

    public Optional<Task> findById(Long id) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(entityManager.find(Task.class, id));
        } finally {
            entityManager.close();
        }
    }

    public List<Task> findAll() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT t FROM Task t ORDER BY t.id ASC", Task.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Task> findPaginated(int page, int size) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT t FROM Task t ORDER BY t.id ASC", Task.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public long countTasks() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT COUNT(t) FROM Task t", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public void delete(Task task) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.remove(entityManager.contains(task) ? task : entityManager.merge(task));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new DatabaseException("Error deleting task", e);
            }
        }
    }
}
