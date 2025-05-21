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

    public Response registerPassenger(long id, String firstname, String lastname,
                                      int birthYear, int birthMonth, int birthDay,
                                      String phoneCodeStr, String phoneStr, String country) {
        // Validate ID
        if (id < 0) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Passenger ID must be non-negative.", null);
        }
        if (String.valueOf(id).length() > 15) {
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
        try {
            birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        } catch (DateTimeException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid birth date.", null);
        }

        // Validate phoneCode
        int phoneCode;
        try {
            phoneCode = Integer.parseInt(phoneCodeStr);
            if (phoneCode < 0 || String.valueOf(phoneCode).length() > 3) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone code.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone code format.", null);
        }

        // Validate phone
        long phone;
        try {
            phone = Long.parseLong(phoneStr);
            if (phone < 0 || String.valueOf(phone).length() > 11) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone number.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone number format.", null);
        }

        // Create and add passenger
        Passenger newPassenger = new Passenger(id, firstname, lastname, birthDate, phoneCode, phone, country);
        dataRepository.addPassenger(newPassenger);

        try {
            return new Response(Status.CREATED, "Passenger registered successfully.", newPassenger.clone());
        } catch (CloneNotSupportedException e) {
            // This should ideally not happen if Passenger implements Cloneable correctly
            System.err.println("Error cloning passenger: " + e.getMessage());
            // Return the original object if cloning fails, though this is not ideal
            return new Response(Status.CREATED, "Passenger registered successfully (cloning failed).", newPassenger);
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
                // This should ideally not happen
                System.err.println("Error cloning passenger in getAllPassengersOrderedById: " + e.getMessage());
                // Add original object if cloning fails, or handle error differently
                clonedPassengers.add(p); 
            }
        }
        return new Response(Status.OK, "Passengers retrieved successfully.", clonedPassengers);
    }

    public Response updatePassengerInfo(long id, String firstname, String lastname,
                                        int birthYear, int birthMonth, int birthDay,
                                        String phoneCodeStr, String phoneStr, String country) {
        Passenger passenger = dataRepository.findPassengerById(id);
        if (passenger == null) {
            return new Response(Status.NOT_FOUND, "Passenger with ID " + id + " not found.", null);
        }

        // Validate names and country
        if (firstname == null || firstname.trim().isEmpty() ||
            lastname == null || lastname.trim().isEmpty() ||
            country == null || country.trim().isEmpty()) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Firstname, lastname, and country cannot be empty.", null);
        }

        // Validate and create birthDate
        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        } catch (DateTimeException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid birth date.", null);
        }

        // Validate phoneCode
        int phoneCode;
        try {
            phoneCode = Integer.parseInt(phoneCodeStr);
            if (phoneCode < 0 || String.valueOf(phoneCode).length() > 3) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone code.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone code format.", null);
        }

        // Validate phone
        long phone;
        try {
            phone = Long.parseLong(phoneStr);
            if (phone < 0 || String.valueOf(phone).length() > 11) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone number.", null);
            }
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid phone number format.", null);
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
            return new Response(Status.OK, "Passenger updated successfully (cloning failed).", passenger);
        }
    }

    public Response addPassengerToFlight(long passengerId, String flightId) {
        Passenger passenger = dataRepository.findPassengerById(passengerId);
        if (passenger == null) {
            return new Response(Status.NOT_FOUND, "Passenger not found.", null);
        }

        Flight flight = dataRepository.findFlightById(flightId);
        if (flight == null) {
            return new Response(Status.NOT_FOUND, "Flight not found.", null);
        }

        passenger.addFlight(flight);
        flight.addPassenger(passenger);

        return new Response(Status.OK, "Passenger added to flight successfully.", null);
    }

    public Response getPassengerById(long passengerId) {
        Passenger passenger = dataRepository.findPassengerById(passengerId);
        if (passenger != null) {
            try {
                return new Response(Status.OK, "Passenger found.", passenger.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning passenger: " + e.getMessage());
                // Return original object if cloning fails, or a specific error response
                return new Response(Status.INTERNAL_SERVER_ERROR, "Error cloning passenger: " + e.getMessage(), passenger); 
            }
        } else {
            return new Response(Status.NOT_FOUND, "Passenger not found.", null);
        }
    }
}

