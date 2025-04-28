package com.evapi.tracking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.evapi.tracking.entity.*;
import com.evapi.tracking.exception.ResourceNotFoundException;
import com.evapi.tracking.model.VehicleRegistrationRequest;
import com.evapi.tracking.repos.*;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class) 
public class VehicleRegistrationServiceTest {
	@Mock 
    private VehicleRegistrationRepository registrationRepository;
    @Mock
    private VehicleDefinitionRepository definitionRepository;
    @Mock
    private VehicleLocationRepository locationRepository;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Counter counter; // Mock the counter itself for verification

    @InjectMocks // Inject mocks into the service
    private VehicleRegistrationService service;

    private VehicleRegistration registrationEntity;
    private VehicleRegistrationRequest createRequest;
    private VehicleDefinition definitionEntity;
    private VehicleLocation locationEntity;

    @BeforeEach
    void setUp() {
    	// Need lenient() because not all tests might interact with all counters
        lenient().when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter); 

        definitionEntity = new VehicleDefinition();
        definitionEntity.setVehDefinitionId(1);
        definitionEntity.setMake("TESLA");
        definitionEntity.setModel("MODEL Y");
        definitionEntity.setModelYear(2023);
        definitionEntity.setBaseMsrp(35000);

        locationEntity = new VehicleLocation();
        locationEntity.setLocationId(1);
        locationEntity.setCity("Seattle");
        locationEntity.setCounty("KING");
        locationEntity.setState("WA");
        locationEntity.setPostalCode("98101");
        locationEntity.setVehLocation("POINT(-122.33, 47.61)");
        
        registrationEntity = new VehicleRegistration();
        registrationEntity.setDolVehId("12345L");
        registrationEntity.setVinPrefix("9876543210");
        registrationEntity.setCafvType("Clean Alternative Fuel Vehicle Eligible");
        registrationEntity.setEvType("Battery Electric Vehicle (BEV)");
        registrationEntity.setElectricRange(300);
        registrationEntity.setLegislativeDistrict("46");
        registrationEntity.setCensusTract("12345678");
        registrationEntity.setElectricUtility("CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)");
        registrationEntity.setVehDefinition(definitionEntity);
        registrationEntity.setVehLocation(locationEntity);
        registrationEntity.setCreatedTs(OffsetDateTime.now());
        registrationEntity.setUpdatedTs(OffsetDateTime.now());
        

         createRequest = new VehicleRegistrationRequest();
         createRequest.setDolVehicleId("12345L");
         createRequest.setVinPrefix("9876543210");
         createRequest.setMake("TESLA");
         createRequest.setModel("MODEL Y");
         createRequest.setModelYear(2023);
         createRequest.setElectricVehicleType("Battery Electric Vehicle (BEV)");
         createRequest.setCity("Seattle");
         createRequest.setState("WA");
         createRequest.setPostalCode("98101");
         createRequest.setVehicleLocation("POINT(-122.33, 47.61)");
         createRequest.setCafvEligibility("Clean Alternative Fuel Vehicle Eligible");
         createRequest.setElectricRange(300);
         createRequest.setBaseMsrp(55000);
         createRequest.setElectricUtilityName("CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)");
         createRequest.setCensusTract("123456789");
         createRequest.setLegislativeDistrict("46");
         createRequest.setCounty("KING");
    }

     @Test  //Found
    void getRegistrationById_whenFound_returnDto() {
        when(registrationRepository.findByDolVehicleId("12345L")).thenReturn(Optional.of(registrationEntity));
        
        Optional<VehicleRegistration> result = service.getRegistrationById("12345L");
        assertThat(result).isPresent();
        assertThat(result.get().getDolVehId()).isEqualTo("12345L");
        verify(registrationRepository).findByDolVehicleId("12345L");
    }

     @Test  //notFound
    void getRegistrationById_whenNotFound_returnEmpty() {
        when(registrationRepository.findByDolVehicleId("999999L")).thenReturn(Optional.empty());

        Optional<VehicleRegistration> result = service.getRegistrationById("999999L");

        assertThat(result).isNotPresent();
        verify(registrationRepository).findByDolVehicleId("999999L");
    }

    @Test
    void createRegistration_saveAndReturnDto() {
        // Arrange Mocks
        when(registrationRepository.existsById(anyString())).thenReturn(false); // DolVehId doesn't exist
        when(definitionRepository.findByMakeAndModelAndModelYear(anyString(), anyString(), anyInt()))
                .thenReturn(Optional.of(definitionEntity)); // Definition record exists
        when(locationRepository.findByVehLocation(anyString()))
                .thenReturn(Optional.of(locationEntity)); // location record exists
        when(registrationRepository.save(any(VehicleRegistration.class))).thenReturn(registrationEntity); // Mock save

        VehicleRegistration result = service.createRegistration(createRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDolVehId()).isEqualTo(createRequest.getDolVehicleId());
        assertThat(result.getVinPrefix()).isEqualTo(createRequest.getVinPrefix());

        // Verify interactions
        verify(registrationRepository).existsById(createRequest.getDolVehicleId());
        verify(registrationRepository).save(any(VehicleRegistration.class)); // Check that save was called
        verify(counter).increment(); // Verify counter increment
    }

    @Test  //duplicate check
    void createRegistration_whenIdExists_shouldThrowException() {
        when(registrationRepository.existsById("12345L")).thenReturn(true); // ID exists

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.createRegistration(createRequest);
        });

        assertThat(exception.getMessage()).contains("already exists");
        verify(registrationRepository).existsById("12345L");
        verify(registrationRepository, never()).save(any());
        verify(counter, never()).increment();
    }

    @Test
    void deleteRegistration_whenExists_shouldDelete() {
        when(registrationRepository.existsById("12345L")).thenReturn(true);
        doNothing().when(registrationRepository).deleteById("12345L"); // Mock void method

        service.deleteRegistration("12345L");

        verify(registrationRepository).existsById("12345L");
        verify(registrationRepository).deleteById("12345L");
        //verify(counter).increment(); // Verify counter increment
    }

    @Test
    void deleteRegistration_whenNotExists_shouldThrowNotFound() {
        when(registrationRepository.existsById("999999L")).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteRegistration("999999L");
        });

        assertThat(exception.getResourceName()).isEqualTo("VehicleRegistration");
        verify(registrationRepository).existsById("999999L");
        verify(registrationRepository, never()).deleteById(anyString());
        verify(counter, never()).increment();
    }

    // --- Add tests for updateRegistration (success and not found)
    //updateRegistration(String dolVehicleId, VehicleRegistrationRequest request)
}
