package com.example.LoadBalancerSpringBoot.controller;

import com.example.LoadBalancerSpringBoot.service.LoadBalancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loadbalancer")
@CrossOrigin(origins = "http://localhost:3000")
public class LoadBalancerController {

    private final Map<String, LoadBalancerService> services;

    @Autowired
    public LoadBalancerController(Map<String, LoadBalancerService> services) {
        this.services = services;
    }

    @PostMapping("/initialize/{strategy}/{noOfServers}")
    public ResponseEntity<String> initializeLoadBalancer(
            @PathVariable String strategy,
            @PathVariable int noOfServers) {

        LoadBalancerService service = services.get(strategy.toLowerCase());
        if (service != null) {
            service.initializeServers(noOfServers);
            return ResponseEntity.ok("Initialized " + strategy + " load balancer with " + noOfServers + " servers.");
        } else {
            return ResponseEntity.badRequest().body("Invalid strategy: " + strategy);
        }
    }

    @PostMapping("/request/{strategy}")
    public ResponseEntity<Integer> handleRequest(@PathVariable String strategy) {
        LoadBalancerService service = services.get(strategy.toLowerCase());
        if (service != null) {
            int serverId = service.handleRequest();
            return ResponseEntity.ok(serverId);
        } else {
            return ResponseEntity.badRequest().body(-1);
        }
    }
}