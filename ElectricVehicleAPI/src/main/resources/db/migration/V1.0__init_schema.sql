CREATE TABLE if not exists veh_locations (
    id SERIAL PRIMARY KEY,
    city VARCHAR(100),
    state VARCHAR(2),
    postal_code VARCHAR(10),
    county VARCHAR(100),
    --veh_location GEOGRAPHY(POINT),
    veh_location VARCHAR(150),     
    CONSTRAINT uq_vehicle_location UNIQUE (veh_location)
);

CREATE TABLE if not exists veh_definitions (
    id SERIAL PRIMARY KEY,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    model_year INTEGER NOT NULL,
    base_msrp INTEGER NOT NULL,
    CONSTRAINT uq_vehicle_definition UNIQUE (make, model, model_year)
);

CREATE TABLE if not exists veh_registrations (
    dol_vehicle_id varchar(20) PRIMARY KEY, 
	vin_prefix VARCHAR(10) NOT NULL,
	definition_id INTEGER REFERENCES veh_definitions(id),
    location_id INTEGER REFERENCES veh_locations(id),
	electric_range INTEGER,
	electric_utility VARCHAR(255),
	ev_type VARCHAR(100)  NOT NULL,
    legislative_district VARCHAR(50)  NOT NULL,
    cafv_type VARCHAR(255),
    census_tract VARCHAR(50),
    created_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

--indexes
CREATE INDEX idx_registrations_vin ON veh_registrations(vin_prefix);
CREATE INDEX idx_locations_postal_code ON veh_locations(veh_location);
CREATE INDEX idx_veh_make_model_yr ON veh_definitions(make, model, model_year);
--CREATE INDEX idx_geo_location_gist ON veh_location USING GIST (veh_location);
--CREATE INDEX idx_locations_city ON veh_locations(city);
--CREATE INDEX idx_locations_postal_code ON veh_locations(postal_code);

