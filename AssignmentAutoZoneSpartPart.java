/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autozonespareparts;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author DishtiChitapain
 */
public class AssignmentAutoZoneSpartPart extends javax.swing.JFrame {

    String filename = null;
    int catid;
    DefaultTableModel model;
    byte[] person_image;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int pid;

    /**
     * Creates new form AssignmentAtoZoneSpartPart
     */
    public AssignmentAutoZoneSpartPart() {
        initComponents();
        con = AutoZoneSparePart.connectDB();
        Toolkit tk = Toolkit.getDefaultToolkit();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.person_image = null;
        GetToday();
        setRecordToTable();
        getcatid();
        getpurchaseid();
        FillCombo();
        SPRecordToTable();
        setCustRecordToTable();
        setCatRecordToTable();
        setsparepartspRecordToTable();
        setsupplierspRecordToTable();
        FillSupplier();
        FillSparepart();
        updateSupplierpartTable();
        SupplierPartRecordToTable();
        FillCustomer();
        FillSP1();
        setcustomerspRecordToTable();
        setsparepartsp1RecordToTable();
        updateCustomerPurchaseTable();
        FillCatCombo();
        filtercattable();
        updateCategoryTable();
        updateSupplierTable();
        updateCustomerTable();
        updateSparePartTable();
    }

