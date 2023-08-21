package test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.restassured.http.ContentType;
import rest_core.BaseTest;

public class BarrigaTest extends BaseTest {
	private String TOKEN;
	
	@Before
	public void logar() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "oxe@oxe");
		login.put("senha", "oxeoxe");

		 TOKEN = 
				given().log().all()
				.body(login)
				.contentType(ContentType.JSON)
				.when().post("/signin")
				.then()
				.log()
				.all()
				.statusCode(200)
				.extract().path("token");
	}

	@Test
	public void naoDeveacessarAPIsemToken() {
		given().when().get("/contas").then().statusCode(401);
	}

	@Test
	public void deveIncluirContaComSucesso() {
			given()
		.header("Authorization", "JWT " + TOKEN)
		.body("{ \"nome\": \"judbrito3\" }")// aqui vc cria uma conta nova 
		.when()
		.post("/contas")
		.then()
		.statusCode(201);

	}
	@Test
	public void deveAlterarContaComSucesso() {
			given()
		.header("Authorization", "JWT " + TOKEN)
		.body("{ \"nome\": \"Alterar Conta\" }")
		.when()
		.put("/contas/1865357")
		.then()
		.log()
		.all()
		.statusCode(200)
		.body("nome", is("Alterar Conta"));

	}
}

