package com.example.LoadBalancerSpringBoot.service;

public interface LoadBalancerService {
    void initializeServers(int noOfServers);
    int handleRequest();
}