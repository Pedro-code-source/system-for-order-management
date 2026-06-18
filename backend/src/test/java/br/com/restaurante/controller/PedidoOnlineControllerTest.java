package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroPedidoOnline;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.service.PedidoOnlineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoOnlineController.class)
class PedidoOnlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PedidoOnlineService pedidoOnlineService;

    @Test
    void cadastrar() throws Exception {
        DadosCadastroPedidoOnline dadosCadastro = new DadosCadastroPedidoOnline(1L, Collections.emptyList(), null, null);
        PedidoOnline pedido = new PedidoOnline();
        ReflectionTestUtils.setField(pedido, "id", 1L);
        ReflectionTestUtils.setField(pedido, "itens", Collections.emptyList());
        when(pedidoOnlineService.cadastrar(any(DadosCadastroPedidoOnline.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidosOnline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void listar() throws Exception {
        when(pedidoOnlineService.listarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/pedidosOnline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void buscarPorId() throws Exception {
        PedidoOnline pedido = new PedidoOnline();
        ReflectionTestUtils.setField(pedido, "id", 1L);
        ReflectionTestUtils.setField(pedido, "itens", Collections.emptyList());
        when(pedidoOnlineService.buscarPorId(anyLong())).thenReturn(pedido);

        mockMvc.perform(get("/pedidosOnline/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void atualizar() throws Exception {
        DadosCadastroPedidoOnline dadosCadastro = new DadosCadastroPedidoOnline(1L, Collections.emptyList(), null, null);
        PedidoOnline pedido = new PedidoOnline();
        ReflectionTestUtils.setField(pedido, "id", 1L);
        ReflectionTestUtils.setField(pedido, "itens", Collections.emptyList());
        when(pedidoOnlineService.atualizar(anyLong(), any(DadosCadastroPedidoOnline.class))).thenReturn(pedido);

        mockMvc.perform(put("/pedidosOnline/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deletar() throws Exception {
        doNothing().when(pedidoOnlineService).deletarPorId(anyLong());

        mockMvc.perform(delete("/pedidosOnline/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void finalizarPedido() throws Exception {
        doNothing().when(pedidoOnlineService).finalizarPedido(anyLong());

        mockMvc.perform(put("/pedidosOnline/1/finalizar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelarPedido() throws Exception {
        doNothing().when(pedidoOnlineService).cancelarPedido(anyLong());

        mockMvc.perform(delete("/pedidosOnline/1/cancelar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void confirmarEndereco() throws Exception {
        doNothing().when(pedidoOnlineService).confirmarEndereco(anyLong());

        mockMvc.perform(put("/pedidosOnline/1/confirmarEndereco"))
                .andExpect(status().isNoContent());
    }
}