package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.CassandraModel;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author djl
 * @create 2020/12/21 17:17
 */
public interface CassandraRepository extends ReactiveCrudRepository<CassandraModel, String> {
    /**
     * 在将@AllowFiltering注解放到findByUsername()方法上之后，所形成的CQL查询如下所示：
     * select * from users where username='some username' al1ow filtering;
     * 查询末尾的allow filtering子句提醒Cassandra，我们已经意识到查询性能的潜在影响，并且无论如何都需要它。在这种情况下，Cassandra将允许使用where子句并按需过滤结果。
     * @param name
     * @return
     */
    @AllowFiltering
    Mono<CassandraModel> findByName(String name);
}
