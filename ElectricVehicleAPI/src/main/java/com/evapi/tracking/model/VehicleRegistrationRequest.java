package com.evapi.tracking.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
//import lombok.Data;

//@Data
public class VehicleRegistrationRequest {
	@JsonProperty("dol_vehicle_id")
	@NotNull(message = "DOL Vehicle ID cannot be null")
    private String dolVehicleId;

	@JsonProperty("vin_prefix")
    @NotBlank(message = "VIN cannot be blank")
    @Size(min = 0, max = 10, message = "VIN must be 17 characters")
    private String vinPrefix;

    @NotBlank(message = "Make cannot be blank")
    private String make;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @JsonProperty("model_year")
    @NotNull(message = "Model Year cannot be null")
    @Min(value = 1980, message = "Invalid model year: EV not existed yet")
    @Max(value = 2030, message = "Model year too far out in future")
    private Integer modelYear;

    @JsonProperty("electric_vehicle_type")
    @NotNull(message = "EV Type cannot be null")
    private String electricVehicleType;

    // Location
    private String county;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(min = 2, max = 2, message = "State must be 2 characters")
    private String state;

    @JsonProperty("postal_code")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid US postal code format")
    private String postalCode;

    @JsonProperty("legislative_district")
    private String legislativeDistrict;
 
    @JsonProperty("census_tract")
    private String censusTract;

    @JsonProperty("cafv_eligibility")
    @NotNull(message = "CAFV Eligibility cannot be null")
    private String cafvEligibility;

    @JsonProperty("electric_range")
    @NotNull(message = "Electric Range cannot be null")
    @Min(value = 0, message = "Electric range cannot be negative")
    private Integer electricRange;

    @JsonProperty("base_msrp")
    @NotNull(message = "Base MSRP cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base MSRP cannot be negative")
    private Integer baseMsrp;

    @JsonProperty("electric_utility_name")
    @NotEmpty(message = "At least one electric utility name must be provided")
    private String electricUtilityName;
    
    @JsonProperty("vehicle_location")
    @NotBlank(message = "Geo location is required")
    private String vehicleLocation;

	public String getDolVehicleId() {
		return dolVehicleId;
	}

	public void setDolVehicleId(String dolVehicleId) {
		this.dolVehicleId = dolVehicleId;
	}

	public String getVinPrefix() {
		return vinPrefix;
	}

	public void setVinPrefix(String vinPrefix) {
		this.vinPrefix = vinPrefix;
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

	public String getElectricVehicleType() {
		return electricVehicleType;
	}

	public void setElectricVehicleType(String electricVehicleType) {
		this.electricVehicleType = electricVehicleType;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
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

	public String getLegislativeDistrict() {
		return legislativeDistrict;
	}

	public void setLegislativeDistrict(String legislativeDistrict) {
		this.legislativeDistrict = legislativeDistrict;
	}

	public String getCensusTract() {
		return censusTract;
	}

	public void setCensusTract(String censusTract) {
		this.censusTract = censusTract;
	}

	public String getCafvEligibility() {
		return cafvEligibility;
	}

	public void setCafvEligibility(String cafvEligibility) {
		this.cafvEligibility = cafvEligibility;
	}

	public Integer getElectricRange() {
		return electricRange;
	}

	public void setElectricRange(Integer electricRange) {
		this.electricRange = electricRange;
	}

	public Integer getBaseMsrp() {
		return baseMsrp;
	}

	public void setBaseMsrp(Integer baseMsrp) {
		this.baseMsrp = baseMsrp;
	}

	public String getElectricUtilityName() {
		return electricUtilityName;
	}

	public void setElectricUtilityName(String electricUtilityName) {
		this.electricUtilityName = electricUtilityName;
	}

	public String getVehicleLocation() {
		return vehicleLocation;
	}

	public void setVehicleLocation(String vehicleLocation) {
		this.vehicleLocation = vehicleLocation;
	}
  
}
