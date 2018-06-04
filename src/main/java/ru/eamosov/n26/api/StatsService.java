package ru.eamosov.n26.api;

/**
 * StatsService
 */
public interface StatsService {

    /**
     * POST /transactions
     *
     * @param transaction
     * @return
     */
    EmptyResult transaction(Transaction transaction);

    /**
     * GET /statistics
     *
     * @return
     */
    Statistics statistics();
}
