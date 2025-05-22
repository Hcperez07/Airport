/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controllers;

import airport.models.Flight;
import airport.models.Passenger;
import airport.controllers.utils.Response;
import airport.controllers.utils.Status;
import airport.models.DataRepository;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Comparator;

public class PassengerController {

    private DataRepository dataRepository;

    public PassengerController() {
        this.dataRepository = new DataRepository();
    }

    public Response registerPassenger(String idStr, String firstname, String lastname,
                                      String birthYearStr, String birthMonthStr, String birthDayStr,
                                      String phoneCodeStr, String phoneStr, String country) {
        // Validate ID
        long id;
        if (idStr == null || idStr.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Passenger ID cannot be empty.", null);
        }
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid Passenger ID format.", null); // Matches spec
        }
        if (id < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Passenger ID must be non-negative.", null);
        }
        if (idStr.length() > 15) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Passenger ID must have at most 15 digits.", null);
        }
        if (dataRepository.findPassengerById(id) != null) {
            return new Response(Status.CONFLICT, "Conflict: Passenger ID already exists.", null);
        }

        // Validate names and country
        if (firstname == null || firstname.trim().isEmpty() ||
            lastname == null || lastname.trim().isEmpty() ||
            country == null || country.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Firstname, lastname, and country cannot be empty.", null);
        }

        // Validate and create birthDate
        LocalDate birthDate;
        int birthYear, birthMonth, birthDay;
        try {
            if (birthYearStr == null || birthYearStr.trim().isEmpty() ||
                birthMonthStr == null || birthMonthStr.trim().isEmpty() || "Month".equals(birthMonthStr) ||
                birthDayStr == null || birthDayStr.trim().isEmpty() || "Day".equals(birthDayStr) ) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Birth year, month, and day must be specified.", null);
            }
            birthYear = Integer.parseInt(birthYearStr);
            birthMonth = Integer.parseInt(birthMonthStr);
            birthDay = Integer.parseInt(birthDayStr);
            birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        } catch (DateTimeException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid birth date.", null);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid birth date format.", null); // Matches spec
        }

        // Validate phoneCode
        int phoneCode;
        if (phoneCodeStr == null || phoneCodeStr.trim().isEmpty()) {
             return new Response(Status.BAD_REQUEST, "Bad Request: Phone code cannot be empty.", null);
        }
        try {
            phoneCode = Integer.parseInt(phoneCodeStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone code format.", null);
        }
        if (phoneCode < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone code must be non-negative.", null);
        }
        if (phoneCodeStr.length() > 3) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone code must have at most 3 digits.", null);
        }
        
        // Validate phone
        long phone;
        if (phoneStr == null || phoneStr.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone number cannot be empty.", null);
        }
        try {
            phone = Long.parseLong(phoneStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone number format.", null);
        }
        if (phone < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone number must be non-negative.", null);
        }
        if (phoneStr.length() > 11) {
             return new Response(Status.BAD_REQUEST, "Bad Request: Phone number must have at most 11 digits.", null);
        }

        // Create and add passenger
        Passenger newPassenger = new Passenger(id, firstname, lastname, birthDate, phoneCode, phone, country);
        dataRepository.addPassenger(newPassenger);

        try {
            return new Response(Status.CREATED, "Passenger registered successfully.", newPassenger.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning passenger: " + e.getMessage());
            // Spec: "the Passenger object returned in Response.data must be a clone ... if CloneNotSupportedException occurs, a Response with Status.INTERNAL_SERVER_ERROR and the message "Passenger registered but failed to clone: " + e.getMessage()" is returned, with the original (uncloned) passenger object as data"
            return new Response(Status.INTERNAL_SERVER_ERROR, "Passenger registered but failed to clone: " + e.getMessage(), newPassenger);
        }
    }

    public Response getAllPassengersOrderedById() {
        ArrayList<Passenger> passengers = dataRepository.getAllPassengers();
        passengers.sort(Comparator.comparingLong(Passenger::getId));

        ArrayList<Passenger> clonedPassengers = new ArrayList<>();
        for (Passenger p : passengers) {
            try {
                clonedPassengers.add((Passenger) p.clone());
            } catch (CloneNotSupportedException e) {
                 System.err.println("Error cloning passenger in getAllPassengersOrderedById: " + e.getMessage() + " for passenger ID: " + p.getId());
                 // As per spec, method should return successfully retrieved passengers (cloned if possible).
                 // If a single clone fails, we can log and add the original, or skip, or return an error for the whole operation.
                 // Given the context, logging and adding the original (or a marker/null) might be acceptable if the goal is to show as much data as possible.
                 // However, for consistency with other methods that demand cloning, if any clone fails, it might be better to signal an issue.
                 // For this exercise, I'll stick to the provided logic which adds the original on failure and logs.
                 // A more robust solution might involve a partial success or a specific error status.
                 clonedPassengers.add(p); 
            }
        }
        return new Response(Status.OK, "Passengers retrieved successfully.", clonedPassengers);
    }

    public Response updatePassengerInfo(long passengerId, String firstname, String lastname,
                                        String birthYearStr, String birthMonthStr, String birthDayStr,
                                        String phoneCodeStr, String phoneStr, String country) {
        Passenger passenger = dataRepository.findPassengerById(passengerId);
        if (passenger == null) {
            return new Response(Status.NOT_FOUND, "Passenger with ID " + passengerId + " not found.", null);
        }
        
        // ID specific validations (non-negative, length) are for the passengerId parameter.
        if (passengerId < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Passenger ID must be non-negative.", null);
        }
        if (String.valueOf(passengerId).length() > 15) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Passenger ID must have at most 15 digits.", null);
        }

        // Validate names and country
        if (firstname == null || firstname.trim().isEmpty() ||
            lastname == null || lastname.trim().isEmpty() ||
            country == null || country.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Firstname, lastname, and country cannot be empty.", null);
        }

        // Validate and create birthDate
        LocalDate birthDate;
        int birthYear, birthMonth, birthDay;
        try {
             if (birthYearStr == null || birthYearStr.trim().isEmpty() ||
                birthMonthStr == null || birthMonthStr.trim().isEmpty() || "Month".equals(birthMonthStr) ||
                birthDayStr == null || birthDayStr.trim().isEmpty() || "Day".equals(birthDayStr) ) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Birth year, month, and day must be specified.", null);
            }
            birthYear = Integer.parseInt(birthYearStr);
            birthMonth = Integer.parseInt(birthMonthStr);
            birthDay = Integer.parseInt(birthDayStr);
            birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        } catch (DateTimeException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid birth date.", null);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid birth date format.", null); // Matches spec
        }

        // Validate phoneCode
        int phoneCode;
        if (phoneCodeStr == null || phoneCodeStr.trim().isEmpty()) {
             return new Response(Status.BAD_REQUEST, "Bad Request: Phone code cannot be empty.", null);
        }
        try {
            phoneCode = Integer.parseInt(phoneCodeStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone code format.", null);
        }
        if (phoneCode < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone code must be non-negative.", null);
        }
        if (phoneCodeStr.length() > 3) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone code must have at most 3 digits.", null);
        }

        // Validate phone
        long phone;
         if (phoneStr == null || phoneStr.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone number cannot be empty.", null);
        }
        try {
            phone = Long.parseLong(phoneStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone number format.", null);
        }
        if (phone < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Phone number must be non-negative.", null);
        }
        if (phoneStr.length() > 11) {
             return new Response(Status.BAD_REQUEST, "Bad Request: Phone number must have at most 11 digits.", null);
        }

        // Update passenger
        passenger.setFirstname(firstname);
        passenger.setLastname(lastname);
        passenger.setBirthDate(birthDate);
        passenger.setCountryPhoneCode(phoneCode);
        passenger.setPhone(phone);
        passenger.setCountry(country);

        try {
            return new Response(Status.OK, "Passenger updated successfully.", passenger.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning passenger: " + e.getMessage());
            return new Response(Status.INTERNAL_SERVER_ERROR, "Passenger updated but failed to clone: " + e.getMessage(), passenger);
        }
    }

    public Response addPassengerToFlight(long passengerId, String flightId) {
        Passenger passenger = dataRepository.findPassengerById(passengerId);
        if (passenger == null) {
            return new Response(Status.NOT_FOUND, "Passenger not found.", null); // Matches spec
        }

        Flight flight = dataRepository.findFlightById(flightId);
        if (flight == null) {
            return new Response(Status.NOT_FOUND, "Flight not found.", null); // Matches spec
        }
        
        passenger.addFlight(flight);
        flight.addPassenger(passenger);

        return new Response(Status.OK, "Passenger added to flight successfully.", null); // Matches spec
    }

    public Response getPassengerById(long passengerId) {
        Passenger passenger = dataRepository.findPassengerById(passengerId);
        if (passenger != null) {
            try {
                return new Response(Status.OK, "Passenger found.", passenger.clone()); // Matches spec
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning passenger with ID " + passengerId + ": " + e.getMessage());
                return new Response(Status.INTERNAL_SERVER_ERROR, "Error cloning passenger: " + e.getMessage(), null); // Matches spec (data is null)
            }
        } else {
            return new Response(Status.NOT_FOUND, "Passenger not found.", null); // Matches spec
        }
    }
}

