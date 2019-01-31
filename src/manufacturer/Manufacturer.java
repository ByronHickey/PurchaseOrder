/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manufacturer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0006557
 */
public class Manufacturer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //  import scanner
        Scanner kb = new Scanner(System.in);
        
        // declare variables for update statement string
        String custId = "";
        String productId = "";
        String orderNo = "";
        String quantity = "";
        String shippingCost = "";
        String saleDate = "";
        String shippingDate = "";
        String freightCo = "";
        
        // create date formatter for date conversion  
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"); 
          

            // sql queries 
        String sql1 = "SELECT * FROM CUSTOMER";
        String sql2 = "SELECT * From PRODUCT";
        String sql3 = "insert into PURCHASE_ORDER (ORDER_NUM, CUSTOMER_ID, "
                + "PRODUCT_ID, QUANTITY, SHIPPING_COST, SALES_DATE,"
                + "SHIPPING_DATE, FREIGHT_COMPANY)"
                + "values (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            //what does this do exactly
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            // String used for connection
            String jdbc = "jdbc:derby://localhost:1527/sample";
            // connect to db
            Connection conn = DriverManager.getConnection(jdbc, "app", "app");
            
            // pre compile string into prepared sql statement.. is this JDSQL?? 
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            
            
            // execute SQL statement and return data as ResultSet Object
             ResultSet rs = pstmt.executeQuery();
            
            //format headers for table
            String header = String.format("%-5s %-1s %6s %13s %30s %30s"
                    + "%30s %18s %6s %12s %25s %30s  \t\t",
                    "id", "discount", "zip", "name", "addr1", "addr2",
                    "city", "state", "phone", "fax", "email",
                    "credit_limit");
            System.out.println(header);
            
           
           // loop through results and initialise variables for each row 
            while (rs.next()) {
                String id = rs.getString("customer_id");
                String discount = rs.getString("Discount_code");
                String zip = rs.getString("ZIP");
                String name = rs.getString("NAME");
                String addr1 = rs.getString("ADDRESSLINE1");
                String addr2 = rs.getString("ADDRESSLINE2");
                String city = rs.getString("CITY");
                String state = rs.getString("STATE");
                String phone = rs.getString("PHONE");
                String fax = rs.getString("FAX");
                String email = rs.getString("EMAIL");
                String cl = rs.getString("CREDIT_LIMIT");
                // format the table
                String format = String.format("%-10s %-4s %-8s %-30s %-30s %-30s"
                        + "%-25s %-2s %-12s %-12s %-40s %-10s  \t\t",
                        id, discount, zip, name, addr1, addr2, city, state, phone,
                        fax, email, cl);
                //print table
                System.out.println(format);

            }
            
            //get user to choose customer id
            System.out.println("What is the Customer ID");
            custId = kb.nextLine();
            
            //prepare 2nd sql statement
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            
            // get sql results again
            ResultSet rs2 = pstmt2.executeQuery();
            
            //format table
            String header2 = String.format("%10s %10s %2s %8s %4s %10s"
                    + "%13s %16s \t\t",
                    "product_id", "Manufacturer_id", "product_code", "purchase_cost",
                    "QOH", "Markup", "Availability", "Description",
                    "credit_limit");
            System.out.println(header2);
            
             // loop through results and initialise variables for each row 
            while (rs2.next()) {
                String id = rs2.getString("product_id");
                String manId = rs2.getString("manufacturer_id");
                String pCode = rs2.getString("product_code");
                String pCost = rs2.getString("purchase_cost");
                String qoh = rs2.getString("quantity_on_hand");
                String markup = rs2.getString("markup");
                String available = rs2.getString("available");
                String description = rs2.getString("description");
                String format = String.format("%-10s %-15s %-12s %-12s %-10s "
                        + "%-8s %-10s %-50s \t\t", id, manId, pCode, pCost, qoh,
                        markup, available, description);
                System.out.println(format);

            }
            //get purchase order information
            System.out.println("What is the product ID");
            productId = kb.nextLine();
            System.out.println("What is the order number");
            orderNo = kb.nextLine();
            System.out.println("What is the quantity");
            quantity = kb.nextLine();
            System.out.println("What is the Shipping Cost");
            shippingCost = kb.nextLine();
            System.out.println("What is the sale date ues dd-MM-yyyy format");
            saleDate = kb.nextLine();
            System.out.println("What is the shipping date ues dd-MM-yyyy format");
            shippingDate = kb.nextLine();
            System.out.println("What is the Freight company name");
            freightCo = kb.nextLine();
            
              
            // return a Date format object with the pattern set during inititalisation
            java.util.Date dateSale = sdf1.parse(saleDate); 
            java.sql.Date sqlSaleDate = new java.sql.Date(dateSale.getTime());
            
             java.util.Date dateShipping = sdf1.parse(shippingDate); 
            java.sql.Date sqlShippingDate = new java.sql.Date(dateShipping.getTime());
            
           
             // load prepared statement with the arguments for the ? values in sql3        
      PreparedStatement preparedStmt = conn.prepareStatement(sql3);
      preparedStmt.setString (1, orderNo);
      preparedStmt.setString (2, custId);
      preparedStmt.setString (3, productId);
      preparedStmt.setString (4, quantity);
      preparedStmt.setString (5, shippingCost);
      preparedStmt.setDate   (6, sqlSaleDate);
      preparedStmt.setDate   (7, sqlShippingDate);
      preparedStmt.setString (8, freightCo);
      

      // update the table in the database
            preparedStmt.execute();
            conn.close();
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Manufacturer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Manufacturer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Manufacturer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
