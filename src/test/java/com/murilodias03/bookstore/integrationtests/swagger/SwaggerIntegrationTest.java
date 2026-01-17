package com.murilodias03.bookstore.integrationtests.swagger;

import com.murilodias03.bookstore.config.TestConfigs;
import com.murilodias03.bookstore.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;

@Disabled("Docker com problemas no Windows - Pular temporariamente")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldDisplaySwaggerUIPage() {
        var content = given()
            .basePath("/swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT)
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        assertTrue(content.contains("Swagger UI"));
    }

}