    private void GetToday(){
       DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
       LocalDateTime now = LocalDateTime.now();
       datelbl.setText(dtf.format(now));
    }
    
    
    private void getcatid() {
        try {
            String catsql = "select max(CATEGORYID) from CATEGORY";
            ps = con.prepareStatement(catsql);
            ps.execute();
            if (rs.next()) {
                catid = rs.getInt(1);
                catid++;
                CATEGORYID.setText(Integer.toString(catid));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void getpurchaseid() {
        try {
            String psql = "select max(PURCHASEID) from CUSTOMERPURCHASE";
            ps = con.prepareStatement(psql);
            ps.execute();
            if (rs.next()) {
                pid = rs.getInt(1);
                pid++;
                purchaseidtxt.setText(Integer.toString(pid));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void updateCategoryTable() {
        try {
            String sql = "select * from CATEGORY";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            cattable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Category table error: " + e);
        }
    }

    public void catsearch(String str) {
        model = (DefaultTableModel) cattable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        cattable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }

    private void FillCombo() {
        try {
            String catcombosql = "select * from CATEGORY";
            ps = con.prepareStatement(catcombosql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ddlcat.addItem(rs.getString("CATEGORYID"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void FillSupplier() {
        try {
            String suppsql = "select * from SUPPLIER";
            ps = con.prepareStatement(suppsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ddlsupsp.addItem(rs.getString("SUPNIC"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillSparepart() {
        try {
            String sparepsql = "select * from SPAREPART";
            ps = con.prepareStatement(sparepsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ddlsnsp.addItem(rs.getString("SERIALNUMBER"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void updateSparePartTable() {
        try {
            String sql = "select * from SPAREPART";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            spareparttable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Spare Part table error: " + e);
        }
    }

    private void SPRecordToTable(){
        try {
          String sql="select * from SPAREPART";
          ps=con.prepareStatement(sql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String cat = rs.getString("CATEGORYID");
              String snum = rs.getString("SERIALNUMBER");
              String spn = rs.getString("PARTNAME");
              String pp = rs.getString("PURCHASEPRICE");
              String sp = rs.getString("SELLINGPRICE");
              String pd = rs.getString("PURCHASEDATE");
              String desc = rs.getString("DESCRIPTION");
              Object[] obj = {cat, snum, spn, pp, sp, pd, desc};
              model = (DefaultTableModel)spsearchtable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Spare part table error: " + e);
       } 
    }
    
    public void search (String str){
        model =(DefaultTableModel)spsearchtable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        spsearchtable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    
    //to store supplier database result in an ArrayList Method
    public ArrayList<AutoSpart> supplierlist() {
        ArrayList<AutoSpart> supplierlists = new ArrayList<AutoSpart>();
        String supsql = "select * from SUPPLIER";
        AutoSpart supplier = new AutoSpart();

        try {
            ps = con.prepareStatement(supsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                supplier.setSUPNIC(rs.getString("SUPNIC"));
                supplier.setSUPFIRSTNAME(rs.getString("SUPFIRSTNAME"));
                supplier.setSUPLASTNAME(rs.getString("SUPLASTNAME"));
                supplier.setSTREETADDRESS(rs.getString("STREETADDRESS"));
                supplier.setCITY(rs.getString("CITY"));
                supplier.setPOSTCODE(rs.getInt("POSTCODE"));
                supplier.setPHONE(rs.getInt("PHONE"));
                supplier.setEMAIL(rs.getString("EMAIL"));
                supplier.setDATEOFBIRTH(rs.getString("DATEOFBIRTH"));
                supplier.setPROFILEPHOTO(rs.getBytes("PROFILEPHOTO"));
                supplierlists.add(supplier);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return supplierlists;
    }

    private void updateSupplierTable() {
        try {
            String sql = "select * from SUPPLIER";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            suptable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Supplier table error: " + e);
        }
    }

    private void setRecordToTable() {
       try {
          String sql="select * from SUPPLIER";
          ps=con.prepareStatement(sql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String supid = rs.getString("SUPNIC");
              String supfn = rs.getString("SUPFIRSTNAME");
              String supln = rs.getString("SUPLASTNAME");
              String street = rs.getString("STREETADDRESS");
              String scity = rs.getString("CITY");
              String suppc = rs.getString("POSTCODE");
              String supp = rs.getString("PHONE");
              String supemail = rs.getString("EMAIL");
              String dob = rs.getString("DATEOFBIRTH");
              
              Object[] obj = {supid, supfn, supln, street, scity, suppc, supp, supemail, dob};
              model = (DefaultTableModel)suppliertable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Supplier table error: " + e);
       }
    }

    public void searchSupplier(String str) {
        model = (DefaultTableModel) suppliertable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        suppliertable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    
    //to store customer database result in an ArrayList Method
    public ArrayList<AutoSpart> customerlist() {
        ArrayList<AutoSpart> customerlists = new ArrayList<AutoSpart>();
        String supsql = "select * from CUSTOMER";
        AutoSpart customer = new AutoSpart();

        try {
            ps = con.prepareStatement(supsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                customer.setSUPNIC(rs.getString("CUSTNIC"));
                customer.setSUPFIRSTNAME(rs.getString("CUSTFIRSTNAME"));
                customer.setSUPLASTNAME(rs.getString("CUSTLASTNAME"));
                customer.setSTREETADDRESS(rs.getString("STREETADDRESS"));
                customer.setCITY(rs.getString("CITY"));
                customer.setPOSTCODE(rs.getInt("POSTCODE"));
                customer.setPHONE(rs.getInt("PHONE"));
                customer.setEMAIL(rs.getString("EMAIL"));
                customer.setDATEOFBIRTH(rs.getString("DATEOFBIRTH"));
                customer.setPROFILEPHOTO(rs.getBytes("PROFILEPHOTO"));
                customerlists.add(customer);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return customerlists;
    }

    private void updateCustomerTable() {
        try {
            String sql = "select * from CUSTOMER";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            custtable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Customer table error: " + e);
        }
    }

    private void setCustRecordToTable() {
       try {
          String sql="select * from CUSTOMER";
          ps=con.prepareStatement(sql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String cid = rs.getString("CUSTNIC");
              String cfn = rs.getString("CUSTFIRSTNAME");
              String cln = rs.getString("CUSTLASTNAME");
              String cstreet = rs.getString("STREETADDRESS");
              String ccity = rs.getString("CITY");
              String cpc = rs.getString("POSTCODE");
              String cp = rs.getString("PHONE");
              String cemail = rs.getString("EMAIL");
              String cdob = rs.getString("DATEOFBIRTH");
              
              Object[] obj = {cid, cfn, cln, cstreet, ccity, cpc, cp, cemail, cdob};
              model = (DefaultTableModel)ctable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Customer table error: " + e);
       }
    }

    public void searchCustomer(String str) {
        model = (DefaultTableModel) ctable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        ctable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    
    private void setCatRecordToTable(){
        try {
          String sql="select * from CATEGORY";
          ps=con.prepareStatement(sql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String cat = rs.getString("CATEGORYID");
              String catname = rs.getString("CATNAME");
              Object[] obj = {cat, catname};
              model = (DefaultTableModel)scattable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Category table error: " + e);
       } 
    }
    
    //to store SPAREPART database result in an ArrayList Method
    public ArrayList<AutoSpart> sparepartlist() {
        ArrayList<AutoSpart> sparepartlists = new ArrayList<AutoSpart>();
        String supsql = "select * from SUPPLIER";
        AutoSpart sp = new AutoSpart();

        try {
            ps = con.prepareStatement(supsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sp.setcatid(rs.getInt("CATEGORYID"));
                sp.setserialnum(rs.getString("SERIALNUMBER"));
                sp.setname(rs.getString("PARTNAME"));
                sp.setpprice(rs.getDouble("PURCHASEPRICE"));
                sp.setsprice(rs.getDouble("SELLINGPRICE"));
                sp.setquantity(rs.getInt("QUANTITYAVAILABLE"));
                sp.setdoob(rs.getString("PURCHASEDATE"));
                sp.setdesc(rs.getString("DESCRIPTION"));
                sp.setPHOTO(rs.getBytes("PHOTO"));
                sparepartlists.add(sp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return sparepartlists;
    }
    
    private void setsparepartspRecordToTable(){
        try {
          String Ssql="select SERIALNUMBER, PARTNAME, QUANTITYAVAILABLE from SPAREPART";
          ps=con.prepareStatement(Ssql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String snum = rs.getString("SERIALNUMBER");
              String spn = rs.getString("PARTNAME");
              String qa = rs.getString("QUANTITYAVAILABLE");
              Object[] obj = {snum, spn, qa};
              model = (DefaultTableModel)sparepartsptable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Spare part table error: " + e);
       } 
    }
    
    private void setsupplierspRecordToTable() {
       try {
          String sql="select SUPNIC, SUPFIRSTNAME, SUPLASTNAME, EMAIL from SUPPLIER";
          ps=con.prepareStatement(sql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String supid = rs.getString("SUPNIC");
              String supfn = rs.getString("SUPFIRSTNAME");
              String supln = rs.getString("SUPLASTNAME");
              String supemail = rs.getString("EMAIL");
              
              Object[] obj = {supid, supfn, supln, supemail};
              model = (DefaultTableModel)suppliersptable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Supplier table error: " + e);
       }
    }

    private void updateSupplierpartTable() {
        try {
            String sql = "select * from SUPPLIERPART";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            supparttable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Supplier part table error: " + e);
        }
    }
    
    private void SupplierPartRecordToTable(){
        try {
          String sql="select * from SUPPLIERPART";
          ps=con.prepareStatement(sql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String suppnic = rs.getString("SUPNIC");
              String serialnum = rs.getString("SERIALNUMBER");
              String qunt = rs.getString("QUANTITYSUPPLIED");
              String dates = rs.getString("DATESUPPLIED");
              Object[] obj = {suppnic, serialnum, qunt, dates};
              model = (DefaultTableModel)suppartsearchtable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Supplier part table error: " + e);
       } 
    }
    
    public void searchSupplierPart (String str){
        model =(DefaultTableModel)suppartsearchtable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        suppartsearchtable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    
    private void FillCustomer() {
        try {
            String customerpsql = "select * from CUSTOMER";
            ps = con.prepareStatement(customerpsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ddlcustomer.addItem(rs.getString("CUSTNIC"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillSP1() {
        try {
            String spppsql = "select * from SPAREPART";
            ps = con.prepareStatement(spppsql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ddlserialnumber1.addItem(rs.getString("SERIALNUMBER"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void setcustomerspRecordToTable(){
        try {
          String Csql="select CUSTNIC, CUSTFIRSTNAME, CUSTLASTNAME from CUSTOMER";
          ps=con.prepareStatement(Csql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String cnic = rs.getString("CUSTNIC");
              String fn = rs.getString("CUSTFIRSTNAME");
              String ln = rs.getString("CUSTLASTNAME");
              Object[] obj = {cnic, fn, ln};
              model = (DefaultTableModel)custsptable.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Customer table error: " + e);
       } 
    }
    
    private void setsparepartsp1RecordToTable(){
        try {
          String Spsql="select SERIALNUMBER, PARTNAME, PURCHASEPRICE, SELLINGPRICE from SPAREPART";
          ps=con.prepareStatement(Spsql);
          rs=ps.executeQuery();   
          
          while(rs.next()){
              String snum = rs.getString("SERIALNUMBER");
              String spn = rs.getString("PARTNAME");
              String pp = rs.getString("PURCHASEPRICE");
              String sp = rs.getString("SELLINGPRICE");
              Object[] obj = {snum, spn, pp, sp};
              model = (DefaultTableModel)sparepartsptable1.getModel();
              model.addRow(obj);
          }
          
        }catch(Exception e){
           JOptionPane.showMessageDialog(null, "Spare part table error: " + e);
       } 
    }
    
    private void updateCustomerPurchaseTable() {
        try {
            String cpsql = "select * from CUSTOMERPURCHASE";
            ps = con.prepareStatement(cpsql);
            rs = ps.executeQuery();
            purchasetable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Customer Purchase table error: " + e);
        }
    }
    
    private void filtercattable() {
      try{
        String catt = "SELECT CATEGORYID, CATNAME, PARTNAME FROM " + "CATEGORY" + " NATURAL JOIN " + "SPAREPART";
        ps = con.prepareStatement(catt);
        rs = ps.executeQuery();
        filtertable.setModel(DbUtils.resultSetToTableModel(rs));
      }  catch(Exception e){
          JOptionPane.showMessageDialog(null, e);
      }
    }
    
    private void FillCatCombo() {
        try {
            String catcombosql = "select * from CATEGORY";
            ps = con.prepareStatement(catcombosql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ddlscat.addItem(rs.getString("CATEGORYID"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void catFilter(String str) {
        model = (DefaultTableModel) filtertable.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        filtertable.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        suppropic = new javax.swing.JLabel();
        supnictxt = new javax.swing.JTextField();
        supfntxt = new javax.swing.JTextField();
        suplntxt = new javax.swing.JTextField();
        supstreet = new javax.swing.JTextField();
        supcity = new javax.swing.JTextField();
        suppostcodetxt = new javax.swing.JTextField();
        suppnumtxt = new javax.swing.JTextField();
        supemailtxt = new javax.swing.JTextField();
        supdob = new com.toedter.calendar.JDateChooser();
        attachsupppbtn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        suptable = new javax.swing.JTable();
        addsupbtn = new javax.swing.JButton();
        updatesupbtn = new javax.swing.JButton();
        deletesupbtn = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        suppliertable = new javax.swing.JTable();
        searchsuptxt = new javax.swing.JTextField();
        searchsup = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        custfnametxt = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        custpropic = new javax.swing.JLabel();
        custnictxt = new javax.swing.JTextField();
        custfn = new javax.swing.JTextField();
        custlnametxt = new javax.swing.JTextField();
        custstreettxt = new javax.swing.JTextField();
        custcitytxt = new javax.swing.JTextField();
        custpostcodetxt = new javax.swing.JTextField();
        custphonetxt = new javax.swing.JTextField();
        custemailtxt = new javax.swing.JTextField();
        attachcustppbtn = new javax.swing.JButton();
        custdob = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        custtable = new javax.swing.JTable();
        addnewcustbtn = new javax.swing.JButton();
        updatecustbtn = new javax.swing.JButton();
        deletecustbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        searchcusttxt = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        ctable = new javax.swing.JTable();
        searchcust = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        catnametxt = new javax.swing.JTextField();
        savecatbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cattable = new javax.swing.JTable();
        sortcatinalorbtn = new javax.swing.JButton();
        sortcatindescorbtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cattypesearch = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        CATEGORYID = new javax.swing.JTextField();
        searchcat = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        ddlscat = new javax.swing.JComboBox<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        filtertable = new javax.swing.JTable();
        filtersp = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        spsearchtxt = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        spsearchtable = new javax.swing.JTable();
        searchsp = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        sppic = new javax.swing.JLabel();
        ddlcat = new javax.swing.JComboBox<>();
        snumtxt = new javax.swing.JTextField();
        spntxt = new javax.swing.JTextField();
        pptxt = new javax.swing.JTextField();
        sptxt = new javax.swing.JTextField();
        qatxt = new javax.swing.JTextField();
        desctxt = new javax.swing.JTextField();
        spattchbtn = new javax.swing.JButton();
        addsparespartbtn = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        spareparttable = new javax.swing.JTable();
        updatesparepart = new javax.swing.JButton();
        deletesparepart = new javax.swing.JButton();
        sortspinalorbtn = new javax.swing.JButton();
        sortindescordbtn = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        scattable = new javax.swing.JTable();
        pdate = new com.toedter.calendar.JDateChooser();
        jPanel10 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        supplierparttxt = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        suppartsearchtable = new javax.swing.JTable();
        searchspp = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        ddlsupsp = new javax.swing.JComboBox<>();
        ddlsnsp = new javax.swing.JComboBox<>();
        qstxt = new javax.swing.JTextField();
        datesupplied = new com.toedter.calendar.JDateChooser();
        addnewsuppartbtn = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        supparttable = new javax.swing.JTable();
        sortspinalporbtn = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        suppliersptable = new javax.swing.JTable();
        jScrollPane14 = new javax.swing.JScrollPane();
        sparepartsptable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        ddlcustomer = new javax.swing.JComboBox<>();
        ddlserialnumber1 = new javax.swing.JComboBox<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        purchasetable = new javax.swing.JTable();
        qpurchsedtxt = new javax.swing.JTextField();
        datepur = new com.toedter.calendar.JDateChooser();
        paymenttxt = new javax.swing.JTextField();
        addpurbtn = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        custsptable = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        sparepartsptable1 = new javax.swing.JTable();
        jLabel55 = new javax.swing.JLabel();
        purchaseidtxt = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        datelbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("AutoZone Spare Parts");

        jTabbedPane1.setForeground(new java.awt.Color(0, 51, 153));

        jTabbedPane3.setForeground(new java.awt.Color(0, 0, 153));

        jPanel2.setForeground(new java.awt.Color(0, 51, 153));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 153));
        jLabel20.setText("Create a new supplier :-");

        jLabel21.setForeground(new java.awt.Color(0, 0, 153));
        jLabel21.setText("Supplier NIC: ");

        jLabel22.setForeground(new java.awt.Color(0, 0, 153));
        jLabel22.setText("First Name: ");

        jLabel23.setForeground(new java.awt.Color(0, 0, 153));
        jLabel23.setText("Last Name: ");

        jLabel24.setForeground(new java.awt.Color(0, 0, 153));
        jLabel24.setText("Street: ");

        jLabel25.setForeground(new java.awt.Color(0, 0, 153));
        jLabel25.setText("City: ");

        jLabel26.setForeground(new java.awt.Color(0, 0, 153));
        jLabel26.setText("Postcode: ");

        jLabel27.setForeground(new java.awt.Color(0, 0, 153));
        jLabel27.setText("Phone number: ");

        jLabel28.setForeground(new java.awt.Color(0, 0, 153));
        jLabel28.setText("Email: ");

        jLabel29.setForeground(new java.awt.Color(0, 0, 153));
        jLabel29.setText("DateBirth: ");

        attachsupppbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        attachsupppbtn.setForeground(new java.awt.Color(0, 51, 153));
        attachsupppbtn.setText("Attach");
        attachsupppbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachsupppbtnActionPerformed(evt);
            }
        });

        suptable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        suptable.setForeground(new java.awt.Color(0, 0, 102));
        suptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "FirstName", "LastName", "Street", "City", "Postcode", "Phone", "Email", "DOB", "Pic"
            }
        ));
        suptable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suptableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(suptable);

        addsupbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addsupbtn.setForeground(new java.awt.Color(0, 0, 153));
        addsupbtn.setText("Add New Supplier");
        addsupbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addsupbtnActionPerformed(evt);
            }
        });

        updatesupbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        updatesupbtn.setForeground(new java.awt.Color(0, 0, 153));
        updatesupbtn.setText("Update Supplier");
        updatesupbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatesupbtnActionPerformed(evt);
            }
        });

        deletesupbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deletesupbtn.setForeground(new java.awt.Color(0, 0, 153));
        deletesupbtn.setText("Delete Supplier");
        deletesupbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletesupbtnActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 0, 153));
        jButton6.setText("Sort Supplier in alphabetical order");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 0, 153));
        jButton7.setText("Sort Supplier in descending order");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(suppropic, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supnictxt)
                            .addComponent(supfntxt)
                            .addComponent(suplntxt)
                            .addComponent(supstreet)
                            .addComponent(supcity)
                            .addComponent(suppostcodetxt)
                            .addComponent(suppnumtxt)
                            .addComponent(supemailtxt)
                            .addComponent(supdob, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(attachsupppbtn))))
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(addsupbtn)
                        .addGap(113, 113, 113)
                        .addComponent(updatesupbtn)
                        .addGap(90, 90, 90)
                        .addComponent(deletesupbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE))
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(supnictxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(supfntxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(suplntxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(supstreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(supcity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(suppostcodetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(suppnumtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(supemailtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(supdob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(suppropic, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(attachsupppbtn)
                            .addComponent(addsupbtn)
                            .addComponent(updatesupbtn)
                            .addComponent(deletesupbtn))))
                .addContainerGap())
        );

        jTabbedPane3.addTab("Manage Supplier", jPanel2);

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 153));
        jLabel30.setText("Search Supplier by Name: ");

        suppliertable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        suppliertable.setForeground(new java.awt.Color(0, 0, 102));
        suppliertable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "FirstName", "LastName", "Street", "City", "Postcode", "Phone", "Email", "DateBirth"
            }
        ));
        jScrollPane5.setViewportView(suppliertable);
        if (suppliertable.getColumnModel().getColumnCount() > 0) {
            suppliertable.getColumnModel().getColumn(8).setResizable(false);
        }

        searchsuptxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchsuptxtKeyReleased(evt);
            }
        });

        searchsup.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        searchsup.setForeground(new java.awt.Color(0, 0, 153));
        searchsup.setText("Search");
        searchsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchsupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchsup)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(searchsuptxt, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(searchsuptxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(searchsup)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Search Supplier", jPanel8);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Supplier ", jPanel3);

        jTabbedPane2.setForeground(new java.awt.Color(0, 0, 102));

        custfnametxt.setForeground(new java.awt.Color(0, 0, 153));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("Create a new Customer :-");

        jLabel10.setForeground(new java.awt.Color(0, 0, 153));
        jLabel10.setText("Customer NIC: ");

        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("First Name: ");

        jLabel12.setForeground(new java.awt.Color(0, 0, 153));
        jLabel12.setText("Last Name: ");

        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("Street: ");

        jLabel14.setForeground(new java.awt.Color(0, 0, 153));
        jLabel14.setText("City: ");

        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("Postcode: ");

        jLabel16.setForeground(new java.awt.Color(0, 0, 153));
        jLabel16.setText("Phone number: ");

        jLabel17.setForeground(new java.awt.Color(0, 0, 153));
        jLabel17.setText("Email: ");

        jLabel18.setForeground(new java.awt.Color(0, 0, 153));
        jLabel18.setText("DateBirth: ");

        attachcustppbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        attachcustppbtn.setForeground(new java.awt.Color(0, 0, 204));
        attachcustppbtn.setText("Attach");
        attachcustppbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachcustppbtnActionPerformed(evt);
            }
        });

        custtable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        custtable.setForeground(new java.awt.Color(0, 51, 102));
        custtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "FirstName", "LastName", "Street", "City", "Postcode", "Phone", "Email", "DOB", "Pic"
            }
        ));
        custtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                custtableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(custtable);

        addnewcustbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addnewcustbtn.setForeground(new java.awt.Color(0, 0, 153));
        addnewcustbtn.setText("Add New Customer");
        addnewcustbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addnewcustbtnActionPerformed(evt);
            }
        });

        updatecustbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        updatecustbtn.setForeground(new java.awt.Color(0, 0, 153));
        updatecustbtn.setText("Update Customer");
        updatecustbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatecustbtnActionPerformed(evt);
            }
        });

        deletecustbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deletecustbtn.setForeground(new java.awt.Color(0, 0, 153));
        deletecustbtn.setText("Delete Customer");
        deletecustbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletecustbtnActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 51, 153));
        jButton1.setText("Sort Customer in alphabetical order");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 51, 153));
        jButton2.setText("Sort Customer in descending order");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout custfnametxtLayout = new javax.swing.GroupLayout(custfnametxt);
        custfnametxt.setLayout(custfnametxtLayout);
        custfnametxtLayout.setHorizontalGroup(
            custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custfnametxtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addComponent(custpropic, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(attachcustppbtn))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(custnictxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(35, 35, 35)
                        .addComponent(custfn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(37, 37, 37)
                        .addComponent(custlnametxt))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(45, 45, 45)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(custemailtxt)
                            .addComponent(custdob, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addGap(14, 14, 14)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(custphonetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(custpostcodetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(62, 62, 62)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(custcitytxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(custstreettxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addComponent(addnewcustbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(updatecustbtn)
                        .addGap(68, 68, 68)
                        .addComponent(deletecustbtn)
                        .addGap(0, 128, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, custfnametxtLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        custfnametxtLayout.setVerticalGroup(
            custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custfnametxtLayout.createSequentialGroup()
                .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(custnictxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(custfn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(custlnametxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(custstreettxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(custcitytxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(custpostcodetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(custphonetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(custemailtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(custdob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(custpropic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(custfnametxtLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addGroup(custfnametxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(attachcustppbtn)
                            .addComponent(addnewcustbtn)
                            .addComponent(updatecustbtn)
                            .addComponent(deletecustbtn))))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Manage Customer", custfnametxt);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 153));
        jLabel19.setText("Search Customer by Name: ");

        searchcusttxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchcusttxtKeyReleased(evt);
            }
        });

        ctable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ctable.setForeground(new java.awt.Color(0, 0, 102));
        ctable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "FirstName", "LastName", "Street", "City", "Postcode", "Phone", "Email", "DateBirth"
            }
        ));
        jScrollPane3.setViewportView(ctable);

        searchcust.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        searchcust.setForeground(new java.awt.Color(0, 0, 102));
        searchcust.setText("Search");
        searchcust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchcustActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchcust)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(searchcusttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(569, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchcusttxt))
                .addGap(18, 18, 18)
                .addComponent(searchcust)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Search Customer", jPanel9);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Customer", jPanel4);

        jLabel5.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 102));
        jLabel5.setText("Create a new category: ");

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 102));
        jLabel6.setText("Category Name: ");

        savecatbtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        savecatbtn.setForeground(new java.awt.Color(0, 0, 153));
        savecatbtn.setText("Save");
        savecatbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savecatbtnActionPerformed(evt);
            }
        });

        cattable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cattable.setForeground(new java.awt.Color(0, 0, 153));
        cattable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category Id", "Category Name"
            }
        ));
        jScrollPane1.setViewportView(cattable);

        sortcatinalorbtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sortcatinalorbtn.setForeground(new java.awt.Color(0, 51, 153));
        sortcatinalorbtn.setText("Sort Category in alphabetical order");
        sortcatinalorbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortcatinalorbtnActionPerformed(evt);
            }
        });

        sortcatindescorbtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sortcatindescorbtn.setForeground(new java.awt.Color(0, 51, 153));
        sortcatindescorbtn.setText("Sort Category in descending order");
        sortcatindescorbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortcatindescorbtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Search for a category in the autozone databse:-");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("Type your Category: ");

        cattypesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cattypesearchKeyReleased(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 102));
        jLabel54.setText("Category ID: ");

        CATEGORYID.setEnabled(false);

        searchcat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        searchcat.setForeground(new java.awt.Color(0, 0, 153));
        searchcat.setText("Search");
        searchcat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchcatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sortcatindescorbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sortcatinalorbtn)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(savecatbtn)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel54))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(catnametxt, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                .addComponent(CATEGORYID))))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(51, 51, 51))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchcat)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(cattypesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CATEGORYID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cattypesearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(catnametxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(savecatbtn)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(searchcat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(sortcatinalorbtn)
                .addGap(18, 18, 18)
                .addComponent(sortcatindescorbtn)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        jTabbedPane1.addTab("Category", jPanel6);

        jTabbedPane4.setForeground(new java.awt.Color(0, 0, 153));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 51, 153));
        jLabel40.setText("Filter Spare part by category: ");

        filtertable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        filtertable.setForeground(new java.awt.Color(0, 0, 102));
        filtertable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CategoryId", "Category Name", "Spare Part Name"
            }
        ));
        jScrollPane7.setViewportView(filtertable);

        filtersp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        filtersp.setForeground(new java.awt.Color(0, 0, 102));
        filtersp.setText("Filter");
        filtersp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterspActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ddlscat, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(320, 320, 320))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(215, 215, 215)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filtersp)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(230, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(ddlscat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(filtersp)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Filter by Category", jPanel12);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 51, 102));
        jLabel41.setText("Search Spare Part by Name: ");

        spsearchtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spsearchtxtKeyReleased(evt);
            }
        });

        spsearchtable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spsearchtable.setForeground(new java.awt.Color(0, 0, 153));
        spsearchtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CategoryId", "Serial Number", "Name", "Purchase Price", "Selling Price", "Date", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(spsearchtable);

        searchsp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        searchsp.setForeground(new java.awt.Color(0, 0, 153));
        searchsp.setText("Search");
        searchsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchspActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchsp)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addGap(18, 18, 18)
                                .addComponent(spsearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(570, Short.MAX_VALUE))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(spsearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchsp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Search Spare Part", jPanel13);

        jPanel11.setForeground(new java.awt.Color(51, 0, 153));

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 153));
        jLabel31.setText("Create a new spare part :-");

        jLabel32.setForeground(new java.awt.Color(51, 0, 153));
        jLabel32.setText("Choose Category: ");

        jLabel33.setForeground(new java.awt.Color(51, 0, 153));
        jLabel33.setText("Serial Number: ");

        jLabel34.setForeground(new java.awt.Color(51, 0, 153));
        jLabel34.setText("Spare Part Name: ");

        jLabel35.setForeground(new java.awt.Color(51, 0, 153));
        jLabel35.setText("Purchase Price: ");

        jLabel36.setForeground(new java.awt.Color(51, 0, 153));
        jLabel36.setText("Selling Price: ");

        jLabel37.setForeground(new java.awt.Color(51, 0, 153));
        jLabel37.setText("Quantity Available: ");

        jLabel38.setForeground(new java.awt.Color(51, 0, 153));
        jLabel38.setText("Purchase Date: ");

        jLabel39.setForeground(new java.awt.Color(51, 0, 153));
        jLabel39.setText("Description: ");

        spattchbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spattchbtn.setForeground(new java.awt.Color(0, 0, 153));
        spattchbtn.setText("Attach");
        spattchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spattchbtnActionPerformed(evt);
            }
        });

        addsparespartbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addsparespartbtn.setForeground(new java.awt.Color(0, 0, 102));
        addsparespartbtn.setText("Add New Spare Part");
        addsparespartbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addsparespartbtnActionPerformed(evt);
            }
        });

        spareparttable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spareparttable.setForeground(new java.awt.Color(0, 51, 102));
        spareparttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Serial Number", "Name", "Price", "Quantity", "Date", "Description", "Photo"
            }
        ));
        spareparttable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spareparttableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(spareparttable);

        updatesparepart.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        updatesparepart.setForeground(new java.awt.Color(0, 0, 153));
        updatesparepart.setText("Update Spare Part");
        updatesparepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatesparepartActionPerformed(evt);
            }
        });

        deletesparepart.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deletesparepart.setForeground(new java.awt.Color(0, 0, 102));
        deletesparepart.setText("Delete Spare Part");
        deletesparepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletesparepartActionPerformed(evt);
            }
        });

        sortspinalorbtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sortspinalorbtn.setForeground(new java.awt.Color(0, 51, 102));
        sortspinalorbtn.setText("Sort Spare Part in alphabetical order");
        sortspinalorbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortspinalorbtnActionPerformed(evt);
            }
        });

        sortindescordbtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sortindescordbtn.setForeground(new java.awt.Color(0, 0, 102));
        sortindescordbtn.setText("Sort Spare Part in descending order");
        sortindescordbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortindescordbtnActionPerformed(evt);
            }
        });

        scattable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        scattable.setForeground(new java.awt.Color(0, 0, 102));
        scattable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category Id", "Category Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(scattable);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addsparespartbtn))
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37)
                            .addComponent(jLabel39)
                            .addComponent(jLabel32)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(sppic, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(snumtxt)
                            .addComponent(spntxt)
                            .addComponent(pptxt)
                            .addComponent(sptxt)
                            .addComponent(qatxt)
                            .addComponent(desctxt)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addGap(22, 22, 22)
                                        .addComponent(spattchbtn))
                                    .addComponent(ddlcat, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 119, Short.MAX_VALUE))
                            .addComponent(pdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(updatesparepart)
                        .addGap(124, 124, 124)
                        .addComponent(deletesparepart)
                        .addGap(164, 164, 164))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sortspinalorbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sortindescordbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(ddlcat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(snumtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(spntxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(pptxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(sptxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(qatxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(pdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(desctxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sppic, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spattchbtn)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(sortspinalorbtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sortindescordbtn))
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addsparespartbtn)
                    .addComponent(updatesparepart)
                    .addComponent(deletesparepart))
                .addContainerGap())
        );

        jTabbedPane4.addTab("Manage Spare Part", jPanel11);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Spare Part", jPanel7);

        jTabbedPane5.setForeground(new java.awt.Color(0, 0, 102));

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 0, 102));
        jLabel47.setText("Search Supplier Part by Supplier NIC: ");

        supplierparttxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supplierparttxtKeyReleased(evt);
            }
        });

        suppartsearchtable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        suppartsearchtable.setForeground(new java.awt.Color(0, 0, 102));
        suppartsearchtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "Serial Number", "Quantity Supplied", "Date Supplied"
            }
        ));
        jScrollPane10.setViewportView(suppartsearchtable);

        searchspp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        searchspp.setForeground(new java.awt.Color(0, 0, 102));
        searchspp.setText("Search");
        searchspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchsppActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchspp)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addGap(18, 18, 18)
                        .addComponent(supplierparttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(238, 238, 238))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(193, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(supplierparttxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(searchspp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Search Supplier Part", jPanel15);

        jPanel14.setForeground(new java.awt.Color(0, 0, 153));

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 153));
        jLabel42.setText("Create a new supplier part :-");

        jLabel43.setForeground(new java.awt.Color(0, 0, 153));
        jLabel43.setText("Choose supplier: ");

        jLabel44.setForeground(new java.awt.Color(0, 0, 153));
        jLabel44.setText("Choose serial number: ");

        jLabel45.setForeground(new java.awt.Color(0, 0, 153));
        jLabel45.setText("Quantity Supplied: ");

        jLabel46.setForeground(new java.awt.Color(0, 0, 153));
        jLabel46.setText("Date supplied: ");

        addnewsuppartbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addnewsuppartbtn.setForeground(new java.awt.Color(0, 51, 153));
        addnewsuppartbtn.setText("Add New Supplier Part");
        addnewsuppartbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addnewsuppartbtnActionPerformed(evt);
            }
        });

        supparttable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supparttable.setForeground(new java.awt.Color(0, 51, 102));
        supparttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier", "Serial Number", "Quantity", "Date supplied"
            }
        ));
        jScrollPane9.setViewportView(supparttable);

        sortspinalporbtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sortspinalporbtn.setForeground(new java.awt.Color(0, 0, 153));
        sortspinalporbtn.setText("Sort Supplier part in ascending order ");
        sortspinalporbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortspinalporbtnActionPerformed(evt);
            }
        });

        suppliersptable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        suppliersptable.setForeground(new java.awt.Color(0, 0, 102));
        suppliersptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "First Name", "Last Name", "Email"
            }
        ));
        jScrollPane13.setViewportView(suppliersptable);

        sparepartsptable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        sparepartsptable.setForeground(new java.awt.Color(0, 0, 102));
        sparepartsptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial Number", "Part Name", "Quantity Available"
            }
        ));
        jScrollPane14.setViewportView(sparepartsptable);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(addnewsuppartbtn))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel44)
                                            .addComponent(jLabel45)
                                            .addComponent(jLabel46)
                                            .addComponent(jLabel43)
                                            .addComponent(jLabel42)))
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addGap(165, 165, 165)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sortspinalporbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(ddlsnsp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(ddlsupsp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(qstxt)
                                                .addComponent(datesupplied, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(ddlsupsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(ddlsnsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(qstxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addComponent(datesupplied, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addnewsuppartbtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(sortspinalporbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane5.addTab("Manage Supplier Part", jPanel14);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Supplier Part", jPanel10);

        jPanel5.setForeground(new java.awt.Color(0, 0, 153));

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 102));
        jLabel48.setText("Create a new purchase: ");

        jLabel49.setForeground(new java.awt.Color(0, 0, 153));
        jLabel49.setText("Choose Customer: ");

        jLabel50.setForeground(new java.awt.Color(0, 0, 153));
        jLabel50.setText("Choose Serial Number: ");

        jLabel51.setForeground(new java.awt.Color(0, 0, 153));
        jLabel51.setText("Quantity Purchased: ");

        jLabel52.setForeground(new java.awt.Color(0, 0, 153));
        jLabel52.setText("Date Purchased: ");

        jLabel53.setForeground(new java.awt.Color(0, 0, 153));
        jLabel53.setText("Payment: ");

        purchasetable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchasetable.setForeground(new java.awt.Color(0, 0, 102));
        purchasetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "Serial Number", "Quantity ", "Date", "Total Payment"
            }
        ));
        jScrollPane11.setViewportView(purchasetable);

        paymenttxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymenttxtActionPerformed(evt);
            }
        });

        addpurbtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addpurbtn.setForeground(new java.awt.Color(0, 0, 153));
        addpurbtn.setText("Add New Purchase");
        addpurbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addpurbtnActionPerformed(evt);
            }
        });

        custsptable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        custsptable.setForeground(new java.awt.Color(0, 0, 102));
        custsptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIC", "First Name", "Last Name"
            }
        ));
        jScrollPane15.setViewportView(custsptable);

        sparepartsptable1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        sparepartsptable1.setForeground(new java.awt.Color(0, 0, 102));
        sparepartsptable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial Number", "Part name", "Purchase Price", "Selling Price"
            }
        ));
        jScrollPane16.setViewportView(sparepartsptable1);

        jLabel55.setForeground(new java.awt.Color(0, 0, 153));
        jLabel55.setText("Purchase Id: ");

        purchaseidtxt.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(jLabel48)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel50)
                                    .addComponent(jLabel49)
                                    .addComponent(jLabel53)
                                    .addComponent(jLabel51)
                                    .addComponent(jLabel55))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(purchaseidtxt, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ddlcustomer, javax.swing.GroupLayout.Alignment.LEADING, 0, 222, Short.MAX_VALUE)
                                        .addComponent(ddlserialnumber1, javax.swing.GroupLayout.Alignment.LEADING, 0, 222, Short.MAX_VALUE)
                                        .addComponent(qpurchsedtxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                        .addComponent(datepur, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(paymenttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(addpurbtn)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane11)
                        .addGap(10, 10, 10)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                    .addComponent(jScrollPane16))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(purchaseidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(ddlcustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(ddlserialnumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qpurchsedtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel52)
                            .addComponent(datepur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53)
                            .addComponent(paymenttxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addpurbtn))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Customer Purchase", jPanel5);

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Spare Parts for all vehicles");

        datelbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        datelbl.setForeground(new java.awt.Color(0, 0, 153));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(datelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Spare Parts Inventory");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autozonespareparts/logo.jpg"))); // NOI18N
        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jTabbedPane1))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void attachsupppbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachsupppbtnActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(suppropic.getWidth(), suppropic.getHeight(), Image.SCALE_SMOOTH));
        suppropic.setIcon(imageIcon);
        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);

            }
            person_image = bos.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_attachsupppbtnActionPerformed

    private void addsupbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addsupbtnActionPerformed
        // To add a new supplier to the database table of supplier
        try {
            String AddSuppliersql = "insert into SUPPLIER(SUPNIC, SUPFIRSTNAME, SUPLASTNAME, STREETADDRESS, CITY, POSTCODE, PHONE, EMAIL, DATEOFBIRTH, PROFILEPHOTO) values(?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(AddSuppliersql);
            ps.setString(1, supnictxt.getText());
            ps.setString(2, supfntxt.getText());
            ps.setString(3, suplntxt.getText());
            ps.setString(4, supstreet.getText());
            ps.setString(5, supcity.getText());
            ps.setInt(6, Integer.parseInt(suppostcodetxt.getText()));
            ps.setInt(7, Integer.parseInt(suppnumtxt.getText()));
            ps.setString(8, supemailtxt.getText());
            Date d = supdob.getDateEditor().getDate();
            ps.setDate(9, new java.sql.Date(d.getTime()));
            ps.setBytes(10, person_image);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nNew Supplier is added!!!");
            updateSupplierTable();
            supnictxt.setText("");
            supfntxt.setText("");
            suplntxt.setText("");
            supstreet.setText("");
            supcity.setText("");
            suppostcodetxt.setText("");
            suppnumtxt.setText("");
            supemailtxt.setText("");
            supdob.setDate(null);
            suppropic.setIcon(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Insertion Error!!" + e);
        }
    }//GEN-LAST:event_addsupbtnActionPerformed

    private void suptableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suptableMouseClicked
        int i = suptable.getSelectedRow();
        TableModel model = suptable.getModel();
        supnictxt.setText(model.getValueAt(i, 0).toString());
        supfntxt.setText(model.getValueAt(i, 1).toString());
        suplntxt.setText(model.getValueAt(i, 2).toString());
        supstreet.setText(model.getValueAt(i, 3).toString());
        supcity.setText(model.getValueAt(i, 4).toString());
        suppostcodetxt.setText(model.getValueAt(i, 5).toString());
        suppnumtxt.setText(model.getValueAt(i, 6).toString());
        supemailtxt.setText(model.getValueAt(i, 7).toString());
        byte[] img = (supplierlist().get(i).getPROFILEPHOTO());
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(suppropic.getWidth(), suppropic.getHeight(), Image.SCALE_SMOOTH));
        suppropic.setIcon(imageIcon);
    }//GEN-LAST:event_suptableMouseClicked

    private void updatesupbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatesupbtnActionPerformed
      // update a record from the Supplier table 
      String s1 = supnictxt.getText();
      int p = JOptionPane.showConfirmDialog(null, "Are you sure you want to update?", "Update Record", JOptionPane.YES_NO_OPTION);
      if(p==0){
          try{
            String s2, s3, s4;  
            int a, b;

            s2 = supstreet.getText();
            s3 = supcity.getText();
            a = Integer.parseInt(suppostcodetxt.getText());
            b = Integer.parseInt(suppnumtxt.getText());
            s4 = supemailtxt.getText();
            
            String updatesuppliersql = "update SUPPLIER set STREETADDRESS=?,CITY=?, POSTCODE=? ,PHONE=? ,EMAIL=? where SUPNIC=?";
            ps = con.prepareStatement(updatesuppliersql);
            
            ps.setString(1, s2); 
            ps.setString(2, s3); 
            ps.setInt(3, a);
            ps.setInt(4, b);
            ps.setString(5, s4); 
            ps.setString(6, s1); 
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Success!!\nOne record updated!!!");
            updateSupplierTable();
            
          }
          catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Update Error!!" + e);
          }
          
          try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] image = new byte[(int)file.length()];
            fis.read(image);  
            
            String sql ="update SUPPLIER set PROFILEPHOTO = ? where SUPNIC = ?";
            ps = con.prepareStatement(sql);
            ps.setBytes(1, image);
            ps.executeUpdate();
            ps.close();
          }
          catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
          }
      }
    }//GEN-LAST:event_updatesupbtnActionPerformed

    private void deletesupbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletesupbtnActionPerformed
        // delete a record from the supplier table 
        try {
            String deleteSql = "delete from SUPPLIER where SUPNIC=?";
            ps = con.prepareStatement(deleteSql);
            ps.setString(1, supnictxt.getText());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nOne record deleted!!!");
            supnictxt.setText("");
            supfntxt.setText("");
            suplntxt.setText("");
            supstreet.setText("");
            supcity.setText("");
            suppostcodetxt.setText("");
            suppnumtxt.setText("");
            supemailtxt.setText("");
            supdob.setDate(null);
            suppropic.setIcon(null);
            updateSupplierTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Transaction Error: " + e);
        }
    }//GEN-LAST:event_deletesupbtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //to sort supplier in alphabetical order
        try {
            String sql = "select * from SUPPLIER ORDER BY SUPFIRSTNAME";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            suptable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Supplier table error: " + e);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //to sort supplier in descending order  
        try {
            String sql = "select * from SUPPLIER ORDER BY SUPNIC DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            suptable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Supplier table error: " + e);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void searchsuptxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchsuptxtKeyReleased
//        String searchSupplier = searchsuptxt.getText();
//        searchSupplier(searchSupplier);
    }//GEN-LAST:event_searchsuptxtKeyReleased

    private void savecatbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savecatbtnActionPerformed
        try {
            String AddCategorysql = "insert into CATEGORY(CATNAME) values(?)";
            ps = con.prepareStatement(AddCategorysql);
            ps.setString(1, catnametxt.getText());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nNew category is added!!!");
            updateCategoryTable();
            catnametxt.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Insertion Error!!" + e);
        }
    }//GEN-LAST:event_savecatbtnActionPerformed

    private void sortcatinalorbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortcatinalorbtnActionPerformed
        // To sort category in alphabetical order
        try {
            String sql = "select * from CATEGORY ORDER BY CATNAME";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            cattable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Category table error: " + e);
        }
    }//GEN-LAST:event_sortcatinalorbtnActionPerformed

    private void sortcatindescorbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortcatindescorbtnActionPerformed
        // To sort category in descending order
        try {
            String sql = "select * from CATEGORY ORDER BY CATNAME DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            cattable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Category table error: " + e);
        }
    }//GEN-LAST:event_sortcatindescorbtnActionPerformed

    private void cattypesearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cattypesearchKeyReleased
        // To search category by name
