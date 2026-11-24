package org.example.Tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetUser implements Task {

    private final String username;

    public GetUser(String username) { this.username = username; }

    public static Performable byUsername(String username) {
        return instrumented(GetUser.class, username);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/user/" + username)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .log().all()
                        )
        );
    }
}