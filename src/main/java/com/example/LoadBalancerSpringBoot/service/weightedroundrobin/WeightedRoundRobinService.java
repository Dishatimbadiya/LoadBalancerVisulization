package com.example.LoadBalancerSpringBoot.service.weightedroundrobin;

import com.example.LoadBalancerSpringBoot.service.LoadBalancerService;
import com.example.LoadBalancerSpringBoot.util.Server;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service("weightedroundrobin")
public class WeightedRoundRobinService implements LoadBalancerService {

    private int noOfServers;
    private List<Server> serverList;
    private AtomicInteger currentIndex;

    @Override
    public void initializeServers(int noOfServers) {
        List<Integer> defaultWeights = new ArrayList<>();
        for (int i = 0; i < noOfServers; i++) {
            defaultWeights.add(1); // Default weight of 1 for all servers
        }
        initializeServers(noOfServers, defaultWeights);
    }

    public void initializeServers(int noOfServers, List<Integer> weights) {
        if (weights.size() != noOfServers) {
            throw new IllegalArgumentException("Number of weights must match the number of servers.");
        }

        this.noOfServers = noOfServers;
        this.serverList = new ArrayList<>();
        this.currentIndex = new AtomicInteger(0);

        for (int i = 0; i < noOfServers; i++) {
            int weight = weights.get(i);
            serverList.add(new Server(i + 1, weight));
        }
    }

    @Override
    public int handleRequest() {
        if (noOfServers == 0) {
            System.out.println("No servers available to handle the request.");
            return -1; // Indicate no servers are available
        }

        int attempts = 0;
        while (attempts < noOfServers) {
            int currentIdx = currentIndex.getAndUpdate(idx -> (idx + 1) % noOfServers);
            Server server = serverList.get(currentIdx);

            if (server.getWeight() > server.getActiveConnections()) {
                return server.assignRequest(); 
            }

            attempts++;
        }

        System.out.println("All servers are at full capacity.");
        return -1; // Indicate all servers are at full capacity
    }
}