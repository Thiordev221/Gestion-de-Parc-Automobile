package sn.abdoulayeThior.app.Utilitaires;
import java.util.Calendar;

public final class DateUtils {

    private DateUtils() {
        // Prevent instantiation
    }

    public static Calendar createCalendar(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month - 1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.getTime(); // forces validation
        return calendar;
    }

    public static int getAge(Calendar birthDate) {
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        int currentMonth = today.get(Calendar.MONTH);
        int birthMonth = birthDate.get(Calendar.MONTH);

        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int birthDay = birthDate.get(Calendar.DAY_OF_MONTH);

        if (currentMonth < birthMonth ||
                (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }

        return age;
    }
}
