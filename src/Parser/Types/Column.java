package Parser.Types;

import Lexer.TokenType;

import java.io.Serializable;

public class Column implements Serializable {
    
    private String name;
    private Integer maxLength;
    private TokenType dataType;

    public Column(String name, Integer maxLength, TokenType dataType) {
        this.name = name;
        this.maxLength = maxLength;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public TokenType getDataType() {
        return dataType;
    }

    public void setDataType(TokenType dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", maxLength=" + maxLength +
                ", dataType=" + dataType +
                '}';
    }
}
