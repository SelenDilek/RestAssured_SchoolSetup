package SchoolSetupApiTest.Locations;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class SchoolSetupLocationsApi_Test {

    Faker faker = new Faker();
    RequestSpecification requestSpecification;
    String locID="";
    String locName="";
    String locCode="";
    @BeforeClass
    public void setup(){

        baseURI = "https://test.mersys.io";
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "turkeyts");
        userInfo.put("password", "TechnoStudy123");
        userInfo.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .body(userInfo)
                        .when()
                        .post("/auth/login")

                        .then()

                        .contentType(ContentType.JSON)
                        .extract().response().getDetailedCookies();

                requestSpecification=new RequestSpecBuilder()
                        .addCookies(cookies)
                        .setContentType(ContentType.JSON)
                        .build()
                        ;

    }


    @Test
    public void addNewLocation(){
        Map<String,Object> newLoc = new HashMap<>();

        locName=faker.name().firstName();
        locCode=faker.name()+faker.number().digits(4);

        // newDept.put("id",null);
        newLoc.put("name",locName);
        newLoc.put("shortName",locCode);
        newLoc.put("active",true);
        newLoc.put("capacity",64);
        newLoc.put("type","CLASS");
        newLoc.put("school","646cbb07acf2ee0d37c6d984");

        locID=
                given()
                        .spec(requestSpecification)
                        .body(newLoc)

                        .when()
                        .post("/school-service/api/location")

                        .then()
                        .log().body()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id")

        ;

    }



}
