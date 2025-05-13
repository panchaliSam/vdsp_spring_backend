package com.app.vdsp.type;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    SUCCESS(2),
    PENDING(0),
    FAILED(-2),
    CANCELED(-1),
    CHARGEBACK(-3);

    private final int statusCode;

    PaymentStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public static PaymentStatus fromStatusCode(int code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getStatusCode() == code) {
                return status;
            }
        }
        return null;
    }
}
