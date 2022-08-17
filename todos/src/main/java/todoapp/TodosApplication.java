package todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import todoapp.web.model.SiteProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TodosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodosApplication.class, args);
	}

	/* @ConfigurationPropertiesScan으로 자동으로 빈이 등록되기 때문에 수동으로 등록할 필요가 없다.
	@Bean
	public SiteProperties siteProperties() {
		return new SiteProperties();
	}
	*/
}
