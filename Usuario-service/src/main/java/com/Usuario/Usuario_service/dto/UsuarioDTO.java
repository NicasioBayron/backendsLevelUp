package com.Usuario.Usuario_service.dto;

import com.Usuario.Usuario_service.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsuarioDTO {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "El correo es requerido")
        @Email(message = "Correo no válido")
        private String correo;

        @NotBlank(message = "La contraseña es requerida")
        private String password;
    }

    @Data
    public static class RegistroRequest {
        @NotBlank(message = "El nombre es requerido")
        private String nombre;

        @NotBlank(message = "El correo es requerido")
        @Email(message = "Correo no válido")
        private String correo;

        @NotBlank(message = "La contraseña es requerida")
        private String password;

        private String rol; // USER o ADMIN
        private String telefono;
        private String direccion;
        private String comuna;
    }

    @Data
    public static class UpdateRequest {
        private String nombre;
        private String password;
        private String rol;
        private String telefono;
        private String direccion;
        private String comuna;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthResponse {
        private String token;
        private String tokenType;
        private Long id;
        private String nombre;
        private String correo;
        private String rol;
        private String telefono;
        private String direccion;
        private String comuna;

        public static AuthResponse fromUsuario(Usuario u, String token) {
            return AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .id(u.getId())
                    .nombre(u.getNombre())
                    .correo(u.getCorreo())
                    .rol(u.getRol())
                    .telefono(u.getTelefono())
                    .direccion(u.getDireccion())
                    .comuna(u.getComuna())
                    .build();
        }
    }
}
