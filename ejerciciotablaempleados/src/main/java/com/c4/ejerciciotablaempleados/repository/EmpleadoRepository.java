package com.c4.ejerciciotablaempleados.repository;

import java.sql.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c4.ejerciciotablaempleados.model.Empleado;

import jakarta.transaction.Transactional;

@Repository
public interface EmpleadoRepository  extends JpaRepository<Empleado,Long>{

    List<Empleado> findByNameContainingIgnoreCase(String Name);
    List<Empleado> findByOfficeContainingIgnoreCase(String Office);
    
    @Modifying
    @Transactional
    @Query("UPDATE Empleado e SET e.Name = :name, e.Position = Position, e.Office = Office , e.Age = Age , e.Stardate = Stardate, e.Salary = Salary where e.id = id ")
    int actualizarEmpleadoConJPQL(
        @Param("id") Long id,
        @Param("Name") String name,
        @Param("Position") String position,
        @Param("Office") String office,
        @Param("Age") int age,
        @Param("Startdate") Date stardate,
        @Param("Salary") Double salary 
    );
}
