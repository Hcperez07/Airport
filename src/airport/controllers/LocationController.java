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
        if (id == null || id.length() != 3) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must be 3 characters.", null);
        }
        if (!id.matches("[A-Z]{3}")) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must be 3 uppercase letters.", null);
        }
        if (dataRepository.findLocationById(id) != null) {
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
        try {
            latitude = Double.parseDouble(latitudeStr);
            if (latitude < -90 || latitude > 90 || !hasMaxFourDecimalPlaces(latitudeStr)) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid latitude.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid latitude format.", null);
        }

        // Validate longitude
        double longitude;
        try {
            longitude = Double.parseDouble(longitudeStr);
            if (longitude < -180 || longitude > 180 || !hasMaxFourDecimalPlaces(longitudeStr)) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid longitude.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid longitude format.", null);
        }

        // Create and add location
        Location newLocation = new Location(id, name, city, country, latitude, longitude);
        dataRepository.addLocation(newLocation);

        try {
            return new Response(Status.CREATED, "Location created successfully.", newLocation.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning location: " + e.getMessage());
            return new Response(Status.CREATED, "Location created successfully (cloning failed).", newLocation);
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
                System.err.println("Error cloning location in getAllLocationsOrderedById: " + e.getMessage());
                clonedLocations.add(loc); // Add original if cloning fails
            }
        }
        return new Response(Status.OK, "Locations retrieved successfully.", clonedLocations);
    }
}