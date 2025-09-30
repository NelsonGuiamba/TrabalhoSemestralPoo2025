package util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Utils {
    public static int getHoraDeFecho(){
        LocalDate  date = LocalDate.now();
        DayOfWeek diaDeSemana = date.getDayOfWeek();
        if(diaDeSemana.equals(DayOfWeek.SUNDAY))
            return 20;
        else
            return 23;
    }

    public static int getHoraDeAbertura(){
        return 9;
    }
}
