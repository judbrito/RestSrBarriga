package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.restassured.RestAssured;

public class DataUtils {
	public static String getDataDiferencaDias(Integer qtdDias) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, qtdDias);
		return getDataFormatada(cal.getTime());

	}

	public static String getDataFormatada(Date date) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyy");
		return format.format(date);

	}
	public static Integer getIdMovDescricao(String desc) {
		return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");
	}
}
