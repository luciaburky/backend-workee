package com.example.demo.controllers;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entities.Base;

public interface BaseController<E extends Base, ID extends Serializable> {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getOne(@PathVariable ID id);
    ResponseEntity<?> save(@RequestBody E entity);
    ResponseEntity<?> update(@PathVariable ID id, @RequestBody E entity);
    ResponseEntity<?> delete(@PathVariable ID id);    
}
