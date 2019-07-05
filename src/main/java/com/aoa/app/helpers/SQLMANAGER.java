package com.aoa.app.helpers;

import com.aoa.app.config.db.*;
import com.aoa.app.config.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JVega
 */
public class SQLMANAGER {
    private Statement stmt;
    private final db_config_aoa db_config = new db_config_aoa();
    private Connection con;
    public  ResultSet result;
    private  List<Map<String, String>> records;  
    
 
    
    public void connect_db()
    {
        Dbconfig db = new Dbconfig(this.db_config.getDriver(),this.db_config.getConnectString(),this.db_config.getUser(),this.db_config.getPassword());
        this.con = db.connect();
    }
    
    public SQLMANAGER ExecuteSql(String query) throws SQLException
    {   
        connect_db();
        System.out.println(query);  
        
        
        this.stmt = this.con.createStatement();
       // this.result = this.stmt.executeQuery(query);
       if(query.toUpperCase().contains("CREATE")||query.toUpperCase().contains("UPDATE")||query.toUpperCase().contains("DELETE")||query.toUpperCase().contains("INSERT"))
       {           
           this.stmt.execute(query);
           this.con.close();
       }
       else
       {
           this.result = this.stmt.executeQuery(query);
       }       
        //this.con.close();
        return this;
    }
    
    public void print_resultdata(ResultSet result)
    {
        try {
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (result.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.out.println("error en print_resultdata");
            Logger.getLogger(SQLMANAGER.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SQLMANAGER fetch_query(ResultSet result)
    {
        if(result == null)
        {
            result = this.result;
        }
        try {
            ResultSetMetaData rsmd = result.getMetaData();                      
            this.records = new ArrayList<>();
            int cols = result.getMetaData().getColumnCount();
           
            while (result.next()) {
                 Map<String, String> map = new HashMap<>();
                   for(int i=1; i<=cols; i++){
                    //System.out.println("Columna "+rsmd.getColumnName(i)+" valor "+result.getString(i));
                    map.put(rsmd.getColumnName(i), result.getString(i));
                  }
                this.records.add(map);              
            }            
            if(records.size()>0)
            {
                //System.out.println("id registro "+records.get(0).get("id"));
                 //System.out.println("retorno normal");
                return this;
            }         
            else
            {
                 //System.out.println("null else");
                this.records = null;
                return this;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(SQLMANAGER.class.getName()).log(Level.SEVERE, null, ex);
        }
         //System.out.println("null final");
        this.records = null;
        return this;
    }
    
    public Map<String, String> first_row() throws SQLException
    {
        this.con.close(); 
        if(this.records != null)
        {
            System.out.println("no nulo");
            return this.records.get(0);
        }
        else
        {
            System.out.println("nulo");
            return null;
        }
        
    }
    
    public List<Map<String, String>> get_rows() throws SQLException
    {
        this.con.close();
        return this.records;
    }
    
   
}
