package com.tcs.vetclinic;

import com.tcs.vetclinic.domain.person.Person;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.HashMap;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonControllerUnitTests {
    RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("Сохранение пользователя с пустыми id и не пустым name")
    @AllureId("1")
    public void postTest() {
        String postUrl = "http://localhost:8080/api/person";
        Person person = new Person("Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с параметрами id = null, name = 'Ivan'", () -> {
            HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);

            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                    postUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Long.class
            );

            step("Проверяем, что в ответе от POST /person получен id", () -> {
                assertNotNull(createPersonResponse.getBody());
            });

            step("Проверяем, что через метод GET /person/{id} мы получим созданного пользователя", () -> {
                String getUrl = "http://localhost:8080/api/person/%s".formatted(createPersonResponse.getBody());
                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                assertNotNull(getResponseEntity);
                assertEquals(createPersonResponse.getBody(), getResponseEntity.getBody().getId());
                assertEquals(person.getName(), getResponseEntity.getBody().getName());
                System.out.println(getResponseEntity.getBody());
            });
        });
    }

    @Test
    @DisplayName("Сохранение пользователя с пустым id и пустым name")
    @AllureId("2")
    public void postTest1() {
        String postUrl = "http://localhost:8080/api/person";
        Person person = new Person();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        step("Отправляем запрос POST /person с пустыми id, name", () -> {
            try {
                HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);
                ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                        postUrl,
                        HttpMethod.POST,
                        requestEntity,
                        Long.class
                );
            } catch (Exception e) {
                String code = e.getMessage();
                code = code.split(" ")[0];
                assertEquals("400", code);
            }
        });
    }

    @Test
    @DisplayName("Изменение данных с помощью существующего id")
    @AllureId("3")
    public void putTest() {
        String postUrl = "http://localhost:8080/api/person";
        Person person = new Person("Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с пустыми id, name", () -> {
            HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);

            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                    postUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Long.class
            );

            String putUrl = "http://localhost:8080/api/person/%s".formatted(createPersonResponse.getBody());
            HttpEntity<Person> putRequestEntity = new HttpEntity<>(new Person("John"), headers);
            restTemplate.put(putUrl, putRequestEntity);

            String getUrl = "http://localhost:8080/api/person/%s".formatted(createPersonResponse.getBody());
            ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
            System.out.println(getResponseEntity.getBody());
        });
    }

    @Test
    @DisplayName("Изменение данных по несуществующему id")
    @AllureId("4")
    public void putTest1() {
        Person person = new Person(222L, "Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с пустыми id, name", () -> {
            try {
                String putUrl = "http://localhost:8080/api/person/%s".formatted(person.getId());
                HttpEntity<Person> putRequestEntity = new HttpEntity<>(new Person("John"), headers);
                restTemplate.put(putUrl, putRequestEntity);

                String getUrl = "http://localhost:8080/api/person/%s".formatted(person.getId());
                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                System.out.println(getResponseEntity.getBody());
            }
            catch (Exception e) {
                String code = e.getMessage();
                code = code.split(" ")[0];
                assertEquals("404", code);
            }
        });
    }

    @Test
    @DisplayName("Изменение данных по недопустимому id")
    @AllureId("5")
    public void putTest2() {
        Person person = new Person(-222L, "Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с пустыми id, name", () -> {
            try {
                String putUrl = "http://localhost:8080/api/person/%s".formatted(person.getId());
                HttpEntity<Person> putRequestEntity = new HttpEntity<>(new Person("John"), headers);
                restTemplate.put(putUrl, putRequestEntity);

                String getUrl = "http://localhost:8080/api/person/%s".formatted(person.getId());
                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                System.out.println(getResponseEntity.getBody());
            }
            catch (Exception e) {
                String code = e.getMessage();
                code = code.split(" ")[0];
                assertEquals("500", code);
            }
        });
    }

    @Test
    @DisplayName("Изменение данных с помощью существующего id")
    @AllureId("6")
    public void deleteTest() {
        String postUrl = "http://localhost:8080/api/person";
        Person person = new Person("Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с пустыми id, name", () -> {
            HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);

            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                    postUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Long.class
            );

            String deleteUrl = "http://localhost:8080/api/person/%s".formatted(createPersonResponse.getBody());
            restTemplate.delete(deleteUrl);
            step("Проверяем, что пользователь действительно удален", () -> {
                try{
                    String getUrl = deleteUrl.formatted(createPersonResponse.getBody());
                    ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                }
                catch (Exception e){
                    String code = e.getMessage();
                    code = code.split(" ")[0];
                    assertEquals("404", code);
                }
            });
        });
    }

    @Test
    @DisplayName("Удаление данных по несуществующему id")
    @AllureId("7")
    public void deleteTest1() {
        Person person = new Person(134L, "Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с пустыми id, name", () -> {
            try {
                String deleteUrl = "http://localhost:8080/api/person/%s".formatted(person.getId());
                restTemplate.delete(deleteUrl);
            }
            catch (Exception e){
                String code = e.getMessage();
                code = code.split(" ")[0];
                assertEquals("409", code);
            }
        });
    }

    @Test
    @DisplayName("Изменение данных по недопустимому id")
    @AllureId("8")
    public void deleteTest2() {
        Person person = new Person(-222L, "Ivan");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        step("Отправляем запрос POST /person с пустыми id, name", () -> {
                try {
                    String deleteUrl = "http://localhost:8080/api/person/%s".formatted(person.getId());
                    restTemplate.delete(deleteUrl);
                }
                catch (Exception e){
                    String code = e.getMessage();
                    code = code.split(" ")[0];
                    assertEquals("500", code);
                }
        });
    }

