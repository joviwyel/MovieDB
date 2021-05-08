package servlets;



import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class LogConfig extends HttpServlet {


    private void setPath(String configFile, String logLocation) throws IOException{
        FileInputStream in = new FileInputStream(configFile);
        Properties props = new Properties();
        props.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream(configFile);
        props.setProperty("log", logLocation);
        props.store(out, null);
        out.close();
    }

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        super.init();
        String prefix =  getServletContext().getRealPath("/");
        String file = getInitParameter("log-config-file");
        System.out.println("finding config file...");
        if(file!=null){
            System.out.println("get log config file! at" + prefix);

            try {
                setPath(prefix+file, prefix+"log/");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            PropertyConfigurator.configure(prefix+file);
        }
    }

}