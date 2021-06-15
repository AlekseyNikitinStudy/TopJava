package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.repository.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class InMemoryAbstractRepository<T extends AbstractBaseEntity> implements Repository<T> {
    protected final AtomicInteger counter = new AtomicInteger(0);

    protected final Map<Integer, T> repository = new ConcurrentHashMap<>();

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public T get(int id) {
        return repository.get(id);
    }

    @Override
    public T save(T entity) {
        if (entity.isNew()) {
            entity.setId(counter.incrementAndGet());
            repository.put(entity.getId(), entity);
            return entity;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(entity.getId(), (id, oldEntity) -> entity);
    }

    @Override
    public List<T> getAll() {
        log.info("getAll() deprecated for {}", this.getClass());
        return Collections.emptyList();
    }
}
