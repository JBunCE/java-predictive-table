package com.jbunce.analizadorlexico.analizers.lexical;

import com.jbunce.analizadorlexico.analizers.Tokens;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Analizer {

    public static void analize(TextFlow logArea, Lexer lexer) {
        AtomicReference<String> result = new AtomicReference<>("");

        while (true) {
            try {
                Tokens token = lexer.yylex();
                if (token == null) {
                    String res = result.get() + "EOF";
                    result.set(res);
                    break;
                }
                switch (token) {
                    case QUOTE -> result.set(result.get() + "Quote: " + lexer.lexeme + "\n");
                    case DISPLAY_DATA -> result.set(result.get() + "Display data: " + lexer.lexeme + "\n");
                    case OPEN_BRACE -> result.set(result.get() + "Open brace: " + lexer.lexeme + "\n");
                    case CLOSE_BRACE -> result.set(result.get() + "Close brace: " + lexer.lexeme + "\n");
                    case OPEN_PARENTHESIS -> result.set(result.get() + "Open parenthesis: " + lexer.lexeme + "\n");
                    case CLOSE_PARENTHESIS -> result.set(result.get() + "Close parenthesis: " + lexer.lexeme + "\n");
                    case SEMICOLON -> result.set(result.get() + "Semicolon: " + lexer.lexeme + "\n");
                    case NON_SPECIFIC -> result.set(result.get() + "Non specific: " + lexer.lexeme + "\n");
                    case ERROR -> result.set(result.get() + "Unexpected: " + lexer.lexeme + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Platform.runLater(() -> {
            logArea.getChildren().clear();
            Text text = new Text(result.get());
            logArea.getChildren().add(text);
        });
    }

}
