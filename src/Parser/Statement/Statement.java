package Parser.Statement;

import Parser.Enums.StatementType;

public abstract class Statement {
    
    private final StatementType statementType;
    
    public Statement(StatementType statementType) {
        this.statementType = statementType;
    }
    
    public StatementType getStatementType() {
        return this.statementType;
    }
}
