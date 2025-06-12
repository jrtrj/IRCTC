package org.example;

import org.example.services.UserBookingService;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public String getGreeting() {
        return "Running Train Booking System";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize UserBookingService",e);
        }
        while(option!=7) {
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();

        }
    }
}