//    @Test
//    @DisplayName("Запрос пользователя через существующий id")
//    @AllureId("10")
//    public void getTest() {
////        String postUrl = "http://localhost:8080/api/person/%s";
////        for(long i = 1L; i < 40L; i++){
////            String getUrl = postUrl.formatted(i);
////            try{
////                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
////                System.out.println(getResponseEntity);
////            }
////            catch (Exception e){
////
////            }
////        }
//
//        step("Создаем пользователя и получаем его id с помощью POST/person", () -> {
//            Person person = new Person("Ivan");
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);
//            String postUrl = "http://localhost:8080/api/person/page=%s&size=%s&sort=%s";
////            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
////                    "http://localhost:8080/api/person",
////                    HttpMethod.POST,
////                    requestEntity,
////                    Long.class
////            );
//            step("Отправляем запрос GET/{id} с полученным id", () -> {
//                String getUrl = postUrl.formatted("0", "10", "ASK");
//                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
//                System.out.println(getResponseEntity);
//            });
//        });
//    }

    @Test
    @DisplayName("Запрос пользователя через существующий id")
    @AllureId("9")
    public void getIdTest() {
        String postUrl = "http://localhost:8080/api/person/%s";
        step("Создаем пользователя и получаем его id с помощью POST/person", () -> {
            Person person = new Person("Ivan");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Person> requestEntity = new HttpEntity<>(person, headers);

            ResponseEntity<Long> createPersonResponse = restTemplate.exchange(
                    "http://localhost:8080/api/person",
                    HttpMethod.POST,
                    requestEntity,
                    Long.class
            );
            step("Отправляем запрос GET/{id} с полученным id", () -> {
                String getUrl = postUrl.formatted(createPersonResponse.getBody());
                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                assertNotNull(getResponseEntity);
                assertEquals(createPersonResponse.getBody(), getResponseEntity.getBody().getId());
                System.out.println(getResponseEntity.getBody());
                assertEquals(person.getName(), getResponseEntity.getBody().getName());
            });
        });
    }

    @Test
    @DisplayName("Запрос пользователя через несуществующий id")
    @AllureId("10")
    public void getIdTest1() {
        String postUrl = "http://localhost:8080/api/person/%s";

        Person person = new Person(1205641L, "Ivan");
        step("Отправляем запрос GET/{id} с существующим id", () -> {
            try {
                String getUrl = postUrl.formatted(person.getId());
                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                assertNotNull(getResponseEntity);
                assertEquals(person.getId(), getResponseEntity.getBody().getId());
                System.out.println(getResponseEntity.getBody());
                assertEquals(person.getName(), getResponseEntity.getBody().getName());
            } catch (Exception e) {
                String code = e.getMessage();
                code = code.split(" ")[0];
                assertEquals("404", code);
            }
        });
    }

    @Test
    @DisplayName("Запрос пользователя через недопустимый id")
    @AllureId("11")
    public void getIdTest2() {
        String postUrl = "http://localhost:8080/api/person/%s";

        Person person = new Person(-129L, "Ivan");
        step("Отправляем запрос GET/{id} с недопустимым id", () -> {
            try {
                String getUrl = postUrl.formatted(person.getId());
                ResponseEntity<Person> getResponseEntity = restTemplate.getForEntity(getUrl, Person.class);
                assertNotNull(getResponseEntity);
                assertEquals(person.getId(), getResponseEntity.getBody().getId());
                System.out.println(getResponseEntity.getBody());
                assertEquals(person.getName(), getResponseEntity.getBody().getName());
            } catch (Exception e) {
                String code = e.getMessage();
                code = code.split(" ")[0];
                assertEquals("500", code);
            }
        });
    }
}
