package Aggregator;

public class SumAggregator implements Aggregator {
    private int sum = 0;

    @Override
    public void add(Integer value) {
        sum += value;
    }

    @Override
    public Integer getResult() {
        return sum;
    }

    @Override
    public void clear() {
        sum = 0;
    }
}
