package com.madeiradelei.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.madeiradelei.controller.dto.ProdutoRequest;
import com.madeiradelei.controller.dto.ProdutoResponse;
import com.madeiradelei.controller.dto.ProdutoUpdateRequest;
import com.madeiradelei.domain.produto.Dimensoes;
import com.madeiradelei.domain.produto.Produto;
import com.madeiradelei.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    // --- CREATE ---
    public ProdutoResponse cadastrarNovoProduto(ProdutoRequest request) {
        Dimensoes dimensoes = new Dimensoes(
                request.dimensoes().altura(),
                request.dimensoes().largura(),
                request.dimensoes().profundidade()
        );

        Produto novoProduto = new Produto(
                request.nome(),
                dimensoes,
                request.preco(),
                request.tempoFabricacaoDias()
        );

        Produto produtoSalvo = repository.save(novoProduto);
        return mapearParaResponse(produtoSalvo);
    }

    // --- READ ALL ---
    public List<ProdutoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::mapearParaResponse)
                .toList();
    }

    // --- READ BY ID ---
    public ProdutoResponse buscarPorId(String id) {
        Produto produto = buscarProdutoOuFalhar(id);
        return mapearParaResponse(produto);
    }

    // --- UPDATE (Apenas Preço e Tempo) ---
    public ProdutoResponse atualizarValores(String id, ProdutoUpdateRequest request) {
        Produto produto = buscarProdutoOuFalhar(id);
        
        // Regra de Negócio: Alterar apenas o que faz sentido
        produto.atualizarPrecoETempo(request.preco(), request.tempoFabricacaoDias());
        
        Produto produtoAtualizado = repository.save(produto);
        return mapearParaResponse(produtoAtualizado);
    }

    // --- DELETE ---
    public void deletar(String id) {
        Produto produto = buscarProdutoOuFalhar(id);
        repository.delete(produto);
    }

    // --- MÉTODOS AUXILIARES ---
    
    private Produto buscarProdutoOuFalhar(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private ProdutoResponse mapearParaResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco(),
                produto.getTempoFabricacaoDias()
        );
    }

    @Transactional
    public void inativarProduto(String id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        
        produto.inativar();
        repository.save(produto);
    }
}