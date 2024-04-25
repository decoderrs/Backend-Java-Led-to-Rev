package com.leadtorev.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**Entry point of the spring boot application.
 * 1.The main method initializes the Spring Application Context by starting up the Spring Boot application. 
 * 2..It creates an instance of the application and wires up all the beans, components, configurations, and 
 * other Spring-managed objects defined in your application.
 * 3.The main method calls SpringApplication.run(ProductAppApisApplication.class, args) to bootstrap the Spring context and start the embedded web server. 
 * 4.The ProductAppApisApplication class is passed as an argument to indicate the primary Spring configuration class for the application. 
 * 5.The args array contains any command-line arguments passed to the application.
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ProductAppApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductAppApisApplication.class, args);
	}

}
