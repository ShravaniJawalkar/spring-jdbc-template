package org.example.springjdbctemplate.service;

import org.example.springjdbctemplate.dao.User;
import org.example.springjdbctemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<User> createUser(User user) {
        userRepository.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/users/" + user.getUsername())
                .body(user);
    }

    public ResponseEntity<String> updateUser(int id, String email) {
        userRepository.updateUserEmail(id, email);
        return ResponseEntity.status(HttpStatus.OK).body("User updated");
    }

    public ResponseEntity<String> deleteUser(int id) {
        userRepository.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }

    public ResponseEntity<User> getUser(int id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }
}
