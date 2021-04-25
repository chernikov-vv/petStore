package petstore;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.pet.Category;
import models.pet.Pet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestPetstore {
    String baseURL = "https://petstore.swagger.io/v2/pet/";
    String baseURLId = "https://petstore.swagger.io/v2/pet/100";

    public static Pet fillingPet(){
        Pet myPet = new Pet();
        myPet.setId(100);
        myPet.setName("Rex");
        Category category = new Category();
        category.setName("Dog");
        myPet.setCategory(category);
        myPet.setStatus("available");
        return myPet;
    }

    @BeforeEach
    public void beforeEach(){
        RestAssured.delete(baseURLId);
    }

    @Test
    public void post200(){
        Pet myPet1 = RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .body(fillingPet()).post(baseURL).then().statusCode(200)
                .extract().as(Pet.class);

        Assertions.assertEquals(fillingPet(),myPet1);
    }

    @Test
    public void get404(){
        RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .get(baseURLId).then().statusCode(404);
    }

    @Test
    public void get200(){
        RestAssured.given().contentType(ContentType.JSON)
                .body(fillingPet())
                .post(baseURL).then().statusCode(200);

        Pet myPet1 = RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .body(fillingPet())
                .get(baseURLId).then().statusCode(200)
                .extract().as(Pet.class);

        Assertions.assertEquals(fillingPet(),myPet1);
    }

    @Test
    public void put200() {
        RestAssured.given().contentType(ContentType.JSON).body(fillingPet()).post(baseURL);
        Pet myPet = fillingPet();
        String beforeName = myPet.getName();
        myPet.setName("Charley");

        Pet myPet1 = RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .body(myPet).put(baseURL).then().statusCode(200)
                .extract().as(Pet.class);

        Assertions.assertEquals(myPet,myPet1);
        Assertions.assertNotEquals(beforeName, myPet1.getName());
    }

    @Test
    public void put404() {
        /* фэйлится тест из-за того, что на Сваггере методом put создается новый питомец
         и код статуса запроса по факту получается 200
         */
        Pet myPet = fillingPet();
        myPet.setName("Charley");

        RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .body(myPet)
                .put(baseURL).then().statusCode(404);
    }

    @Test
    public void delete200(){
        RestAssured.given().contentType(ContentType.JSON)
                .body(fillingPet())
                .post(baseURL);

        RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .get(baseURLId).then().statusCode(200);

        RestAssured.delete(baseURLId).then().statusCode(200);

        RestAssured.given().contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .get(baseURLId).then().statusCode(404);
    }

    @Test
    public void delete404(){
        RestAssured.delete(baseURLId).then().statusCode(404);
    }

    @AfterAll
    public static void AfterAll(){
        RestAssured.delete("https://petstore.swagger.io/v2/pet/100");
    }
}
