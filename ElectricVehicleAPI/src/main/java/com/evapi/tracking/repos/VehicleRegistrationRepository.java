package com.evapi.tracking.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.evapi.tracking.entity.VehicleRegistration;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRegistrationRepository extends JpaRepository<VehicleRegistration, String>{
	List<VehicleRegistration> findByVinPrefix(String vinPrefix);
	
	Optional<VehicleRegistration> findByDolVehicleId(String dolVehicleId);
}
