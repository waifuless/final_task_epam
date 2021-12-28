package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.DaoEntity;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends DaoEntity<T>> {

    Optional<T> save(T t) throws DaoException;

    Optional<T> find(long id) throws DaoException;

    List<T> findAll() throws DaoException;

    void update(T t) throws DaoException;

    void delete(long id) throws DaoException;

    long count() throws DaoException;

}
