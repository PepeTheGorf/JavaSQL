package FileManager;

import Parser.Types.Column;
import Parser.Types.Row;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RowReader implements Iterator<Row>, AutoCloseable {
    private final DataInputStream dataInputStream;
    private final List<Column> columns;

    public RowReader(Path tableDataPath, List<Column> columns) throws IOException {
        this.dataInputStream = new DataInputStream(new FileInputStream(tableDataPath.toFile()));
        this.columns = columns;
    }

    @Override
    public void close() throws Exception {
        dataInputStream.close();
    }

    @Override
    public boolean hasNext() {
        try {
            return dataInputStream.available() > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Row next() {
        try {
            List<Object> values = new ArrayList<>();
            for(Column column : columns) {
                switch (column.getDataType()) {
                    case INTEGER -> values.add(dataInputStream.readInt());
                    case VARCHAR -> values.add(dataInputStream.readUTF());
                    default -> throw new IllegalStateException("Unknown type...");
                }
            }
            
            return new Row(columns, values);
        } catch (IOException e) {
            System.out.println("Failed reading from data file: " + e);
        }
        return null;
    }
}
