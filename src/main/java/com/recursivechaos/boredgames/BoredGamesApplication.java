package com.recursivechaos.boredgames;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import com.recursivechaos.boredgames.cli.RenderCommand;
import com.recursivechaos.boredgames.core.Game;
import com.recursivechaos.boredgames.core.Template;
import com.recursivechaos.boredgames.db.GameDAO;
import com.recursivechaos.boredgames.db.PersonDAO;
import com.recursivechaos.boredgames.health.TemplateHealthCheck;
import com.recursivechaos.boredgames.resources.GameResource;
import com.recursivechaos.boredgames.resources.HelloWorldResource;
import com.recursivechaos.boredgames.resources.PeopleResource;
import com.recursivechaos.boredgames.resources.ViewResource;

public class BoredGamesApplication extends Application<BoredGamesConfiguration> {
    public static void main(String[] args) throws Exception {
        new BoredGamesApplication().run(args);
    }

    private final HibernateBundle<BoredGamesConfiguration> hibernateBundle =
            new HibernateBundle<BoredGamesConfiguration>(Game.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(BoredGamesConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "boredgamesapi";
    }

    @Override
    public void initialize(Bootstrap<BoredGamesConfiguration> bootstrap) {
        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<BoredGamesConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BoredGamesConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(BoredGamesConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        final GameDAO gameDao = new GameDAO(hibernateBundle.getSessionFactory());
        final Template template = configuration.buildTemplate();

        environment.healthChecks().register("template", new TemplateHealthCheck(template));

        environment.jersey().register(new HelloWorldResource(template));
        environment.jersey().register(new ViewResource());
        environment.jersey().register(new PeopleResource(dao));
        environment.jersey().register(new GameResource(gameDao));
        
        environment.getApplicationContext()
        	.addFilter((Class<? extends Filter>) CrossDomainFilter.class, "/*", 
        	EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));
    }
}
