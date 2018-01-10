package pe.gob.trabajo.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import pe.gob.trabajo.domain.Datlab;
import pe.gob.trabajo.domain.Empleador;
import pe.gob.trabajo.domain.Modcontrato;
import pe.gob.trabajo.domain.Perjuridica;
import pe.gob.trabajo.domain.Pernatural;
import pe.gob.trabajo.domain.Regimenlab;
import pe.gob.trabajo.domain.Tipvinculo;
import pe.gob.trabajo.domain.Trabajador;
import pe.gob.trabajo.domain.Calperiodo;


public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Empleador empleador = new Empleador();
		Perjuridica perjuridica = new Perjuridica();
		perjuridica.setvNumdoc("1070413933");
		perjuridica.setvRazsocial("PIDMAD - Servicios Mecatronicos");
		empleador.setPerjuridica(perjuridica);
		
        Trabajador trabajador = new Trabajador();//""," Maldonado Diaz");
        Pernatural pernatural = new Pernatural();
        pernatural.setvNumdoc("704139336");
        pernatural.setvNombres("Pierre David");
        trabajador.setPernatural(pernatural);
        
        Regimenlab regimenlab = new Regimenlab();
        regimenlab.setId((long)1);
        regimenlab.setvDesreglab("Regimen General Común");
        
        Modcontrato modcontrato = new Modcontrato();
        Tipvinculo tipvinculo = new Tipvinculo();
        tipvinculo.setId((long)1); //1 Obrero - 2 Empleado
        tipvinculo.setvDestipvin("Obrero");
        
        Datlab datosLaborales = new Datlab();//(empleador, trabajador, ,"Empleado","General", );
        datosLaborales.setTrabajador(trabajador);
        datosLaborales.setEmpleador(empleador);
        datosLaborales.setRegimenlab(regimenlab);
        datosLaborales.setModcontrato(modcontrato);
        datosLaborales.setTipvinculo(tipvinculo);
        
        datosLaborales.setdFecvincul(LocalDate.parse("1963-02-05"));
        datosLaborales.setdFeccese(LocalDate.parse("2016-12-12"));
        
        
        
        ArrayList<String> listaBeneficios = new ArrayList<>();
        listaBeneficios.add("CTS");

        PeriodosGeneratorService periodosGeneratorService = new PeriodosGeneratorServiceImp() ;//(datosLaborales,listaBeneficios);
        
        ArrayList<Calperiodo> listaPeriodo = periodosGeneratorService.getListaPeriodosCts(datosLaborales);
        
        /*  Recorriendo los periodos */
        for(Calperiodo per : listaPeriodo){
            Period tiempoComputable = (per.gettFecini().until(per.gettFecfin().plusDays(1)));//perCmptble
            //LocalDate tiempoComputable = LocalDate.of(perCmptble.getYears(),perCmptble.getMonths(),perCmptble.getDays());//.plusDays(1);
            
            if(per.gettFecini().until(per.gettFecfin(), ChronoUnit.DAYS) > 0) {
            	System.out.println("\t\t\tPeriodo N°" + per.getnNumper() + " \t(" + per.gettFecini() + " - " + per.gettFecfin() + ")" + "\tTmpo. Computable: " + tiempoComputable.getYears() + " años " + tiempoComputable.getMonths() + " meses " + tiempoComputable.getDays() + " dias");
            }
        }
	}

}
