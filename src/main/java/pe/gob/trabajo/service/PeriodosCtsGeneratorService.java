package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;
import java.io.Serializable;
import java.util.ArrayList;

public interface PeriodosCtsGeneratorService extends Serializable {

    /*  Para el calculo de la Cts*/

    public ArrayList<Calperiodo> getListPerCtsAnualObreroT1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualObreroT2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST3(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualSemestral1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualMensual(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

    public ArrayList<Calperiodo> getListPerCtsAnualSemestral2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales);

}
