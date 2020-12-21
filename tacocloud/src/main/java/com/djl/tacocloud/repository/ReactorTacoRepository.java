package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Taco;
import reactor.core.publisher.Mono;

/**
 * @author djl
 * @create 2020/12/21 16:08
 */
public interface ReactorTacoRepository {
    Mono<Taco> findTacoById(long id);

    Mono<Taco> save(Mono<Taco> taco);
}
