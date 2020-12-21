package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Taco;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author djl
 * @create 2020/12/21 16:11
 */
@Repository
public class ReactorTacoRepositoryImpl implements ReactorTacoRepository {
    @Override
    public Mono<Taco> findTacoById(long id) {
        // 通过使用Mono.just()和Flux的fromIterable()、fromArray()和fromStream()方法，
        // 我们可以将非反应式阻塞代码隔离在repository中，在应用的其他地方，我们都可以使用反应式类型。
        final Mono<Taco> mono = Mono.just(new Taco());
        return mono;
    }

    @Override
    public Mono<Taco> save(Mono<Taco> taco) {
        return null;
    }
}
