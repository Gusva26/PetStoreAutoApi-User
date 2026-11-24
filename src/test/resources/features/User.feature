@UserModule
  Feature: Gestion de usuarios en PetStore
    Como usuario del sistema PetStore
    Quiero gestionar las cuentas de usuario
    Para asegurar la correcta administracion de perfiles

  Background:
    Given el actor establece la base url de petstore

  @CrearUsuario @Positvo
  Scenario Outline: Crear un usuario exitosamente
    When el actor crea un usuario con el "<id>" "<username>" "<firstname>" "<lastname>" "<email>" "<password>" "<phone>"
    Then el codigo de respuesta debe ser 200

    Examples:
    | id    | username      | firstname   | lastname   | email                   | password           | phone        |
    | 1001  | gusva26       | Luis        | Silva      | gusva.2608@gmail.com    | contraseña123      | 960077918    |


    @ConsultarUsuario @Positivo
    Scenario: Consultar un usuario existente por su username
      Given que el actor tiene un usuario registrado previamente
      When el actor consulta los datos del usuario registrado
      Then el codigo de respuesta debe ser 200
      And los datos del usuario consultado deben coincidir con los registrados


    @ActualizarUsuario @Positivo
    Scenario: Actualizar la informacion de un usuario existente
      Given que el actor tiene un usuario registrado previamente
      When el actor actualiza el email a "lucho26@gmail.com" y telefono a "970641645"
      Then el codigo de respuesta debe ser 200
      And el usuario consultado debe reflejar el nuevo email "lucho26@gmail.com"


    @ConsultarUsuarioInexistente @Negativo
    Scenario: Consultar un usuario que no existe
      When el actor consulta el usuario con username "carlitosNTT29"
      Then el codigo de respuesta debe ser 404
      And el mensaje de error debe ser "User not found"
