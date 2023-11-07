package SchoolSetupApiTest.Departments;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class SchoolSetupDepartmentsApi_Test {
    RequestSpecification requestSpecification;
    Faker faker = new Faker();
    String deptName="";
    String depttCode="";
    String deptID="";
    String updName="";
    String updCode="";




    @BeforeClass
    public void setup() {
        baseURI = "https://test.mersys.io";
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "turkeyts");
        userInfo.put("password", "TechnoStudy123");
        userInfo.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .body(userInfo)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
                ;

        requestSpecification=new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void clikSchoolDepartment(){

        Response response =
        given()
                .spec(requestSpecification)
                .when()
                .get("/school-service/api/school/646cbb07acf2ee0d37c6d984/department")

                .then()
                .log().body()
                .statusCode(200)
                .extract().response();

                ;


    }

    @Test(dependsOnMethods = "clikSchoolDepartment")
    public void addNewDepartment(){
        Map<String,Object> newDept = new HashMap<>();
        Object[] arr = new Object[1];

         deptName=faker.name().firstName();
         depttCode=faker.name()+faker.number().digits(4);

         newDept.put("name",deptName);
         newDept.put("code",depttCode);
         newDept.put("active",true);
         newDept.put("school","646cbb07acf2ee0d37c6d984");
         newDept.put("sections",arr);

         deptID=
                 given()
                         .spec(requestSpecification)
                         .body(newDept)

                         .when()
                         .post("/school-service/api/department")

                         .then()
                         .log().body()
                         .contentType(ContentType.JSON)
                         .statusCode(201)
                         .extract().path("id")

                 ;

    }

    @Test(dependsOnMethods = "addNewDepartment")
    public void editExistingdepartment(){

        Map<String,Object> editDept = new HashMap<>();
         updName=faker.name().name();
         updCode=faker.code().asin();
        editDept.put("id",deptID);
        editDept.put("name",updName);
        editDept.put("code",updCode);

        given()
                .spec(requestSpecification)
                .body(editDept)

                .when()
                .put("/school-service/api/department")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name" ,equalTo(updName))

                ;
    }

    @Test(dependsOnMethods = "editExistingdepartment")
    public void negativeUpdateTest(){

        Map<String,Object> editDept = new HashMap<>();

        editDept.put("id",deptID);
        editDept.put("name",updName);
        editDept.put("code",updCode);

        given()
                .spec(requestSpecification)
                .body(editDept)

                .when()
                .put("/school-service/api/department")

                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("code",equalTo(updCode))

                ;

    }

    @Test(dependsOnMethods = "negativeUpdateTest")
    public void deletePositivetest(){


        given()
                .spec(requestSpecification)

                .when()
                .delete("/school-service/api/department/"+deptID)


                .then()
                .statusCode(204)

                ;


    }

    @Test(dependsOnMethods = "deletePositivetest")
    public void nagativeDeletetest(){


        given()
                .spec(requestSpecification)

                .when()
                .delete("/school-service/api/department/"+deptID)


                .then()
                .statusCode(400)

        ;


    }



}
