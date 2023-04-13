package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.userEndpointsFromPropertyFile;
import api.paylod.User;
import io.restassured.response.Response;

public class userTestFromPropertyFile {
	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setup() {
		
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		
		//logs
		logger = LogManager.getLogger(this.getClass());
		
	}
	
	@Test(priority = 1)
	public void testPostUser() {
		
		logger.info("*******Creating User*******");
		
		Response response = userEndpointsFromPropertyFile.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*******User is Created*******");
		
	}
	
	@Test(priority = 2)
	public void testGetUserByName() {
		
		logger.info("*******Reading User Info*******");
		
		Response response = userEndpointsFromPropertyFile.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*******User Info is Displayed*******");
	}
	
	@Test(priority = 3)
	public void testUpdateUserName() {
		
		logger.info("*******Updating User*******");
		
		//Update data using pay load
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = userEndpointsFromPropertyFile.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*******User Updated*******");
		
		//Checking data after update
		Response responseAfterUpdate = userEndpointsFromPropertyFile.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
	}
	
	@Test(priority = 4)
	public void testDeleteUserName() {
		
		logger.info("*******Deleting User*******");
		
		Response response = userEndpointsFromPropertyFile.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*******User Deleted*******");
	}

}
