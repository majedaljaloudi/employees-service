package com.example.employees.repository;

import com.example.employees.repository.model.EmployeeEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class EmployeeFileRepository implements IEmployeeRepository {

    private static final Logger log = LoggerFactory.getLogger(EmployeeFileRepository.class);

    private final ObjectMapper mapper;
    private final File dbFile;
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public EmployeeFileRepository(
            ObjectMapper mapper,
            @Value("${employee.db.path:src/main/resources/employees.json}") String filePath) {
        this.mapper = mapper;
        this.dbFile = new File(filePath);
    }

    @PostConstruct
    private void initializeIdCounter() {
        List<EmployeeEntity> all = findAllInternal();
        int maxId = all.stream()
                .mapToInt(EmployeeEntity::getId)
                .max()
                .orElse(0);
        idCounter.set(maxId + 1);
    }

    @Override
    public EmployeeEntity save(EmployeeEntity employeeEntity) {
        lock.writeLock().lock();
        try {
            List<EmployeeEntity> employeeEntities = findAllInternal();
            employeeEntity.setId(idCounter.getAndIncrement());
            employeeEntities.add(employeeEntity);
            writeToFile(employeeEntities);
            return employeeEntity;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<EmployeeEntity> findAll() {
        lock.readLock().lock();
        try {
            return findAllInternal();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<EmployeeEntity> findById(int id) {
        return findAll().stream()
                .filter(emp -> emp.getId() == id)
                .findFirst();
    }

    private List<EmployeeEntity> findAllInternal() {
        if (!dbFile.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(dbFile, new TypeReference<List<EmployeeEntity>>() {
            });
        } catch (IOException e) {
            log.error("Error reading employee data from file", e);
            throw new IllegalStateException("Could not read employee data", e);
        }
    }

    private void writeToFile(List<EmployeeEntity> employeeEntities) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(dbFile, employeeEntities);
        } catch (IOException e) {
            log.error("Error writing employee data to file", e);
            throw new IllegalStateException("Could not write employee data", e);
        }
    }
}
