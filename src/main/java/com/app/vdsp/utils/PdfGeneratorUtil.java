package com.app.vdsp.utils;

import com.app.vdsp.entity.Reservation;
import com.app.vdsp.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class PdfGeneratorUtil {

    public byte[] generateReservationConfirmationPdf(Reservation reservation) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        User user = reservation.getUser();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        document.add(new Paragraph("Reservation Confirmation Letter", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Dear " + user.getFirstName() + " " + user.getLastName() + ","));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Your payment was successful. Your reservation has been confirmed."));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Reservation Details:"));
        document.add(new Paragraph("Reservation ID: " + reservation.getId()));
        document.add(new Paragraph("Event Type: " + reservation.getEventType()));
        document.add(new Paragraph("Location: " + reservation.getEventLocation()));
        document.add(new Paragraph("Date: " + reservation.getEventDate().format(dateFormatter)));
        document.add(new Paragraph("Time: " + reservation.getEventStartTime().format(timeFormatter) + " to " + reservation.getEventEndTime().format(timeFormatter)));
        document.add(new Paragraph("Package: " + reservation.getEventPackage().getName()));
        document.add(new Paragraph("Price: " + reservation.getEventPackage().getPrice()));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Issued Date: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        document.close();
        return outputStream.toByteArray();
    }
}