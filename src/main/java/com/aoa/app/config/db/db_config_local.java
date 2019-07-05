package com.aoa.app.config.db;

public class db_config_local {
    protected String description = "Parametros de configuración de base de datos local";
    private String driver="com.mysql.jdbc.Driver";
    private String connectString="jdbc:mysql://127.0.0.1/Testing_bd";
    private String user="root";
    private String password="";   
    private final String test = "Select * from pais";
    
    public db_config_local() {
        this.driver = "com.mysql.jdbc.Driver";
    }
    
    public String getDriver()
    {
        return this.driver;
    }
    
    public String getDescription()
    {
        return this.description;
    }
    
    public String getConnectString()
    {
        return this.connectString;
    }
    
    public String getUser()
    {
        return this.user;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public String getTest()
    {
        return this.test;
    }
}
