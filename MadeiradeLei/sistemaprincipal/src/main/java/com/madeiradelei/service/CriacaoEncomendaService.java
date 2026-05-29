package com.madeiradelei.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madeiradelei.controller.dto.EncomendaResponse;
import com.madeiradelei.controller.dto.ItemEncomendaRequest;
import com.madeiradelei.domain.cliente.Cliente;
import com.madeiradelei.domain.encomenda.Encomenda;
import com.madeiradelei.domain.encomenda.ItemEncomenda;
import com.madeiradelei.domain.encomenda.strategy.FormaPagamentoFactory;
import com.madeiradelei.domain.encomenda.strategy.FormaPagamentoStrategy;
import com.madeiradelei.domain.produto.Produto;
import com.madeiradelei.repository.ClienteRepository;
import com.madeiradelei.repository.EncomendaRepository;
import com.madeiradelei.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // O Lombok cria o construtor para injetar os 3 repositórios
public class CriacaoEncomendaService {

    private final EncomendaRepository encomendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public EncomendaResponse processarNovaEncomenda(String clienteId, List<ItemEncomendaRequest> itensRequest, String tipoPagamento, Integer parcelas) {
        
        // 1. Busca o Cliente (Fail-fast se não existir)
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID fornecido."));

        // 2. Transforma a lista de 'Requests' na lista rica de 'ItemEncomenda'
        List<ItemEncomenda> itens = itensRequest.stream().map(req -> {
            Produto produto = produtoRepository.findById(req.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + req.produtoId()));

            // Regra de Negócio: A data de entrega é baseada no tempo de fabricação
            LocalDate dataEntregaPrevista = LocalDate.now().plusDays(produto.getTempoFabricacaoDias());

            // Tira a "fotografia" do preço atual do produto
            return new ItemEncomenda(
                    produto.getCodigo(), 
                    req.quantidade(), 
                    produto.getPreco(), 
                    dataEntregaPrevista
            );
        }).toList();

        // 3. Padrão Strategy: Transforma a String "PIX" na regra de cálculo concreta
        FormaPagamentoStrategy estrategia = FormaPagamentoFactory.obterEstrategia(tipoPagamento);

        // 4. Gera um número de pedido (Num sistema real, usaríamos uma sequence no banco)
        Long numeroPedido = System.currentTimeMillis();

        // 5. Instancia a Encomenda (que já se auto-valida e calcula os totais no construtor)
        Encomenda novaEncomenda = new Encomenda(numeroPedido, cliente, itens, estrategia, parcelas);

        // 6. Salva no MongoDB
        Encomenda encomendaSalva = encomendaRepository.save(novaEncomenda);

        // 7. Retorna o DTO limpo para o Controller
        return new EncomendaResponse(
                encomendaSalva.getId(),
                encomendaSalva.getNumero(),
                encomendaSalva.getDataSolicitacao(),
                encomendaSalva.getValorTotal(),
                encomendaSalva.getValorDesconto(),
                encomendaSalva.getValorLiquido()
        );
    }
}