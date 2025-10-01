package com.petfriendly.backend.service.impl;

import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.enums.Role;
import com.petfriendly.backend.repository.UserRepository;
import com.petfriendly.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());

        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        if (user.getActive() == null) {
            user.setActive(true);
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        if (user.getActive() != null) {
            existingUser.setActive(user.getActive());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findActiveUsers(Pageable pageable) {
        return userRepository.findByActiveTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByNameContaining(String name) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByNameContaining(String name, Pageable pageable) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByRole(Role role) {
        return userRepository.countByRole(role);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User updateProfile(Long id, String firstName, String lastName, String phone) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName.trim());
        }
        if (lastName != null && !lastName.isBlank()) {
            user.setLastName(lastName.trim());
        }
        if (phone != null && !phone.isBlank()) {
            user.setPhone(phone.trim());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatistics getStatistics() {
        long total = userRepository.count();
        long active = userRepository.countByActiveTrue();
        long visitors = userRepository.countByRole(Role.VISITOR);
        long standard = userRepository.countByRole(Role.USER);
        long foundationAdmins = userRepository.countByRole(Role.FOUNDATION_ADMIN);
        long superAdmins = userRepository.countByRole(Role.SUPER_ADMIN);
        return new UserStatistics(total, active, visitors, standard, foundationAdmins, superAdmins);
    }
}