package br.com.criandoapi.projeto.service;

import br.com.criandoapi.projeto.dto.UsuarioDto;
import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.repository.IUsuario;
import br.com.criandoapi.projeto.security.Token;
import br.com.criandoapi.projeto.security.TokenUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final IUsuario repository;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(IUsuario repository) {
        this.repository = repository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public List<Usuario> listarUsuario() {
        logger.info("Usuario: " + getLogado() + " listando usuarios");
        return repository.findAll();
    }

    public Usuario criarUsuario(Usuario usuario) {
        String encoder = this.encoder.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        logger.info("Usuario: " + getLogado() + " criando usuário " + usuario.getNome());
        return repository.save(usuario);
    }

    public Usuario editarUsuario(Usuario usuario) {
        String encoder = this.encoder.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        logger.info("Usuario: " + getLogado() + " editando usuário " + usuario.getNome());
        return repository.save(usuario);
    }

    public boolean excluirUsuario(Integer id) {
        repository.deleteById(id);
        logger.info("Usuario: " + getLogado() + " excluindo usuario " + id);
        return true;
    }

    public Token gerarToken(@Valid UsuarioDto usuario) {
        Usuario user = repository.findByNomeOrEmail(usuario.getNome(), usuario.getEmail());
        if(user != null) { // Verifica se o usuário existe
            Boolean valid = encoder.matches(usuario.getSenha(), user.getSenha());
            if(valid) {
                return new Token(TokenUtil.createToken(user));
            }
        }
        return null;
    }

    private String getLogado() {
        Authentication userLogado = SecurityContextHolder.getContext().getAuthentication();

        if (userLogado != null) {
            return userLogado.getName();
        }
        return "Null";
    }
}
