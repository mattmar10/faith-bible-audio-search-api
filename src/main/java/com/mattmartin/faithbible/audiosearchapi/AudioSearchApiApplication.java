package com.mattmartin.faithbible.audiosearchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.mattmartin.faithbible.audiosearchapi")
@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
public class AudioSearchApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudioSearchApiApplication.class, args);
	}
}
