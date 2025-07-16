package org.example.springjdbctemplate.controller;

import org.example.springjdbctemplate.dao.User;
import org.example.springjdbctemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userRepository.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/users/" + user.getUsername())
                .body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestParam("email") String email) {
        userRepository.updateUserEmail(id, email);
        return ResponseEntity.status(HttpStatus.OK).body("User updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userRepository.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }
}
