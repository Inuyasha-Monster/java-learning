package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Ingredient;
import com.djl.tacocloud.entity.Taco;
import com.djl.tacocloud.repository.TacoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author djl
 * @create 2020/12/21 16:21
 */
public class DesignTacoControllerTest {

    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        return taco;
    }

    @Test
    public void shouldReturnRecentTacos() {
        Taco[] tacos = {
                testTaco(1L), testTaco(2L), testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L), testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L), testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L), testTaco(15L), testTaco(16L)
        };

        Flux<Taco> tacoFlux = Flux.just(tacos);
        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);

//        when(tacoRepo.findAll(null)).thenReturn(tacoFlux);
        WebTestClient testClient = WebTestClient.bindToController(
                new DesignTacoController(null, tacoRepo)).build();

        testClient.get().uri("/design/recent")
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(tacos[0].getId())
                .jsonPath("$[0].name").isEqualTo("Taco 1")
                .jsonPath("$[1].id").isEqualTo(tacos[1].getId())
                .jsonPath("$[1].name").isEqualTo("Taco 2")
                .jsonPath("$[11].id").isEqualTo(tacos[11].getId())
                .jsonPath("$[11].name").isEqualTo("Taco 12")
                .jsonPath("$[12]").doesNotExist()
                .jsonPath("$[12]").doesNotExist();
    }

    @Test
    public void shouldSaveATaco() {
        TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);
        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(null));
        Taco savedTaco = testTaco(null);
        savedTaco.setId(1L);
        Mono<Taco> savedTacoMono = Mono.just(savedTaco);
//        when(tacoRepo.save(any())).thenReturn(savedTacoMono);
        WebTestClient testClient = WebTestClient.bindToController(
                new DesignTacoController(null, tacoRepo)).build();

        testClient.post()
                .uri("/design")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);
    }

    /**
     * 在这里，你使用 create() 创建了一个新的 WebClient 实例。然后，你可以使用 get() 和 uri() 定义对 http://localhost:8080/ingredients/{id} 的 GET 请求，其中 {id} 占位符将会被 ingredientId 的值所替换。接着，retrieve() 会执行请求。最后，我们调用 bodyToMono()将响应体的载荷抽取到 Mono<Ingredient> 中，就可以继续使用 Mono 的额外操作了。
     */
    @Test
    public void test() {
        Mono<Ingredient> ingredient = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredients/{id}", 1)
                .retrieve()
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(i -> {

        });

        Flux<Ingredient> ingredients = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class); // 最大的差异在于我们不再是使用 bodyToMono() 将响应体抽取为 Mono，而是使用 bodyToFlux() 将其抽取为一个 Flux
        ingredients.subscribe(i -> {

        });

        Flux<Ingredient> ingredients2 = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class);
        ingredients2.timeout(Duration.ofSeconds(1)) // 提供了超时处理
                .subscribe(
                        i -> {

                        },
                        e -> {
                            // handle timeout error
                        });

        // 到目前为止，在使用WebClient的时候，我们都是利用它的retrieve()方法来发送请求。在这些场景中，retrieve()方法会返回一个ResponseSpec类型的对象，通过调用它的onStatus()、bodyToFlux()和bodyToMono()方法，我们就能处理响应
        // 在使用ResponseSpec遇到困难时，我们就可以通过调用exchange()方法来替换retrieve()方法。exchange()方法会返回ClientResponse类型的Mono，我们可以对它采用各种反应式操作，以便于探测和使用整个响应中的数据，包括载荷、头信息和cookie


        // 在exchange()样例中，我们不是使用ResponseSpec对象的bodyToMono()方法来获取Mono<Ingredient>，而是得到了一个Mono<ClientResponse>，通过它我们可以执行扁平化映射（flat-mapping）函数，将ClientResponse映射为Mono<Ingredient>，这样扁平化为最终想要的Mono。
        Mono<Ingredient> ingredient2 = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredients/{id}", 1)
                .exchange()
                .flatMap(x -> x.bodyToMono(Ingredient.class));

        // 假设请求的响应中会包含一个名为X_UNAVAILABLE的头信息，如果它的值为true，则表明该配料是不可用的（因为某种原因）。为了讨论方便，假设如果这个头信息存在，那么我们希望得到的Mono是空的，不返回任何内容。通过添加另外一个flatMap()调用，我们就能实现这一点。整个的WebClient调用过程如下所示
        Mono<Ingredient> ingredient3 = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredients/{id}", 1)
                .exchange()
                .flatMap(x -> {
                    if (x.headers().header("test").contains("true")) {
                        return Mono.empty();
                    }
                    return Mono.just(x);
                })
                .flatMap(x -> x.bodyToMono(Ingredient.class)); // 新的flatMap()调用会探查给定ClientRequest对象的响应头，查看是否存在值为true的X_UNAVAILABLE头信息。如果能够找到，就将会返回一个空的Mono；否则，返回一个包含ClientResponse的新Mono。不管是哪种情况，返回的Mono都会扁平化为下一个flatMap()操作所要使用的Mono。
    }
}
