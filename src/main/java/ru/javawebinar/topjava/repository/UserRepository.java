package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

public interface UserRepository extends Repository<User> {
    User getByEmail(String email);
}