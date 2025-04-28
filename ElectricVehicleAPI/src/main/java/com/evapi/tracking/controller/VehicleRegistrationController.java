package com.evapi.tracking.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.evapi.tracking.entity.VehicleRegistration;
import com.evapi.tracking.model.VehicleRegistrationRequest;
import com.evapi.tracking.service.VehicleRegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/registrations") 
public class VehicleRegistrationController {
	Logger log = LoggerFactory.getLogger(VehicleRegistrationController.class);
	
	@Autowired
	VehicleRegistrationService registrationService;
	
    @GetMapping("/{dolVehId}")  //WA state registration id (dol_vehicle_id)
    public ResponseEntity<VehicleRegistration> getRegistrationById(@PathVariable("dolVehId") String id) {
        log.info("Received request to get registration with ID " + id);
        return registrationService.getRegistrationById(id)
                .map(ResponseEntity::ok) // If found, return 200 OK
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    @PostMapping
    public ResponseEntity<VehicleRegistration> createRegistration(
            @Valid @RequestBody VehicleRegistrationRequest request) { // Add @Valid for input validation
        log.info("Received create registration for vinPrefix: {}", request.getVinPrefix());
        VehicleRegistration createdDto = registrationService.createRegistration(request);

        // Build location header for created resource
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{dol_vehicle_id}")
                .buildAndExpand(createdDto.getDolVehId())
                .toUri();
        log.debug("Responding with CREATED status and location: {}", location);
        return ResponseEntity.created(location).body(createdDto); // Return 201 Created
    }

    @PutMapping("/{dolVehId}")
    public ResponseEntity<VehicleRegistration> updateRegistration(
            @PathVariable("dolVehId") String dolVehId,
            @Valid @RequestBody VehicleRegistrationRequest request) { // Add @Valid for input validation
        log.info("Update requet for registration with vinPrefix: {}", request.getVinPrefix());
        VehicleRegistration updatedDto = registrationService.updateRegistration(dolVehId, request);
        return ResponseEntity.ok(updatedDto); // Return 200 OK
    }

    @DeleteMapping("/{dolVehId}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable("dolVehId") String dolVehId) {
        log.info("Request to delete registration with ID: {}*******", dolVehId.subSequence(0, 4));
        registrationService.deleteRegistration(dolVehId);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
    
    /*
     @GetMapping
    public ResponseEntity<Page<VehicleRegistration>> getAllRegistrations(
            @PageableDefault(size = 20, sort = "dolVehicleId") Pageable pageable) { 
        log.info("Received request: get all registrations");
        Page<VehicleRegistration> page = registrationService.getAllRegistrations(pageable);
        return ResponseEntity.ok(page);
    }
     
     */
}


