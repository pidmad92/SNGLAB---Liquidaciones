package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;

import java.util.ArrayList;

public class PeriodosGeneratorServiceImp implements PeriodosGeneratorService{

    @Override
    public ArrayList<Calperiodo> getListaPeriodosCts(Datlab datosLaborales) {
    	
    	ArrayList<Calperiodo> listaCalperiodo = new ArrayList<>();
    	PeriodosCtsGeneratorService periodosCtsGeneratorService = new PeriodosCtsGeneratorServiceImp();
    	
    	
    	//Valida Vinculo Laboral
    	switch (datosLaborales.getTipvinculo().getId().intValue()) {
		case 1:
			listaCalperiodo = periodosCtsGeneratorService.getListPerCtsAnualObrero(new ArrayList<Calperiodo>() , datosLaborales);
			break;
		
		case 2:
			listaCalperiodo = periodosCtsGeneratorService.getListPerCtsAnualEmpleado(new ArrayList<Calperiodo>() , datosLaborales);
			break;

		default:
			break;
		}

        return listaCalperiodo;
    }

    @Override
    public ArrayList<Calperiodo> getListaPeriodosGratificaciones(Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListaPeriodosVacaciones(Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListaPeriodosIndemnizaciones(Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListaPeriodosRemuneracionesI(Datlab datosLaborales) {
        return null;
    }

}