//        String searchCategory = cattypesearch.getText();
//        catsearch(searchCategory);
    }//GEN-LAST:event_cattypesearchKeyReleased

    private void spattchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spattchbtnActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(sppic.getWidth(), sppic.getHeight(), Image.SCALE_SMOOTH));
        sppic.setIcon(imageIcon);
        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);

            }
            person_image = bos.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_spattchbtnActionPerformed

    private void addsparespartbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addsparespartbtnActionPerformed
        // To add a new spare part to the database table of spare part
        try {
            String AddSparepartsql = "insert into SPAREPART(CATEGORYID, SERIALNUMBER, PARTNAME, PURCHASEPRICE, SELLINGPRICE, QUANTITYAVAILABLE, PURCHASEDATE, DESCRIPTION, PHOTO) values(?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(AddSparepartsql);
            String value = ddlcat.getSelectedItem().toString();
            ps.setString(1, value);
            ps.setString(2, snumtxt.getText());
            ps.setString(3, spntxt.getText());
            ps.setDouble(4, Double.parseDouble(pptxt.getText()));
            ps.setDouble(5, Double.parseDouble(sptxt.getText()));
            ps.setInt(6, Integer.parseInt(qatxt.getText()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String date = sdf.format(pdate.getDate());
//            ps.setString(7, date);
            Date d = pdate.getDateEditor().getDate();
            ps.setDate(7, new java.sql.Date(d.getTime()));
            ps.setString(8, desctxt.getText());
            ps.setBytes(9, person_image);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nNew Spare part is added!!!");
            updateSparePartTable();
            snumtxt.setText("");
            spntxt.setText("");
            pptxt.setText("");
            sptxt.setText("");
            qatxt.setText("");
            pdate.setDate(null);
            desctxt.setText("");
            sppic.setIcon(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Insertion Error!!" + e);
        }
    }//GEN-LAST:event_addsparespartbtnActionPerformed

    private void spareparttableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spareparttableMouseClicked
        int i = spareparttable.getSelectedRow();
        TableModel model = spareparttable.getModel();
       
        String cat = model.getValueAt(i, 0).toString();
        for(int j = 0; j < ddlcat.getItemCount(); j++){
           if(ddlcat.getItemAt(j).equalsIgnoreCase(cat)) 
               ddlcat.setSelectedIndex(j);
        }
        snumtxt.setText(model.getValueAt(i, 1).toString());
        spntxt.setText(model.getValueAt(i, 2).toString());
        pptxt.setText(model.getValueAt(i,3).toString());
        sptxt.setText(model.getValueAt(i, 4).toString());
        qatxt.setText(model.getValueAt(i, 5).toString());
        
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String)model.getValueAt(i, 6));
            pdate.setDate(date);
        } catch (ParseException ex) {
            Logger.getLogger(AssignmentAutoZoneSpartPart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        desctxt.setText(model.getValueAt(i, 7).toString());
        
        byte[] img = (sparepartlist().get(i).getPHOTO());
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(sppic.getWidth(), sppic.getHeight(), Image.SCALE_SMOOTH));
        sppic.setIcon(imageIcon);
        
    }//GEN-LAST:event_spareparttableMouseClicked

    private void sortspinalorbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortspinalorbtnActionPerformed
       // To sort spare part in alphabetical order
        try {
            String sql = "select * from SPAREPART ORDER BY PARTNAME";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            spareparttable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Spare Part table error: " + e);
        } 
    }//GEN-LAST:event_sortspinalorbtnActionPerformed

    private void sortindescordbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortindescordbtnActionPerformed
       // To sort spare part in descending order
        try {
            String sql = "select * from SPAREPART ORDER BY PARTNAME DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            spareparttable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Spare Part table error: " + e);
        }
    }//GEN-LAST:event_sortindescordbtnActionPerformed

    private void updatesparepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatesparepartActionPerformed
      // update a record from the Spare part table  
      String s1 = snumtxt.getText();
      int p = JOptionPane.showConfirmDialog(null, "Are you sure you want to update?", "Update Record", JOptionPane.YES_NO_OPTION);
      if(p==0){
          try{
             int a;
             String s2, s3;
             double d1, d2;
            
             s2 = spntxt.getText();
             d1 = Double.parseDouble(pptxt.getText());
             d2 = Double.parseDouble(sptxt.getText());
             a = Integer.parseInt(qatxt.getText());
             s3 = desctxt.getText();
             
             
             String updatesuppliersql = "update SPAREPART set PARTNAME=?, PURCHASEPRICE=?, SELLINGPRICE=?, QUANTITYAVAILABLE=?, DESCRIPTION=? where SERIALNUMBER=?";
             ps = con.prepareStatement(updatesuppliersql);
             
             ps.setString(1, s2);
             ps.setDouble(2, d1);
             ps.setDouble(3, d2);
             ps.setInt(4, a);
             ps.setString(5, s3);
             ps.setString(6, s1);
             ps.execute();
             JOptionPane.showMessageDialog(null, "Success!!\nOne record updated!!!");
             updateSparePartTable();
             
          }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Update Error!!" + e);
          }
          
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] image = new byte[(int)file.length()];
            fis.read(image);  
            
            String sql ="update SPAREPART set PHOTO = ? where SERIALNUMBER = ?";
            ps = con.prepareStatement(sql);
            ps.setBytes(1, image);
            ps.executeUpdate();
            ps.close();
          }
          catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
          }
        
      }
    }//GEN-LAST:event_updatesparepartActionPerformed

    private void deletesparepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletesparepartActionPerformed
       // delete a record from the Spare part table 
        try {
            String deleteSql = "delete from SPAREPART where SERIALNUMBER=?";
            ps = con.prepareStatement(deleteSql);
            ps.setString(1, snumtxt.getText());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nOne record deleted!!!");
            snumtxt.setText("");
            spntxt.setText("");
            pptxt.setText("");
            sptxt.setText("");
            qatxt.setText("");
            pdate.setDate(null);
            desctxt.setText("");
            sppic.setIcon(null);
            updateSparePartTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Transaction Error: " + e);
        } 
    }//GEN-LAST:event_deletesparepartActionPerformed

    private void spsearchtxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spsearchtxtKeyReleased
