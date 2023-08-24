package test.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import rest_core.BaseTest;

public class ContasTest extends BaseTest {
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
	@Test
	public void deveIncluirContaComSucesso() {
		 given().body("{\"nome\": \"Conta inserida\"}").when().post("/contas").then().statusCode(201);
				
	}

	@Test
	public void deveAlterarContaComSucesso() {
	Integer	CONTA_ID = getIdContaPeloNome("Conta para alterar");
		given().body("{\"nome\": \"Conta alterada\"}").pathParam("id", CONTA_ID).when().put("/contas/{id}")
				.then().statusCode(200).body("nome", is("Conta alterada"));
	}
	public Integer getIdContaPeloNome(String nome) {
		return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
	}
	
	@Test
	public void naoDeveInserirContaMesmoNome() {
		given().body("{\"nome\": \"Conta mesmo nome\"}").when().post("/contas").then().statusCode(400)
		.body("error", is("Já existe uma conta com esse nome!"));
	}
}
