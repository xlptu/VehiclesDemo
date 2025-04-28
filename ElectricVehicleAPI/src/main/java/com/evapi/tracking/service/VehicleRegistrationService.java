package com.evapi.tracking.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evapi.tracking.entity.*;
import com.evapi.tracking.model.VehicleRegistrationRequest;
import com.evapi.tracking.repos.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional; // Important for writes
import com.evapi.tracking.exception.*;

@Service
public class VehicleRegistrationService {
	Logger log = LoggerFactory.getLogger(VehicleRegistrationService.class);
	
	@Autowired
	private VehicleRegistrationRepository regRepository;
	
	@Autowired
	private VehicleDefinitionRepository defRepository;
    
	@Autowired
	private VehicleLocationRepository locRepository;
    
	@Autowired
    public MeterRegistry meterRegistry; // For metrics

    private Counter regCreatedCounter;
    private Counter regUpdatedCounter;
    private Counter regDeletedCounter;
    
    @PostConstruct
    public void initialize() {
    	// Initialize counters 
        this.regCreatedCounter = Counter.builder("registrations.created")
                .description("Number of vehicle registrations created")
                .register(meterRegistry);
        this.regUpdatedCounter = Counter.builder("registrations.updated")
                .description("Number of vehicle registrations updated")
                .register(meterRegistry);
        this.regDeletedCounter = Counter.builder("registrations.deleted")
                .description("Number of vehicle registrations deleted")
                .register(meterRegistry);       
    }
        
    @Transactional(readOnly = true)
    public Optional<VehicleRegistration> getRegistrationById(String dolVehicleId) {
        log.info("Fetching registration with ID: {}", dolVehicleId);
        return regRepository.findByDolVehicleId(dolVehicleId);
    }

    @Transactional // Read-write transaction
    public VehicleRegistration createRegistration(VehicleRegistrationRequest request) {
        log.info("Attempting to create registration for dolId", request.getVinPrefix()); //VIN-Prefix not unique; DOL_ID is
        // Prevent duplicate DOL ID
        if (regRepository.existsById(request.getDolVehicleId())) {
             log.warn("Registration with DOL ID {} already exists.", request.getDolVehicleId());
             throw new IllegalArgumentException("Registration with DOL ID " + maskDolId(request.getDolVehicleId()) + " already exists.");
        }

        // Find or create Vehicle Definition
        VehicleDefinition definition = findOrCreateDefinition(request);
        
     // Find or create Location
        VehicleLocation location = findOrCreateLocation(request);

     //build new entity
        VehicleRegistration regVeh = new VehicleRegistration();
        regVeh.setDolVehId(request.getDolVehicleId());
        regVeh.setVinPrefix(request.getVinPrefix());
        regVeh.setVehDefinition(definition);
        regVeh.setVehLocation(location);
        regVeh.setEvType(request.getElectricVehicleType());
        regVeh.setCafvType(request.getCafvEligibility());
        regVeh.setCensusTract(request.getCensusTract());
        regVeh.setElectricRange(request.getElectricRange());
        regVeh.setElectricUtility(request.getElectricUtilityName());
        regVeh.setLegislativeDistrict(request.getLegislativeDistrict());

        VehicleRegistration savedRegistration = regRepository.save(regVeh);
        regCreatedCounter.increment(); // Increment metric
        log.info("Successfully created registration with DOL_ID: {} and VinPrefix: {}", maskDolId(savedRegistration.getDolVehId()), savedRegistration.getVinPrefix());
        return savedRegistration;
    }


