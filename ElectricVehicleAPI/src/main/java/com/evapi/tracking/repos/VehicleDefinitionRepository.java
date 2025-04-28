package com.evapi.tracking.repos;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evapi.tracking.entity.VehicleDefinition;

@Repository
public interface VehicleDefinitionRepository extends JpaRepository<VehicleDefinition, Long>{
	//Optional<VehicleDefinition> findByDefId(String vehDefinitionId);
	Optional<VehicleDefinition> findByMakeAndModelAndModelYear(String make, String model, Integer modelYear);
}
