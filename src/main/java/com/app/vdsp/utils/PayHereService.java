package com.app.vdsp.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PayHereService {

    private final Dotenv dotenv;

    public PayHereService() {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    public String merchantId(){
        return dotenv.get("PAYHERE.MERCHANT_ID");
    }

    public String merchantSecret(){
        return dotenv.get("payhere.merchant_secret");
    }

    public String getSandboxUrl() {
        return dotenv.get("PAYHERE.SANDBOX_URL");
    }

    public String getNotifyUrl() {
        return dotenv.get("PAYHERE.NOTIFY_URL");
    }

    public String generateHash(String orderId, double amount, String currency) {
        String merchantId = dotenv.get("PAYHERE.MERCHANT_ID");
        String merchantSecret = dotenv.get("PAYHERE.SECRET_KEY");

        if (merchantId == null || merchantSecret == null) {
            throw new IllegalArgumentException("Merchant ID or Secret Key not found in environment variables");
        }

        String amountFormatted = String.format("%.2f", amount);

        return getMd5(merchantId + orderId + amountFormatted + currency + getMd5(merchantSecret));
    }

    private String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating MD5 hash: " + e.getMessage(), e);
        }
    }

    public boolean verifyPaymentStatus(String merchantId, String orderId, double amount, String currency, int statusCode, String receivedMd5Sig) {
        if (merchantId == null || orderId == null || currency == null || receivedMd5Sig == null) {
            throw new IllegalArgumentException("One or more required parameters are null");
        }

        String merchantSecret = dotenv.get("PAYHERE.SECRET_KEY");

        if (merchantSecret == null) {
            throw new IllegalArgumentException("Merchant Secret Key not found in environment variables");
        }

        String amountFormatted = String.format("%.2f", amount);

        String localMd5Sig = getMd5(
                merchantId +
                        orderId +
                        amountFormatted +
                        currency +
                        statusCode +
                        getMd5(merchantSecret).toUpperCase()
        ).toUpperCase();

        return localMd5Sig.equals(receivedMd5Sig);
    }

}
