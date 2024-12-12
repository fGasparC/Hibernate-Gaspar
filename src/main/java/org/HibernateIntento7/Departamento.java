package org.HibernateIntento7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departamentos")
public class Departamento {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 20)
    private String nombre;

    @Column(name = "localizacion", length = 50)
    private String localizacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

}