//        String searchsp = spsearchtxt.getText();
//        search(searchsp );
    }//GEN-LAST:event_spsearchtxtKeyReleased

    private void attachcustppbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachcustppbtnActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(custpropic.getWidth(), custpropic.getHeight(), Image.SCALE_SMOOTH));
        custpropic.setIcon(imageIcon);
        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);

            }
            person_image = bos.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_attachcustppbtnActionPerformed

    private void addnewcustbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addnewcustbtnActionPerformed
      // To add a new customer to the database table of customer
        try {
            String AddCustomersql = "insert into CUSTOMER(CUSTNIC, CUSTFIRSTNAME, CUSTLASTNAME, STREETADDRESS, CITY, POSTCODE, PHONE, EMAIL, DATEOFBIRTH, PROFILEPHOTO) values(?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(AddCustomersql);
            ps.setString(1, custnictxt.getText());
            ps.setString(2, custfn.getText());
            ps.setString(3, custlnametxt.getText());
            ps.setString(4, custstreettxt.getText());
            ps.setString(5, custcitytxt.getText());
            ps.setInt(6, Integer.parseInt(custpostcodetxt.getText()));
            ps.setInt(7, Integer.parseInt(custphonetxt.getText()));
            ps.setString(8, custemailtxt.getText());
            Date d = custdob.getDateEditor().getDate();
            ps.setDate(9, new java.sql.Date(d.getTime()));
            ps.setBytes(10, person_image);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nNew Customer is added!!!");
            updateCustomerTable();
            custnictxt.setText("");
            custfn.setText("");
            custlnametxt.setText("");
            custstreettxt.setText("");
            custcitytxt.setText("");
            custpostcodetxt.setText("");
            custphonetxt.setText("");
            custemailtxt.setText("");
            custdob.setDate(null);
            custpropic.setIcon(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Insertion Error!!" + e);
        }  
    }//GEN-LAST:event_addnewcustbtnActionPerformed

    private void updatecustbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatecustbtnActionPerformed
      // update a record from the Customer table 
      String s1 = custnictxt.getText();
      int p = JOptionPane.showConfirmDialog(null, "Are you sure you want to update?", "Update Record", JOptionPane.YES_NO_OPTION);
      if(p==0){
          try{
            String s2, s3, s4;  
            int a, b;

            s2 = custstreettxt.getText();
            s3 = custcitytxt.getText();
            a = Integer.parseInt(custpostcodetxt.getText());
            b = Integer.parseInt(custphonetxt.getText());
            s4 = custemailtxt.getText();
            
            String updatesuppliersql = "update CUSTOMER set STREETADDRESS=?,CITY=?, POSTCODE=? ,PHONE=? ,EMAIL=? where CUSTNIC = ?";
            ps = con.prepareStatement(updatesuppliersql);
            
            ps.setString(1, s2); 
            ps.setString(2, s3); 
            ps.setInt(3, a);
            ps.setInt(4, b);
            ps.setString(5, s4); 
            ps.setString(6, s1); 
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Success!!\nOne record updated!!!");
            updateCustomerTable();
            
          }
          catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Update Error!!" + e);
          }
          
          try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] image = new byte[(int)file.length()];
            fis.read(image);  
            
            String sql ="update CUSTOMER set PROFILEPHOTO = ? where CUSTNIC = ?";
            ps = con.prepareStatement(sql);
            ps.setBytes(1, image);
            ps.executeUpdate();
            ps.close();
          }
          catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
          }
      } 
    }//GEN-LAST:event_updatecustbtnActionPerformed

    private void deletecustbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletecustbtnActionPerformed
       // delete a record from the Customer table 
        try {
            String deleteSql = "delete from CUSTOMER where CUSTNIC=?";
            ps = con.prepareStatement(deleteSql);
            ps.setString(1, custnictxt.getText());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nOne record deleted!!!");
            custnictxt.setText("");
            custfn.setText("");
            custlnametxt.setText("");
            custstreettxt.setText("");
            custcitytxt.setText("");
            custpostcodetxt.setText("");
            custphonetxt.setText("");
            custemailtxt.setText("");
            custdob.setDate(null);
            custpropic.setIcon(null);
            updateCustomerTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Transaction Error: " + e);
        } 
    }//GEN-LAST:event_deletecustbtnActionPerformed

    private void custtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_custtableMouseClicked
        int i = custtable.getSelectedRow();
        TableModel model = custtable.getModel();
        custnictxt.setText(model.getValueAt(i, 0).toString());
        custfn.setText(model.getValueAt(i, 1).toString());
        custlnametxt.setText(model.getValueAt(i, 2).toString());
        custstreettxt.setText(model.getValueAt(i, 3).toString());
        custcitytxt.setText(model.getValueAt(i, 4).toString());
        custpostcodetxt.setText(model.getValueAt(i, 5).toString());
        custphonetxt.setText(model.getValueAt(i, 6).toString());
        custemailtxt.setText(model.getValueAt(i, 7).toString());
        
