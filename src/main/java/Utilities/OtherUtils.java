package Utilities;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OtherUtils extends DBUtils{
    public List<String> listNamesOfAllFiles(String path){

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

    public void processCSV(String path, String fileName){
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

            badCsvWriter.append("A, B, C, D, E, F, G, H, I, J \n"); //adding first line of column names into badCsv file

            while ((line = reader.readNext())!=null){
                goodLine = true;

                //if it's a bad line write in bad.csv
                List<String > cells = Arrays.asList(line);
                for (String cell: cells) {
                    if (cell.isEmpty()){
                        goodLine=false;

                        badCsvWriter.append((Arrays.toString(line)).substring(1,((Arrays.toString(line)).length()-1)) + "\n");

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


        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
