package be.velovista.Model.DAL;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBProperties {
        
    private Properties properties;

    public DBProperties(){
        try(InputStream input = DBProperties.class.getResourceAsStream("/config.properties")){
            properties = new Properties();
            properties.load(input);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public String getUrl(){
        return properties.getProperty("database.url");
    }
    public String getDefaultUrl(){
        return properties.getProperty("database.defaultUrl");
    }
    public String getUsername(){
        return properties.getProperty("database.username");
    }
    public String getPassword(){
        return properties.getProperty("database.password");
    }
}
