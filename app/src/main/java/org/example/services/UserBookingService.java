package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Ticket;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class UserBookingService {
    private User user;  // The current user making the booking
    private List<User> userList;  // List of all users
    private final ObjectMapper objectMapper = new ObjectMapper();  // Used to read/write JSON data
    private static final String USER_PATH = "app/src/main/java/org/example/localDB/Users.json";  // Path to the users' JSON file

    // Constructor that initializes the user and loads all users from the JSON file
    public UserBookingService(User user) throws IOException {
        this.user = user;  // Store the user who is making the booking
        loadUserListFromFile();
    }
    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException{
        File users = new File(USER_PATH);  // Point to the users' JSON file
        // TypeReference is used here to keep generic type info (List<User>) at runtime.
        // This avoids issues caused by type erasure when deserializing from JSON.
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }
    public Boolean loginUser() {
        // Look for a user with the matching userId and password
        Optional<User> foundUser = userList.stream()  // Convert userList to a stream for filtering
                .filter((existingUser) -> {  // Check each user
                    // See if both userId (case-insensitive) and password match
                    return existingUser.getName().equalsIgnoreCase(user.getName())
                            && UserServiceUtil.checkPassword(user.getPassword(), existingUser.getHashedPassword());
                })
                .findFirst();  // Stop as soon as we find the first match

        if (foundUser.isPresent()){
            this.user = foundUser.get();
            return TRUE;
        }
        else
            return FALSE;
    }

    public Boolean signUp(User user){
        try{
            userList.add(user);
            saveUserToFile();
            return TRUE;
        } catch (IOException e) {
            return FALSE;
        }
    }

    private void saveUserToFile() throws IOException{
        objectMapper.writeValue(new File(USER_PATH),userList); //serialization
    }

    public void fetchBooking() {
       user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) {
        List<Ticket> bookedTickets = user.getTicketsBooked();
        Optional<Ticket> matchingTicket = bookedTickets.stream()
                .filter(ticket -> ticket.getTicketID().equals(ticketId))
                .findFirst();
        if(matchingTicket.isPresent()) {
           bookedTickets.remove(matchingTicket.get());
           System.out.println("Ticket with ID "+ticketId+" has been canceled.");

           //Write to the db
            try {
                saveUserToFile();
            } catch (IOException e) {
                System.err.println("Failed to update database after canceling booking: " + e.getMessage());
                return FALSE;
            }
            return TRUE;
        }
        else {
            System.out.println("No ticket found with ID " + ticketId);
            return FALSE;
        }
    }


}
