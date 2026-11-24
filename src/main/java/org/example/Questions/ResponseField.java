package org.example.Questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseField implements Question<String> {
    private final String fieldPath;

    public ResponseField(String fieldPath) { this.fieldPath = fieldPath; }

    public static Question<String> of(String fieldPath) { return new ResponseField(fieldPath); }

    @Override
    public String answeredBy(Actor actor) {
        Object value = SerenityRest.lastResponse().path(fieldPath);
        return value != null ? value.toString() : null;
    }
}