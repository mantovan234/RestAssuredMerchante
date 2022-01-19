import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;

import java.io.File;

public class ValidateUsersEndpoint {

	
//	private static final File schema = new File(System.getProperty("C:\\Users\\el_ra\\eclipse-workspace\\restAssuredSet\\src\\test\\resources\\")+"schema.json");

	@BeforeTest
	public void setup() {
	    RestAssured.baseURI = "https://reqres.in/api";
	    RestAssured.port = 443;
	}
	
	@Test
	public void basicResponseShowTest() {

		Response response = given().formParams("page", "2").when().get("/users");
		int statusCode = response.getStatusCode();
		String responseText = response.getBody().asString();
		System.out.println(response.statusCode());
		System.out.println(responseText);
		System.out.println(response.statusLine());
		//System.out.println(response.asString());
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(responseText, "{\"page\":2,\"per_page\":6,\"total\":12,\"total_pages\":2,\"data\":[{\"id\":7,\"email\":\"michael.lawson@reqres.in\",\"first_name\":\"Michael\",\"last_name\":\"Lawson\",\"avatar\":\"https://reqres.in/img/faces/7-image.jpg\"},{\"id\":8,\"email\":\"lindsay.ferguson@reqres.in\",\"first_name\":\"Lindsay\",\"last_name\":\"Ferguson\",\"avatar\":\"https://reqres.in/img/faces/8-image.jpg\"},{\"id\":9,\"email\":\"tobias.funke@reqres.in\",\"first_name\":\"Tobias\",\"last_name\":\"Funke\",\"avatar\":\"https://reqres.in/img/faces/9-image.jpg\"},{\"id\":10,\"email\":\"byron.fields@reqres.in\",\"first_name\":\"Byron\",\"last_name\":\"Fields\",\"avatar\":\"https://reqres.in/img/faces/10-image.jpg\"},{\"id\":11,\"email\":\"george.edwards@reqres.in\",\"first_name\":\"George\",\"last_name\":\"Edwards\",\"avatar\":\"https://reqres.in/img/faces/11-image.jpg\"},{\"id\":12,\"email\":\"rachel.howell@reqres.in\",\"first_name\":\"Rachel\",\"last_name\":\"Howell\",\"avatar\":\"https://reqres.in/img/faces/12-image.jpg\"}],\"support\":{\"url\":\"https://reqres.in/#support-heading\",\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"}}");

	}
	
	@Test
	public void schemaValidation(){
		
		final File schema = new File("src/test/resources/schema.json"); 
	    given().header("User-Agent", "MyAppName")
	    .formParams("page", "2")
	    .when().get("/users")
		.then().statusCode(200).body(matchesJsonSchema(schema));
	}
	

	@Test
	public void testDataFields() {

	    given().header("User-Agent", "MyAppName")
	    .formParams("page", "2")
	    .when().get("/users")
		.then().statusCode(200)
		.body("data.id[0]", equalTo(7))
		.body("data.email[0]", equalTo("michael.lawson@reqres.in"))
		.body("data.first_name[0]", equalTo("Michael"))
		.body("data.id", hasItems(9, 11));

	}
	
	
	@Test
	public void whenLogResponseThenOK() {
		given().formParams("page", "2")
	    .when().get("/users")
	    .then().log().body().statusCode(200);
	}
	
	@Test
	public void ValidateResponseTime() {
		given().formParams("page", "2")
	    .when().get("/users")
	    .then().time(lessThan(4000L));
	}
	
	@Test
	public void testHeaders() {

		Response response = given().formParams("filterBy-name", "Achuapa", "fields", "name,country,location", "paginate", "false" )
	    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VyIjp7ImlkIjoiNWFiM2E3ZWM4MDE2NGQyODE2NDE5YjdhIiwidXNlcm5hbWUiOiJycGFycmEiLCJyb2xlIjoiUk9PVCJ9LCJBcHAiOnsiaWQiOm51bGwsImNvZGUiOm51bGx9LCJEZXZpY2UiOnsicGxhdGZvcm0iOm51bGwsImRldmljZUlkIjpudWxsfSwiQ2xpZW50Ijp7ImlkIjpbIjU3NzFhYWFkNjlhMzdhNzhlMjgwOGZmZCJdfSwiQXBwR3JvdXAiOnt9LCJpYXQiOjE2NDI0MjY1MDUsImV4cCI6MTY0MzAzMTMwNX0.D2yC1Xyap_aioAbUttWobFqpFGbrF_SUXdNZGy9DUlU")
	    .when().get("https://services.nunchee.com/api/1.0/sport/teams?filterBy-name=Achuapa&fields=name,country,location&paginate=false");
		response.then().log().ifValidationFails(LogDetail.BODY).statusCode(200);
		//System.out.println(response.body().asString());
	}

}