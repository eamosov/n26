package ru.eamosov.n26.rest;

import org.eclipse.jetty.server.Request;
import ru.eamosov.n26.api.EmptyResult;
import ru.eamosov.n26.api.Transaction;
import ru.eamosov.n26.impl.StatsServiceImpl;
import ru.eamosov.n26.jetty.AbstractRestController;

import java.io.InputStreamReader;

/**
 * Controller for "/transaction" endpoint
 */
public class TransactionController extends AbstractRestController<EmptyResult> {

    public TransactionController(StatsServiceImpl statsService) {
        super(statsService);
    }

    @Override
    public String getPath() {
        return "/transaction";
    }

    @Override
    public EmptyResult handle(Request baseRequest) {

        final Transaction transaction = gson.fromJson(new InputStreamReader(baseRequest.getHttpInput()), Transaction.class);

        if (transaction == null) {
            throw new IllegalArgumentException("Require Transaction");
        }

        return statsService.transaction(transaction);
    }
}
