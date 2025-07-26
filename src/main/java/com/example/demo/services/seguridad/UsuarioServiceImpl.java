package com.example.demo.services.seguridad;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.RecuperarContraseniaDTO;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.dtos.UsuarioResponseDTO;
import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.entities.seguridad.UsuarioEstadoUsuario;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.mappers.UsuarioMapper;
import com.example.demo.repositories.seguridad.UsuarioRepository;
import com.example.demo.repositories.seguridad.UsuarioRolRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.mail.MailService;
import com.example.demo.services.params.EstadoUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Long> implements UsuarioService{

    private final UsuarioRepository usuarioRepository;
    //private final BCrypt
    private final UsuarioMapper usuarioMapper;
    private final EstadoUsuarioService estadoUsuarioService;
    private final UsuarioRolRepository usuarioRolRepository;

    private final MailService mailService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, EstadoUsuarioService estadoUsuarioService, MailService mailService, UsuarioRolRepository usuarioRolRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.estadoUsuarioService = estadoUsuarioService;
        this.mailService = mailService;
        this.usuarioRolRepository = usuarioRolRepository;
    }


    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioDTO usuarioDTO){
        if(existeUsuarioConCorreo(usuarioDTO.getCorreoUsuario())){
            throw new EntityAlreadyExistsException("El correo ingresado ya se encuentra en uso");
        }

        String contraseniaCodificada = encriptarContrasenia(usuarioDTO.getContraseniaUsuario());

        Usuario nuevoUsuario = usuarioMapper.toEntity(usuarioDTO);
        nuevoUsuario.setContraseniaUsuario(contraseniaCodificada);
        nuevoUsuario.setFechaHoraAlta(new Date());

        //Se guarda para que tenga el ID
        usuarioRepository.save(nuevoUsuario);

        //Seteo del estado
        EstadoUsuario estadoUsuario = estadoUsuarioService.obtenerEstadoPorNombre(usuarioDTO.getEstadoUsuarioInicial());
        UsuarioEstadoUsuario usuarioEstadoUsuario = new UsuarioEstadoUsuario();
        usuarioEstadoUsuario.setEstadoUsuario(estadoUsuario);
        usuarioEstadoUsuario.setFechaHoraAlta(new Date());
        nuevoUsuario.setUsuarioEstadoList(new ArrayList<>());
        nuevoUsuario.getUsuarioEstadoList().add(usuarioEstadoUsuario);

        return usuarioRepository.save(nuevoUsuario);
        
    }

    private String encriptarContrasenia(String contrasenia){
        return contrasenia; //TODO: Hacer codificacion de contraseña 
    }
    
    private Boolean existeUsuarioConCorreo(String correoUsuario){
        return usuarioRepository.existsByFechaHoraBajaIsNullAndCorreoUsuarioLike(correoUsuario);
    }

    @Override
    @Transactional
    public void actualizarDatosUsuario(Long idUsuario, String nuevaContrasenia, String repetirContrasenia, String nuevaUrlFoto){
        boolean seCambio = false;
        Usuario usuario = findById(idUsuario);

        if(nuevaContrasenia != null && !nuevaContrasenia.isBlank()){
            String contrasenia = modificarContrasenia(nuevaContrasenia, repetirContrasenia);
            usuario.setContraseniaUsuario(contrasenia);
            seCambio = true;
        }

        if(nuevaUrlFoto != null && !nuevaUrlFoto.isBlank()){
            usuario.setUrlFotoUsuario(nuevaUrlFoto);
            seCambio = true;
        }

        if(seCambio){
            usuarioRepository.save(usuario);
        }
    }
    
    @Transactional
    private String modificarContrasenia(String contrasenia, String repetirContrasenia){
        if(contrasenia == null || repetirContrasenia == null){
            throw new EntityNotValidException("La contraseña no puede estar vacía");
        }
        if(contrasenia.length() < 8 || repetirContrasenia.length() < 8){
            throw new EntityNotValidException("La contraseña debe tener al menos 8 caracteres");
        }
        if(!contrasenia.equals(repetirContrasenia)){
            throw new EntityNotValidException("Las contraseñas deben coincidir");
        }

        String contraseniaEncriptada = encriptarContrasenia(contrasenia);

        return contraseniaEncriptada;

    }

    //ESTE LO DA DE BAJA SIN VERIFICAR SI YA ESTABA DADO DE BAJA (es para cuando se elimina)
    @Override
    @Transactional
    public void darDeBajaUsuario(Long idUsuario){
        Usuario usuario = findById(idUsuario);
        usuario.setFechaHoraBaja(new Date());
        usuarioRepository.save(usuario);
    }

    @Override
    public void solicitarRecuperarContrasenia(String correoUsuario){
        Optional<Usuario> usuario = usuarioRepository.buscarUsuarioPorCorreo(correoUsuario);
        
        if(!usuario.isPresent()){
            throw new EntityNotFoundException("No se encontró un usuario con el correo ingresado");
        }
        Usuario usuarioEncontrado = usuario.get();
        //TODO: Encriptar el id del usuario para generar el link
        String idEncriptado = usuarioEncontrado.getId().toString();
        //TODO: Revisar correo de recuperacion
        String linkRecuperacion = "http://localhost:4200/nuevaContrasenia?correo=" + idEncriptado; 

        enviarMailRecuperacionAUsuario(usuarioEncontrado.getCorreoUsuario(), linkRecuperacion);
    }

    private void enviarMailRecuperacionAUsuario(String mailTo, String urlrecuperacion){
        String templateName = "recuperacionContrasenia";
        Map<String, Object> variables = new HashMap<>();
        variables.put("urlrecuperacion", urlrecuperacion);
        String subject = "Recupera tu contraseña";
        mailService.enviar(mailTo, subject, templateName, variables);
    }

    @Transactional
    public void confirmarRecuperacionContrasenia(String idEncriptado, RecuperarContraseniaDTO recuperarContraseniaDTO){
        if(!recuperarContraseniaDTO.getContraseniaNueva().equals(recuperarContraseniaDTO.getRepetirContrasenia())){
            throw new EntityNotValidException("Las contraseñas no coinciden");
        }

        //TODO: desencriptar id del usuario
        Long idUsuario = 1L;
        
        Usuario usuario = findById(idUsuario);

        //TODO: encriptar contraseña
        String contraseniaEncriptada = recuperarContraseniaDTO.getContraseniaNueva();
        
        usuario.setContraseniaUsuario(contraseniaEncriptada);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> buscarUsuariosActivos(){
        return usuarioRolRepository.buscarUsuariosActivos();
    }

}

