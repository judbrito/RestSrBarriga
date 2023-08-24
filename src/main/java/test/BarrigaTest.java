package test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import rest_core.BaseTest;
import utils.DataUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {

	private static String CONTA_NAME = "Contas" + System.nanoTime();
	private static Integer CONTA_ID;
	private static Integer MOV_ID;

	@BeforeClass
	public static void logar() {
	    Map<String, String> login = new HashMap<String, String>();
	    login.put("email", "oxe@oxe");
	    login.put("senha", "oxeoxe");

	    String token = given().log().all().body(login).contentType(ContentType.JSON)
	        .when().post("/signin").then().log().all().statusCode(200)
	        .extract().path("token");

	    RestAssured.requestSpecification.header("Authorization", "JWT " + token);
	}

	

	
	@Test
	public void t02_deveIncluirContaComSucesso() {
		CONTA_ID = given().body("{\"nome\": \"" + CONTA_NAME + "\"}").when().post("/contas").then().statusCode(201)
				.extract().path("id");
	}

	@Test
	public void t03_deveAlterarContaComSucesso() {
		given().body("{\"nome\": \"" + CONTA_NAME + "judbrito3\"}").pathParam("id", CONTA_ID).when().put("/contas/{id}")
				.then().statusCode(200).body("nome", is(CONTA_NAME + "judbrito3"));
	}

	@Test
	public void t04_naoDeveInserirContaMesmoNome() {
		given().body("{\"nome\": \"" + CONTA_NAME +"}").when().post("/contas").then().statusCode(400);
	}

	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();

		MOV_ID = given().body(mov).when().post("/transacoes").then().statusCode(201).extract().path("id");
	}

	@Test
	public void t06_deveValidarCamposObrigatorioMovimentacao() {
		given().body("{}").when().post("/transacoes").then().statusCode(400).body("$", hasSize(8)).body("msg",
				hasItems("Data do pagamento é obrigatório", "Interessado é obrigatório", "Valor é obrigatório",
						"Valor deve ser um número", "Data da Movimentação é obrigatório", "Conta é obrigatório",
						"Situação é obrigatório", "Descrição é obrigatório"));
	}

	@Test
	public void t07_naoDeveInserirMovimentacaoComDataFutura() {
	    Movimentacao mov = getMovimentacaoValida();
	    mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

	    Response response = given().body(mov).when().post("/transacoes");
	    
	    response.then().statusCode(400)
	        .body("$", hasSize(1)) 
	        .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
	}




	@Test
	public void t08_naoDeveRemoverContaComMovimentacao() {
		given().pathParam("id", CONTA_ID).when().delete("/contas/{id}").then().statusCode(500).body("constraint",
				is("transacoes_conta_id_foreign"));
	}

	@Test
	public void t09_deveCalcularSomaDasContas() {
		given().when().get("/saldo").then().statusCode(200).body("find{it.conta_id == "+ CONTA_ID +"}.saldo",
				is("100.00"));
		
	}


	@Test
	public void t10_deveRemoverMovimentacao() {
		given().pathParam("id", MOV_ID).when().delete("/transacoes/{id}").then().statusCode(204);
	}
	
	@Test
	public void t11_naoDeveAcessarAPIsemToken() {
	    FilterableRequestSpecification req = (FilterableRequestSpecification)RestAssured.requestSpecification;
	    req.removeHeader("Authorization"); 

	    given().when().get("/contas").then().statusCode(401);
	}

	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(CONTA_ID);
		mov.setDescricao("Descrição de movimentação");
		mov.setEnvolvido("Envolvido na mov");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
	

}
