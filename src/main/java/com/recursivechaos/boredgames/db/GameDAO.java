package com.recursivechaos.boredgames.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import org.hibernate.SessionFactory;

import com.google.common.base.Optional;
import com.recursivechaos.boredgames.core.Game;

public class GameDAO extends AbstractDAO<Game> {
    public GameDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Game> findByGid(Long gid) {
        return Optional.fromNullable(get(gid));
    }

    public Game create(Game game) {
        return persist(game);
    }

    public List<Game> findAll() {
        return list(namedQuery("com.recursivechaos.boredgames.core.Game.findAll"));
    }
}