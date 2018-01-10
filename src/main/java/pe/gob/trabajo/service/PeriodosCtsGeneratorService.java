package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public interface PeriodosCtsGeneratorService extends Serializable {

    /*  Para el calculo de la Cts   */

    public ArrayList<Calperiodo> getListPerCtsAnualObrero(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualObreroT2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleado(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST3(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsSemestral1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsMensual(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsSemestral2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    /*  Funcion para pediodos Cts de la Reserva */

    public ArrayList<Calperiodo> getListPerCtsReserva(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales, LocalDate fecFinRan);

}
