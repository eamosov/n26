package ru.eamosov.n26;

import org.junit.Test;
import ru.eamosov.n26.api.EmptyResult;
import ru.eamosov.n26.api.Statistics;
import ru.eamosov.n26.api.StatsService;
import ru.eamosov.n26.api.Transaction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Various tests for {@link StatsService}
 */
public abstract class StatsServiceTest {


    protected abstract StatsService service();

    public StatsServiceTest() {

    }

    @Test
    public void testApi() {

        assertThat(service().statistics(), equalTo(new Statistics(0, null, null, null, 0)));

        assertThat(service().transaction(new Transaction(1, System.currentTimeMillis())), equalTo(EmptyResult.SUCCESS));
        assertThat(service().statistics(), equalTo(new Statistics(1, 1.0, 1.0, 1.0, 1)));

        assertThat(service().transaction(new Transaction(3, System.currentTimeMillis())), equalTo(EmptyResult.SUCCESS));
        assertThat(service().statistics(), equalTo(new Statistics(4, 2.0, 3.0, 1.0, 2)));

        assertThat(service().transaction(new Transaction(1, System.currentTimeMillis() - 60001)), equalTo(EmptyResult.OLD_TRANSACTION));
        assertThat(service().statistics(), equalTo(new Statistics(4, 2.0, 3.0, 1.0, 2)));
    }

}
