package org.HibernateIntento7;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println(infoDepartamentobyCity("Dallas"));
        System.out.println(infoEmpleadosbyDepartamento("Dallas"));
    }

    //1. Buscar los datos del departamento localizado en Dallas
    static public String infoDepartamentobyCity(String ciudad) {
        String res = "";
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Departamento d where d.localizacion='" + ciudad + "'";
        Query sentencia= sesion.createQuery(consulta);
        Departamento objeto_departamento= (Departamento) sentencia.uniqueResult();
        res+="NOMBRE:"+objeto_departamento.getNombre()+"\n";
        res+="LOCALIZACION:"+objeto_departamento.getLocalizacion()+"\n";
        return res;
    }
    //2. Mostrar los empleados que trabajan en el departamento anterior
    static public String infoEmpleadosbyDepartamento(String nombre_departamento) {
        String res = "";
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        //String consulta = "from Empleado e where e.departamento.id='" + nombre_departamento + "'";
        String consulta = "from Empleado e where e.departamento.id = (select d.id from Departamento d where d.nombre ='" + nombre_departamento+"')"; //Esta mal
        System.out.println(consulta);
        Query sentencia= sesion.createQuery(consulta);
        Collection<Empleado> objeto_empleados= sentencia.list();
        for (Empleado empleado : objeto_empleados) {
            res+="NOMBRE:"+empleado.getApellido()+"\n";
            res+="SALARIO:"+empleado.getSalario()+"\n";
        }
        return res;
    }
}


