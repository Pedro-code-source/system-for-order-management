package br.com.restaurante.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
public class Administrador extends Usuario {

    public Administrador(String email, String senha) {
        super(email, senha);
    }
}
