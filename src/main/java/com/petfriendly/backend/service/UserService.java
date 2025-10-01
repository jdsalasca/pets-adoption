package com.petfriendly.backend.service;

import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for User entity operations.
 */
public interface UserService {

    User createUser(User user);

    User updateUser(Long id, User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    List<User> findByRole(Role role);

    Page<User> findByRole(Role role, Pageable pageable);

    List<User> findActiveUsers();

    Page<User> findActiveUsers(Pageable pageable);

    List<User> findByNameContaining(String name);

    Page<User> findByNameContaining(String name, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    long count();

    long countActiveUsers();

    long countByRole(Role role);

    void deleteById(Long id);

    User activateUser(Long id);

    User deactivateUser(Long id);

    User changePassword(Long id, String newPassword);

    User updateProfile(Long id, String firstName, String lastName, String phone);

    UserStatistics getStatistics();

    /**
     * Projection object returned by {@link #getStatistics()}.
     */
    final class UserStatistics {
        private final long totalUsers;
        private final long activeUsers;
        private final long visitors;
        private final long standardUsers;
        private final long foundationAdmins;
        private final long superAdmins;

        public UserStatistics(long totalUsers,
                              long activeUsers,
                              long visitors,
                              long standardUsers,
                              long foundationAdmins,
                              long superAdmins) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.visitors = visitors;
            this.standardUsers = standardUsers;
            this.foundationAdmins = foundationAdmins;
            this.superAdmins = superAdmins;
        }

        public long getTotalUsers() {
            return totalUsers;
        }

        public long getActiveUsers() {
            return activeUsers;
        }

        public long getVisitors() {
            return visitors;
        }

        public long getStandardUsers() {
            return standardUsers;
        }

        public long getFoundationAdmins() {
            return foundationAdmins;
        }

        public long getSuperAdmins() {
            return superAdmins;
        }
    }
}