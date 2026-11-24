package org.example.StepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.example.Models.UserModel;
import org.example.Questions.ResponseCode;
import org.example.Questions.ResponseField;
import org.example.Tasks.GetUser;
import org.example.Tasks.PostUser;
import org.example.Tasks.PutUser;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserStepDefinition {

    @Before
    public void setTheStage(){
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("el {actor} establece la base url de petstore")
    public void elActorEstableceLaBaseUrlDePetstore(Actor actor) {
        actor.whoCan(CallAnApi.at("https://petstore.swagger.io/v2"));
    }

    @When("el actor crea un usuario con el {string} {string} {string} {string} {string} {string} {string}")
    public void elActorCreaUnUsuarioConEl(String id, String username, String firstname, String lastname, String email, String password, String phone) {
        UserModel user = new UserModel(Long.parseLong(id), username, firstname, lastname, email, password, phone, 1);
        theActorInTheSpotlight().attemptsTo(PostUser.withData(user));
    }

    @Then("el codigo de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int responseCode) {
        theActorInTheSpotlight().should(
                seeThat("El codigo de respuesta", ResponseCode.getStatus(), equalTo(responseCode))
        );
    }

    @Given("que el actor tiene un usuario registrado previamente")
    public void queElActorTieneUnUsuarioRegistradoPreviamente() {
        // Generamos un ID dinamico usando la hora actual
        long idDinamico = System.currentTimeMillis() % 100000;
        UserModel user = new UserModel(idDinamico, "user_qa" + idDinamico, "Luis", "Silva", "user_" + idDinamico + "@test.com", "contraseña123", "960077918", 1);
        theActorInTheSpotlight().attemptsTo(PostUser.withData(user));
    }

    @When("el actor consulta los datos del usuario registrado")
    public void elActorConsultaLosDatosDelUsuarioRegistrado() {
        UserModel user = theActorInTheSpotlight().recall("user");
        theActorInTheSpotlight().attemptsTo(GetUser.byUsername(user.getUsername()));
    }

    @And("los datos del usuario consultado deben coincidir con los registrados")
    public void losDatosDelUsuarioConsultadoDebenCoincidirConLosRegistrados() {
        UserModel user = theActorInTheSpotlight().recall("user");
        theActorInTheSpotlight().should(
                seeThat("El username coincide", ResponseField.of("username"), equalTo(user.getUsername())),
                seeThat("El email coincide", ResponseField.of("email"), equalTo(user.getEmail()))
        );
    }

    @When("el actor actualiza el email a {string} y telefono a {string}")
    public void elActorActualizaElEmailAYTelefonoA(String nuevoEmail, String nuevoTelefono) {
        UserModel user = theActorInTheSpotlight().recall("user");
        user.setEmail(nuevoEmail);
        user.setPhone(nuevoTelefono);
        theActorInTheSpotlight().attemptsTo(PutUser.withData(user));
    }

    @And("el usuario consultado debe reflejar el nuevo email {string}")
    public void elUsuarioConsultadoDebeReflejarElNuevoEmail(String nuevoEmail) {
        UserModel user = theActorInTheSpotlight().recall("user");
        theActorInTheSpotlight().attemptsTo(GetUser.byUsername(user.getUsername()));
        theActorInTheSpotlight().should(
                seeThat("El nuevo email coincide", ResponseField.of("email"), equalTo(nuevoEmail))
        );
    }

    @When("el actor consulta el usuario con username {string}")
    public void elActorConsultaElUsuarioConUsername(String usernameInexistente) {
        theActorInTheSpotlight().attemptsTo(
                GetUser.byUsername(usernameInexistente)
        );
    }

    @And("el mensaje de error debe ser {string}")
    public void elMensajeDeErrorDebeSer(String mensajeErrorEsperado) {
        theActorInTheSpotlight().should(
                seeThat("El mensaje de error", ResponseField.of("message"), equalTo(mensajeErrorEsperado))
        );
    }
}
