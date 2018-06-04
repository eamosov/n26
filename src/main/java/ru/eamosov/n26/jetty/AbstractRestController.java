package ru.eamosov.n26.jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.server.Request;
import ru.eamosov.n26.impl.StatsServiceImpl;

/**
 * AbstractRestController - base class for REST controllers
 */
public abstract class AbstractRestController<R> {

    protected final StatsServiceImpl statsService;

    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AbstractRestController(StatsServiceImpl statsService) {
        this.statsService = statsService;
    }

    public abstract String getPath();

    public abstract R handle(String target, Request baseRequest) throws Exception;
}
