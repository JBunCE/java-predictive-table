package com.jbunce.analizadorlexico.analizers.predictive.table;

import com.jbunce.analizadorlexico.analizers.Tokens;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @NoArgsConstructor
public class Production {
    private Tokens symbol;
    private String name;
    private List<Production[]> next;
    private boolean isTerminal;

    public Production(String name) {
        this.name = name;
    }

    public Production(Tokens symbol) {
        this.symbol = symbol;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    @Override
    public String toString() {
        return name;
    }
}
