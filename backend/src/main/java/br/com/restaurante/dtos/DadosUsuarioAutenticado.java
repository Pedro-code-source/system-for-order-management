package br.com.restaurante.dtos;

public record DadosUsuarioAutenticado(
        Long id,
        String nome,
        String email,
        String role
) {}
