package com.example.LoadBalancerSpringBoot.service.roundrobin;

import com.example.LoadBalancerSpringBoot.service.LoadBalancerService;
import com.example.LoadBalancerSpringBoot.util.Server;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("round-robin")
public class RoundRobinService implements LoadBalancerService {

    private List<Server> serverList;
    private int currIndex;

    @Override
    public void initializeServers(int noOfServers) {
        serverList = new ArrayList<>();
        for (int i = 0; i < noOfServers; i++) {
            serverList.add(new Server(i + 1));
        }
        currIndex = 0;
    }

    @Override
    public int handleRequest() {
        if (serverList.isEmpty()) return -1;

        Server server = serverList.get(currIndex);
        currIndex = (currIndex + 1) % serverList.size();
        return server.assignRequest();
    }
}