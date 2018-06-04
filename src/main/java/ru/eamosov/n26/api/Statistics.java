package ru.eamosov.n26.api;

import java.util.Objects;

/**
 * Statistics
 */
public class Statistics {

    private double sum;
    private Double avg;
    private Double max;
    private Double min;
    private long count;

    public Statistics() {

    }

    public Statistics(double sum, Double avg, Double max, Double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public Double getAvg() {
        return avg;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }

    public long getCount() {
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
        Statistics that = (Statistics) o;
        return Double.compare(that.sum, sum) == 0 &&
            count == that.count &&
            Objects.equals(avg, that.avg) &&
            Objects.equals(max, that.max) &&
            Objects.equals(min, that.min);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, avg, max, min, count);
    }

    @Override
    public String toString() {
        return "Statistics{" +
            "sum=" + sum +
            ", avg=" + avg +
            ", max=" + max +
            ", min=" + min +
            ", count=" + count +
            '}';
    }
}
