package suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import rest_core.BaseTest;
import test.refac.AuthTest;
import test.refac.ContasTest;
import test.refac.MovimentaTest;
import test.refac.SaldoTest;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentaTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class Suite extends BaseTest {
	@BeforeClass
	public static void logar() {
	    Map<String, String> login = new HashMap<String, String>();
	    login.put("email", "oxe@oxe");
	    login.put("senha", "oxeoxe");

	    String token = given().log().all().body(login).contentType(ContentType.JSON)
	        .when().post("/signin").then().log().all().statusCode(200)
	        .extract().path("token");

	    RestAssured.requestSpecification.header("Authorization", "JWT " + token);
	    RestAssured.get("/reset").then().statusCode(200);
	}
}
