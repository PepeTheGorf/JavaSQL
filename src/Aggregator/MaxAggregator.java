package Aggregator;

public class MaxAggregator implements Aggregator {
    private int sum = Integer.MIN_VALUE;

    @Override
    public void add(Integer value) {
        sum = Math.max(sum, value);
    }

    @Override
    public Integer getResult() {
        return sum;
    }

    @Override
    public void clear() {
        sum = Integer.MIN_VALUE;
    }
}