    @Transactional 
    public VehicleRegistration updateRegistration(String dolVehicleId, VehicleRegistrationRequest request) {
        log.info("Attempting to update registration with ID: {}", dolVehicleId);

        VehicleRegistration existingRegistration = regRepository.findByDolVehicleId(dolVehicleId)
                .orElseThrow(() -> {
                     log.warn("Update failed: Registration with ID {} not found.", dolVehicleId);
                     return new ResourceNotFoundException("VehicleRegistration", "dolVehicleId", maskDolId(dolVehicleId));
                });

        // Find or create related entities if necessary (optional, depends on update requirements)
        // For this example, we update basic fields and utilities. Definition is usually not changed.
        // Find or create Vehicle Definition --- 
        VehicleDefinition definition = findOrCreateDefinition(request);
        
        // Find or create Location
        VehicleLocation location = findOrCreateLocation(request);

        // Update fields from request
        existingRegistration.setDolVehId(request.getDolVehicleId());
        existingRegistration.setVinPrefix(request.getVinPrefix());
        existingRegistration.setVehDefinition(definition);
        existingRegistration.setVehLocation(location);
        existingRegistration.setEvType(request.getElectricVehicleType());
        existingRegistration.setCafvType(request.getCafvEligibility());
        existingRegistration.setCensusTract(request.getCensusTract());
        existingRegistration.setElectricRange(request.getElectricRange());
        existingRegistration.setElectricUtility(request.getElectricUtilityName());
        //existingRegistration.setLegislativeDistrict(request.getLegislativeDistrict());

        //Don't want to update to vehDefinition or Location per registration...create new if needed.
        VehicleRegistration updatedRegistration = regRepository.save(existingRegistration); // Save updates
        regUpdatedCounter.increment(); // Increment metric
        log.info("Successfully updated registration with ID: {}", maskDolId(updatedRegistration.getDolVehId()));
        return updatedRegistration;
    }

    @Transactional
    public void deleteRegistration(String dolVehicleId) {
        log.info("Attempting to delete registration with ID: {}", maskDolId(dolVehicleId));  
        if (!regRepository.existsById(dolVehicleId)) {
             log.warn("Delete failed: Registration with ID {} not found.", maskDolId(dolVehicleId));
             throw new ResourceNotFoundException("VehicleRegistration", "dolVehicleId", maskDolId(dolVehicleId));
        }
        regRepository.deleteById(dolVehicleId);
        //regDeletedCounter.increment(); // Increment metric
        log.info("Successfully deleted registration with ID: {}", maskDolId(dolVehicleId));
    }

    /*
    @Transactional(readOnly = true)
    public Page<VehicleRegistration> getAllRegistrations(Pageable pageable) {
        log.info("Fetching all registrations, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return registrationRepository.findAll(pageable);
    }
     */
    // --- Helpers
    private VehicleDefinition findOrCreateDefinition(VehicleRegistrationRequest request) {
    	
         return defRepository.findByMakeAndModelAndModelYear(request.getMake(), request.getModel(), request.getModelYear())
            .orElseGet(() -> {
                log.debug("Vehicle definition not found, creating new one for Make: {}, Model: {}, Year: {}", request.getMake(), request.getModel(), request.getModelYear());
                VehicleDefinition newDef = new VehicleDefinition();
                newDef.setMake(request.getMake());
                newDef.setModel(request.getModel());
                newDef.setModelYear(request.getModelYear());
                newDef.setBaseMsrp(request.getBaseMsrp());	
                return defRepository.save(newDef);
            });
    }
    
    private VehicleLocation findOrCreateLocation(VehicleRegistrationRequest request) {
    	
        return locRepository.findByVehLocation(request.getVehicleLocation())
           .orElseGet(() -> {
               log.debug("Loation does not exist, creating new one for vin_prefix: " + request.getVinPrefix());
               VehicleLocation newDef = new VehicleLocation();
               newDef.setCity(request.getCity());
               newDef.setCounty(request.getCounty());
               newDef.setPostalCode(request.getPostalCode());
               newDef.setState(request.getState());
               newDef.setVehLocation(request.getVehicleLocation());
               return locRepository.save(newDef);
           });
   }
    
   private String maskDolId(String dolVehicleId)
   {
	   return dolVehicleId.substring(0, 4) + "************";
   }
}