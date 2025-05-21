/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controllers;

import airport.models.Flight;
import airport.models.Location;
import airport.models.Passenger;
import airport.models.Plane;
import airport.controllers.utils.Response;
import airport.controllers.utils.Status;
import airport.models.DataRepository;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class FlightController {

    private DataRepository dataRepository;

    public FlightController() {
        this.dataRepository = new DataRepository();
    }

    public Response createFlight(String id, String planeId, String departureLocationId, String arrivalLocationId,
                                 String scaleLocationId, int departureYear, int departureMonth, int departureDay,
                                 int departureHour, int departureMinute, int durationArrivalHours,
                                 int durationArrivalMinutes, int durationScaleHours, int durationScaleMinutes) {

        // Validate ID format
        if (id == null || !id.matches("[A-Z]{3}[0-9]{3}")) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Flight ID must follow the format XXXYYY (3 uppercase letters, 3 digits).", null);
        }
        // Validate ID uniqueness
        if (dataRepository.findFlightById(id) != null) {
            return new Response(Status.BAD_REQUEST, "Conflict: Flight ID already exists.", null);
        }

        Plane plane = dataRepository.findPlaneById(planeId);
        if (plane == null) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Plane not found.", null);
        }

        Location departureLocation = dataRepository.findLocationById(departureLocationId);
        if (departureLocation == null) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Departure location not found.", null);
        }

        Location arrivalLocation = dataRepository.findLocationById(arrivalLocationId);
        if (arrivalLocation == null) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Arrival location not found.", null);
        }

        if (departureLocationId.equals(arrivalLocationId)) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Departure and arrival locations cannot be the same.", null);
        }

        Location scaleLocation = null;
        if (scaleLocationId != null && !scaleLocationId.trim().isEmpty()) {
            scaleLocation = dataRepository.findLocationById(scaleLocationId);
            if (scaleLocation == null) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale location not found.", null);
            }
            if (scaleLocationId.equals(departureLocationId) || scaleLocationId.equals(arrivalLocationId)) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale location cannot be the same as departure or arrival.", null);
            }
            if (durationScaleHours <= 0 && durationScaleMinutes <= 0) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale duration must be > 00:00 if scale location is provided.", null);
            }
        } else {
            if (durationScaleHours != 0 || durationScaleMinutes != 0) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale duration must be 00:00 if no scale location.", null);
            }
        }

        LocalDateTime departureDateTime;
        try {
            departureDateTime = LocalDateTime.of(departureYear, departureMonth, departureDay, departureHour, departureMinute);
        } catch (DateTimeException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid departure date/time.", null);
        }

        if (durationArrivalHours <= 0 && durationArrivalMinutes <= 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Flight duration must be > 00:00.", null);
        }

        Flight newFlight;
        if (scaleLocation != null) {
            newFlight = new Flight(id, plane, departureLocation, scaleLocation, arrivalLocation, departureDateTime,
                                   durationArrivalHours, durationArrivalMinutes, durationScaleHours, durationScaleMinutes);
        } else {
            newFlight = new Flight(id, plane, departureLocation, arrivalLocation, departureDateTime,
                                   durationArrivalHours, durationArrivalMinutes);
        }
        dataRepository.addFlight(newFlight);

        try {
            return new Response(Status.CREATED, "Flight created successfully.", newFlight.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning flight: " + e.getMessage());
            return new Response(Status.CREATED, "Flight created successfully (cloning failed).", newFlight);
        }
    }

    public Response delayFlight(String flightId, int delayHours, int delayMinutes) {
        Flight flight = dataRepository.findFlightById(flightId);
        if (flight == null) {
            return new Response(Status.NOT_FOUND, "Flight not found.", null);
        }
        if (delayHours <= 0 && delayMinutes <= 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Delay time must be > 00:00.", null);
        }
        flight.delay(delayHours, delayMinutes);
        try {
            return new Response(Status.OK, "Flight delayed successfully.", flight.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning flight: " + e.getMessage());
            return new Response(Status.OK, "Flight delayed successfully (cloning failed).", flight);
        }
    }

    public Response getAllFlightsOrderedByDepartureDate() {
        ArrayList<Flight> flights = dataRepository.getAllFlights();
        flights.sort(Comparator.comparing(Flight::getDepartureDate));
        ArrayList<Flight> clonedFlights = new ArrayList<>();
        for (Flight f : flights) {
            try {
                clonedFlights.add((Flight) f.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning flight in getAllFlightsOrderedByDepartureDate: " + e.getMessage());
                clonedFlights.add(f); // Add original if cloning fails
            }
        }
        return new Response(Status.OK, "Flights retrieved successfully.", clonedFlights);
    }

    public Response getFlightsForPassengerOrderedByDepartureDate(long passengerId) {
        Passenger passenger = dataRepository.findPassengerById(passengerId);
        if (passenger == null) {
            return new Response(Status.NOT_FOUND, "Passenger not found.", null);
        }
        ArrayList<Flight> passengerFlights = passenger.getFlights();
        // Create a new list for sorting to avoid modifying the passenger's original list directly
        ArrayList<Flight> flightsToSort = new ArrayList<>(passengerFlights);
        flightsToSort.sort(Comparator.comparing(Flight::getDepartureDate));
        
        ArrayList<Flight> clonedFlights = new ArrayList<>();
        for (Flight f : flightsToSort) {
            try {
                clonedFlights.add((Flight) f.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning flight in getFlightsForPassengerOrderedByDepartureDate: " + e.getMessage());
                clonedFlights.add(f); // Add original if cloning fails
            }
        }
        return new Response(Status.OK, "Passenger flights retrieved successfully.", clonedFlights);
    }
}
