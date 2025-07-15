package Parser.Types;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final Map<String, Object> values;
    
    public Row(List<Column> columns, List<Object> rowValues) {
        this.values = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            values.put(columns.get(i).getName(), rowValues.get(i));
        }
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public Object getValue(String column) {
        return values.get(column);
    }

    @Override
    public String toString() {
        return "Row{" +
                "values=" + values +
                '}';
    }
}
