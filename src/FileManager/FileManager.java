package FileManager;

import Parser.Types.Table;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private static final Map<String, Table> tableCache = new HashMap<>();


    public static void saveTableToFile(Table table) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(table.getName());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            
            objectOutputStream.writeObject(table);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    public static Table loadTableFromFile(String tableName) {
        return tableCache.computeIfAbsent(tableName, tn -> {
            try(FileInputStream fileInputStream = new FileInputStream(tableName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                return (Table) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                System.out.println(exception.getMessage());
                return null;
            }
        });
    }
}
