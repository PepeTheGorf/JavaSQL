package FileManager;

import Parser.Types.Column;
import Parser.Types.Row;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RowWriter {

    public static void writeRows(Path path, List<Row> rows, List<Column> columns) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(path.toFile(), true))) {
            for (Row row : rows) {
                for (Column column : columns) {
                    Object value = row.getValue(column.getName());

                    if (value instanceof Integer v) {
                        out.writeInt(v);
                    } else if (value instanceof String v) {
                        if (column.getMaxLength() < v.length())
                            throw new RuntimeException("VARCHAR length is larger than defined " + column.getMaxLength());
                        out.writeUTF(v);
                    } else {
                        throw new IllegalArgumentException("Unsupported type: " + value);
                    }
                }
            }
        }
    }

}
