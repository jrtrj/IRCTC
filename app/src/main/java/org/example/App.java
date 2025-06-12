package org.example;

import org.example.entities.Ticket;
import org.example.entities.User;
import org.example.services.TrainService;
import org.example.services.UserBookingService;
import org.example.util.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class App {
    public String getGreeting() {
        return "Running Train Booking System";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService = null;
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
            switch (option) {
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.nextLine();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.nextLine();
                    User userToSignUp = new User(
                            nameToSignUp,
                            passwordToSignUp,
                            UserServiceUtil.hashedPassword(passwordToSignUp),
                            new ArrayList<Ticket>(),
                            UUID.randomUUID().toString());
                    try{
                        new UserBookingService().signUp(userToSignUp);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Enter the username to login");
                    String nameToLogin = scanner.nextLine();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.nextLine();
                    User userToLogin = new User();
                    userToLogin.setName(nameToLogin);
                    userToLogin.setPassword(passwordToLogin);
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                        if(userBookingService.loginUser()) {
                            System.out.println("Login Successful");
                        }
                        else
                            System.out.println("Invalid Credentials");
                    }
                    catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Fetching your Bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    TrainService.getTrains();
                    break;
            }
        }
    }
}