package com.aoa.app.config.db;


/**
 *
 * @author JVega
 */
public class db_config_aoa {
    protected String description = "Parametros de configuración de base de datos AOA";
    private final String driver;
    private final String connectString="jdbc:mysql://190.85.62.30/aoacol_aoacars?zeroDateTimeBehavior=convertToNull";
    private final String user="aoacol_arturo";
    private final String password="AOA0l1lwpdaa";
    private final String test = "Select * from pais";

    public db_config_aoa() {
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
