import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.util.Date;
import java.util.TimeZone;


import java.util.*;
import java.text.*;
import java.sql.*;

@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee {
   DBConnection connection;

   public final static int DOCTOR = 1;
   public final static int TECHNICIAN = 2;
   public final static int ADMINISTRATOR = 3;

   private final static String ID_TABLENAME = "id";
   private final static String EMAIL_TABLENAME = "email";
   private final static String PASSWORD_TABLENAME = "password";
   private final static String FIRSTNAME_TABLENAME = "firstname";
   private final static String LASTNAME_TABLENAME = "lastname";
   private final static String PHONE_TABLENAME = "phone";
   private final static String VACATION_DAYS_TABLENAME = "vacationDaysLeft";
   private final static String SICK_DAYS_TABLENAME = "sickDaysLeft";
   private final static String DATE_TABLENAME = "date";
   private final static String SHIFTID_TABLENAME = "shift";

   private final static int MAX_NUM_VACATION_DAYS = 8;
   private final static int MAX_NUM_SICK_DAYS = 4;

   private int id;
   private String email;
   private String password;
   private String username;
   private String firstname;
   private String lastname;
   private String phonenumber;
   private int vacationDays;
   private int sickDays;
   private int type;
   
   private Scheduler schedule;
   private Calendar startDate;

   public Employee(int type) {
      this.type = type;
   }

   public Employee(String username) {
      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         String tablename = "Doctors";
         String query = "select * from " + tablename + 
                        "where username = " + username;
         ResultSet result =
            connection.execQuery(query);
         type = DOCTOR;

         if (result.next() == false) {
            tablename = "Technicians";
            query = "select * from " + tablename + 
                    "where username = " + username;

            result = connection.execQuery(query);
            type = TECHNICIAN;

            if (result.next() == false) {
               type = ADMINISTRATOR;
               result.next(); // result starts out before the first row
            } 
         }
         
         id = result.getInt(ID_TABLENAME);
         email = result.getString(EMAIL_TABLENAME);
         this.username = username;
         password = result.getString(PASSWORD_TABLENAME);
         firstname = result.getString(FIRSTNAME_TABLENAME);
         lastname = result.getString(LASTNAME_TABLENAME);
         phonenumber = result.getString(PHONE_TABLENAME);
         vacationDays = result.getInt(VACATION_DAYS_TABLENAME);
         sickDays = result.getInt(SICK_DAYS_TABLENAME);

         con.close();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public int getType() {
      return type;
   }
   
   public int getID() {
      return id;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getEmail() {
      return email;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getUsername() {
      return username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   public void setFirstName(String firstname) {
      this.firstname = firstname;
   }

   public String getFirstName() {
      return firstname;
   }

   public void setLastName(String lastname) {
      this.lastname = lastname;
   }

   public String getLastName() {
      return lastname;
   }

   public void setPhoneNumber(String phonenumber) {
      this.phonenumber = phonenumber;
   }

   public String getPhoneNumber() {
      return phonenumber;
   }

   public int getNumVacationDays() {
      return vacationDays;
   }

   public int getSickDays() {
      return sickDays;
   }

   public void createEmployee(String tablename) {
      try {
         String query = "INSERT INTO " + tablename + " " +
                        "VALUES (" + email + ", "
                                   + username + ", "
                                   + password + ", "
                                   + firstname + ", "
                                   + lastname + ", "
                                   + phonenumber + ")";
         connection.execUpdate(query);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public ArrayList<String> getSchedule() {
      ArrayList<String> mySchedule = getEmployeeSchedule();
      return mySchedule;
   }

   private ArrayList<String> getEmployeeSchedule() {
      ArrayList<String> mySchedule = new ArrayList<>();
      int i;
      String thisEmpShifts;
      String otherEmpShifts;
      String otherEmpInfo;
      String resultLine;
      
      try {
         connection = new DBConnection();
         Connection con = connection.getConnection();

         // get tablename
         thisEmpShifts = "DoctorShifts";
         otherEmpShifts = "TechnicianShifts";
         otherEmpInfo = "Technicians";
         if (type == TECHNICIAN) {
            thisEmpShifts = "TechnicianShifts";
            otherEmpShifts = "DoctorShifts";
            otherEmpInfo = "Doctors";
          }

         String query = "select DoctorShifts.date, fromTime, toTime, " + otherEmpInfo
                 + ".name from DoctorShifts, TechnicianShifts, Shifts, " + otherEmpInfo 
                 + " where " + thisEmpShifts + ".id = " + this.id + " and " + otherEmpInfo
                 + ".id = " + otherEmpShifts + ".id and DoctorShifts.date = "
                 + "TechnicianShifts.date and DoctorShifts.shift = Shifts.name";
         ResultSet result =
            connection.execQuery(query);

         // add shifts to a list in date, fromTime, toTime, name of coworker format
         while(result.next()) {  
            resultLine = "";
            resultLine = resultLine + result.getDate(1);
            resultLine = resultLine + " " + result.getTime(2);
            resultLine = resultLine + " " + result.getTime(3);
            resultLine = resultLine + " " + result.getString(4);
            mySchedule.add(resultLine);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      
      return mySchedule;

   }

   // private String getEmployeeNames() {

   // }

   // private String formatSchedule() {

   // }

   public boolean canChoosePreferredShift() {
      // generate schedule and then find out
      return false;
   }

   // TODO : can choose preferred times
   public void choosePreferredTimes() {

   }

   public boolean canTakeVacation(String username, EmployeeShift shift) {
      return hasVacationDays(); // TODO : && generateSchedule()
   }

   private boolean hasVacationDays() {
      return vacationDays > 0;
   }

   public boolean canTakeSickDay(Employee employee, EmployeeShift shift) {
      return hasSickDays(); // TODO : && generateSchedule()
   }

   private boolean hasSickDays() {
      return sickDays > 0;
   }

  /* 
   * @precondition assumes that sick day is granted;
   */
   public void takeSickDay(EmployeeShift shift) {

      try {
         //update TimeOff
         --sickDays;
         String tablename = getTableName("s");
         String query = "UPDATE " + tablename + " " +
                        "SET sickDays = " + sickDays + " " +
                        "WHERE id = " + id + ";";
         connection.execUpdate(query);

         //update
         tablename = getTableName("TimeOff");
         String date = convertDateToString(shift.getDate());
         Shift genericShift = new Shift(shift.getShift());
         String fromTime = convertTimeToString(genericShift.getFromTime());
         String toTime = convertTimeToString(genericShift.getToTime());
         query = "INSERT INTO "  + tablename + " " +
                      "VALUES (" + id + ", " 
                                 + date + ", "
                                 + fromTime + ", " 
                                 + date + ", "
                                 + toTime + ", "
                                 + "'sickDay'" + ")";
         connection.execUpdate(query);  
      }
      catch (Exception e) {

      }
   }

   private String convertDateToString(Calendar date) {
      SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
      return formatter.format(date.getTime());
   }

   private String convertTimeToString(Time time) {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
      return formatter.format(time.getTime());
   }

   private String getTableName(String secondPart) {
      String tablename = "";

      switch(type) {
         case Employee.DOCTOR:
            tablename = "Doctor" + secondPart;
            break;
         case Employee.TECHNICIAN:
            tablename = "Technician" + secondPart;
            break;
         case Employee.ADMINISTRATOR:
            tablename = "Administrator" + secondPart;
            break;
         default:
            break;
      }

      return tablename;
   }
}