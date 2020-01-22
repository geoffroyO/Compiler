package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidNumberValue extends DecaRecognitionException {


    public InvalidNumberValue(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "syntax error, this value is too large";
    }
}
