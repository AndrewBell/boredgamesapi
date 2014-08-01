package com.recursivechaos.boredgames.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.recursivechaos.boredgames.core.Game;
import com.recursivechaos.boredgames.db.GameDAO;
import com.sun.jersey.api.NotFoundException;

@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {

    private final GameDAO gameDAO;

    public GameResource(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    @GET
    @UnitOfWork
    @Path("/{gameId}")
    public Game getGame(@PathParam("gameId") LongParam gameId) {
        return findSafely(gameId.get());
    }

	private Game findSafely(long gameId) {
		final Optional<Game> game = gameDAO.findByGid(gameId);
        if (!game.isPresent()) {
            throw new NotFoundException("No such game.");
        }
		return game.get();
	}
	
	@POST
    @UnitOfWork
    public Game createGame(Game game) {
        return gameDAO.create(game);
    }

    @GET
    @UnitOfWork
    public List<Game> listGames() {
        return gameDAO.findAll();
    }
}
