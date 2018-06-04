package ru.eamosov.n26.impl;

import org.hamcrest.Matchers;
import org.junit.Test;
import ru.eamosov.n26.StatsServiceTest;
import ru.eamosov.n26.api.EmptyResult;
import ru.eamosov.n26.api.Statistics;
import ru.eamosov.n26.api.StatsService;
import ru.eamosov.n26.api.Transaction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test {@link StatsService} as local service
 */
public class StatsServiceImplTest extends StatsServiceTest {

    final StatsServiceImpl statsService = new StatsServiceImpl();

    public StatsServiceImplTest() {
    }

    protected StatsService service() {
        return statsService;
    }

    @Test
    public void testMakeBackets() {

        StatsServiceImpl s = new StatsServiceImpl(3);
        s.makeBuckets(10000);
        assertThat(s.getBuckets(), Matchers.contains(new Bucket(10000), new Bucket(9000), new Bucket(8000)));
        assertThat(s.getBucket(10000), equalTo(new Bucket(10000)));
        assertThat(s.getBucket(10001), equalTo(new Bucket(10000)));
        assertThat(s.getBucket(10999), equalTo(new Bucket(10000)));
        assertThat(s.getBucket(11000), Matchers.nullValue());
        assertThat(s.getBucket(8000), equalTo(new Bucket(8000)));
        assertThat(s.getBucket(8001), equalTo(new Bucket(8000)));
        assertThat(s.getBucket(8999), equalTo(new Bucket(8000)));
        assertThat(s.getBucket(9000), equalTo(new Bucket(9000)));
        assertThat(s.getBucket(7999), Matchers.nullValue());

        s.makeBuckets(11000);
        assertThat(s.getBuckets(), contains(new Bucket(11000), new Bucket(10000), new Bucket(9000)));

        s.makeBuckets(13000);
        assertThat(s.getBuckets(), contains(new Bucket(13000), new Bucket(12000), new Bucket(11000)));

        s.makeBuckets(18000);
        assertThat(s.getBuckets(), contains(new Bucket(18000), new Bucket(17000), new Bucket(16000)));

        assertThat(s.transaction(18200, new Transaction(1, 18300)), equalTo(EmptyResult.SUCCESS));
        assertThat(s.getBuckets(), contains(new Bucket(18000, 1.0, 1.0, 1.0, 1), new Bucket(17000), new Bucket(16000)));

        assertThat(s.transaction(18200, new Transaction(1, 17300)), equalTo(EmptyResult.SUCCESS));
        assertThat(s.getBuckets(), contains(new Bucket(18000, 1.0, 1.0, 1.0, 1), new Bucket(17000, 1.0, 1.0, 1.0, 1), new Bucket(16000)));

        assertThat(s.transaction(18200, new Transaction(1, 15500)), equalTo(EmptyResult.OLD_TRANSACTION));
        assertThat(s.getBuckets(), contains(new Bucket(18000, 1.0, 1.0, 1.0, 1), new Bucket(17000, 1.0, 1.0, 1.0, 1), new Bucket(16000)));

        assertThat(s.statistics(18200), equalTo(new Statistics(2.0, 1.0, 1.0, 1.0, 2)));
        assertThat(s.statistics(20000), equalTo(new Statistics(1.0, 1.0, 1.0, 1.0, 1)));
    }
}