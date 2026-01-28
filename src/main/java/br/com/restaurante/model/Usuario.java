package br.com.restaurante.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @NotBlank(message = "O e-mail é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @NotBlank(message = "A senha é obrigatória")
    @Column(nullable = false)
    private String senha;

    protected Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}