package com.djl.tacocloud.config;

import com.djl.tacocloud.entity.Taco;
import com.djl.tacocloud.repository.TacoRepository;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
 * @author djl
 * @create 2020/12/24 9:55
 * AbstractRepositoryEventListener，这是Spring Data中的一个类，能够拦截repository事件。我们重写了onAfterCreate()方法，这样每当保存新的Taco对象时都会得到通知
 */
@Component
public class TacoMetrics extends AbstractRepositoryEventListener<Taco> {
    private TacoRepository tacoRepository;

    public TacoMetrics(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    @Override
    protected void onAfterCreate(Taco entity) {
        super.onAfterCreate(entity);
    }
}
