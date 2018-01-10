package pe.gob.trabajo.service;

import pe.gob.trabajo.domain.Calperiodo;
import pe.gob.trabajo.domain.Datlab;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class PeriodosCtsGeneratorServiceImp implements PeriodosCtsGeneratorService{

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
    public ArrayList<Calperiodo> getListPerCtsAnualObrero(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
    	
    	LocalDate f0 = datosLaborales.getdFecvincul();
        LocalDate fi = null;
        LocalDate ff = null;
        Period timeToFinPer = null;
    	
    	/*  Periodos CTS de la Reserva  */
    	if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT1.plusDays(1)))) {
    		System.out.println("Periodos CTS de la Reserva: ");
    		System.out.println("\tCaso: 'Obrero'");
    		/*  Sub-Periodo Tipo 1  */
            if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_OSPT1.plusDays(1)))) {
                System.out.println("\t\tCaso 01");
                listaPeriodos = crearPeriodos(new ArrayList<Calperiodo>(), FEC_FIN_OSPT1, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos(listaPeriodos, FEC_FIN_PT1, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos2(listaPeriodos, FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
            }

            /*  Sub-Periodo Tipo 2  */
            else{
                System.out.println("\t\tCaso 02");
                listaPeriodos = crearPeriodos(new ArrayList<Calperiodo>(), FEC_FIN_PT1, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos2(listaPeriodos, FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
            }
    	}
    	
    	/*  Periodos CTS Tipo 2: Semestral  */
        else if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT2.plusDays(1)))&&(datosLaborales.getdFecvincul().isAfter((FEC_FIN_PT1.minusDays(1))))){
            System.out.println("Periodos CTS Tipo 2 Semestral: ");
            listaPeriodos = crearPeriodos2(new ArrayList<Calperiodo>(), FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
            listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
            listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());

        }

        /*  Periodos CTS Tipo 3: Mensual  */
        else if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT3.plusDays(1)))&&(datosLaborales.getdFecvincul().isAfter((FEC_FIN_PT2.minusDays(1))))){
            System.out.println("Periodos CTS Tipo 3: Mensual ");
            listaPeriodos = crearPeriodos3(new ArrayList<Calperiodo>(), FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
            listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
        }

        /*  Periodos CTS Tipo 4: Semestral */
        else if(datosLaborales.getdFecvincul().isAfter(FEC_INI_PT4.minusDays(1))){
            System.out.println("Periodos CTS Tipo 4: Semestral ");
            listaPeriodos = crearPeriodos4(new ArrayList<Calperiodo>(), f0, datosLaborales.getdFeccese());
        }
    	
    	
    	
        return listaPeriodos;
    }
    
    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualEmpleado(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
    	LocalDate f0 = datosLaborales.getdFecvincul();
        LocalDate fi = null;
        LocalDate ff = null;
        Period timeToFinPer = null;
        
    	 /*  Periodos CTS de la Reserva  */
        if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT1.plusDays(1)))){
            System.out.println("Periodos CTS de la Reserva: ");
            
            System.out.println("Caso: 'Empleado'");

            /*  Caso 1  */
            if(datosLaborales.getdFecvincul().isBefore((FEC_INI_EC2))){
                System.out.println("Caso de Ingreso 1:");
                listaPeriodos = crearPeriodos(new ArrayList<Calperiodo>(), FEC_FIN_PT1, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos2(listaPeriodos, FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
                listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
            }

            /*  Caso 2  */
            else if(datosLaborales.getdFecvincul().isAfter((FEC_INI_EC2.minusDays(1)))){
                System.out.println("Caso de Ingreso 2:");

                /*  Sub-Sub-Periodo Tipo 1  */
                if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_EC2T1.plusDays(1)))) {
                    System.out.println("\t\tSub-Periodo Tipo 1");
                    listaPeriodos = crearPeriodos(new ArrayList<Calperiodo>(), FEC_FIN_EC2T1, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos(listaPeriodos, FEC_FIN_EC2T2, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos(listaPeriodos, FEC_FIN_PT1, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos2(listaPeriodos, FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
                }

                /*  Sub-Sub-Periodo Tipo 2  */
                if((datosLaborales.getdFecvincul().isBefore((FEC_FIN_EC2T2.plusDays(1))))&&(datosLaborales.getdFecvincul().isAfter((FEC_FIN_EC2T1.minusDays(1))))) {
                    System.out.println("\t\tSub-Periodo Tipo 2");
                    listaPeriodos = crearPeriodos(new ArrayList<Calperiodo>(), FEC_FIN_EC2T2, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos(listaPeriodos, FEC_FIN_PT1, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos2(listaPeriodos, FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
                }

                /*  Sub-Sub-Periodo Tipo 3  */
                if((datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT1.plusDays(1))))&&(datosLaborales.getdFecvincul().isAfter((FEC_FIN_EC2T2.minusDays(1))))) {
                    System.out.println("\t\tSub-Periodo Tipo 3");
                    listaPeriodos = crearPeriodos(new ArrayList<Calperiodo>(), FEC_FIN_PT1, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos2(listaPeriodos, FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
                    listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
                }
            }
        }
        
        /*  Periodos CTS Tipo 2: Semestral  */
        else if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT2.plusDays(1)))&&(datosLaborales.getdFecvincul().isAfter((FEC_FIN_PT1.minusDays(1))))){
            System.out.println("Periodos CTS Tipo 2 Semestral: ");
            listaPeriodos = crearPeriodos2(new ArrayList<Calperiodo>(), FEC_FIN_PT2, f0, datosLaborales.getdFeccese());
            listaPeriodos = crearPeriodos3(listaPeriodos, FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
            listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());

        }

        /*  Periodos CTS Tipo 3: Mensual  */
        else if(datosLaborales.getdFecvincul().isBefore((FEC_FIN_PT3.plusDays(1)))&&(datosLaborales.getdFecvincul().isAfter((FEC_FIN_PT2.minusDays(1))))){
            System.out.println("Periodos CTS Tipo 3: Mensual ");
            listaPeriodos = crearPeriodos3(new ArrayList<Calperiodo>(), FEC_FIN_PT3, f0, datosLaborales.getdFeccese());
            listaPeriodos = crearPeriodos4(listaPeriodos, f0, datosLaborales.getdFeccese());
        }

        /*  Periodos CTS Tipo 4: Semestral */
        else if(datosLaborales.getdFecvincul().isAfter(FEC_INI_PT4.minusDays(1))){
            System.out.println("Periodos CTS Tipo 4: Semestral ");
            listaPeriodos = crearPeriodos4(new ArrayList<Calperiodo>(), f0, datosLaborales.getdFeccese());
        }
        
    	return listaPeriodos;
    }

    @Override
    public ArrayList<Calperiodo> getListPerCtsAnualObreroT2(ArrayList<Calperiodo> listaPeriodos, Datlab datosLaborales) {
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
        Period tmpToFin;
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
        	fecFinPer = fecFinRan;
        } else{
            fecFinPer = datosLaborales.getdFeccese();
        }

        per = new Calperiodo();
        np++;
        per.setnNumper(np);
        per.settFecini(fecIniPer);
        LocalDate as;

        System.out.println(" Fecha Vinculo Laboral: "+  " " + datosLaborales.getdFecvincul());
        System.out.println(" Fecha Fin Laboral: "+  " " + fecFinPer);
        System.out.println(" Fecha Fin Laboral Plus Days: "+  " " + fecFinPer.plusDays(1));
        System.out.println(" Fecha before: "+  " " + datosLaborales.getdFecvincul().isBefore(fecFinPer.plusDays(1)));
        System.out.println(" Fecha Agregar Ano: "+  " " + datosLaborales.getdFecvincul().plusYears(1));
        
        for(as = datosLaborales.getdFecvincul();as.isBefore(fecFinPer.plusDays(1));as = as.plusYears(1)){
            if(as.isAfter(fecIniPer.minusDays(1))){
                System.out.println(fecIniPer + " Fecha: "+ as + " " + fecFinPer);
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
        
        fecIniPer = listPeriodos.get(listPeriodos.size() - 1).gettFecfin().plusDays(1);
        np = listPeriodos.get(listPeriodos.size() - 1).getnNumper();

        if(datosLaborales.getdFeccese().isBefore(fecFinRan)){
            fecFinPer = datosLaborales.getdFeccese();
        } else{
            fecFinPer = fecFinRan;
        }
        
        tmpToFin = fecIniPer.until(fecFinPer);
        // a = aÃ±os, para un periodo anual
        for(int a=0;a<=tmpToFin.getYears();a++){
            per = new Calperiodo();
            np++;
            per.setnNumper(np);

            per.settFecini(fecIniPer.plusYears(a));

            if(per.gettFecini().plusYears(1).minusDays(1).isAfter(fecFinPer.minusDays(a+1))){
                per.settFecfin(fecFinPer);
                listPeriodos.add(per);
                break;
            } else{
                //perlista.get((perlista.size()-2)).getFecIni()
                //per.getFecIni()
                per.settFecfin(per.gettFecini().plusYears(1).minusDays(1));
                listPeriodos.add(per);
            }
        }
        return listPeriodos;
    }
    
    
    
    
    
    public ArrayList<Calperiodo> crearPeriodos(ArrayList<Calperiodo> perlista, LocalDate ffPer, LocalDate foUtil, LocalDate ffUtil){
        //perlista - lista de periodos anteriores
        //ffPer - fecha fin del periodo subtipo
        //foUtil fecha inicio de labores
        //ffUtil fecha de cese de labores
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int np = 0;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<Calperiodo>();
        if(perlista.size()==0){
            fecIniPer = foUtil;
        } else {
            listPeriodos = perlista;
            fecIniPer = perlista.get(perlista.size() - 1).gettFecfin().plusDays(1);
            np = perlista.get(perlista.size() - 1).getnNumper();
        }
        if(ffUtil.isBefore(ffPer)){
            fecFinPer = ffUtil;
        } else{
            fecFinPer = ffPer;
        }


        per = new Calperiodo();
        np++;
        per.setnNumper(np);
        per.settFecini(fecIniPer);
        LocalDate as;
        for(as = foUtil;as.isBefore(fecFinPer.plusDays(1));as = as.plusYears(1)){
            if(as.isAfter(fecIniPer.minusDays(1))){
//                System.out.println(fecIniPer + " Fecha: "+ as + " " + fecFinPer);
                //
                if(per.gettFecini().plusYears(1).minusDays(1).isAfter(fecFinPer.minusDays(1))){
                    per.settFecfin(fecFinPer);
                    listPeriodos.add(per);
                    break;
                } else {
                    //per.settFecfin(as.minusDays(1).plusYears(1));//plus
                	per.settFecfin(as.minusDays(1));//plus
                    listPeriodos.add(per);
                    break;
                }
            }
        }

        fecIniPer = listPeriodos.get(listPeriodos.size() - 1).gettFecfin().plusDays(1);
        np = listPeriodos.get(listPeriodos.size() - 1).getnNumper();

        if(ffUtil.isBefore(ffPer)){
            fecFinPer = ffUtil;
        } else{
            fecFinPer = ffPer;
        }

        tmpToFin = fecIniPer.until(fecFinPer);
        // a = aÃ±os, para un periodo anual
        for(int a=0;a<=tmpToFin.getYears();a++){
            per = new Calperiodo();
            np++;
            per.setnNumper(np);

            per.settFecini(fecIniPer.plusYears(a));

            if(per.gettFecini().plusYears(1).minusDays(1).isAfter(fecFinPer.minusDays(a+1))){
                per.settFecfin(fecFinPer);
                listPeriodos.add(per);
                break;
            } else{
                //perlista.get((perlista.size()-2)).getFecIni()
                //per.getFecIni()
                per.settFecfin(per.gettFecini().plusYears(1).minusDays(1));
                listPeriodos.add(per);
            }
        }
        return listPeriodos;
    }

    public ArrayList<Calperiodo> crearPeriodos2(ArrayList<Calperiodo> perlista, LocalDate ffPer, LocalDate foUtil, LocalDate ffUtil){
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int b = 0;
        int np = 1;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<Calperiodo>();
        if(perlista.size()==0){
            fecIniPer = foUtil;
        } else {
            listPeriodos = perlista;
            fecIniPer = perlista.get(perlista.size() - 1).gettFecfin().plusDays(1);
            np = perlista.get(perlista.size() - 1).getnNumper() + 1;
        }
        if(ffUtil.isBefore(ffPer)){
            fecFinPer = ffUtil;
        } else{
            fecFinPer = ffPer;
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

    public ArrayList<Calperiodo> crearPeriodos3(ArrayList<Calperiodo> perlista, LocalDate ffPer, LocalDate foUtil, LocalDate ffUtil){
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int b = 0;
        int np = 1;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<Calperiodo>();
        if(perlista.size()==0){
            fecIniPer = foUtil;
        } else {
            listPeriodos = perlista;
            fecIniPer = perlista.get(perlista.size() - 1).gettFecfin().plusDays(1);
            np = perlista.get(perlista.size() - 1).getnNumper() + 1;
        }
        if(ffUtil.isBefore(ffPer)){
            fecFinPer = ffUtil;
        } else{
            fecFinPer = ffPer;
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
            if(per.gettFecini().plusMonths(1).isAfter(fecFinPer.minusDays(1))){ // Ojo aquÃ­ con las demas funciones minusdays(1)
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

    public ArrayList<Calperiodo> crearPeriodos4(ArrayList<Calperiodo> perlista, LocalDate foUtil, LocalDate ffUtil){
        LocalDate fecIniPer;
        LocalDate fecFinPer;
        Period tmpToFin;
        Calperiodo per;
        int b = 0;
        int np = 1;
        ArrayList<Calperiodo> listPeriodos = new ArrayList<Calperiodo>();
        if(perlista.size()==0){
            fecIniPer = foUtil;
        } else {
            listPeriodos = perlista;
            fecIniPer = perlista.get(perlista.size() - 1).gettFecfin().plusDays(1);
            np = perlista.get(perlista.size() - 1).getnNumper() + 1;
        }

        fecFinPer = ffUtil;

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
}