//        try {
//            Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String)model.getValueAt(i, 8));
//            custdob.setDate(date);
//        } catch (ParseException ex) {
//            Logger.getLogger(AssignmentAutoZoneSpartPart.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        byte[] img = (customerlist().get(i).getPROFILEPHOTO());
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(custpropic.getWidth(), custpropic.getHeight(), Image.SCALE_SMOOTH));
        custpropic.setIcon(imageIcon);
        
    }//GEN-LAST:event_custtableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //to sort customer in alphabetical order
        try {
            String sql = "select * from CUSTOMER ORDER BY CUSTFIRSTNAME";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            custtable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Customer table error: " + e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       //to sort customer in descending order  
        try {
            String sql = "select * from CUSTOMER ORDER BY CUSTNIC DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            custtable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Customer table error: " + e);
        } 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void searchcusttxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchcusttxtKeyReleased
//        String searchCustomer = searchcusttxt.getText();
//        searchCustomer(searchCustomer);
    }//GEN-LAST:event_searchcusttxtKeyReleased

    private void sortspinalporbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortspinalporbtnActionPerformed
        //to sort supplier part in ascending order
        try {
            String sql = "select * from SUPPLIERPART ORDER BY SERIALNUMBER";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            supparttable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Supplier part table error: " + e);
        }
    }//GEN-LAST:event_sortspinalporbtnActionPerformed

    private void addnewsuppartbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addnewsuppartbtnActionPerformed
        try {
            String AddSupplierpartsql = "insert into SUPPLIERPART(SUPNIC, SERIALNUMBER, QUANTITYSUPPLIED, DATESUPPLIED) values(?,?,?,?)";
            ps = con.prepareStatement(AddSupplierpartsql);
            String supplier = ddlsupsp.getSelectedItem().toString();
            ps.setString(1, supplier);
            String sparepart = ddlsnsp.getSelectedItem().toString();
            ps.setString(2, sparepart);
            ps.setInt(3, Integer.parseInt(qstxt.getText()));
            Date d = datesupplied.getDateEditor().getDate();
            ps.setDate(4, new java.sql.Date(d.getTime()));
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nNew supplier part is added!!!");
            updateSupplierpartTable();
            qstxt.setText("");
            datesupplied.setDate(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Insertion Error!!" + e);
        }
    }//GEN-LAST:event_addnewsuppartbtnActionPerformed

    private void supplierparttxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplierparttxtKeyReleased
