package com.c4.ejerciciotablaempleados.model;



import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="empleado")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Empleado {
    @Id
    private Long id;

    private String Name;
    private String Position;
    private String Office;
    private int Age;
    private Date Startdate;
    private Double Salary;
}
