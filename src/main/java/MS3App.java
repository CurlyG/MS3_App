import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MS3App {

    static String path = "src/main/resources/InputFiles/"; //relative path of input file

    public static void main(String[] args) {
        MS3App app = new MS3App(); //create instance of MS3App to access the instance methods and variables

        List<String> filesToProcess =app.listNamesOfAllFiles(path);
        for (String fileName: filesToProcess){
            app.processCSV(path, fileName);
        }
    }

    private List<String> listNamesOfAllFiles(String path){

        File folder = new File(path);
        File[] fileNames = folder.listFiles(); //gets all the files in folder

        List<String >listToReturn =new ArrayList<>();

        for (File file:fileNames){
            //if directory call the same method again
            if (!file.isDirectory()&&file.getName().contains(".csv")&&!file.getName().contains("-bad")){
                listToReturn.add(file.getName().replace(".csv", ""));
            }
        }

        return listToReturn;
    }

    private void processCSV(String path, String fileName){
        String dbPath = "jdbc:sqlite:"+path;
        String dbName = fileName + ".db";
        String csvFileName = fileName + ".csv";
        String badCsvFileName = fileName + "-bad.csv";
        String logFileName = fileName + ".log";

        String[] line = null;
        List<List<String>> data=new ArrayList<>();
        int badLines = 0;
        int totalLines = 0;


        //no need in flush() and close() since we are using try-resources
        try(CSVReader reader = new CSVReader(new FileReader(path+csvFileName));
            FileWriter badCsvWriter = new FileWriter(path + badCsvFileName);
            FileWriter logWriter = new FileWriter(path+logFileName)){

            reader.readNext(); //skipping first line

            boolean goodLine;

            while ((line = reader.readNext())!=null){
                goodLine = true;

                //if it's a bad line write in bad.csv
                List<String > cells = Arrays.asList(line);
                for (String cell: cells) {
                    if (cell.isEmpty()){
                        goodLine=false;

                            badCsvWriter.append(Arrays.toString(line) + "\n");

                        badLines++;
                        break;
                    }
                }

                // if it is a good line add it to list so that it can be saved into DB later
                if (goodLine){
                    data.add(cells);
                }

                totalLines++;
            }

            //save into DB
            createNewTable(dbPath+dbName);
            insert(dbPath+dbName, data);


            //saving statistics into log file
            logWriter.append(totalLines+" records received \n");
            logWriter.append(data.size()+" records successful \n");
            logWriter.append(badLines+" records failed \n");


        }catch (Exception e){

        }

    }

    private void createNewTable(String url) {

        String sql = "CREATE TABLE IF NOT EXISTS challenge (\n"
                + " A text,\n"
                + "	B text,\n"
                + "	C text,\n"
                + "	D text,\n"
                + "	E text,\n"
                + "	F text,\n"
                + "	G text,\n"
                + " H text,\n"
                + "	I text,\n"
                + "	J text\n"
                + ");";

        try (Connection conn = getConnection(url); //If DB doesn't exists this will create it
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);// create a new table defined in the sql above
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insert(String url, List<List<String>> data) {
        try (Connection conn = getConnection(url)) {
            String sql = "INSERT INTO challenge(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";
            for (List<String> line:  data) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, line.get(0));
                    pstmt.setString(2, line.get(1));
                    pstmt.setString(3, line.get(2));
                    pstmt.setString(4, line.get(3));
                    pstmt.setString(5, line.get(4));
                    pstmt.setString(6, line.get(5));
                    pstmt.setString(7, line.get(6));
                    pstmt.setString(8, line.get(7));
                    pstmt.setString(9, line.get(8));
                    pstmt.setString(10, line.get(9));
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection getConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }




}
