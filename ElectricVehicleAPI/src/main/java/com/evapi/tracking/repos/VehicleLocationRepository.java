package com.evapi.tracking.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evapi.tracking.entity.VehicleLocation;

@Repository
public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {
	//Optional<VehicleLocation> findByLocationId(String locationId);	
	Optional<VehicleLocation> findByVehLocation(String vehLocation);
}
