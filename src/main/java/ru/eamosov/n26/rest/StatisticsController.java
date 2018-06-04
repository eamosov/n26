package ru.eamosov.n26.rest;

import org.eclipse.jetty.server.Request;
import ru.eamosov.n26.api.Statistics;
import ru.eamosov.n26.impl.StatsServiceImpl;
import ru.eamosov.n26.jetty.AbstractRestController;

/**
 * Controller for "/statistics" endpoint
 */
public class StatisticsController extends AbstractRestController<Statistics> {

    public StatisticsController(StatsServiceImpl statsService) {
        super(statsService);
    }

    @Override
    public String getPath() {
        return "/statistics";
    }

    @Override
    public Statistics handle(Request baseRequest) {
        return statsService.statistics();
    }
}
