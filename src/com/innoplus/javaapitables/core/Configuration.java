package com.innoplus.javaapitables.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuration {

    private static Log log = LogFactory.getLog(Configuration.class);
    //public static final String PROP_FILE_PATH = "./conf/sot.properties";
    public static final String PROP_FILE_PATH = "D:/web-harvest/sot.properties";
    private static Configuration ourInstance = new Configuration();
    private Properties props = new Properties();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
        try {
            // CVIT
            //this.props.load(new FileInputStream("./conf/sot.properties"));
            //this.props.load(new FileInputStream("D:/web-harvest/sot.properties"));
            this.props.load(Configuration.class.getResourceAsStream("/com/innoplus/javaapitables/resources/sot.properties"));
        } catch (IOException localIOException) {
            log.error("Error while loaidng configuration file. Make sure config file [./conf/sot.properties] is available.", localIOException);
        }
    }

    public String getProperty(String paramString) {
        String str = this.props.getProperty(paramString);
        return str != null ? str.trim() : str;
    }
}
