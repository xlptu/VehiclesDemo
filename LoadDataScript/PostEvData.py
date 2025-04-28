import csv
import json
import requests

CSV_FILE_PATH = r"c:\apps\EvRegDemo\Electric_Vehicle_Data.csv"
POST_URL = "http://localhost:8080/v1/registrations"

def parse_and_post(csv_file_path, post_url):
    with open(csv_file_path, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        
        for row in reader:
            payload = {
                "dol_vehicle_id": row.get("DOL Vehicle ID", ""),
                "vin_prefix": row.get("VIN (1-10)", ""),
                "make": row.get("Make", ""),
                "model": row.get("Model", ""),
                "model_year": int(row.get("Model Year", 0)),
                "electric_vehicle_type": row.get("Electric Vehicle Type", ""),
                "county": row.get("County", ""),
                "city": row.get("City", ""),
                "state": row.get("State", ""),
                "postal_code": row.get("Postal Code", ""),
                "legislative_district": row.get("Legislative District", ""),
                "census_tract": row.get("2020 Census Tract", ""),
                "cafv_eligibility": row.get("Clean Alternative Fuel Vehicle (CAFV) Eligibility", ""),
                "electric_range": int(row.get("Electric Range", 0)),
                "base_msrp": int(row.get("Base MSRP", 0)),
                "electric_utility_name": row.get("Electric Utility", ""),
                "vehicle_location": row.get("Vehicle Location", "")
            }
		    
            try:

                response = requests.post(post_url, json=payload)
                response.raise_for_status()
                print(f"Posted successfully: {payload['dol_vehicle_id']}")
            except requests.exceptions.RequestException as e:
                print(f"Failed to post {payload['dol_vehicle_id']}: {e}")

if __name__ == "__main__":
    parse_and_post(CSV_FILE_PATH, POST_URL)
