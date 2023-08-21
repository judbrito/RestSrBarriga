package test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
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
            given()
                .log().all()
                .body(login)
                .contentType(ContentType.JSON)
            .when()
                .post("/signin")
            .then()
                .log().all()
                .statusCode(200)
                .extract().path("token");
    }

    @Test
    public void naoDeveAcessarAPIsemToken() {
        given()
            .when()
                .get("/contas")
            .then()
                .statusCode(401);
    }

    @Test
    public void deveIncluirContaComSucesso() {
        given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"judbrito4\" }")
            .when()
                .post("/contas")
            .then()
                .statusCode(201);

    }

    @Test
    public void deveAlterarContaComSucesso() {
        given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"judbrito3\" }")
            .when()
                .put("/contas/1865373")
            .then()
                .statusCode(200)
                .body("nome", is("judbrito3"));

    }

    @Test
    public void naoDeveInserirContaMesmoNome() {
        given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"judbrito3\" }")
            .when()
                .post("/contas")
            .then()
                .statusCode(400);

    }

    @Test
    public void deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = new Movimentacao();

        mov.setConta_id(1864454);
        // mov.setUsuario_id(usuario_id);
        mov.setDescricao("Descrição de movimentação");
        mov.setEnvolvido("Envolvido na mov");
        mov.setTipo("REC");
        mov.setData_transacao("01/01/2000");
        mov.setData_pagamento("10/05/2010");
        mov.setValor(100f);
        mov.setStatus(true);

        given()
            .header("Authorization", "JWT " + TOKEN)
            .body(mov)
            .when()
                .post("/transacoes")
            .then()
                .statusCode(201);

    }
    @Test
    public void deveValidarMensagemMovimentacao() {
               given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{}")
            .when()
                .post("/transacoes")
            .then()
                .statusCode(400)
                .body("$",hasSize(8))
                .body("msg",hasItems("Data do pagamento é obrigatório",
                		"Interessado é obrigatório",
                		"Valor é obrigatório",
                		"Valor deve ser um número",
                		"Data da Movimentação é obrigatório",
                		"Conta é obrigatório",
                		"Situação é obrigatório",
                		"Descrição é obrigatório"));
;
    }
}

