### Mini-Project to demonstrate the following:

- Postgres schema design
- Java Springboot API to expose CRUD operations
- Client Script to load all CSV data via http POST

###DB Schema Design Goals:
Flexibility, maintainability, and reduced redundancy.

###Design Decisions: 
- A single flat table would simplify implementation, but a normalized relational schema offers greater flexibility and long-term maintainability.
- Vehicle Definitions: including 'make', 'model', 'model year', and 'Base MSRP' are valuable datasets. Storing them separately supports future functional needs, such as maintaining historical data, pre-populating UI fields, or applying different data retention policies.
- Separating these definitions also simplifies `updates` to the "Base MSRP" for specific make, model, and year combinations.
- For the state of WA EV registrations, the 'dol_vehicle_id' is unique; Partial VIN is not unique.
```
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
```
#To Run Locally:
1.  Ensure postgres is up & running 
2.  Clone down the `git@git.com`; branch main
3.  Modify postgres properties in application.yml to point to your postgres
	a. spring.datasource:   URL; Username: Password  (keep in mind, cridential will be stored in Secret Manager / Vault for prod system)
	b. Ensure the postgres user has appropriate privileges to create tables, etc.
4. Start/Run Springboot Application (default port 8080)
	a.  GET: /v1/registrations/{dolVehId}
	b.  DELETE: /v1/registrations/{dolVehId}
	c.  Create new Veh Regisgtration via POST with json body:
	 ```
	 {
	"dol_vehicle_id":"275305593",
	"vin_prefix":"7SAYGDEE4S",
	"make":"TESLA",
	"model":"MODEL Y",
	"model_year":2025,
	"electric_vehicle_type":"Electric Vehicle (BEV)",
	"county":"Thurston",
	"city":"Olympia",
	"state":"WA",
	"postal_code":"98501",
	"legislative_district":"22",
	"census_tract":"53067010400",
	"cafv_eligibility":"Eligibility unknown as battery range has not been researched",
	"electric_range":0,
	"base_msrp":0,
	"electric_utility_name":"PUGET SOUND ENERGY INC",
	"vehicle_location":"POINT (-122.89166 47.03956)"
}```
### Keep in mind: the create and update APIs have built-in input validations for each individual field. (Will throw http 400 error with detail)

#Load All Sample CSV Data
- Ensure the Springboot Application API is running/listening for request
- Ensure the `LoadDataScript` folder is downloaded on your PC
- Ensure Python is installed on your PC:
	a. Install Python (https://www.python.org/downloads/) and add Python 3.x to PATH
	b. Ensure `requests` Library is installed:
		i. Run `pip install requests` or `python -m pip install requests` on cmd line
	c. Modify the python script to point to your local path for the csv file, and post_url
		i.  CSV_FILE_PATH = r"c:\LoadDataScript\Electric_Vehicle_Data.csv"
		ii. POST_URL = "http://localhost:8080/v1/registrations"
	d. Execute script:   python PostEvData.py

#Further Testing via `Postman`
- Download & import the `postman collection` from the gitRepo `PostmanExport` folder
- Modify query param, and or Json data for various scenario
- Ensure the Springboot Application is up & running, execute different operations


###End