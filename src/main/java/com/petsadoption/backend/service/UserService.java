package com.petsadoption.backend.service;

import com.petsadoption.backend.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de usuarios mínimo para soportar autenticación basada en Supabase.
 */
public interface UserService {

    Optional<User> findById(UUID id);

    User save(User user);
}
