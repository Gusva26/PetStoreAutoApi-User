package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;
import org.example.Models.UserModel;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PutUser implements Task {

    private final UserModel user;

    public PutUser(UserModel user) { this.user = user; }

    public static Performable withData(UserModel user) {  return instrumented(PostUser.class, user); }
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Put.to("/user/" + user.getUsername())
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .body(user) // <-- Enviamos el usuario completo actualizado
                                .log().all()
                        )
        );
    }
}