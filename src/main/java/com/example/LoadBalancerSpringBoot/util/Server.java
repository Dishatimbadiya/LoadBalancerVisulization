package com.example.LoadBalancerSpringBoot.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int id;
    private int weight;
    private int activeConnections;
    private final ExecutorService executorService;

    public Server(int id) {
        this.id = id;
        this.activeConnections = 0;
        this.executorService = Executors.newFixedThreadPool(1);
        this.weight = 1;
    }
    
    public Server(int id, int weight) {
        this.id = id;
        this.weight = weight;
        this.activeConnections = 0;
        this.executorService = Executors.newFixedThreadPool(1);
    }

    public synchronized int assignRequest() {
        activeConnections++;
        weight --;
        executorService.submit(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                synchronized (this) {
                    activeConnections--;
                    weight++;
                }
            }
        });
        return id;
    }

    public synchronized int getActiveConnections() {
        return activeConnections;
    }

	public int getWeight() {
		return this.weight;
	}
}
