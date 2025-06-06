package com.c4.ejerciciotablaempleados.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.c4.ejerciciotablaempleados.model.Empleado;
import com.c4.ejerciciotablaempleados.service.EmpleadoService;

@RestController
@RequestMapping("/api/v1/empleado")
public class EmpleadoController {
     @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerEmpleado(@RequestParam(required = false) String Name, @RequestParam(required = false) String Office) {

        List<Empleado> empleado;

        if(Name != null && !Name.trim().isEmpty())
            empleado = empleadoService.buscarPorNombre(Name);
        else if (Office != null && !Office.trim().isEmpty())
            empleado = empleadoService.buscarPorOficina(Office);
        else
            empleado = empleadoService.ObtenerTodosEmpleados();

        if (!empleado.isEmpty()) 
            return ResponseEntity.ok(empleado); // ok = 200

        return ResponseEntity.noContent().build(); // 204 = No content
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.obtenerporID(id);

        return empleado.map(ResponseEntity::ok) // 200 ok
                    .orElseGet(() -> ResponseEntity.notFound().build()); // 404 not found
    }
    
    @PostMapping
    public ResponseEntity<Empleado> crearNuevoEmpleado(@RequestBody Empleado empleado) {
        try {
            if(empleado.getId() != null)
                return ResponseEntity.badRequest().build();

            Empleado nuevoEmpleado = empleadoService.crearNuevoEmpleado(empleado);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado); // 201 creado
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Empleado> eliminarPorID(@PathVariable Long id) {
        try {
            empleadoService.eliminarPorID(id);
            return ResponseEntity.noContent().build(); // 204 No Content 
        } catch (Exception e) {
            if (e.getMessage() !=null && e.getMessage().contains("no encontrado"))
                return ResponseEntity.notFound().build();

            return ResponseEntity.badRequest().build(); // Otros errores
        }
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleadoEntity(@PathVariable Long id, @RequestBody Empleado detallesEmpleado) {
        try {
            Empleado empleado = empleadoService.actualizarEmpleadoExistente(id, detallesEmpleado);
            return ResponseEntity.ok(empleado);
        } catch (Exception e) {
            if (e.getMessage() !=null && e.getMessage().contains("no encontrado"))
                return ResponseEntity.notFound().build();

            return ResponseEntity.badRequest().build(); // Otros errores
        }
    }
    
}
