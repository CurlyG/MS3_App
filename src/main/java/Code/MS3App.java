package Code;

import Utilities.ConfigurationReader;
import Utilities.OtherUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MS3App extends OtherUtils {

        public static void main(String[] args) {
        MS3App app = new MS3App(); //create instance of Code.MS3App to access the instance methods and variables

            //
        List<String> filesToProcess =app.listNamesOfAllFiles(ConfigurationReader.get("path")); //get list of names of CSV file
        for (String fileName: filesToProcess){
            app.processCSV(ConfigurationReader.get("path"), fileName);
        }
    }











}
