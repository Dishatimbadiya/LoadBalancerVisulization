package com.example.LoadBalancerSpringBoot.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private final int id;
    private final int weight;
    private final AtomicInteger activeConnections;
    private final ExecutorService executorService;

    public Server(int id) {
        this.id = id;
        this.weight = 1;
        this.activeConnections = new AtomicInteger(0);
        this.executorService = Executors.newFixedThreadPool(1);
    }
    
    public Server(int id, int weight) {
        this.id = id;
        this.weight = weight;
        this.activeConnections = new AtomicInteger(0);
        this.executorService = Executors.newFixedThreadPool(1);
    }

    public int assignRequest() {
        activeConnections.incrementAndGet(); // Thread-safe increment
        executorService.submit(() -> {
            try {
                Thread.sleep(5000); // Simulate request processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                activeConnections.decrementAndGet(); // Thread-safe decrement
            }
        });
        return id;
    }

    public int getActiveConnections() {
        return activeConnections.get(); // Thread-safe read
    }

    public int getWeight() {
        return weight;
    }
}