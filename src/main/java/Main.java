import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String fileName = "data.csv";

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);

        writeString(json);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> data = null;
        String[] employee = "1,John,Smith,USA,25".split(",");

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] employeeNext = "2,Ivan,Petrov,RU,23".split(",");

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName, true))) {
            writer.writeNext(employeeNext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            data = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }

    public static String listToJson(List<Employee>list){
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json){
        try (FileWriter file = new
                FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
