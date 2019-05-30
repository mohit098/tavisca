package com.example.searcher.Searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.searcher.Controller.SearchController;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackageClasses = SearchController.class)
public class SearcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearcherApplication.class, args);
	}

}
