package test.refac;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import rest_core.BaseTest;

public class AuthTest extends BaseTest {
	
	
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
