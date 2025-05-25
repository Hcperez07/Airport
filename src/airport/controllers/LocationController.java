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
        this.dataRepository = DataRepository.getInstance();
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
        // Validamos ID
        if (id == null) { 
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must be 3 characters long.", null);
        }
        if (id.length() != 3) { //Verificamos que sea de 3 caracteres
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must be 3 characters long.", null);
        }
        if (!id.matches("[A-Z]{3}")) { // Verificamos formato
            return new Response(Status.BAD_REQUEST, "Bad Request: Location ID must consist of 3 uppercase letters.", null);
        }
        if (dataRepository.findLocationById(id) != null) { //  Que no haya otro iugal
            return new Response(Status.CONFLICT, "Conflict: Location ID already exists.", null);
        }

        // Validat
        if (name == null || name.trim().isEmpty() ||
            city == null || city.trim().isEmpty() ||
            country == null || country.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Name, city, and country cannot be empty.", null);
        }

        // Validamos latitud
        double latitude;
         if (latitudeStr == null || latitudeStr.trim().isEmpty()) { // Revisamos si es null o esta vacio
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

        // Validamos longitud
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

        // Creamos y añadimos ubicaion
        Location newLocation = new Location(id, name, city, country, latitude, longitude);
        dataRepository.addLocation(newLocation);

        try {
            return new Response(Status.CREATED, "Location created successfully.", newLocation.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning location: " + e.getMessage());
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
                clonedLocations.add(loc); 
            }
        }
        return new Response(Status.OK, "Locations retrieved successfully.", clonedLocations); // Matches spec
    }
}