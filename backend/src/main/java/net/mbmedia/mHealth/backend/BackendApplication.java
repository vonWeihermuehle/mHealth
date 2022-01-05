package net.mbmedia.mHealth.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

	public static final String JDBC_URL = "jdbc.url";

	public static void main(String[] args){
		SpringApplication app = new SpringApplication(BackendApplication.class);
		//todo: auf welchem Port soll das Backend laufen
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));

		if(args.length > 0){
			if(args[0].length() > 1){
				System.setProperty(JDBC_URL, args[0]);
			}
		}

		app.run(args);
	}

}
