package ru.eamosov.n26.impl;

import ru.eamosov.n26.api.Transaction;

import java.util.Objects;

/**
 * Bucket - statistics for every second
 */
class Bucket {

    private final long start;

    private Double min;
    private Double max;
    private double sum = 0;
    private long count = 0;

    Bucket(long start) {
        this.start = start;
    }

    Bucket(long start, Double min, Double max, double sum, long count) {
        this.start = start;
        this.min = min;
        this.max = max;
        this.sum = sum;
        this.count = count;
    }

    long getStart() {
        return start;
    }

    void add(Transaction transaction) {
        if (min == null || transaction.getAmount() < min) {
            min = transaction.getAmount();
        }

        if (max == null || transaction.getAmount() > max) {
            max = transaction.getAmount();
        }

        sum = sum + transaction.getAmount();

        count++;
    }

    Double getMin() {
        return min;
    }

    Double getMax() {
        return max;
    }

    double getSum() {
        return sum;
    }

    long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bucket bucket = (Bucket) o;
        return start == bucket.start &&
            Double.compare(bucket.sum, sum) == 0 &&
            count == bucket.count &&
            Objects.equals(min, bucket.min) &&
            Objects.equals(max, bucket.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, min, max, sum, count);
    }

    @Override
    public String toString() {
        return "Bucket{" +
            "start=" + start +
            ", min=" + min +
            ", max=" + max +
            ", sum=" + sum +
            ", count=" + count +
            '}';
    }
}
