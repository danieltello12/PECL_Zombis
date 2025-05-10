package Parte1;

import java.io.IOException;
import java.util.logging.*;

public class Logs {
    private static Logs instancia= null;
    private static Logger logger= Logger.getLogger("ApocalipsisLogger");

    static{
        try{
            //Empezamos desde 0 el logger
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL); //Podemos utilizar todo tipo de logs

            //Lo guardamos en el .txt, para que no sobrescriba sobre el archivo anterior
            //decimos que lo haga en modo append y simplemente lo añada al final
            FileHandler fileHandler= new FileHandler("apocalipsis.txt", true);
            fileHandler.setLevel(Level.ALL); //Podemos utilizar todo tipo de logs
            fileHandler.setFormatter(new SimpleFormatter());
            //Lo añadimim
            logger.addHandler(fileHandler);
        }catch(IOException e){
            System.err.println("No se pudo crear el log: "+e.getMessage());
        }
    }

    //Garantizamos que dos hilos creen do loggets diferentes
    public static synchronized Logs getInstancia(){
        //Si no existe el logger, lo creamos
        if(instancia==null){
            instancia= new Logs();
        }
        return instancia;
    }

    public synchronized void logInfo(String mensaje) {
        logger.info(mensaje); // Ya incluye la marca de tiempo
    }

    public synchronized void logWarning(String mensaje) {
        logger.warning(mensaje); // Ya incluye la marca de tiempo
    }

    public static Logger getLogger(){
        return logger;
    }
}
