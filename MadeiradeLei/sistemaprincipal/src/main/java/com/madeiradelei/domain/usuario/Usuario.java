package com.madeiradelei.domain.usuario;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import lombok.Getter;

@Document(collection = "usuarios")
@Getter
public class Usuario implements UserDetails {

    @Id
    private String id;
    private String email;
    private String senha;
    
    // O pulo do gato: Um usuário pode ter VÁRIOS perfis
    private Set<Perfil> perfis = new HashSet<>();

    public Usuario(String email, String senhaEncriptada) {
        Assert.hasText(email, "Email é obrigatório");
        Assert.hasText(senhaEncriptada, "Senha é obrigatória");
        
        this.email = email;
        this.senha = senhaEncriptada;
        this.perfis.add(Perfil.ROLE_CLIENTE); // Todo cadastro nasce como cliente por padrão
    }

    protected Usuario() {
    } // Construtor protegido para o Spring Data (não queremos que seja usado diretamente)

     
    // --- COMPORTAMENTOS (Object Calisthenics) ---

    public void promoverParaFuncionario() {
        this.perfis.add(Perfil.ROLE_FUNCIONARIO);
    }

    public void promoverParaGerente() {
        this.perfis.add(Perfil.ROLE_GERENTE);
    }

    // --- MÉTODOS OBRIGATÓRIOS DO USERDETAILS (Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte o nosso Enum para o formato que o Spring Security entende
        return this.perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return this.senha; }

    @Override
    public String getUsername() { return this.email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}