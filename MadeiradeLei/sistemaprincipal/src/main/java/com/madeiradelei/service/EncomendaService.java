package com.madeiradelei.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.madeiradelei.controller.dto.EncomendaResponse;
import com.madeiradelei.controller.dto.EncomendaUpdatePagamentoRequest;
import com.madeiradelei.controller.dto.ItemEncomendaRequest;
import com.madeiradelei.controller.dto.MovimentacaoEstoqueRequest;
import com.madeiradelei.domain.cliente.Cliente;
import com.madeiradelei.domain.encomenda.Encomenda;
import com.madeiradelei.domain.encomenda.ItemEncomenda;
import com.madeiradelei.domain.encomenda.strategy.FormaPagamentoFactory;
import com.madeiradelei.domain.encomenda.strategy.FormaPagamentoStrategy;
import com.madeiradelei.domain.produto.ComponenteProduto;
import com.madeiradelei.domain.produto.Produto;
import com.madeiradelei.repository.ClienteRepository;
import com.madeiradelei.repository.EncomendaRepository;
import com.madeiradelei.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EncomendaService {

    private final EncomendaRepository encomendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ComponenteService componenteService;
    
    @Transactional
    public EncomendaResponse processarNovaEncomenda(String clienteId, List<ItemEncomendaRequest> itensRequest, String tipoPagamento, Integer parcelas) {
        
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

        List<ItemEncomenda> itens = itensRequest.stream().map(req -> {
            Produto produto = produtoRepository.findById(req.produtoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + req.produtoId()));
            
            // 3. A CADEIA DE PRODUÇÃO AUTOMÁTICA: Consumir os recursos do estoque
            for (ComponenteProduto cp : produto.getFichaTecnica()) {
                // Multiplica a quantidade que o móvel exige pela quantidade de móveis encomendados
                BigDecimal totalConsumido = cp.quantidadeNecessaria().multiply(BigDecimal.valueOf(req.quantidade()));
                
                // Dispara a baixa no estoque. Se der erro de "Estoque Insuficiente", 
                // o @Transactional cancela toda a operação e devolve o erro!
                componenteService.registrarSaida(cp.componenteId(), new MovimentacaoEstoqueRequest(totalConsumido));
            }

            LocalDate dataEntregaPrevista = LocalDate.now().plusDays(produto.getTempoFabricacaoDias());
            return new ItemEncomenda(produto.getCodigo(), req.quantidade(), produto.getPreco(), dataEntregaPrevista);
        }).toList();

        FormaPagamentoStrategy estrategia = FormaPagamentoFactory.obterEstrategia(tipoPagamento);
        Long numeroPedido = System.currentTimeMillis();
        
        Encomenda novaEncomenda = new Encomenda(numeroPedido, cliente, itens, estrategia, parcelas);
        return mapearParaResponse(encomendaRepository.save(novaEncomenda));
    }
    // --- READ ALL ---
    public List<EncomendaResponse> buscarTodas() {
        return encomendaRepository.findAll().stream()
                .map(this::mapearParaResponse)
                .toList();
    }

    // --- READ BY ID ---
    public EncomendaResponse buscarPorId(String id) {
        Encomenda encomenda = buscarEncomendaOuFalhar(id);
        return mapearParaResponse(encomenda);
    }

    // --- UPDATE (Mudar Pagamento e Recalcular) ---
    public EncomendaResponse alterarFormaPagamento(String id, EncomendaUpdatePagamentoRequest request) {
        Encomenda encomenda = buscarEncomendaOuFalhar(id);
        
        FormaPagamentoStrategy novaEstrategia = FormaPagamentoFactory.obterEstrategia(request.tipoPagamento());
        
        // A regra de negócio recalcula tudo lá dentro!
        encomenda.alterarFormaPagamento(novaEstrategia, request.parcelas());
        
        Encomenda encomendaAtualizada = encomendaRepository.save(encomenda);
        return mapearParaResponse(encomendaAtualizada);
    }

    // --- DELETE ---
    public void deletar(String id) {
        Encomenda encomenda = buscarEncomendaOuFalhar(id);
        encomendaRepository.delete(encomenda);
    }

    // --- MÉTODOS AUXILIARES ---
    
    private Encomenda buscarEncomendaOuFalhar(String id) {
        return encomendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Encomenda não encontrada"));
    }

    private EncomendaResponse mapearParaResponse(Encomenda encomenda) {
        return new EncomendaResponse(
                encomenda.getId(),
                encomenda.getNumero(),
                encomenda.getDataSolicitacao(),
                encomenda.getValorTotal(),
                encomenda.getValorDesconto(),
                encomenda.getValorLiquido()
        );
    }
}