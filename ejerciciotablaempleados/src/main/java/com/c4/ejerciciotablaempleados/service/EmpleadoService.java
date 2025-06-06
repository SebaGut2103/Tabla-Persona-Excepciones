package com.c4.ejerciciotablaempleados.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.c4.ejerciciotablaempleados.model.Empleado;
import com.c4.ejerciciotablaempleados.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional(readOnly = true)
    public List<Empleado> ObtenerTodosEmpleados() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Empleado> obtenerporID(Long id) {
        if (id != null)
            return empleadoRepository.findById(id);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<Empleado> buscarPorNombre(String Name) {
        if (Name == null || Name.trim().isEmpty())
            return new ArrayList<>();
        return empleadoRepository.findByNameContainingIgnoreCase(Name);
    }

    @Transactional(readOnly = true)
    public List<Empleado> buscarPorOficina(String Office) {
        if (Office == null || Office.trim().isEmpty())
            return new ArrayList<>();
        return empleadoRepository.findByOfficeContainingIgnoreCase(Office);
    }

    @Transactional
    public Empleado crearNuevoEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo para poderlo crear");
        }

        if (empleado.getId() != null)
            throw new IllegalArgumentException("El ID deber ser nulo para poder crear un empleado");

        return empleadoRepository.save(empleado);
    }

    @Transactional
    public void eliminarPorID(Long id) {
        if (id == null)
            throw new IllegalArgumentException("El ID no puede ser nulo para eliminar");
        if (!empleadoRepository.existsById(id))
            throw new IllegalArgumentException("Empleado con el ID: " + id + " no existe para poder eliminar");
        empleadoRepository.deleteById(id);
    }

    @Transactional
    public Empleado actualizarEmpleadoExistente(Long id, Empleado EmpleadoNuevo) {
        if (id == null || EmpleadoNuevo == null)
            throw new IllegalArgumentException("El ID y los datos del empleado no pueden ser nulos para actualizar.");

        Optional<Empleado> empleadoExiste = empleadoRepository.findById(id);
        if (!empleadoExiste.isPresent())
            throw new RuntimeException("Funcionario no encontrado con el ID: " + id);

        int filasAfectadas = empleadoRepository.actualizarEmpleadoConJPQL(id,
                EmpleadoNuevo.getName(),
                EmpleadoNuevo.getPosition(),
                EmpleadoNuevo.getOffice(),
                EmpleadoNuevo.getAge(),
                EmpleadoNuevo.getStartdate(),
                EmpleadoNuevo.getSalary());

        if (filasAfectadas > 0)
            return empleadoRepository.findById(id)
                    .orElseThrow(
                            () -> new RuntimeException("Error al recuperar empleado despues de actualizar. ID: " + id));
        else
            throw new RuntimeException("La actualización del funcionario con ID: " + id + " no afectó ninguna fila");
    }
}
