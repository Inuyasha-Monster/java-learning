package com.djl.tacocloud.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import javax.persistence.Table;

/**
 * @author djl
 * @create 2020/12/21 17:12
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "cassandraTest")
public class CassandraModel {
    @PrimaryKey
    private final String id;

    private final String name;
}
