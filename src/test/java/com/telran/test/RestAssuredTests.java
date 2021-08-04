package com.telran.test;

import com.jayway.restassured.RestAssured;
import com.telran.dto.AuthRequestDto;
import com.telran.dto.AuthResponseDto;
import com.telran.dto.ContactDto;
import com.telran.dto.GetAllContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class RestAssuredTests {

    @BeforeMethod
    public void ensurePrecondition() {
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com";
        RestAssured.basePath = "api";
    }

    @Test
    public void loginPositiveTest() {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("neuer@gmail.com")
                .password("Neuer123456~")
                .build();


        AuthResponseDto responseDto = RestAssured.given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5ldWVyQGdtYWlsLmNvbSJ9.fmWsYTleRgSuss2V1yFPI5XtAFFogJHRk7ln_BZpXOo";

        String token2 = given().contentType("application/json")
                .body(requestDto)
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .body(containsString("token"))
                .body("token", equalTo(token))
                .extract().path("token");
        System.out.println(token2);
    }

    @Test
    public void loginNegativeTest() {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("neuer@gmail.com")
                .password("Nr123456~")
                .build();

        String message = given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(401)
                .extract().path("message");
        System.out.println(message);

    }

    @Test
    public void addNewContactTest() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5ldWVyQGdtYWlsLmNvbSJ9.fmWsYTleRgSuss2V1yFPI5XtAFFogJHRk7ln_BZpXOo";
        ContactDto contactDto =
                ContactDto.builder()
                        .address("Dortmund")
                        .description("Forward")
                        .email("emre@gmail.com")
                        .lastName("Can")
                        .name("Emre")
                        .phone("8876543212").build();

        int id = given().header("Authorization", token)
                .contentType("application/json")
                .body(contactDto)
                .post("contact")
                .then().assertThat()
                .statusCode(200)
                .extract().path("id");
        System.out.println(id);
    }


    @Test
    public void getAllContactsTest() {
        GetAllContactDto responseDto = given()
                .header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5ldWVyQGdtYWlsLmNvbSJ9.fmWsYTleRgSuss2V1yFPI5XtAFFogJHRk7ln_BZpXOo")
                .get("/contact")
                .then()
                .assertThat().statusCode(200)
                .extract().body().as(GetAllContactDto.class);

        for (ContactDto contact : responseDto.getContacts()) {
            System.out.println(contact.getId() + "***" + contact.getName() + "***");
            System.out.println("==============================");
        }

    }

    @Test
    public void deleteContactTest() {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5ldWVyQGdtYWlsLmNvbSJ9.fmWsYTleRgSuss2V1yFPI5XtAFFogJHRk7ln_BZpXOo";

        String status = given()
                .header("Authorization", token)
                .when()
                .delete("/contact/10684")
                .then()
                .assertThat().statusCode(200)
                .extract().path("status");
        System.out.println(status);
    }

}
