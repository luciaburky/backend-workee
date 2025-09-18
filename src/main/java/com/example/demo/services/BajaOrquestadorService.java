package com.example.demo.services;

public interface BajaOrquestadorService {
    public void darDeBajaEmpresaYRelacionados(Long idEmpresa);
    public void darDeBajaUsuarioYEntidadRelacionada(Long idUsuario);
}
