/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controllers;

import airport.controllers.utils.Response;
import airport.controllers.utils.Status;
import airport.models.DataRepository;
import airport.models.Plane;
import java.util.ArrayList;
import java.util.Comparator;

public class PlaneController {

    private DataRepository dataRepository;

    public PlaneController() {
        this.dataRepository = new DataRepository();
    }

    public Response createPlane(String id, String brand, String model, String maxCapacityStr, String airline) {
        // Validate ID format
        if (id == null || !id.matches("[A-Z]{2}[0-9]{5}")) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Plane ID must follow the format XXYYYYY (2 uppercase letters, 5 digits).", null);
        }
        // Validate ID uniqueness
        if (dataRepository.findPlaneById(id) != null) {
            return new Response(Status.CONFLICT, "Conflict: Plane ID already exists.", null);
        }

        // Validate brand, model, airline
        if (brand == null || brand.trim().isEmpty() ||
            model == null || model.trim().isEmpty() ||
            airline == null || airline.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Brand, model, and airline cannot be empty.", null);
        }

        // Validate maxCapacity
        int maxCapacity;
        try {
            maxCapacity = Integer.parseInt(maxCapacityStr);
            if (maxCapacity <= 0) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid max capacity.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid max capacity format.", null);
        }

        // Create and add plane
        Plane newPlane = new Plane(id, brand, model, maxCapacity, airline);
        dataRepository.addPlane(newPlane);

        try {
            return new Response(Status.CREATED, "Plane created successfully.", newPlane.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning plane: " + e.getMessage());
            return new Response(Status.CREATED, "Plane created successfully (cloning failed).", newPlane);
        }
    }

    public Response getAllPlanesOrderedById() {
        ArrayList<Plane> planes = dataRepository.getAllPlanes();
        planes.sort(Comparator.comparing(Plane::getId));

        ArrayList<Plane> clonedPlanes = new ArrayList<>();
        for (Plane p : planes) {
            try {
                clonedPlanes.add((Plane) p.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning plane in getAllPlanesOrderedById: " + e.getMessage());
                clonedPlanes.add(p); // Add original if cloning fails
            }
        }
        return new Response(Status.OK, "Planes retrieved successfully.", clonedPlanes);
    }
}

