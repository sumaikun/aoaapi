package com.aoa.app.config;


import com.aoa.app.config.db.*;
import java.sql.*;

public class Dbconfig {
    
    //conexion con php myadmin
    private final String driver;
    private final String connectString;
    private final String user;
    private final String password;
    public Connection con;
        
    public Dbconfig(String driver,String connectString,String user,String password){
       this.driver = driver;
       this.connectString = connectString;
       this.user = user;
       this.password = password;
    }
    
    public Connection connect()
    {
        
        this.con = null;
        try {
            Class.forName(this.driver);
            System.out.println(this.connectString);
            this.con = DriverManager.getConnection(this.connectString, this.user , this.password);
        }catch ( ClassNotFoundException | SQLException e ){
            System.out.println("error: no se pudo conectar a la base de datos: "+e.getMessage());
            //e.printStackTrace();
        }
        if(this.con!=null)
        {
            System.out.println("Conexión establecida");
        } 
        return this.con;
    }
    //check_connection
    
    public static void main(String[]args) throws SQLException{
        
        db_config_aoa config = new db_config_aoa(); 
        Dbconfig db = new Dbconfig(config.getDriver(),config.getConnectString(),config.getUser(),config.getPassword());
        Connection con = db.connect();
        Statement stm;
        stm = con.createStatement();
        ResultSet query = stm.executeQuery(config.getTest());
        System.out.println("test de conexión "+query);        
    }
    
    
}