//        String searchSupplierPart = supplierparttxt.getText();
//        searchSupplierPart(searchSupplierPart);
    }//GEN-LAST:event_supplierparttxtKeyReleased

    Double pay,tot;
    private void addpurbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addpurbtnActionPerformed
        pay = Double.parseDouble(paymenttxt.getText());
        tot = pay * Integer.parseInt(qpurchsedtxt.getText());
        try {
            String Addcustpsql = "insert into CUSTOMERPURCHASE(CUSTNIC, SERIALNUMBER, QUANTITYPURCHASED, DATEPURCHASED, PAYMENT) values(?,?,?,?,?)";
            ps = con.prepareStatement(Addcustpsql);
            String customer = ddlcustomer.getSelectedItem().toString();
            ps.setString(1, customer);
            String sparepart = ddlserialnumber1.getSelectedItem().toString();
            ps.setString(2, sparepart);
            ps.setInt(3, Integer.parseInt(qpurchsedtxt.getText()));
            Date d = datepur.getDateEditor().getDate();
            ps.setDate(4, new java.sql.Date(d.getTime()));
            ps.setDouble(5, tot);
            
            ps.execute();
            JOptionPane.showMessageDialog(null, "Success!!\nNew purchase is added!!!");
            updateCustomerPurchaseTable();
            qstxt.setText("");
            datesupplied.setDate(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Insertion Error!!" + e);
        }   
    }//GEN-LAST:event_addpurbtnActionPerformed

    private void paymenttxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymenttxtActionPerformed
