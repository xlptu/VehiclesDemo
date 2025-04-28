package com.evapi.tracking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "veh_locations")
public class VehicleLocation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for SERIAL
    @Column(name = "id")
    private Integer locationId;
	
    private String city;
    private String state;
    private String postalCode;
    private String county;
    private String vehLocation;

	public Integer getLocationId() {
		return locationId;
	}
	//auto generated getters & setters via IDE /*cleanup - leverage lombok later*/
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getVehLocation() {
		return vehLocation;
	}

	public void setVehLocation(String vehLocation) {
		this.vehLocation = vehLocation;
	}
}
