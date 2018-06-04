package ru.eamosov.n26.impl;

import ru.eamosov.n26.api.EmptyResult;
import ru.eamosov.n26.api.Statistics;
import ru.eamosov.n26.api.StatsService;
import ru.eamosov.n26.api.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Core implementation of StatsService
 */
public class StatsServiceImpl implements StatsService {

    private final int maxBuckets;

    private final List<Bucket> buckets = new ArrayList<>();

    public StatsServiceImpl() {
        this(60);
    }

    StatsServiceImpl(int maxBuckets) {
        this.maxBuckets = maxBuckets;
    }

    private long bucketStart(long millis) {
        return (millis / 1000) * 1000;
    }

    void makeBuckets(long currentTimeMillis) {
        for (int i = 0; i < maxBuckets; i++) {

            final long start = bucketStart(currentTimeMillis) - i * 1000;

            if (i >= buckets.size() || buckets.get(i).getStart() != start) {
                buckets.add(i, new Bucket(start));
            } else if (i < buckets.size() && buckets.get(i).getStart() == start) {
                break;
            }
        }
        if (buckets.size() > maxBuckets) {
            buckets.subList(maxBuckets, buckets.size()).clear();
        }
    }

    List<Bucket> getBuckets() {
        return buckets;
    }

    private Bucket firstBucket() {
        return buckets.get(0);
    }

    Bucket getBucket(long timeMillis) {
        final int index = (int) ((firstBucket().getStart() - bucketStart(timeMillis)) / 1000);
        if (index < 0 || index >= buckets.size()) {
            return null;
        }

        return buckets.get(index);
    }

    synchronized EmptyResult transaction(long currentTimeMillis, Transaction transaction) {

        makeBuckets(currentTimeMillis);
        final Bucket bucket = getBucket(transaction.getTimestamp());
        if (bucket == null) {
            return EmptyResult.OLD_TRANSACTION;
        }

        bucket.add(transaction);

        return EmptyResult.SUCCESS;
    }

    @Override
    public EmptyResult transaction(Transaction transaction) {
        return transaction(System.currentTimeMillis(), transaction);
    }

    synchronized Statistics statistics(long currentTimeMillis) {

        makeBuckets(currentTimeMillis);

        double sum = 0;
        Double max = null;
        Double min = null;
        long count = 0;

        for (Bucket b : buckets) {
            if (b.getMin() != null && (min == null || min > b.getMin())) {
                min = b.getMin();
            }

            if (b.getMax() != null && (max == null || max < b.getMax())) {
                max = b.getMax();
            }

            sum += b.getSum();
            count += b.getCount();
        }

        return new Statistics(sum, count > 0 ? sum / count : null, max, min, count);
    }

    @Override
    public Statistics statistics() {
        return statistics(System.currentTimeMillis());
    }
}
