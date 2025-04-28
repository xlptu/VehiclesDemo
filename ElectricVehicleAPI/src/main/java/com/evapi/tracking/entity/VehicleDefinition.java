package com.evapi.tracking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "veh_definitions")
public class VehicleDefinition {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for SERIAL
    @Column(name = "id")
    private Integer vehDefinitionId;

	private String make;
    private String model;
    private Integer modelYear;
    private Integer baseMsrp;
	public Integer getVehDefinitionId() {
		return vehDefinitionId;
	}
	public void setVehDefinitionId(Integer vehDefinitionId) {
		this.vehDefinitionId = vehDefinitionId;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getModelYear() {
		return modelYear;
	}
	public void setModelYear(Integer modelYear) {
		this.modelYear = modelYear;
	}
	public Integer getBaseMsrp() {
		return baseMsrp;
	}
	public void setBaseMsrp(Integer baseMsrp) {
		this.baseMsrp = baseMsrp;
	}

}
