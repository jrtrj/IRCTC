package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper = new ObjectMapper();  // Used to read/write JSON data
    private static final String USER_PATH = "app/src/main/java/org/example/localDB/Users.json";  // Path to the users' JSON file

    // Constructor that initializes the user and loads all users from the JSON file
    public UserBookingService(User user) throws IOException {
        this.user = user;  // Store the user who is making the booking
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
                    return existingUser.getUserId().equalsIgnoreCase(user.getUserId())
                            && UserServiceUtil.checkPassword(user.getPassword(), existingUser.getHashedPassword());
                })
                .findFirst();  // Stop as soon as we find the first match

        return foundUser.isPresent();  // True if user exists with correct password, false otherwise
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



}
