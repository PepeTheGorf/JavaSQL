package Parser.Types;

import FileManager.RowReader;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public class Table implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private String name;
    private List<Column> columns;

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                '}';
    }

    public RowReader getRowReader() throws IOException {
        return new RowReader(Path.of(name + "Data.bin"), columns);
    }
}
