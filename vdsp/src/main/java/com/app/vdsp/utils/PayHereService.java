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
        return dotenv.get("payhere.merchant_id");
    }

    public String getSandboxUrl() {
        return dotenv.get("payhere.sandbox_url");
    }

//    public String getReturnUrl() {
//        return dotenv.get("payhere.return_url");
//    }
//
//    public String getCancelUrl() {
//        return dotenv.get("payhere.cancel_url");
//    }

    public String getNotifyUrl() {
        return dotenv.get("payhere.notify_url");
    }

    public String generateHash(String orderId, double amount, String currency) {
        String merchantId = dotenv.get("payhere.merchant_id");
        String merchantSecret = dotenv.get("payhere.secret_key");

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
}
