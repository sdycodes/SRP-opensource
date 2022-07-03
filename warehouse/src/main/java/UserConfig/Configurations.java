package UserConfig;

import SimulatorCore.*;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import com.opencsv.exceptions.CsvValidationException;



public class Configurations {
    
    public static boolean IGNORE_PICKING_TIME = true;
    public static final String ROOT_PATH = "/path/to/data";
    public static final String DATA_PATH = Paths.get(ROOT_PATH, "transactions").toString();
    public static final String LAYOUT_PATH = Paths.get(ROOT_PATH, "layouts").toString();

    public static Service service;
    public static String LAYOUT_FILE_NAME;
    public static int LOG_SEG;
    public static String LAYOUT_FILE_PATH;
    public static String TASK_FILE_PATH;
    public static String RACK_TO_PICKERS_PATH;
    public static PrintStream ps;
    

    public static void setConfigurations(String[] args) {
        String method = args[0];
        String layout = args[1];
        if (layout.indexOf('_') == -1) {
            LAYOUT_FILE_NAME = layout;
        } else {
            LAYOUT_FILE_NAME = layout.substring(0, layout.indexOf('_'));
        }
        
        TASK_FILE_PATH = Paths.get(DATA_PATH, "tasks_" + layout + ".csv").toString();
        RACK_TO_PICKERS_PATH = Paths.get(DATA_PATH, "rackToPickers_" + layout + ".csv").toString();
        switch (LAYOUT_FILE_NAME) {
            case "synA": 
                LOG_SEG = 100; 
                break;
            case "synB": 
                LOG_SEG = 100; 
                break;
            case "real1": 
                LOG_SEG = 1000; 
                break;
            case "real2": 
                LOG_SEG = 1000; 
                break;
            case "real3":
                LOG_SEG = 1000;
            default:
                LOG_SEG = 1;
        }
        LAYOUT_FILE_PATH = Paths.get(LAYOUT_PATH, LAYOUT_FILE_NAME + ".txt").toString();
        switch (method) {
            case "SAP": service = new Solvers.SAP.Solver(); break;
            case "ACP": service = new Solvers.ACP.Solver(); break;
            case "SRP": service = new Solvers.GMAPF.Solver(LAYOUT_FILE_PATH); break;
            
            default:
                service = null;
                System.out.println("No specified algorithm name");
                System.exit(1);
        }
        try {
            ps = System.out;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws CsvValidationException, NumberFormatException {
        Configurations.setConfigurations(args);
        System.setOut(Configurations.ps);
        TaskPool.INSTANCE.setIgnorePickingTime(Configurations.IGNORE_PICKING_TIME);
        // init user defined Service
        Configurations.service.initialize(PickerGroup.INSTANCE, RackGroup.INSTANCE);
        System.out.println("testing " + Configurations.service.getClass().getName());
        // simulate
        ServerProxy sp = new ServerProxy(Configurations.service);
        HashMap<String, Object> res = ClockDriver.simulate(sp);
        // out put statistics
        System.out.println("total real time usage: " + (Long)res.get("totalUsage") / 1000.0 + "s");
        System.out.println("selection time usage: " + (Long)res.get("matchingUsage") / 1000.0 + "s");
        System.out.println("planning time usage: " + (Long)res.get("planningUsage") / 1000.0 + "s");
        System.out.println("Total time usage: " + res.get("simulationStep"));
        System.out.println("Memory Usage: " + Arrays.toString((double[]) res.get("memory")));
        Configurations.ps.flush();
        Configurations.ps.close();
    }
}
