package com.example.LoadBalancerSpringBoot.controller;

public class InitializeRequest {
    private String strategy;
    private int noOfServers;

    // Getters and Setters
    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
    	System.out.println("Hey Str set...");
        this.strategy = strategy;
    }

    public int getNoOfServers() {
        return noOfServers;
    }

    public void setNoOfServers(int noOfServers) {
        this.noOfServers = noOfServers;
    }
}
