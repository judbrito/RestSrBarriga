package test.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static utils.BarrigaUtils.getIdContaPeloNome;
import static utils.BarrigaUtils.getIdMovDescricao;

import org.junit.Test;

import io.restassured.response.Response;
import rest_core.BaseTest;
import test.Movimentacao;
import utils.DataUtils;
public class MovimentaTest extends BaseTest {
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();

	 given().body(mov).when().post("/transacoes").then().statusCode(201);
	}
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
		mov.setDescricao("Descrição de movimentação");
		mov.setEnvolvido("Envolvido na mov");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
	
	
	@Test
	public void deveValidarCamposObrigatorioMovimentacao() {
		given().body("{}").when().post("/transacoes").then().statusCode(400).body("$", hasSize(8)).body("msg",
				hasItems("Data do pagamento é obrigatório", "Interessado é obrigatório", "Valor é obrigatório",
						"Valor deve ser um número", "Data da Movimentação é obrigatório", "Conta é obrigatório",
						"Situação é obrigatório", "Descrição é obrigatório"));
	}
	@Test
	public void naoDeveInserirMovimentacaoComDataFutura() {
	    Movimentacao mov = getMovimentacaoValida();
	    mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

	    Response response = given().body(mov).when().post("/transacoes");
	    
	    response.then().statusCode(400)
	        .body("$", hasSize(1)) 
	        .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
	}
	@Test
	public void naoDeveRemoverContaComMovimentacao() {
		Integer	CONTA_ID = getIdContaPeloNome("Conta com movimentacao");
		given().pathParam("id", CONTA_ID).when().delete("/contas/{id}").then().statusCode(500).body("constraint",
				is("transacoes_conta_id_foreign"));
	}
	@Test
	public void deveRemoverMovimentacao() {
		Integer	MOV_ID = getIdMovDescricao("Movimentacao para exclusao");
		given().pathParam("id", MOV_ID).when().delete("/transacoes/{id}").then().statusCode(204);
	}

}
