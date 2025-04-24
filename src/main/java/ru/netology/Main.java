package ru.netology;

import ru.netology.model.Employee;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Задача 1: CSV → JSON
        System.out.println("=== CSV to JSON ===");
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> csvList = parseCSV(columnMapping, "src/main/resources/data.csv");
        writeJsonToFile(csvList, "data_csv.json");
        System.out.println("Файл data_csv.json создан");

        // Задача 2: XML → JSON
        System.out.println("\n=== XML to JSON ===");
        List<Employee> xmlList = parseXML("src/main/resources/data.xml");
        writeJsonToFile(xmlList, "data_xml.json");
        System.out.println("Файл data_xml.json создан");

        // Задача 3: JSON → Java объекты
        System.out.println("\n=== JSON to Objects ===");
        List<Employee> jsonList = parseJSON("src/main/resources/new_data.json");
        jsonList.forEach(System.out::println);
    }

    // Методы для задачи 1 (CSV → JSON)
    private static List<Employee> parseCSV(String[] columnMapping, String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            return new CsvToBeanBuilder<Employee>(reader)
                    .withType(Employee.class)
                    .withMappingStrategy(strategy)
                    .build()
                    .parse();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении CSV: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Методы для задачи 2 (XML → JSON)
    private static List<Employee> parseXML(String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));

            NodeList nodeList = doc.getDocumentElement().getElementsByTagName("employee");
            List<Employee> employees = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Employee emp = new Employee();
                    emp.id = Long.parseLong(getElementValue(element, "id"));
                    emp.firstName = getElementValue(element, "firstName");
                    emp.lastName = getElementValue(element, "lastName");
                    emp.country = getElementValue(element, "country");
                    emp.age = Integer.parseInt(getElementValue(element, "age"));
                    employees.add(emp);
                }
            }
            return employees;
        } catch (Exception e) {
            System.err.println("Ошибка при чтении XML: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static String getElementValue(Element element, String tagName) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    // Методы для задачи 3 (JSON → Java объекты)
    private static List<Employee> parseJSON(String filePath) {
        try {
            String json = readFileToString(filePath);
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(json);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Employee>>(){}.getType();
            return gson.fromJson(jsonArray.toJSONString(), listType);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Общие вспомогательные методы
    private static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    private static void writeJsonToFile(List<Employee> employees, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(employees);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Ошибка при записи JSON: " + e.getMessage());
        }
    }
}