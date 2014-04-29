import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import java.util.Date;

public class Serial implements SerialPortEventListener {
    final int PROGRESSBAR_LENGTH = 20;
    SerialPort serialPort;
    String connectedportname;
    Function tester = new Function("TEST");
    int count = 0;

    DBCollection gpsDataCollection;

    BasicDBList gpsDataList = new BasicDBList();

    BasicDBList bpmDataList = new BasicDBList();

    BasicDBObject data = new BasicDBObject();
    BasicDBObject userData = new BasicDBObject();

    Date d = new Date();



    // Authenticate - optional
    // boolean auth = db.authenticate("foo", "bar");


    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/ttyUSB0", // Linux
        "COM1",// Windows
        "COM2", // Windows
        "COM3",// Windows
        "COM4", // Windows
        "COM5",// Windows
        "COM6", // Windows
        "COM7", // Windows
        "COM8", // Windows
        "COM9", // Windows
        "COM10", // Windows
        "COM11", // Windows
        "COM12", // Windows
        "COM13",// Windows
        "COM14", // Windows
        "COM15",// Windows
        "COM16", // Windows
        "COM17",// Windows
        "COM18", // Windows
        "COM19", // Windows
        "COM20", // Windows
        "COM21", // Windows
        "COM22", // Windows
        "COM23", // Windows
        "COM24" // Windows
    };

    /**
    * A BufferedReader which will be fed by a InputStreamReader
    * converting the bytes into characters
    * making the displayed results codepage independent
    */
    private BufferedReader input;
    /** The output stream to the port */
    private OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    public void initialize() {
        try {
            // connect to the local database server
            MongoClient mongoClient = new MongoClient();

            // get handle to "mydb"
            DB db = mongoClient.getDB("gpsData");

            // get a list of the collections in this database and print them out
            Set<String> collectionNames = db.getCollectionNames();
            for (final String s : collectionNames) {
                System.out.println(s);
            }

            // get a collection object to work with
            gpsDataCollection = db.getCollection("gpsDataCollection");


            // drop all the data in it
        } catch (UnknownHostException e) {

        }


        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    connectedportname = portName;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                                                  TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                                           SerialPort.DATABITS_8,
                                           SerialPort.STOPBITS_1,
                                           SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            System.out.println("Connected to port: " + connectedportname);

            userData.put("username", "test");
            userData.put("dateTime", d);

        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {

            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();

                //double input_val =  Double.parseDole(inputLine);
                System.out.println(inputLine);

                if (inputLine.substring(0, 3).matches("GPS")) {

                    System.out.println("Processing gps...");
                    String[] parts = inputLine.split(",");
                    String longitude = parts[0].substring(3);
                    String lattitude =parts[1].replaceAll("\\s","");

                    if(lattitude.length()<11){
                        lattitude = '0'+lattitude;
                    }

                    gpsDataList.add(longitude);
                    gpsDataList.add(lattitude);

                } else if (inputLine.substring(0, 2).matches("<3")) {
                    System.out.println("Processing bpm...");
                    bpmDataList.add(inputLine.substring(2));
                    count ++;
                } else if (inputLine.matches("close")) {

                    System.out.println("closing...");
                    data.append("coordinates", gpsDataList);
                    data.append("bpm", bpmDataList);
                    userData.put("data", data);
                    gpsDataCollection.insert(userData);

                }


            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        // Ignore all the other eventTypes, but you should consider the other ones.

    }

    /**
     * Draw a status bar
     * code courtesy of
     * http://stackoverflow.com/questions/3225672/text-based-loading-bar-when-running-java-in-command-prompt
     */
    public void drawProgressBar(int numerator, int denominator) {
        int percent = (int) (((double) numerator / (double) denominator) * 100);

        String bar = "[";
        int lines = round((PROGRESSBAR_LENGTH * numerator) / denominator);
        int blanks = PROGRESSBAR_LENGTH - lines;

        for (int i = 0; i < lines; i++)
            bar += "*";

        for (int i = 0; i < blanks; i++)
            bar += " ";

        bar += "] " + percent + "%";

        System.out.print(bar + "\r");
    }

    private int round(double dbl) {
        int noDecimal = (int) dbl;
        double decimal = dbl - noDecimal;

        if (decimal >= 0.5)
            return noDecimal + 1;
        else
            return noDecimal;
    }

    public static void main(String[] args) throws Exception {
        Serial main = new Serial();
        main.initialize();
        Thread t = new Thread() {
            public void run() {

                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ie) {}

            }
        };
        t.start();
        System.out.println("Started");

    }
}