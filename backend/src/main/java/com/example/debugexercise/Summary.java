package com.example.debugexercise;

public class Summary {
    private final int total;
    private final int completedCount;

    public Summary(int total, int completedCount) {
        this.total = total;
        this.completedCount = completedCount;
    }

    public int getTotal() {
        return total;
    }

    public int getCompletedCount() {
        return completedCount;
    }
}
