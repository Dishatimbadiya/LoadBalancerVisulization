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
        this.noOfServers = noOfServers;
        this.serverList = new ArrayList<>();
        this.currentIndex = 0;

        for (int i = 0; i < noOfServers; i++) {
            int weight = i + 1;
            serverList.add(new Server(i + 1, weight));
        }
    }

    @Override
    public int handleRequest() {
        if (noOfServers == 0) {
            System.out.println("No servers available to handle the request.");
            return -1; 
        }

        while(serverList.get(currentIndex).getWeight() <= 0 ) {
        	currentIndex = (currentIndex + 1) % serverList.size();
        }
        Server selectedServer = serverList.get(currentIndex);
        return selectedServer.assignRequest();

    }
}