//        double quantity = Double.parseDouble("QUANTITYPURCHASED");
//        double payment = Double.parseDouble("PAYMENT");
//        
//        double Payment = quantity * payment;
    }//GEN-LAST:event_paymenttxtActionPerformed

    private void filterspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterspActionPerformed
        String catFilter = ddlscat.getSelectedItem().toString();
        catFilter(catFilter);
    }//GEN-LAST:event_filterspActionPerformed

    private void searchsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchsupActionPerformed
        String searchSupplier = searchsuptxt.getText();
        searchSupplier(searchSupplier);
    }//GEN-LAST:event_searchsupActionPerformed

    private void searchcustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchcustActionPerformed
         String searchCustomer = searchcusttxt.getText();
         searchCustomer(searchCustomer);
    }//GEN-LAST:event_searchcustActionPerformed

    private void searchcatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchcatActionPerformed
        // To search category by name
        String searchCategory = cattypesearch.getText();
        catsearch(searchCategory);
    }//GEN-LAST:event_searchcatActionPerformed

    private void searchspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchspActionPerformed
        String searchsp = spsearchtxt.getText();
        search(searchsp );
    }//GEN-LAST:event_searchspActionPerformed

    private void searchsppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchsppActionPerformed
        String searchSupplierPart = supplierparttxt.getText();
        searchSupplierPart(searchSupplierPart);
    }//GEN-LAST:event_searchsppActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AssignmentAutoZoneSpartPart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AssignmentAutoZoneSpartPart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AssignmentAutoZoneSpartPart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AssignmentAutoZoneSpartPart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AssignmentAutoZoneSpartPart().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CATEGORYID;
    private javax.swing.JButton addnewcustbtn;
    private javax.swing.JButton addnewsuppartbtn;
    private javax.swing.JButton addpurbtn;
    private javax.swing.JButton addsparespartbtn;
    private javax.swing.JButton addsupbtn;
    private javax.swing.JButton attachcustppbtn;
    private javax.swing.JButton attachsupppbtn;
    private javax.swing.JTextField catnametxt;
    private javax.swing.JTable cattable;
    private javax.swing.JTextField cattypesearch;
    private javax.swing.JTable ctable;
    private javax.swing.JTextField custcitytxt;
    private com.toedter.calendar.JDateChooser custdob;
    private javax.swing.JTextField custemailtxt;
    private javax.swing.JTextField custfn;
    private javax.swing.JPanel custfnametxt;
    private javax.swing.JTextField custlnametxt;
    private javax.swing.JTextField custnictxt;
    private javax.swing.JTextField custphonetxt;
    private javax.swing.JTextField custpostcodetxt;
    private javax.swing.JLabel custpropic;
    private javax.swing.JTable custsptable;
    private javax.swing.JTextField custstreettxt;
    private javax.swing.JTable custtable;
    private javax.swing.JLabel datelbl;
    private com.toedter.calendar.JDateChooser datepur;
    private com.toedter.calendar.JDateChooser datesupplied;
    private javax.swing.JComboBox<String> ddlcat;
    private javax.swing.JComboBox<String> ddlcustomer;
    private javax.swing.JComboBox<String> ddlscat;
    private javax.swing.JComboBox<String> ddlserialnumber1;
    private javax.swing.JComboBox<String> ddlsnsp;
    private javax.swing.JComboBox<String> ddlsupsp;
    private javax.swing.JButton deletecustbtn;
    private javax.swing.JButton deletesparepart;
    private javax.swing.JButton deletesupbtn;
    private javax.swing.JTextField desctxt;
    private javax.swing.JButton filtersp;
    private javax.swing.JTable filtertable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextField paymenttxt;
    private com.toedter.calendar.JDateChooser pdate;
    private javax.swing.JTextField pptxt;
    private javax.swing.JTextField purchaseidtxt;
    private javax.swing.JTable purchasetable;
    private javax.swing.JTextField qatxt;
    private javax.swing.JTextField qpurchsedtxt;
    private javax.swing.JTextField qstxt;
    private javax.swing.JButton savecatbtn;
    private javax.swing.JTable scattable;
    private javax.swing.JButton searchcat;
    private javax.swing.JButton searchcust;
    private javax.swing.JTextField searchcusttxt;
    private javax.swing.JButton searchsp;
    private javax.swing.JButton searchspp;
    private javax.swing.JButton searchsup;
    private javax.swing.JTextField searchsuptxt;
    private javax.swing.JTextField snumtxt;
    private javax.swing.JButton sortcatinalorbtn;
    private javax.swing.JButton sortcatindescorbtn;
    private javax.swing.JButton sortindescordbtn;
    private javax.swing.JButton sortspinalorbtn;
    private javax.swing.JButton sortspinalporbtn;
    private javax.swing.JTable sparepartsptable;
    private javax.swing.JTable sparepartsptable1;
    private javax.swing.JTable spareparttable;
    private javax.swing.JButton spattchbtn;
    private javax.swing.JTextField spntxt;
    private javax.swing.JLabel sppic;
    private javax.swing.JTable spsearchtable;
    private javax.swing.JTextField spsearchtxt;
    private javax.swing.JTextField sptxt;
    private javax.swing.JTextField supcity;
    private com.toedter.calendar.JDateChooser supdob;
    private javax.swing.JTextField supemailtxt;
    private javax.swing.JTextField supfntxt;
    private javax.swing.JTextField suplntxt;
    private javax.swing.JTextField supnictxt;
    private javax.swing.JTable suppartsearchtable;
    private javax.swing.JTable supparttable;
    private javax.swing.JTextField supplierparttxt;
    private javax.swing.JTable suppliersptable;
    private javax.swing.JTable suppliertable;
    private javax.swing.JTextField suppnumtxt;
    private javax.swing.JTextField suppostcodetxt;
    private javax.swing.JLabel suppropic;
    private javax.swing.JTextField supstreet;
    private javax.swing.JTable suptable;
    private javax.swing.JButton updatecustbtn;
    private javax.swing.JButton updatesparepart;
    private javax.swing.JButton updatesupbtn;
    // End of variables declaration//GEN-END:variables
}
