package com.app.vdsp.helpers;

import com.app.vdsp.entity.Reservation;
import com.app.vdsp.repository.ReservationRepository;
import com.app.vdsp.type.SessionType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SessionHelper {

    public static SessionType calculateSessionType(LocalTime startTime, LocalTime endTime) {
        long duration = java.time.Duration.between(startTime, endTime).toHours();

        if (duration >= 6) {
            return SessionType.FULLDAY_SESSION;
        } else if (startTime.isBefore(LocalTime.NOON)) {
            return SessionType.MORNING_SESSION;
        } else {
            return SessionType.EVENING_SESSION;
        }
    }

    public static void validateSessionType(SessionType sessionType, LocalDate eventDate, ReservationRepository reservationRepository) {
        List<Reservation> existingReservations = reservationRepository.findByEventDate(eventDate);

        for (Reservation reservation : existingReservations) {
            if (reservation.getSessionType() == SessionType.FULLDAY_SESSION) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "A full-day session is already reserved for this date. No further reservations are allowed.");
            }
            if (sessionType == SessionType.FULLDAY_SESSION) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Cannot create a full-day session as morning or evening sessions already exist for this date.");
            }
            if ((sessionType == SessionType.MORNING_SESSION && reservation.getSessionType() == SessionType.EVENING_SESSION) ||
                    (sessionType == SessionType.EVENING_SESSION && reservation.getSessionType() == SessionType.MORNING_SESSION)) {
                continue;
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Invalid session type. Morning and evening sessions cannot overlap or conflict.");
            }
        }
    }
}
