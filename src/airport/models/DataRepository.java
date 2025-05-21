/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.models;

import airport.controllers.utils.FileUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class DataRepository {

    private ArrayList<Passenger> passengers = new ArrayList<>();
    private ArrayList<Plane> planes = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Flight> flights = new ArrayList<>();

    public DataRepository() {
        loadPassengers();
        loadPlanes();
        loadLocations();
        loadFlights();
    }

    public ArrayList<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers);
    }

    public ArrayList<Plane> getAllPlanes() {
        return new ArrayList<>(planes);
    }

    public ArrayList<Location> getAllLocations() {
        return new ArrayList<>(locations);
    }

    public ArrayList<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    public Plane findPlaneById(String id) {
        for (Plane plane : planes) {
            if (plane.getId().equals(id)) {
                return plane;
            }
        }
        return null;
    }

    public Location findLocationById(String id) {
        for (Location location : locations) {
            if (location.getAirportId().equals(id)) {
                return location;
            }
        }
        return null;
    }

    public Passenger findPassengerById(long id) {
        for (Passenger passenger : passengers) {
            if (passenger.getId() == id) {
                return passenger;
            }
        }
        return null;
    }

    public void addPassenger(Passenger passenger) {
        if (passenger != null && findPassengerById(passenger.getId()) == null) {
            this.passengers.add(passenger);
        }
        // Optionally, handle the case where passenger is null or ID already exists,
        // though the controller should validate this.
    }
    
    public Flight findFlightById(String id) {
        for (Flight flight : flights) {
            if (flight.getId().equals(id)) {
                return flight;
            }
        }
        return null;
    }

    public void addLocation(Location location) {
        if (location != null && findLocationById(location.getAirportId()) == null) {
            this.locations.add(location);
        }
        // Optionally, handle the case where location is null or ID already exists,
        // though the controller should validate this.
    }

    public void addPlane(Plane plane) {
        if (plane != null && findPlaneById(plane.getId()) == null) {
            this.planes.add(plane);
        }
        // Optionally, handle the case where plane is null or ID already exists,
        // though the controller should validate this.
    }

    public void addFlight(Flight flight) {
        if (flight != null && findFlightById(flight.getId()) == null) {
            this.flights.add(flight);
        }
        // Optionally, handle the case where flight is null or ID already exists,
        // though the controller should validate this.
    }

    private void loadPassengers() {
        String fileContent = FileUtils.readFileToString("json/passengers.json");
        if (fileContent == null) {
            System.err.println("Failed to read passengers.json");
            return;
        }
        try {
            JSONArray passengersArray = new JSONArray(fileContent);
            for (int i = 0; i < passengersArray.length(); i++) {
                JSONObject passengerJson = passengersArray.getJSONObject(i);
                long id = passengerJson.getLong("id");
                String firstname = passengerJson.getString("firstname");
                String lastname = passengerJson.getString("lastname");
                LocalDate birthDate = LocalDate.parse(passengerJson.getString("birthDate"));
                int countryPhoneCode = passengerJson.getInt("countryPhoneCode");
                long phone = passengerJson.getLong("phone");
                String country = passengerJson.getString("country");
                passengers.add(new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country));
            }
        } catch (JSONException e) {
            System.err.println("Error parsing passengers.json: " + e.getMessage());
        }
    }

    private void loadPlanes() {
        String fileContent = FileUtils.readFileToString("json/planes.json");
        if (fileContent == null) {
            System.err.println("Failed to read planes.json");
            return;
        }
        try {
            JSONArray planesArray = new JSONArray(fileContent);
            for (int i = 0; i < planesArray.length(); i++) {
                JSONObject planeJson = planesArray.getJSONObject(i);
                String id = planeJson.getString("id");
                String brand = planeJson.getString("brand");
                String model = planeJson.getString("model");
                int maxCapacity = planeJson.getInt("maxCapacity");
                String airline = planeJson.getString("airline");
                planes.add(new Plane(id, brand, model, maxCapacity, airline));
            }
        } catch (JSONException e) {
            System.err.println("Error parsing planes.json: " + e.getMessage());
        }
    }

    private void loadLocations() {
        String fileContent = FileUtils.readFileToString("json/locations.json");
        if (fileContent == null) {
            System.err.println("Failed to read locations.json");
            return;
        }
        try {
            JSONArray locationsArray = new JSONArray(fileContent);
            for (int i = 0; i < locationsArray.length(); i++) {
                JSONObject locationJson = locationsArray.getJSONObject(i);
                String airportId = locationJson.getString("airportId");
                String airportName = locationJson.getString("airportName");
                String airportCity = locationJson.getString("airportCity");
                String airportCountry = locationJson.getString("airportCountry");
                double airportLatitude = locationJson.getDouble("airportLatitude");
                double airportLongitude = locationJson.getDouble("airportLongitude");
                locations.add(new Location(airportId, airportName, airportCity, airportCountry, airportLatitude, airportLongitude));
            }
        } catch (JSONException e) {
            System.err.println("Error parsing locations.json: " + e.getMessage());
        }
    }

    private void loadFlights() {
        String fileContent = FileUtils.readFileToString("json/flights.json");
        if (fileContent == null) {
            System.err.println("Failed to read flights.json");
            return;
        }
        try {
            JSONArray flightsArray = new JSONArray(fileContent);
            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject flightJson = flightsArray.getJSONObject(i);
                String id = flightJson.getString("id");
                String planeId = flightJson.getString("plane");
                String departureLocationId = flightJson.getString("departureLocation");
                String arrivalLocationId = flightJson.getString("arrivalLocation");
                String scaleLocationId = flightJson.optString("scaleLocation", null); // Optional
                LocalDateTime departureDate = LocalDateTime.parse(flightJson.getString("departureDate"));
                int hoursDurationArrival = flightJson.getInt("hoursDurationArrival");
                int minutesDurationArrival = flightJson.getInt("minutesDurationArrival");

                
                Plane plane = findPlaneById(planeId);
                Location departureLocation = findLocationById(departureLocationId);
                Location arrivalLocation = findLocationById(arrivalLocationId);

                if (plane == null || departureLocation == null || arrivalLocation == null) {
                    System.err.println("Skipping flight " + id + " due to missing plane or location references.");
                    continue;
                }

                if (scaleLocationId != null && !scaleLocationId.isEmpty()) {
                    Location scaleLocation = findLocationById(scaleLocationId);
                    if (scaleLocation != null) {
                        int hoursDurationScale = flightJson.getInt("hoursDurationScale");
                        int minutesDurationScale = flightJson.getInt("minutesDurationScale");
                        flights.add(new Flight(id, plane, departureLocation, scaleLocation, arrivalLocation, departureDate, hoursDurationArrival, minutesDurationArrival, hoursDurationScale, minutesDurationScale));
                    } else {
                         System.err.println("Skipping flight " + id + " due to missing scale location reference: " + scaleLocationId);
                         // Fallback to flight without scale or skip, depending on desired behavior
                         // For now, creating without scale if scaleLocation is invalid but specified
                         flights.add(new Flight(id, plane, departureLocation, arrivalLocation, departureDate, hoursDurationArrival, minutesDurationArrival));
                    }
                } else {
                    flights.add(new Flight(id, plane, departureLocation, arrivalLocation, departureDate, hoursDurationArrival, minutesDurationArrival));
                }
            }
        } catch (JSONException e) {
            System.err.println("Error parsing flights.json: " + e.getMessage());
        }
    }
}