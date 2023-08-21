package rest_core;

import io.restassured.http.ContentType;

public interface Constantes {

	String APP_BASE_URL = "https://barrigarest.wcaquino.me";
	Integer APP_PORT = 443; // port 80 somente para http sem "https"
	String APP_BASE_PATH = "";
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	Long MAX_TIMEOUT = 5000L;
}
