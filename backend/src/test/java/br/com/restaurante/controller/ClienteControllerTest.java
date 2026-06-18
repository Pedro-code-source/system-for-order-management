package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroCliente;
import br.com.restaurante.dtos.DadosCadastroEndereco;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void cadastrarClienteSucesso() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente = new Cliente(dadosCadastro);
        ReflectionTestUtils.setField(cliente, "id", 1L);
        when(clienteService.salvar(any(DadosCadastroCliente.class))).thenReturn(cliente);
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/clientes/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Fulano"))
                .andExpect(jsonPath("$.email").value("fulano@email.com"))
                .andExpect(jsonPath("$.telefone").value("999999999"));
    }

    @Test
    void cadastrarClienteErroValidacao() throws Exception {
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("", "", "", "", null);
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastrarClienteErroEmailJaCadastrado() {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        when(clienteService.salvar(any(DadosCadastroCliente.class))).thenThrow(new RuntimeException("E-mail já cadastrado."));
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(post("/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dadosCadastro)));
        });
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause() instanceof RuntimeException);
        org.junit.jupiter.api.Assertions.assertEquals("E-mail já cadastrado.", exception.getCause().getMessage());
    }

    @Test
    void listarTodosClientes() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente = new Cliente(dadosCadastro);
        ReflectionTestUtils.setField(cliente, "id", 1L);
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Fulano"))
                .andExpect(jsonPath("$[0].email").value("fulano@email.com"));
    }

    @Test
    void listarClientesFiltradosPorNome() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro1 = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente1 = new Cliente(dadosCadastro1);
        ReflectionTestUtils.setField(cliente1, "id", 1L);
        DadosCadastroCliente dadosCadastro2 = new DadosCadastroCliente("Beltrano", "beltrano@email.com", "888888888", "senha123", enderecoDto);
        Cliente cliente2 = new Cliente(dadosCadastro2);
        ReflectionTestUtils.setField(cliente2, "id", 2L);
        when(clienteService.listarTodos()).thenReturn(List.of(cliente1, cliente2));
        mockMvc.perform(get("/clientes").param("nome", "fula"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Fulano"));
    }

    @Test
    void listarClientesFiltradosPorNomeInexistente() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro1 = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente1 = new Cliente(dadosCadastro1);
        ReflectionTestUtils.setField(cliente1, "id", 1L);
        when(clienteService.listarTodos()).thenReturn(List.of(cliente1));
        mockMvc.perform(get("/clientes").param("nome", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void listarClientesFiltradosPorNomeVazio() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente = new Cliente(dadosCadastro);
        ReflectionTestUtils.setField(cliente, "id", 1L);
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        mockMvc.perform(get("/clientes").param("nome", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Fulano"));
    }

    @Test
    void buscarClientePorIdSucesso() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente = new Cliente(dadosCadastro);
        ReflectionTestUtils.setField(cliente, "id", 1L);
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Fulano"));
    }

    @Test
    void buscarClientePorIdInexistente() {
        when(clienteService.buscarPorId(99L)).thenThrow(new RuntimeException("Cliente não encontrado com o ID: 99"));
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/clientes/99"));
        });
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause() instanceof RuntimeException);
        org.junit.jupiter.api.Assertions.assertEquals("Cliente não encontrado com o ID: 99", exception.getCause().getMessage());
    }

    @Test
    void atualizarClienteSucesso() throws Exception {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano Atualizado", "fulano@email.com", "999999999", "senha123", enderecoDto);
        Cliente cliente = new Cliente(dadosCadastro);
        ReflectionTestUtils.setField(cliente, "id", 1L);
        when(clienteService.atualizar(eq(1L), any(DadosCadastroCliente.class))).thenReturn(cliente);
        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fulano Atualizado"));
    }

    @Test
    void atualizarClienteErroEmailDuplicado() {
        DadosCadastroEndereco enderecoDto = new DadosCadastroEndereco("12345-678", "Rua A", "123", "Bairro B", "Cidade C");
        DadosCadastroCliente dadosCadastro = new DadosCadastroCliente("Fulano", "fulano@email.com", "999999999", "senha123", enderecoDto);
        when(clienteService.atualizar(eq(1L), any(DadosCadastroCliente.class))).thenThrow(new RuntimeException("O e-mail já pertence a outro cliente."));
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(put("/clientes/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dadosCadastro)));
        });
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause() instanceof RuntimeException);
        org.junit.jupiter.api.Assertions.assertEquals("O e-mail já pertence a outro cliente.", exception.getCause().getMessage());
    }

    @Test
    void deletarClienteSucesso() throws Exception {
        doNothing().when(clienteService).deletarPorId(1L);
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletarClienteErroInexistente() {
        doThrow(new RuntimeException("Cliente não encontrado para deletar.")).when(clienteService).deletarPorId(99L);
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(delete("/clientes/99"));
        });
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause() instanceof RuntimeException);
        org.junit.jupiter.api.Assertions.assertEquals("Cliente não encontrado para deletar.", exception.getCause().getMessage());
    }

    @Test
    void deletarClienteErroIntegridadeDados() {
        doThrow(new org.springframework.dao.DataIntegrityViolationException("Integrity violation")).when(clienteService).deletarPorId(1L);
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(delete("/clientes/1"));
        });
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause() instanceof org.springframework.dao.DataIntegrityViolationException);
    }
}
