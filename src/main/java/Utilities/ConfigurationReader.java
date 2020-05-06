package Utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties properties; //no need to use outside the class, can be accessible with the get(String keyName) method


    static {
        try (FileInputStream input = new FileInputStream("configuration.properties")){
            properties=new Properties();
            properties.load(input);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String get(String keyName){
        return properties.getProperty(keyName);
    }
}
