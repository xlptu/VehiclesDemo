package com.evapi.tracking.entity;

import java.time.OffsetDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "veh_registrations")
public class VehicleRegistration {
	
	@Id
	@Column(name = "dol_vehicle_id") 
	private String dolVehicleId;
	
	@Column(nullable = false, length = 10)
	private String vinPrefix;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id", nullable = false)
	VehicleDefinition vehDefinition;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
	VehicleLocation vehLocation;
	
	private Integer electricRange;
	private String electricUtility;
	private String evType;
	private String legislativeDistrict;
	private String cafvType;
	private String censusTract;
	
	@Column(name = "created_ts")
    private OffsetDateTime createdTs = OffsetDateTime.now(); // Set on creation

    @UpdateTimestamp // Automatic on update by Hibernate
    @Column(name = "updated_ts")
    private OffsetDateTime updatedTs;

	public String getDolVehId() {
		return dolVehicleId;
	}

	public void setDolVehId(String dolVehId) {
		this.dolVehicleId = dolVehId;
	}

	public String getVinPrefix() {
		return vinPrefix;
	}

	public void setVinPrefix(String vinPrefix) {
		this.vinPrefix = vinPrefix;
	}

	public VehicleDefinition getVehDefinition() {
		return vehDefinition;
	}

	public void setVehDefinition(VehicleDefinition vehDefinition) {
		this.vehDefinition = vehDefinition;
	}

	public VehicleLocation getVehLocation() {
		return vehLocation;
	}

	public void setVehLocation(VehicleLocation vehLocation) {
		this.vehLocation = vehLocation;
	}

	public Integer getElectricRange() {
		return electricRange;
	}

	public void setElectricRange(Integer electricRange) {
		this.electricRange = electricRange;
	}

	public String getElectricUtility() {
		return electricUtility;
	}

	public void setElectricUtility(String electricUtility) {
		this.electricUtility = electricUtility;
	}

	public String getEvType() {
		return evType;
	}

	public void setEvType(String evType) {
		this.evType = evType;
	}

	public String getLegislativeDistrict() {
		return legislativeDistrict;
	}

	public void setLegislativeDistrict(String legislativeDistrict) {
		this.legislativeDistrict = legislativeDistrict;
	}

	public String getCafvType() {
		return cafvType;
	}

	public void setCafvType(String cafvType) {
		this.cafvType = cafvType;
	}

	public String getCensusTract() {
		return censusTract;
	}

	public void setCensusTract(String censusTract) {
		this.censusTract = censusTract;
	}

	public OffsetDateTime getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(OffsetDateTime createdTs) {
		this.createdTs = createdTs;
	}

	public OffsetDateTime getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(OffsetDateTime updatedTs) {
		this.updatedTs = updatedTs;
	}
}
