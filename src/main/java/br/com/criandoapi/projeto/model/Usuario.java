package br.com.criandoapi.projeto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "O nome é obrigatório!")
    @Size(min = 4, message = "O nome deve ter no mínimo 4 caracteres!")
    @Column(name = "nome", length = 200)
    private String nome;

    @Email(message = "Insira um email válido!")
    @NotBlank(message = "O email é obrigatório!")
    @Column(name = "email", length = 50)
    private String email;

    @NotBlank(message = "Digite uma senha!")
    @Column(name = "senha", columnDefinition = "TEXT")
    private String senha;

    @NotBlank(message = "Digite seu número de telefone!")
    @Column(name = "telefone", length = 15)
    private String telefone;

}

