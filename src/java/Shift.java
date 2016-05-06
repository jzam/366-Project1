import java.util.*;
import java.sql.*;
import java.text.*;

public class Shift {
   public final static String EARLY = "7:30 Shift";
   public final static String MORNING = "8:30 Shift";
   public final static String LATE = "9:30 Shift";
   public final static String OVERNIGHT = "Overnight Shift";
   public final static String SUNDAY = "Sunday Shift";
   public final static String SURGERY = "Surgery";

   private final static int NOT_SET = -1;

   private String shift;
   private Calendar date;
   private int firstDoctor;
   private int secondDoctor;
   private int firstTechnician;
   private int secondTechnician;

   public Shift() {
      firstDoctor = NOT_SET;
      secondDoctor = NOT_SET;
      firstTechnician = NOT_SET;
      secondTechnician = NOT_SET;
   }

   public Shift(String shift, java.sql.Date date, int firstD, int secondD, int firstT, int secondT) {
      this.shift = shift;
      this.date = new GregorianCalendar();
      this.date.setTime(date);
      this.firstDoctor = firstD;
      this.secondDoctor = secondD;
      this.firstTechnician = firstT;
      this.secondTechnician = secondT;
   }
   
   public void setShift(String shift) {
      this.shift = shift;
   }

   public String getShift() {
      return shift;
   }

   public void setDate(java.sql.Date date) {
      this.date = new GregorianCalendar();
      this.date.setTime(date);
   }

   public String getShiftDate() {
       return date.getTime().toString();
   }
   
   public Calendar getDate() {
      return date;
   }

   public String getDateAsString() {
      SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
      return formatter.format(date.getTime());
   }

   public void setFirstDoctor(int doctor) {
      firstDoctor = doctor;
   }

   public int getFirstDoctor() {
      return firstDoctor;
   }

   public boolean hasFirstDoctor() {
      return firstDoctor != NOT_SET;
   }

   public void setSecondDoctor(int doctor) {
      secondDoctor = doctor;
   }

   public int getSecondDoctor() {
      return secondDoctor;
   }

   public boolean hasSecondDoctor() {
      return secondDoctor != NOT_SET;
   }

   public void setFirstTechnician(int technician) {
      firstTechnician = technician;
   }

   public int getFirstTechnician() {
      return firstTechnician;
   }

   public boolean hasFirstTechnician() {
      return firstTechnician != NOT_SET;
   }

   public void setSecondTechnician(int technician) {
      secondTechnician = technician;
   }

   public int getSecondTechnician() {
      return secondTechnician;
   }

   public boolean hasSecondTechnician() {
      return secondTechnician != NOT_SET;
   }

   public boolean equals(java.sql.Date date, String shift) {
      Calendar cal = new GregorianCalendar();
      cal.setTime(date);
      return this.date.equals(cal) && this.shift.equals(shift);
   }

   public void print() {
      System.out.println(
         "On " + getDateAsString() + "\n" + 
         "   Shift : " + shift + "\n" +
         "   Doctor : " + firstDoctor + ", " + secondDoctor + "\n" + 
         "   Technician : " + firstTechnician + ", " + secondTechnician + "\n" 
      );
   }
}
