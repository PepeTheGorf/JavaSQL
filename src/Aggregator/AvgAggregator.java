package Aggregator;

public class AvgAggregator implements Aggregator {
    private int sum = 0;
    private int count = 0;

    @Override
    public void add(Integer value) {
        sum += value;
        count++;
    }

    @Override
    public Integer getResult() {
        return count == 0 ? null : sum / count;
    }
    
    @Override
    public void clear() {
        sum = 0;
        count = 0;
    }
}
