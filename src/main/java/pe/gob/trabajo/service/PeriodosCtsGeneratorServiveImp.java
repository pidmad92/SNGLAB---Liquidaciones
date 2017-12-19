package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;

import java.time.LocalDate;
import java.util.ArrayList;

public class PeriodosCtsGeneratorServiveImp implements PeriodosCtsGeneratorService{

    /*  Fechas para CTS*/
    private static final LocalDate FEC_FIN_PT1 = LocalDate.of(1990,12,31);
    private static final LocalDate FEC_FIN_OSPT1 = LocalDate.of(1962,01,11);
    private static final LocalDate FEC_INI_EC2 = LocalDate.of(1962,07,12);
    private static final LocalDate FEC_FIN_EC2T1 = LocalDate.of(1979,9,30);
    private static final LocalDate FEC_FIN_EC2T2 = LocalDate.of(1989,12,31);
    private static final LocalDate FEC_FIN_PT2 = LocalDate.of(2000,10,31);
    private static final LocalDate FEC_FIN_PT3 = LocalDate.of(2004,10,31);
    private static final LocalDate FEC_INI_PT4 = LocalDate.of(2004,11,01);

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualObreroT1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        ArrayList<Calperiodo> listPeriodos;
        Calperiodo calperiodoInt;
        LocalDate fecFinPer;
        LocalDate fecIniPer;
        int np;

        if(listaPeriodos.size()==0){
            fecIniPer = datosLaborales.getdFecvincul();
        } else {
            listPeriodos = listaPeriodos;
            fecIniPer = listaPeriodos.get(listaPeriodos.size() - 1).gettFecfin().plusDays(1);
            np = listaPeriodos.get(listaPeriodos.size() - 1).getnNumper();
        }
        if(datosLaborales.getdFeccese().isBefore(FEC_FIN_PT1)){
            fecFinPer = datosLaborales.getdFeccese();
        } else{
            fecFinPer = FEC_FIN_PT1;
        }




        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualObreroT2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST3(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualSemestral1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualMensual(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualSemestral2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return null;
    }
}
