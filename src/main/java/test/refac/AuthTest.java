package test.refac;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.FilterableRequestSpecification;
import rest_core.BaseTest;

public class AuthTest extends BaseTest {
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
	
	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
	}

	@Test
	public void t11_naoDeveAcessarAPIsemToken() {
	    FilterableRequestSpecification req = (FilterableRequestSpecification)RestAssured.requestSpecification;
	    req.removeHeader("Authorization"); 

	    given().when().get("/contas").then().statusCode(401);
	}
	
}
