package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Endpoint;
import com.api.model.UpdateEndpointStatusRequest;
import com.api.service.EndpointService;


@RestController
@RequestMapping("/endpoints")
public class EndpointController {

    private final EndpointService endpointService;

    @Autowired
    public EndpointController(EndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @GetMapping("/getEndpoint")
    public List<Endpoint> getAllEndpoints() {
        return endpointService.getAllEndpoints();
    }
    
    @PostMapping("/addEndpoint")
    public ResponseEntity<String> addEndpoint(@RequestBody Endpoint endpoint) {
        endpointService.addEndpoint(endpoint);
        return ResponseEntity.status(HttpStatus.CREATED).body("Endpoint added successfully");
    }
    
    @PutMapping("/updateEndpoint/{id}")
    public ResponseEntity<String> updateEndpoint(@PathVariable("id") int id, @RequestBody Endpoint endpoint) {
        boolean updated = endpointService.updateEndpoint(id, endpoint);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body("Endpoint updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint not found");
        }
    }
    
    @DeleteMapping("/deleteEndpoint/{id}")
    public ResponseEntity<String> deleteEndpoint(@PathVariable("id") int id) {
        boolean deleted = endpointService.deleteEndpoint(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Endpoint deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint not found");
        }
    }
    
    @PutMapping("/updateEndpointStatus/{id}")
    public ResponseEntity<String> updateEndpointStatus(@PathVariable("id") int id, @RequestBody UpdateEndpointStatusRequest request) {
        int status = request.getStatus();
        boolean updated = endpointService.updateEndpointStatus(id, status);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body("Endpoint status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint not found or status could not be updated");
        }
    }
    
    


}
