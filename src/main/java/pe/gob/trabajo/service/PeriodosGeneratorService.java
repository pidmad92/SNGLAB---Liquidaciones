package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public interface PeriodosGeneratorService extends Serializable {

    public ArrayList<Calperiodo> getListaPeriodosCts(Datlab datosLaborales);

    public ArrayList<Calperiodo> getListaPeriodosGratificaciones(Datlab datosLaborales);

    public ArrayList<Calperiodo> getListaPeriodosVacaciones(Datlab datosLaborales);

    public ArrayList<Calperiodo> getListaPeriodosIndemnizaciones(Datlab datosLaborales);

    public ArrayList<Calperiodo> getListaPeriodosRemuneracionesI(Datlab datosLaborales);

}
