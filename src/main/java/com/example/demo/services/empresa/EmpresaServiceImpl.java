package com.example.demo.services.empresa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.dtos.FiltrosEmpresaRequestDTO;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.params.Provincia;
import com.example.demo.entities.params.Rubro;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.mappers.EmpresaMapper;
import com.example.demo.repositories.empresa.EmpresaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.mail.MailService;
import com.example.demo.services.params.ProvinciaService;
import com.example.demo.services.params.RubroService;
import com.example.demo.services.seguridad.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa, Long> implements EmpresaService{

    private final EmpresaRepository empresaRepository;
    private final RubroService rubroService;
    private final EmpresaMapper empresaMapper;
    private final UsuarioService usuarioService;
    private final ProvinciaService provinciaService;
    private final MailService mailService;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, RubroService rubroService, EmpresaMapper empresaMapper, UsuarioService usuarioService, ProvinciaService provinciaService, MailService mailService) {
        super(empresaRepository);
        this.empresaRepository = empresaRepository;
        this.rubroService = rubroService;
        this.empresaMapper = empresaMapper;
        this.usuarioService = usuarioService;
        this.provinciaService = provinciaService;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public Empresa modificarEmpresa(Long id, EmpresaRequestDTO empresaRequestDTO) {
        if(id == null){
            throw new IllegalArgumentException("El ID de la empresa no puede ser nulo");
        }
        Empresa empresaOriginal = findById(id);
        empresaMapper.updateEntityFromDto(empresaRequestDTO, empresaOriginal);
        
        if(empresaRequestDTO.getIdRubro() != null && empresaRequestDTO.getIdRubro() != empresaOriginal.getRubro().getId()){
            Rubro rubro = rubroService.findById(empresaRequestDTO.getIdRubro());
            empresaOriginal.setRubro(rubro);
        }
        
        usuarioService.actualizarDatosUsuario(empresaOriginal.getUsuario().getId(), empresaRequestDTO.getContrasenia(), empresaRequestDTO.getRepetirContrasenia(), empresaRequestDTO.getUrlFotoPerfil());

        return empresaRepository.save(empresaOriginal);
    }

    @Override
    public List<Empresa> buscarEmpresasConFiltros(FiltrosEmpresaRequestDTO filtrosEmpresaRequestDTO){
        return empresaRepository.buscarEmpresasConFiltros(filtrosEmpresaRequestDTO.getIdsRubros(), filtrosEmpresaRequestDTO.getIdsProvincias());
    }

    @Override
    public List<Empresa> buscarEmpresasPorNombre(String nombreEmpresa){
        if(nombreEmpresa.isEmpty() || nombreEmpresa == null){
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío");
        }
        return empresaRepository.buscarEmpresasPorNombre(nombreEmpresa);
    }

    @Override
    public Empresa crearEmpresa(EmpresaRequestDTO empresaRequestDTO){
        Empresa nuevaEmpresa = empresaMapper.toEntity(empresaRequestDTO);
        Rubro rubroEmpresa = rubroService.findById(empresaRequestDTO.getIdRubro());
        Provincia provinciaEmpresa = provinciaService.findById(empresaRequestDTO.getIdProvincia());
        
        UsuarioDTO usuarioDTO = new UsuarioDTO(empresaRequestDTO.getEmailEmpresa(), empresaRequestDTO.getContrasenia(), empresaRequestDTO.getUrlFotoPerfil());
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuarioDTO);

        nuevaEmpresa.setRubro(rubroEmpresa);
        nuevaEmpresa.setProvincia(provinciaEmpresa);
        nuevaEmpresa.setUsuario(nuevoUsuario);

        empresaRepository.save(nuevaEmpresa);

        enviarMailAEmpresa(nuevaEmpresa.getUsuario().getCorreoUsuario(), nuevaEmpresa.getNombreEmpresa());

        return nuevaEmpresa;
    }

    private void enviarMailAEmpresa(String mailTo, String nombreEmpresa){
        String templateName = "mailRevisionEmpresa";
        Map<String, Object> variables = new HashMap<>();
        
        variables.put("nombreEmpresa", nombreEmpresa);

        String subject = "¡Bienvenido a Workee! Tu cuenta se encuentra en revisión.";
        mailService.enviar(mailTo, subject, templateName, variables);
    }
    
}


