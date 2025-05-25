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
        this.dataRepository = DataRepository.getInstance();
    }

    public Response createFlight(String id, String planeId, String departureLocationId, String arrivalLocationId,
                                 String scaleLocationIdComboBoxStr, // From ComboBox, might be "No Scale" or "Location"
                                 String departureYearStr, String departureMonthStr, String departureDayStr,
                                 String departureHourStr, String departureMinuteStr, String durationArrivalHoursStr,
                                 String durationArrivalMinutesStr, String durationScaleHoursStr, String durationScaleMinutesStr) {

        // Validamos el ID
        if (id == null || id.trim().isEmpty()) { 
             return new Response(Status.BAD_REQUEST, "Bad Request: Flight ID must follow the format XXXYYY (3 uppercase letters, 3 digits).", null);
        }
        if (!id.matches("[A-Z]{3}[0-9]{3}")) { //Aseguramos que el formato del ID sea correcto
            return new Response(Status.BAD_REQUEST, "Bad Request: Flight ID must follow the format XXXYYY (3 uppercase letters, 3 digits).", null);
        }
        if (dataRepository.findFlightById(id) != null) {
            return new Response(Status.CONFLICT, "Conflict: Flight ID already exists.", null);
        }

        // Validamos el avion
        Plane plane = dataRepository.findPlaneById(planeId);
        if (plane == null) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Plane with ID '"+planeId+"' not found.", null);
        }

        // Validamos la ubicacion del departamento
        Location departureLocation = dataRepository.findLocationById(departureLocationId);
        if (departureLocation == null) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Departure location with ID '"+departureLocationId+"' not found.", null);
        }

        // Validamos la ubicacion de aterrizaje
        Location arrivalLocation = dataRepository.findLocationById(arrivalLocationId);
        if (arrivalLocation == null) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Arrival location with ID '"+arrivalLocationId+"' not found.", null);
        }
        if (departureLocationId.equals(arrivalLocationId)) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Departure and arrival locations cannot be the same.", null);
        }
        
        // Parseamos la duracion del vuelo
        int durationArrivalHours, durationArrivalMinutes, durationScaleHours, durationScaleMinutes;
        try {
            if (durationArrivalHoursStr == null || "Hour".equals(durationArrivalHoursStr) || durationArrivalMinutesStr == null || "Minute".equals(durationArrivalMinutesStr)) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Flight duration hours and minutes must be specified.", null);
            }
            durationArrivalHours = Integer.parseInt(durationArrivalHoursStr);
            durationArrivalMinutes = Integer.parseInt(durationArrivalMinutesStr);

            if (durationScaleHoursStr == null || "Hour".equals(durationScaleHoursStr) || durationScaleMinutesStr == null || "Minute".equals(durationScaleMinutesStr)) {
                durationScaleHours = "Hour".equals(durationScaleHoursStr) ? 0 : Integer.parseInt(durationScaleHoursStr);
                durationScaleMinutes = "Minute".equals(durationScaleMinutesStr) ? 0 : Integer.parseInt(durationScaleMinutesStr);
            } else {
                durationScaleHours = Integer.parseInt(durationScaleHoursStr);
                durationScaleMinutes = Integer.parseInt(durationScaleMinutesStr);
            }

        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid duration format.", null);
        }

        // Validamos la duracion del vuelo
        if (durationArrivalHours < 0 || durationArrivalMinutes < 0 || (durationArrivalHours == 0 && durationArrivalMinutes == 0)) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Flight duration must be > 00:00.", null);
        }
        
        // Validanos el "scaleLocation" y su duracion
        Location scaleLocation = null;
        String actualScaleLocationId = null;

        if (scaleLocationIdComboBoxStr != null && !scaleLocationIdComboBoxStr.trim().isEmpty() &&
            !"No Scale".equals(scaleLocationIdComboBoxStr) && !"Location".equals(scaleLocationIdComboBoxStr)) {
            actualScaleLocationId = scaleLocationIdComboBoxStr;
        }

        if (actualScaleLocationId != null) {
            scaleLocation = dataRepository.findLocationById(actualScaleLocationId);
            if (scaleLocation == null) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale location with ID '"+actualScaleLocationId+"' not found.", null);
            }
            if (actualScaleLocationId.equals(departureLocationId) || actualScaleLocationId.equals(arrivalLocationId)) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale location cannot be the same as departure or arrival.", null);
            }
            if (durationScaleHours < 0 || durationScaleMinutes < 0 || (durationScaleHours == 0 && durationScaleMinutes == 0)) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Scale duration must be > 00:00 if scale location is provided.", null);
            }
        } else { // En caso  de que no haya "scaleLocation" o se seleccionó "No Scale"
            if (durationScaleHours != 0 || durationScaleMinutes != 0) {
                boolean isScaleDurationHourPlaceholder = "Hour".equals(durationScaleHoursStr);
                boolean isScaleDurationMinutePlaceholder = "Minute".equals(durationScaleMinutesStr);
                if (!((isScaleDurationHourPlaceholder || durationScaleHours == 0) && (isScaleDurationMinutePlaceholder || durationScaleMinutes == 0))) {
                    return new Response(Status.BAD_REQUEST, "Bad Request: Scale duration must be 00:00 if no scale location is provided.", null);
                }
                if(isScaleDurationHourPlaceholder) durationScaleHours = 0;
                if(isScaleDurationMinutePlaceholder) durationScaleMinutes = 0;
                 if (durationScaleHours != 0 || durationScaleMinutes != 0) { 
                    return new Response(Status.BAD_REQUEST, "Bad Request: Scale duration must be 00:00 if no scale location is provided.", null);
                }
            }
        }

        // Validamos "departureDateTime"
        LocalDateTime departureDateTime;
        int departureYear, departureMonth, departureDay, departureHour, departureMinute;
        try {
            if (departureYearStr == null || departureYearStr.trim().isEmpty() ||
                departureMonthStr == null || "Month".equals(departureMonthStr) || departureMonthStr.trim().isEmpty() ||
                departureDayStr == null || "Day".equals(departureDayStr) || departureDayStr.trim().isEmpty() ||
                departureHourStr == null || "Hour".equals(departureHourStr) || departureHourStr.trim().isEmpty() ||
                departureMinuteStr == null || "Minute".equals(departureMinuteStr) || departureMinuteStr.trim().isEmpty()) {
                return new Response(Status.BAD_REQUEST, "Bad Request: Invalid departure date/time.", null);
            }
            departureYear = Integer.parseInt(departureYearStr);
            departureMonth = Integer.parseInt(departureMonthStr);
            departureDay = Integer.parseInt(departureDayStr);
            departureHour = Integer.parseInt(departureHourStr);
            departureMinute = Integer.parseInt(departureMinuteStr);
            departureDateTime = LocalDateTime.of(departureYear, departureMonth, departureDay, departureHour, departureMinute);
        } catch (DateTimeException | NumberFormatException e) { // Catch both exceptions
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid departure date/time.", null);
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
            return new Response(Status.INTERNAL_SERVER_ERROR, "Flight created but failed to clone: " + e.getMessage(), newFlight);
        }
    }

    public Response delayFlight(String flightId, String delayHoursStr, String delayMinutesStr) {
        Flight flight = dataRepository.findFlightById(flightId);
        if (flight == null) {
            return new Response(Status.NOT_FOUND, "Flight not found.", null);
        }
        
        int delayHours, delayMinutes;
        try {
            if (delayHoursStr == null || "Hour".equals(delayHoursStr) || delayMinutesStr == null || "Minute".equals(delayMinutesStr)) {
                 return new Response(Status.BAD_REQUEST, "Bad Request: Delay time must be specified.", null);
            }
            delayHours = Integer.parseInt(delayHoursStr);
            delayMinutes = Integer.parseInt(delayMinutesStr);
        } catch (NumberFormatException e) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Invalid delay time format.", null);
        }

        if (delayHours < 0 || delayMinutes < 0 || (delayHours == 0 && delayMinutes == 0)) {
            return new Response(Status.BAD_REQUEST, "Bad Request: Delay time must be > 00:00.", null);
        }
        flight.delay(delayHours, delayMinutes);
        dataRepository.notifyObservers(); 
        try {
            return new Response(Status.OK, "Flight delayed successfully.", flight.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning flight: " + e.getMessage());
            return new Response(Status.INTERNAL_SERVER_ERROR, "Flight delayed but failed to clone: " + e.getMessage(), flight);
        }
    }

    public Response getAllFlightsOrderedByDepartureDate() {
        ArrayList<Flight> flights = dataRepository.getAllFlights();
        // Ordena la lista "flights" en orden ascendente según la fecha de salida de cada vuelo, estoy metodos propios de las listas
        flights.sort(Comparator.comparing(Flight::getDepartureDate));
        ArrayList<Flight> clonedFlights = new ArrayList<>();
        //Clonamos los datos para evitar dañar los originales
        for (Flight f : flights) {
            try {
                clonedFlights.add((Flight) f.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning flight in getAllFlightsOrderedByDepartureDate: " + e.getMessage());
                clonedFlights.add(f); // Hacemos add al original en caso de que no se pueda clonar
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
        // Creamos una lista para ordenar y no afectar la original
        ArrayList<Flight> flightsToSort = new ArrayList<>(passengerFlights);
        flightsToSort.sort(Comparator.comparing(Flight::getDepartureDate));
        
        ArrayList<Flight> clonedFlights = new ArrayList<>();
        for (Flight f : flightsToSort) {
            try {
                clonedFlights.add((Flight) f.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning flight in getFlightsForPassengerOrderedByDepartureDate: " + e.getMessage());
                clonedFlights.add(f); 
            }
        }
        return new Response(Status.OK, "Passenger flights retrieved successfully.", clonedFlights);
    }
}