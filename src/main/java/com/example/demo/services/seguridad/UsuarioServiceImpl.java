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
import com.example.demo.entities.seguridad.Rol;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.entities.seguridad.UsuarioEstadoUsuario;
import com.example.demo.entities.seguridad.UsuarioRol;
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
    private final UsuarioMapper usuarioMapper;
    private final EstadoUsuarioService estadoUsuarioService;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolService rolService;

    private final MailService mailService;

    //private final BCrypt

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, EstadoUsuarioService estadoUsuarioService, MailService mailService, UsuarioRolRepository usuarioRolRepository, RolService rolService) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.estadoUsuarioService = estadoUsuarioService;
        this.mailService = mailService;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolService = rolService;
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

    @Override
    public List<UsuarioResponseDTO> buscarUsuariosActivosPorRol(String nombreRol){
        return usuarioRolRepository.buscarUsuariosActivosPorRol(nombreRol);
    }

    @Override
    @Transactional
    public void modificarRolUsuario(Long idUsuario, Long idRol){
        if(idUsuario == null){
            throw new EntityNotValidException("El id del usuario no puede estar vacio");
        }
        if(idRol == null){
            throw new EntityNotValidException("El id del rol no puede estar vacio");
        }

        Optional<UsuarioRol> actual = usuarioRolRepository.buscarUsuarioRolActualSegunIdUsuario(idUsuario);
        if(!actual.isPresent()){
            throw new EntityNotFoundException("El usuario no posee ningún rol actual");
        }
        UsuarioRol usuarioRolActual = actual.get();
        if(idRol == usuarioRolActual.getRol().getId()){
            throw new EntityAlreadyExistsException("El usuario actualmente posee el rol ingresado");
        }

        Boolean sonMismaCategoria = verificarQueRolPerteneceACategoria(idRol, usuarioRolActual.getRol().getId());
        if(!sonMismaCategoria){
            throw new EntityNotValidException("El rol ingresado no pertenece a la categoria de roles del usuario");
        }

        Optional<UsuarioRol> usuarioRolExistenteOptional = usuarioRolRepository.buscarUsuarioRolAnteriorSegunIdUsuarioEIdRol(idRol, idUsuario);
        if(usuarioRolExistenteOptional.isPresent()){
            System.out.println("Entreeeee");
            UsuarioRol usuarioRolExistente = usuarioRolExistenteOptional.get();
            usuarioRolExistente.setFechaHoraBaja(null);
            usuarioRolRepository.save(usuarioRolExistente);
        } else {
            Rol rolNuevo = rolService.buscarRolActivoPorId(idRol);
            Usuario usuario = findById(idUsuario);

            UsuarioRol nuevoUsuarioRol = new UsuarioRol();
            nuevoUsuarioRol.setRol(rolNuevo);
            nuevoUsuarioRol.setFechaHoraAlta(new Date());
            nuevoUsuarioRol.setUsuario(usuario);
            usuarioRolRepository.save(nuevoUsuarioRol);
        }

        usuarioRolActual.setFechaHoraBaja(new Date());
        usuarioRolRepository.save(usuarioRolActual);
    }

    private Boolean verificarQueRolPerteneceACategoria(Long idRolNuevo, Long idRolActual){
        Rol rolNuevo = rolService.findById(idRolNuevo);
        Rol rolActual = rolService.findById(idRolActual);

        if(rolActual.getCategoriaRol().getId() != rolNuevo.getCategoriaRol().getId()){
            return false;
        } else {
            return true;
        }
    }

}

