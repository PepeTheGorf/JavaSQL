package Parser.Statement;

import Parser.Enums.StatementType;
import Parser.Types.Result;

import java.util.Map;

public abstract class Statement {
    
    private final StatementType statementType;
    
    public Statement(StatementType statementType) {
        this.statementType = statementType;
    }
    
    public StatementType getStatementType() {
        return this.statementType;
    }
    
    public abstract Result execute(Map<String, Object> context, boolean print) throws Exception;
}
