package test;

import org.junit.Test;

import io.restassured.RestAssured;
import rest_core.BaseTest;

public class BarrigaTest extends BaseTest {

	@Test
	public void naoDeveacessarAPIsemToken() {
		RestAssured
		.when()
		.get("/contas")
		.then()
		.statusCode(200);
	}
}
