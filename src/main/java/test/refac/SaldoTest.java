package test.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static utils.BarrigaUtils.getIdContaPeloNome;

import org.junit.Test;

import rest_core.BaseTest;
public class SaldoTest extends BaseTest {
	
	
	

	@Test
	public void deveCalcularSomaDasContas() {
		Integer	CONTA_ID = getIdContaPeloNome("Conta para saldo");
		given().when().get("/saldo").then().statusCode(200).body("find{it.conta_id == "+ CONTA_ID +"}.saldo",
				is("534.00"));
		
	}
	
}
