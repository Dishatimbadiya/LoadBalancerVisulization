package com.example.LoadBalancerSpringBoot;

import com.example.LoadBalancerSpringBoot.controller.LoadBalancerController;
import com.example.LoadBalancerSpringBoot.service.LoadBalancerService;
import com.example.LoadBalancerSpringBoot.service.leastconnections.LeastConnectionsService;
import com.example.LoadBalancerSpringBoot.service.roundrobin.RoundRobinService;
import com.example.LoadBalancerSpringBoot.service.weightedroundrobin.WeightedRoundRobinService;
import com.example.LoadBalancerSpringBoot.util.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoadBalancerSpringBootApplicationTests {

    @Mock
    private LeastConnectionsService leastConnectionsService;

    @Mock
    private RoundRobinService roundRobinService;

    @Mock
    private WeightedRoundRobinService weightedRoundRobinService;

    @InjectMocks
    private LoadBalancerController controller;

    private Map<String, LoadBalancerService> services;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the services map and populate it with mocked strategies
        services = new HashMap<>();
        services.put("least-connections", leastConnectionsService);
        services.put("round-robin", roundRobinService);
        services.put("weightedroundrobin", weightedRoundRobinService);

        // Inject the services map into the controller
        controller = new LoadBalancerController(services);
    }

    @Test
    void testInitializeLoadBalancer_Success() {
        // Mock the behavior of the round-robin service
        doNothing().when(roundRobinService).initializeServers(3);

        // Call the controller method
        ResponseEntity<String> response = controller.initializeLoadBalancer("round-robin", 3);

        // Verify the response
        assertEquals("Initialized round-robin load balancer with 3 servers.", response.getBody());
        verify(roundRobinService, times(1)).initializeServers(3);
    }

    @Test
    void testInitializeLoadBalancer_InvalidStrategy() {
        // Call the controller method with an invalid strategy
        ResponseEntity<String> response = controller.initializeLoadBalancer("invalid-strategy", 3);

        // Verify the response
        assertEquals("Invalid strategy: invalid-strategy", response.getBody());
    }

    @Test
    void testHandleRequest_Success() {
        // Mock the behavior of the least-connections service
        when(leastConnectionsService.handleRequest()).thenReturn(1);

        // Call the controller method
        ResponseEntity<Integer> response = controller.handleRequest("least-connections");

        // Verify the response
        assertEquals(1, response.getBody());
        verify(leastConnectionsService, times(1)).handleRequest();
    }

    @Test
    void testHandleRequest_InvalidStrategy() {
        // Call the controller method with an invalid strategy
        ResponseEntity<Integer> response = controller.handleRequest("invalid-strategy");

        // Verify the response
        assertEquals(-1, response.getBody());
    }

    @Test
    void testServerAssignment() {
        // Create a server instance
        Server server = new Server(1);

        // Assign a request to the server
        int serverId = server.assignRequest();

        // Verify the server ID and active connections
        assertEquals(1, serverId);
        assertEquals(1, server.getActiveConnections());
    }

    @Test
    void testServerWeightAssignment() {
        // Create a server instance with a specific weight
        Server server = new Server(1, 5);

        // Verify the server weight
        assertEquals(5, server.getWeight());
    }

    @Test
    void testServerWeightDecrement() {
        // Create a server instance with a specific weight
        Server server = new Server(1, 5);

        // Assign a request to the server
        server.assignRequest();

        // Verify the server weight after assignment
        assertEquals(4, server.getWeight());
    }

    @Test
    void testServerWeightIncrementAfterRequestCompletion() throws InterruptedException {
        // Create a server instance with a specific weight
        Server server = new Server(1, 5);

        // Assign a request to the server
        server.assignRequest();

        // Simulate the request completion after 2 seconds
        Thread.sleep(2500); // Slightly longer than the 2-second delay in the Server class

        // Verify the server weight after request completion
        assertEquals(5, server.getWeight());
    }
}