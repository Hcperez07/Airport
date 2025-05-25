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
        this.dataRepository = DataRepository.getInstance();
    }

    public Response createPlane(String id, String brand, String model, String maxCapacityStr, String airline) {
        // Validamos ID
        if (id == null || id.trim().isEmpty()) { 
            return new Response(Status.BAD_REQUEST, "Bad Request: Plane ID must follow the format XXYYYYY (2 uppercase letters, 5 digits).", null);
        }
        if (!id.matches("[A-Z]{2}[0-9]{5}")) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Plane ID must follow the format XXYYYYY (2 uppercase letters, 5 digits).", null);
        }
        // Valido que el ID se unico
        if (dataRepository.findPlaneById(id) != null) {
            return new Response(Status.CONFLICT, "Conflict: Plane ID already exists.", null);
        }

        // Validamos marca, modelo y aero linea
        if (brand == null || brand.trim().isEmpty() ||
            model == null || model.trim().isEmpty() ||
            airline == null || airline.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Brand, model, and airline cannot be empty.", null);
        }

        // Validamos la capacidad maxima
        int maxCapacity;
        if (maxCapacityStr == null || maxCapacityStr.trim().isEmpty()) { 
             return new Response(Status.BAD_REQUEST, "Bad Request: Invalid max capacity format.", null);
        }
        try {
            maxCapacity = Integer.parseInt(maxCapacityStr);
            if (maxCapacity <= 0) {

                return new Response(Status.BAD_REQUEST, "Bad Request: Max capacity must be greater than 0.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid max capacity format.", null);
        }

        // Creamos y añadimos un avion
        Plane newPlane = new Plane(id, brand, model, maxCapacity, airline);
        dataRepository.addPlane(newPlane);

        try {
            return new Response(Status.CREATED, "Plane created successfully.", newPlane.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning plane: " + e.getMessage());
            return new Response(Status.INTERNAL_SERVER_ERROR, "Plane created but failed to clone: " + e.getMessage(), newPlane);
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
                System.err.println("Error cloning plane in getAllPlanesOrderedById: " + e.getMessage() + " for plane ID: " + p.getId());
                clonedPlanes.add(p); 
            }
        }
        return new Response(Status.OK, "Planes retrieved successfully.", clonedPlanes); 
    }
}