package org.HibernateIntento7;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //1
        Departamento departamento = infoDepartamentobyCity("Dallas");
        System.out.println("Nombre:" + departamento.getNombre() +
                "\nLocalizacion: " + departamento.getLocalizacion() +
                "\nId: " + departamento.getId());
        //2
        System.out.println("Empleados del departamento de Investigacion:");
        mostrarEmpleadosDelDepartamento(departamento);
        //3
        System.out.println("Empleado con mayor sueldo:");
        Empleado empleadoSueldoso= mostrarEmpleadoSuertudo();
        //4
        Departamento departamentodelEmpleadoSueldoso=mostrarDepartamentoTrabajaEmpleado(empleadoSueldoso);
        System.out.println(departamentodelEmpleadoSueldoso.getNombre());
        //5
        System.out.println("Total sueldos del departamento del Empleado con mas billete");
        System.out.println(sumaSueldos(departamentodelEmpleadoSueldoso));
        //6
        System.out.println("Sueldos por departamento");
        Departamento[] departamentos=obtenerTodosLosDepartamentos();
        for(int i=0;i<departamentos.length;i++){
            System.out.println(departamentos[i].getNombre());
            System.out.println(sumaSueldos(departamentos[i]));
        }
        //7
        Empleado miller=encuentraMiller();
        System.out.println(miller.getApellido());
        Empleado jefeMiller=mostrarJefeDeMiller(miller);
        System.out.println("Jefe de Miller es " + jefeMiller.getApellido());
        //8
        Empleado[] subordinadosDeKing=subordinadosDeKing();
        System.out.println("Subordinados de King");
        for(int i=0;i<subordinadosDeKing.length;i++){
            System.out.println(subordinadosDeKing[i].getApellido());
        }
        //9. Mostrar un resumen con cada departamento con sus datos y los datos de todos los trabajadores
        //incluyendo quien su jefe, si lo tienen, y de que empleados son jefes.
        for(int i=0;i<departamentos.length;i++){
            System.out.println(departamentos[i].getId());
            System.out.println(departamentos[i].getNombre());
            System.out.println(departamentos[i].getLocalizacion());
            mostrarEmpleadosDelDepartamento(departamentos[i]);
            mostrarEmpleadosDelDepartamento(departamentos[i]);

        }
        //10
        System.out.println("JEFES:");
        Empleado[] jefes =empleadosJefes();
        for(int i=0;i<jefes.length;i++){
            System.out.println(jefes[i].getApellido());
        }
        System.out.println("NO JEFES");
        Empleado[] noJefes =empleadosNoJefes(jefes);
        for(int i=0;i<noJefes.length;i++){
            System.out.println(noJefes[i].getApellido());
        }

    }

    //1. Buscar los datos del departamento localizado en Dallas
    static public Departamento infoDepartamentobyCity(String ciudad) {
        String res = "";
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Departamento d where d.localizacion='" + ciudad + "'";
        Query sentencia= sesion.createQuery(consulta);
        Departamento objeto_departamento= (Departamento) sentencia.uniqueResult();
        return objeto_departamento;
    }

    //2. Mostrar los empleados que trabajan en el departamento anterior
    static public void mostrarEmpleadosDelDepartamento(Departamento departamento) {
        String res = "";
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Empleado e where e.departamento='" + departamento.getId() + "'";
        Query sentencia= sesion.createQuery(consulta);
        Collection<Empleado> empleados = (Collection<Empleado>) sentencia.list();
        for (Empleado empleado : empleados) {
            res+= "Id: " + empleado.getId() + "\n";
            res += "Nombre: " + empleado.getApellido() + "\n";
            res+= "Cargo: " + empleado.getCargo() + "\n";
            String jefecillo = (empleado.getJefe() != null) ? empleado.getJefe().getApellido() : "null";
            res+= "Jefe: " + jefecillo;
            res+= "Salario: " + empleado.getSalario() + "\n";
            res+= "Fecha alta: " + empleado.getFechaAlta() + "\n";
            res+= "Comision: " + empleado.getComision() + "\n";
            res+= "Departamento: " + empleado.getDepartamento().getNombre() + "\n";
        }
        System.out.println(res);
    }
    
    //3. Buscar el empleado con mayor sueldo de toda la empresa.
    static public Empleado mostrarEmpleadoSuertudo(){
        String res = "";
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Empleado e where e.salario = (select max(salario) from Empleado)";
        Query sentencia= sesion.createQuery(consulta);
        Empleado empleado= (Empleado) sentencia.uniqueResult();
        System.out.println("Nombre: "+empleado.getApellido());
        return empleado;
    }
    //4. Mostrar en qué departamento trabaja el empleado anterior.
    static public Departamento mostrarDepartamentoTrabajaEmpleado(Empleado empleado){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Departamento d where d.id = (select e.departamento.id from Empleado e where e.id = :empleadoId)";
        Query sentencia = sesion.createQuery(consulta);
        sentencia.setParameter("empleadoId", empleado.getId());
        Departamento departamento = (Departamento) sentencia.uniqueResult();
        sesion.close();
        return departamento;
    }
    //5. Sumar los sueldos del departamento anterior.
    static public Double sumaSueldos(Departamento departamento){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "select sum(e.salario) from Empleado e where e.departamento.id = :departamentoId";
        Query sentencia = sesion.createQuery(consulta);
        sentencia.setParameter("departamentoId", departamento.getId());
        Double suma = (Double) sentencia.uniqueResult();
        sesion.close();
        return suma;
    }
    //6. Encontrar el departamento cuya suma de sueldos es la mayor de la empresa

    static public Departamento[] obtenerTodosLosDepartamentos() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Departamento";
        Query sentencia = sesion.createQuery(consulta);
        Collection<Departamento> departamentos = (Collection<Departamento>) sentencia.list();
        sesion.close();
        return departamentos.toArray(new Departamento[departamentos.size()]);
    }
    //7. Quien es el jefe de MILLER.
    static public Empleado encuentraMiller(){
        String res = "";
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Empleado e where e.apellido='MILLER'";
        Query sentencia= sesion.createQuery(consulta);
        Empleado empleado= (Empleado) sentencia.uniqueResult();
        return empleado;
    }
    static public Empleado mostrarJefeDeMiller(Empleado miller){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Empleado e where e.id = :millerJefe";
        Query sentencia = sesion.createQuery(consulta);
        sentencia.setParameter("millerJefe", miller.getJefe().getId());
        Empleado jefe = (Empleado) sentencia.uniqueResult();
        sesion.close();
        return jefe;
    }
    //8. Que empleados tienen como jefe a KING.
    static public Empleado[] subordinadosDeKing() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Empleado e where e.jefe.id = (select k.id from Empleado k where k.apellido = 'KING')";
        Query sentencia = sesion.createQuery(consulta);
        Collection<Empleado> empleados = (Collection<Empleado>) sentencia.list();
        sesion.close();
        return empleados.toArray(new Empleado[empleados.size()]);
    }


    //10. Que empleados son jefes de algún empleado y cuáles no.
    static public Empleado[] empleadosJefes() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "select distinct e.jefe from Empleado e where e.jefe is not null";
        Query sentencia = sesion.createQuery(consulta);
        Collection<Empleado> jefes = (Collection<Empleado>) sentencia.list();
        sesion.close();
        return jefes.toArray(new Empleado[jefes.size()]);
    }
    static public Empleado[] empleadosNoJefes(Empleado[] empleadosJefes) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        String consulta = "from Empleado e where e not in (:empleadosJefes)";
        Query sentencia = sesion.createQuery(consulta);
        sentencia.setParameterList("empleadosJefes", empleadosJefes);
        Collection<Empleado> noJefes = (Collection<Empleado>) sentencia.list();
        sesion.close();
        return noJefes.toArray(new Empleado[noJefes.size()]);
       
    }
}


