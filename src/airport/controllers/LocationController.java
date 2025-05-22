/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controllers;

import airport.models.Location;
import airport.controllers.utils.Response;
import airport.controllers.utils.Status;
import airport.models.DataRepository;
import java.util.ArrayList;
import java.util.Comparator;

public class LocationController {

    private DataRepository dataRepository;

    public LocationController() {
        this.dataRepository = new DataRepository();
    }

    private boolean hasMaxFourDecimalPlaces(String numberStr) {
        if (numberStr == null) {
            return false;
        }
        if (!numberStr.contains(".")) {
            return true; 
        }
        String[] parts = numberStr.split("\\.");
        return parts.length == 2 && parts[1].length() <= 4;
    }

    public Response createLocation(String id, String name, String city, String country, 
                                   String latitudeStr, String longitudeStr) {
        // Validate ID
        if (id == null) { // Must not be null
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must be 3 characters long.", null);
        }
        if (id.length() != 3) { // Must be strictly 3 characters long
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must be 3 characters long.", null);
        }
        if (!id.matches("[A-Z]{3}")) { // Must consist of 3 uppercase letters
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must consist of 3 uppercase letters.", null);
        }
        if (dataRepository.findLocationById(id) != null) { // Must be unique
            return new Response(Status.CONFLICT, "Conflict: Location ID already exists.", null);
        }

        // Validate name, city, country
        if (name == null || name.trim().isEmpty() ||
            city == null || city.trim().isEmpty() ||
            country == null || country.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Name, city, and country cannot be empty.", null);
        }

        // Validate latitude
        double latitude;
         if (latitudeStr == null || latitudeStr.trim().isEmpty()) { // Check for null or empty before parsing
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid latitude format.", null);
        }
        try {
            latitude = Double.parseDouble(latitudeStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid latitude format.", null);
        }
        if (latitude < -90 || latitude > 90) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Latitude must be between -90 and 90.", null);
        }
        if (!hasMaxFourDecimalPlaces(latitudeStr)) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Latitude must have at most 4 decimal places.", null);
        }

        // Validate longitude
        double longitude;
        if (longitudeStr == null || longitudeStr.trim().isEmpty()) { // Check for null or empty before parsing
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid longitude format.", null);
        }
        try {
            longitude = Double.parseDouble(longitudeStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid longitude format.", null);
        }
        if (longitude < -180 || longitude > 180) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Longitude must be between -180 and 180.", null);
        }
        if (!hasMaxFourDecimalPlaces(longitudeStr)) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Longitude must have at most 4 decimal places.", null);
        }

        // Create and add location
        Location newLocation = new Location(id, name, city, country, latitude, longitude);
        dataRepository.addLocation(newLocation);

        try {
            return new Response(Status.CREATED, "Location created successfully.", newLocation.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning location: " + e.getMessage());
            // Spec: "If cloning fails, return Status.INTERNAL_SERVER_ERROR with the original (uncloned) Location object as data and an error message indicating the cloning failure."
            return new Response(Status.INTERNAL_SERVER_ERROR, "Location created but failed to clone: " + e.getMessage(), newLocation);
        }
    }

    public Response getAllLocationsOrderedById() {
        ArrayList<Location> locations = dataRepository.getAllLocations();
        locations.sort(Comparator.comparing(Location::getAirportId));

        ArrayList<Location> clonedLocations = new ArrayList<>();
        for (Location loc : locations) {
            try {
                clonedLocations.add((Location) loc.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning location in getAllLocationsOrderedById: " + e.getMessage() + " for location ID: " + loc.getAirportId());
                clonedLocations.add(loc); // Add original if cloning fails, as per current logic and interpretation for list methods.
            }
        }
        return new Response(Status.OK, "Locations retrieved successfully.", clonedLocations); // Matches spec
    }
}