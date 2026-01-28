package br.com.restaurante.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table (name = "administrador")
public class Administrador extends Usuario {

    public Administrador(String email, String senha) {
        super(email, senha);
    }
}
