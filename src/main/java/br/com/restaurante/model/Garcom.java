package br.com.restaurante.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
public class Garcom extends Usuario {

    @Setter
    @NotBlank
    private String nome;

    public Garcom(String email, String senha, String nome) {
        super(email, senha);
        this.nome = nome;
    }
}
