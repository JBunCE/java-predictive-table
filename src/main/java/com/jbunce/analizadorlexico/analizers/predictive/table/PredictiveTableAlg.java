package com.jbunce.analizadorlexico.analizers.predictive.table;

import com.jbunce.analizadorlexico.analizers.Tokens;
import com.jbunce.analizadorlexico.analizers.lexical.Lexer;
import javafx.application.Platform;
import javafx.scene.text.TextFlow;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class PredictiveTableAlg {

    public static Production INITIAL_PRODUCTION = new Production("S");

    public HashMap<String, HashMap<Tokens, Production[]>> getTable() {
        Production F = new Production(Tokens.CLOSE_BRACE);
        F.setName("F");
        F.setTerminal(Boolean.TRUE);

        Production I = new Production(Tokens.OPEN_BRACE);
        I.setName("I");
        I.setTerminal(Boolean.TRUE);

        Production D = new Production(Tokens.DISPLAY_DATA);
        D.setName("D");
        D.setTerminal(Boolean.TRUE);

        Production L = new Production(Tokens.NON_SPECIFIC);
        L.setName("L");
        L.setTerminal(Boolean.TRUE);

        Production P = new Production(Tokens.OPEN_PARENTHESIS);
        P.setName("P");
        P.setTerminal(Boolean.TRUE);

        Production PC = new Production(Tokens.CLOSE_PARENTHESIS);
        PC.setName("PC");
        PC.setTerminal(Boolean.TRUE);

        Production SC = new Production(Tokens.SEMICOLON);
        SC.setName("SC");
        SC.setTerminal(Boolean.TRUE);

        Production Q = new Production(Tokens.QUOTE);
        Q.setName("Q");
        Q.setTerminal(Boolean.TRUE);

        // NON TERMINAL PRODUCTIONS

        Production C = new Production();
        C.setName("C");
        C.setTerminal(Boolean.FALSE);

        Production A = new Production();
        A.setName("A");
        A.setTerminal(Boolean.FALSE);

        Production R = new Production();
        R.setName("R");
        R.setTerminal(Boolean.FALSE);

        Production T = new Production();
        T.setName("T");
        T.setTerminal(Boolean.FALSE);

        Production M = new Production();
        M.setName("M");
        M.setTerminal(Boolean.FALSE);

        HashMap<String, HashMap<Tokens, Production[]>> table = new HashMap<>();

        table.put("R", new HashMap<>());
        table.get("R").put(Tokens.NON_SPECIFIC, new Production[]{L, R});
        table.get("R").put(Tokens.QUOTE, new Production[]{});

        table.put("T", new HashMap<>());
        table.get("T").put(Tokens.NON_SPECIFIC, new Production[]{L, R});

        table.put("C", new HashMap<>());
        table.get("C").put(Tokens.QUOTE, new Production[]{Q, T, Q});

        table.put("M", new HashMap<>());
        table.get("M").put(Tokens.DISPLAY_DATA, new Production[]{D, P, C, PC});

        table.put("A", new HashMap<>());
        table.get("A").put(Tokens.SEMICOLON, new Production[]{SC, M, A});
        table.get("A").put(Tokens.CLOSE_BRACE, new Production[]{});

        table.put("S", new HashMap<>());
        table.get("S").put(Tokens.OPEN_BRACE, new Production[]{I, M, A, F});

        return table;
    }

    public void parse(LexerTable lexer, AtomicReference<String> result, TextFlow logArea) {
        HashMap<String, HashMap<Tokens, Production[]>> table = getTable();

        Production currentProduction;
        Stack<Production> stack = new Stack<>();
        stack.push(PredictiveTableAlg.INITIAL_PRODUCTION);

        while (true) {
            try {
                Tokens token = lexer.yylex();
                if (token == null) {
                    String res = result.get() + "EOF";
                    result.set(res);
                    break;
                }

                currentProduction = stack.pop();

                if (currentProduction.isTerminal()) {
                    if (currentProduction.getSymbol() == token) {
                        result.set(result.get() + "Token: " + token + "\n");
                    } else {
                        Platform.runLater(() -> {
                            logArea.getChildren().add(new javafx.scene.text.Text("Unexpected token: " + token + "\n"));
                        });
                    }
                } else {
                    Production[] productions = table.get(currentProduction.getName()).get(token);

                    if (productions == null) {
                        Production finalCurrentProduction = currentProduction;

                        Platform.runLater(() -> {
                            Tokens expectedToken = table
                                    .get(finalCurrentProduction.getName())
                                    .keySet().stream().findFirst().orElse(null);

                            logArea.getChildren()
                                    .add(new javafx.scene.text.Text("Expected token: " + expectedToken + "\n"));

                            logArea.getChildren()
                                    .add(new javafx.scene.text.Text("Unexpected token: " + token + "\n"));
                        });
                        break;
                    }

                    for (int i = productions.length - 1; i >= 0; i--) {
                        stack.push(productions[i]);
                    }

                    if (stack.pop().getSymbol() == token) {
                        result.set(result.get() + "Token: " + token + "\n");
                    } else {
                        Platform.runLater(() -> {
                            logArea.getChildren().add(new javafx.scene.text.Text("Unexpected token: " + token));
                        });
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!stack.isEmpty()) {
            Platform.runLater(() -> {
                logArea.getChildren().add(new javafx.scene.text.Text("SyntaxError: Unexpected EOF\n"));
            });
        }
    }
}
