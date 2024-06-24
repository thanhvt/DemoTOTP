package com.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate(); // A4UV7SC3PNXUQND2EICPZXOIB53H2PCU
        System.out.println(secret);

        QrData data = new QrData.Builder()
                .label("thanhvt1.ho")
                .secret(secret)
                .issuer("MegaApp")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        try {
            byte[] imageData = generator.generate(data);

            String outputFile = "qr_code.png";
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(imageData);
                System.out.println("QR code saved to " + outputFile);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        // verifier.setTimePeriod(30);

        // allow codes valid for 2 time periods before/after to pass as valid
        // verifier.setAllowedTimePeriodDiscrepancy(2);
        // secret = the shared secret for the user
        // code = the code submitted by the user

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the code: ");
        String userCode;
        boolean valid;

        do {
            System.out.print("Enter the code (or 'C' to exit): ");
            userCode = scanner.nextLine();

            if ("C".equalsIgnoreCase(userCode)) {
                break;
            }

            valid = verifier.isValidCode(secret, userCode);
            System.out.println("Code verification result: " + valid);
        } while (true);
        // boolean successful = verifier.isValidCode(secret, "123");

    }
}
