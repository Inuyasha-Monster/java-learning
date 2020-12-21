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
        final Mono<Taco> mono = Mono.just(new Taco());
        return mono;
    }

    @Override
    public Mono<Taco> save(Mono<Taco> taco) {
        return null;
    }
}
