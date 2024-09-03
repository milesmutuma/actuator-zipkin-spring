package com.example.actuatortest;

import com.example.actuatortest.post.JsonPlaceHolderService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class ActuatortestApplication {


    public static void main(String[] args) {
        SpringApplication.run(ActuatortestApplication.class, args);
    }

    @Bean
    JsonPlaceHolderService jsonPlaceHolderService() {
        var restClient = RestClient.create("https://jsonplaceholder.typicode.com");
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(JsonPlaceHolderService.class);
    }

    //without Aop

//    @Bean
//    CommandLineRunner commandLineRunner(JsonPlaceHolderService jsonPlaceHolderService, ObservationRegistry observationRegistry) {
//        return args -> {
//            Observation.createNotStarted("posts-load-all-posts", observationRegistry)
//                    .lowCardinalityKeyValue("author", "Ed Mutuma")
//                    .contextualName("post-service.find-all")
//                    .observe(jsonPlaceHolderService::findAll);
//
//        };

//    }

    @Bean
    @Observed(name = "posts-load-all-posts",contextualName = "post.find-all")
    CommandLineRunner commandLineRunner(JsonPlaceHolderService jsonPlaceHolderService) {
        return args -> jsonPlaceHolderService.findAll();
    }


}
