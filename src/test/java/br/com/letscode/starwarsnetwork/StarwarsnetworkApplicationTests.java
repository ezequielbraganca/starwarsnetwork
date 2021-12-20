package br.com.letscode.starwarsnetwork;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
class StarwarsnetworkApplicationTests {

	@Test
	void contextLoads() {
	}

}
