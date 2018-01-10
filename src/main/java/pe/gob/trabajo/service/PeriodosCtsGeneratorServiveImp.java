package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class PeriodosCtsGeneratorServiveImp implements PeriodosCtsGeneratorService{

    /*  Fechas para CTS*/
    private static final LocalDate FEC_FIN_PT1 = LocalDate.of(1990,12,31);
    private static final LocalDate FEC_FIN_OSPT1 = LocalDate.of(1962,1,11);
    private static final LocalDate FEC_INI_EC2 = LocalDate.of(1962,7,12);
    private static final LocalDate FEC_FIN_EC2T1 = LocalDate.of(1979,9,30);
    private static final LocalDate FEC_FIN_EC2T2 = LocalDate.of(1989,12,31);
    private static final LocalDate FEC_FIN_PT2 = LocalDate.of(2000,10,31);
    private static final LocalDate FEC_FIN_PT3 = LocalDate.of(2004,10,31);
    private static final LocalDate FEC_INI_PT4 = LocalDate.of(2004,11,1);

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualObreroT1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return getListPerCtsReserva(listaPeriodos,datosLaborales,FEC_FIN_OSPT1);
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualObreroT2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return getListPerCtsReserva(listaPeriodos,datosLaborales,FEC_FIN_PT1);
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return getListPerCtsReserva(listaPeriodos,datosLaborales,FEC_FIN_PT1);
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return getListPerCtsReserva(listaPeriodos,datosLaborales,FEC_FIN_EC2T1);
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return getListPerCtsReserva(listaPeriodos,datosLaborales,FEC_FIN_EC2T2);
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleadoT2ST3(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        return getListPerCtsReserva(listaPeriodos,datosLaborales,FEC_FIN_PT1);
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsSemestral1(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        LocalDate fecFinRan = FEC_FIN_PT2;
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int b = 0;
        int np = 1;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<>();
        if(listaPeriodos.size()==0){
            fecIniPer = datosLaborales.getdFecvincul();
        } else {
            listPeriodos = listaPeriodos;
            fecIniPer = listaPeriodos.get(listaPeriodos.size() - 1).gettFecfin().plusDays(1);
            np = listaPeriodos.get(listaPeriodos.size() - 1).getnNumper() + 1;
        }
        if(datosLaborales.getdFeccese().isBefore(fecFinRan)){
            fecFinPer = datosLaborales.getdFeccese();
        } else{
            fecFinPer = fecFinRan;
        }

        per = new Calperiodo();
        per.setnNumper(np);
        per.settFecini(fecIniPer);

        if((per.gettFecini().isAfter(LocalDate.of(fecIniPer.getYear(),1,1).minusDays(1))&&
            per.gettFecini().isBefore(LocalDate.of(per.gettFecini().getYear(),5,1)))){

            per.settFecfin(LocalDate.of(per.gettFecini().getYear(),4,30));
            listPeriodos.add(per);
            fecIniPer = per.gettFecfin().plusDays(1);

        }else if((per.gettFecini().isAfter(LocalDate.of(fecIniPer.getYear(),11,1).minusDays(1))&&
            per.gettFecini().isBefore(LocalDate.of(per.gettFecini().getYear()+1,1,1)))){

            per.settFecfin(LocalDate.of(per.gettFecini().getYear() +1,4,30));
            listPeriodos.add(per);
            fecIniPer = per.gettFecfin().plusDays(1);


        }else if (per.gettFecini().isAfter(LocalDate.of(fecIniPer.getYear(),5,1).minusDays(1))&&
            per.gettFecini().isBefore(LocalDate.of(per.gettFecini().getYear(),11,1))){

            per.settFecfin(LocalDate.of(per.gettFecini().getYear(),10,31));
            listPeriodos.add(per);
            fecIniPer = per.gettFecfin().plusDays(1);
        }
        np++;

        tmpToFin = fecIniPer.until(fecFinPer);

        for(int a=0;a<=tmpToFin.toTotalMonths();a=a+6){
            per = new Calperiodo();
            per.setnNumper(np);
            per.settFecini(fecIniPer.plusMonths(a));
            if(per.gettFecini().plusMonths(6).isAfter(fecFinPer.minusDays(b+1))){
                per.settFecfin(fecFinPer);
                listPeriodos.add(per);
                break;
            } else{
                per.settFecfin(per.gettFecini().plusMonths(6).minusDays(1));
                listPeriodos.add(per);
            }
            b++;
            np++;
        }
        return listPeriodos;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsMensual(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        LocalDate fecFinRan = FEC_FIN_PT3;
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int b = 0;
        int np = 1;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<>();
        if(listaPeriodos.size()==0){
            fecIniPer = datosLaborales.getdFecvincul();
        } else {
            listPeriodos = listaPeriodos;
            fecIniPer = listaPeriodos.get(listaPeriodos.size() - 1).gettFecfin().plusDays(1);
            np = listaPeriodos.get(listaPeriodos.size() - 1).getnNumper() + 1;
        }
        if(datosLaborales.getdFeccese().isBefore(fecFinRan)){
            fecFinPer = datosLaborales.getdFeccese();
        } else{
            fecFinPer = fecFinRan;
        }

        per = new Calperiodo();
        per.setnNumper(np);
        per.settFecini(fecIniPer);

        per.settFecfin(per.gettFecini().withDayOfMonth(1).plusMonths(1).minusDays(1));
        listPeriodos.add(per);
        fecIniPer = per.gettFecfin().plusDays(1);

        np++;
        tmpToFin = fecIniPer.until(fecFinPer);

        for(int a=0;a<=tmpToFin.toTotalMonths() + b ;a++){
            per = new Calperiodo();
            per.setnNumper(np);
            per.settFecini(fecIniPer.plusMonths(a));
            if(per.gettFecini().plusMonths(1).isAfter(fecFinPer.minusDays(1))){ // Ojo aquí con las demas funciones minusdays(1)
                per.settFecfin(fecFinPer);
                listPeriodos.add(per);
                System.out.println("ROMPIO CON F0: " + per.gettFecini() + " FF: " + per.gettFecfin());
                break;
            } else{
                per.settFecfin(per.gettFecini().plusMonths(1).minusDays(1));
                listPeriodos.add(per);
            }
            np++;
        }
        return listPeriodos;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsSemestral2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int b = 0;
        int np = 1;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<>();
        if(listaPeriodos.size()==0){
            fecIniPer = datosLaborales.getdFecvincul();
        } else {
            listPeriodos = listaPeriodos;
            fecIniPer = listaPeriodos.get(listaPeriodos.size() - 1).gettFecfin().plusDays(1);
            np = listaPeriodos.get(listaPeriodos.size() - 1).getnNumper() + 1;
        }

        fecFinPer = datosLaborales.getdFecvincul();

        per = new Calperiodo();
        per.setnNumper(np);
        per.settFecini(fecIniPer);

        if((per.gettFecini().isAfter(LocalDate.of(fecIniPer.getYear(),1,1).minusDays(1))&&
            per.gettFecini().isBefore(LocalDate.of(per.gettFecini().getYear(),5,1)))){

            per.settFecfin(LocalDate.of(per.gettFecini().getYear(),4,30));
            listPeriodos.add(per);
            fecIniPer = per.gettFecfin().plusDays(1);

        }else if((per.gettFecini().isAfter(LocalDate.of(fecIniPer.getYear(),11,1).minusDays(1))&&
            per.gettFecini().isBefore(LocalDate.of(per.gettFecini().getYear()+1,1,1)))){

            per.settFecfin(LocalDate.of(per.gettFecini().getYear() +1,4,30));
            listPeriodos.add(per);
            fecIniPer = per.gettFecfin().plusDays(1);


        }else if (per.gettFecini().isAfter(LocalDate.of(fecIniPer.getYear(),5,1).minusDays(1))&&
            per.gettFecini().isBefore(LocalDate.of(per.gettFecini().getYear(),11,1))){

            per.settFecfin(LocalDate.of(per.gettFecini().getYear(),10,31));
            listPeriodos.add(per);
            fecIniPer = per.gettFecfin().plusDays(1);
        }
        np++;
        tmpToFin = fecIniPer.until(fecFinPer);

        for(int a=0;a<=tmpToFin.toTotalMonths();a=a+6){
            per = new Calperiodo();
            per.setnNumper(np);
            per.settFecini(fecIniPer.plusMonths(a));
            if(per.gettFecini().plusMonths(6).isAfter(fecFinPer.minusDays(b+1))){
                per.settFecfin(fecFinPer);
                listPeriodos.add(per);
                break;
            } else{
                per.settFecfin(per.gettFecini().plusMonths(6).minusDays(1));
                listPeriodos.add(per);
            }
            b++;
            np++;
        }
        return listPeriodos;
    }

    /*  Funcion para pediodos Cts de la Reserva */

    @Override
    public ArrayList<Calperiodo> getListPerCtsReserva(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales, LocalDate fecFinRan ) {

        ArrayList<Calperiodo> listPeriodos = new ArrayList<>();
        Calperiodo per;
        LocalDate fecFinPer;
        LocalDate fecIniPer;
        int np = 0;

        if(listaPeriodos.size()==0){
            fecIniPer = datosLaborales.getdFecvincul();
        } else {
            listPeriodos = listaPeriodos;
            fecIniPer = listaPeriodos.get(listaPeriodos.size() - 1).gettFecfin().plusDays(1);
            np = listaPeriodos.get(listaPeriodos.size() - 1).getnNumper();
        }

        if(datosLaborales.getdFeccese().isBefore(fecFinRan)){
            fecFinPer = datosLaborales.getdFeccese();
        } else{
            fecFinPer = fecFinRan;
        }

        per = new Calperiodo();
        np++;
        per.setnNumper(np);
        per.settFecini(fecIniPer);
        LocalDate as;

        for(as = datosLaborales.getdFecvincul();as.isBefore(fecFinPer.plusDays(1));as = as.plusYears(1)){
            if(as.isAfter(fecIniPer.minusDays(1))){
//                System.out.println(fecIniPer + " Fecha: "+ as + " " + fecFinPer);
                //
                if(per.gettFecini().plusYears(1).minusDays(1).isAfter(fecFinPer.minusDays(1))){
                    per.settFecfin(fecFinPer);
                    listPeriodos.add(per);
                    break;
                } else {
                    per.settFecfin(as.minusDays(1).plusYears(1));//plus
                    listPeriodos.add(per);
                    break;
                }
            }
        }

        return null;
    }
}
