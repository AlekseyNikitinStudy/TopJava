package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class InMemoryAbstractRepository {
    protected final AtomicInteger counter = new AtomicInteger(0);

    protected final Logger log = LoggerFactory.getLogger(getClass());
}
