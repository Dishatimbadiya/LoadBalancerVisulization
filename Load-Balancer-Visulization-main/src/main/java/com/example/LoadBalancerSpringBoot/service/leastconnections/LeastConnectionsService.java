package com.example.LoadBalancerSpringBoot.service.leastconnections;

import com.example.LoadBalancerSpringBoot.service.LoadBalancerService;
import com.example.LoadBalancerSpringBoot.util.Server;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("least-connections")
public class LeastConnectionsService implements LoadBalancerService {

    private List<Server> serverList;

    @Override
    public void initializeServers(int noOfServers) {
        serverList = new ArrayList<>();
        for (int i = 0; i < noOfServers; i++) {
            serverList.add(new Server(i + 1));
        }
    }

    @Override
    public int handleRequest() {
        if (serverList.isEmpty()) return -1;

        Server leastLoadedServer = serverList.stream()
                .min((s1, s2) -> Integer.compare(s1.getActiveConnections(), s2.getActiveConnections()))
                .orElse(null);

        return leastLoadedServer != null ? leastLoadedServer.assignRequest() : -1;
    }
}