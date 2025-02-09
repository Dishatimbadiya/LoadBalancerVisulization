package com.example.LoadBalancerSpringBoot.service.weightedroundrobin;

import com.example.LoadBalancerSpringBoot.service.LoadBalancerService;
import com.example.LoadBalancerSpringBoot.util.Server;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("weightedroundrobin")
public class WeightedRoundRobinService implements LoadBalancerService {

    private int noOfServers;
    private List<Server> serverList;
    private int currentIndex;

    @Override
    public void initializeServers(int noOfServers) {
        // Default weights: 1, 2, 3, ..., noOfServers
        List<Integer> defaultWeights = new ArrayList<>();
        for (int i = 0; i < noOfServers; i++) {
            defaultWeights.add(i + 1);
        }
        initializeServers(noOfServers, defaultWeights);
    }

    public void initializeServers(int noOfServers, List<Integer> weights) {
        this.noOfServers = noOfServers;
        this.serverList = new ArrayList<>();
        this.currentIndex = 0;

        for (int i = 0; i < noOfServers; i++) {
            int weight = weights.get(i);
            System.out.print(" "+ weight+" ");
            serverList.add(new Server(i + 1, weight));
        }
        System.out.println("");
    }

    @Override
    public int handleRequest() {
        if (noOfServers == 0) {
            System.out.println("No servers available to handle the request.");
            return -1;
        }
        
        System.out.println("curr index"+currentIndex+" curr Weighgt "+ serverList.get(currentIndex).getWeight() + "  get active connection " + serverList.get(currentIndex).getActiveConnections());
        
        while (serverList.get(currentIndex).getWeight() <= serverList.get(currentIndex).getActiveConnections()) {
            currentIndex = (currentIndex + 1) % serverList.size();
        }
        Server selectedServer = serverList.get(currentIndex);
        return selectedServer.assignRequest();
    }
}