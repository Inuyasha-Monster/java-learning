package com.djl.tacocloud.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author djl
 * @create 2020/12/24 10:02
 * 不管怎么说，NotesEndpoint类使用了@Component注解，这样它会被Spring的组件扫描所发现，并将其初始化为Spring应用上下文中的bean。但是，与我们的讨论关联最大的事情是它还使用了@Endpoint注解，使其成为一个ID为notes的Actuator端点。它默认就是启用的，所以我们不需要在management.web.endpoints.web.exposure.include配置属性中显式启用它。
 * 很重要的一点就是，尽管我只展现了如何使用HTTP与端点交互，但是它们还会暴露为MBean，我们可以使用任意的JMX客户端来进行访问。如果你只想暴露HTTP端点，那么可以使用@WebEndpoint注解而不是@Endpoint来标注端点类
 * 类似的，如果你只想暴露MBean端点，那么可以使用@JmxEndpoint注解进行标注。
 */
@Component
@Endpoint(id = "notes", enableByDefault = true)
public class NotesEndpoint {

    @RequiredArgsConstructor
    private class Note {
        @Getter
        private Date time = new Date();
        @Getter
        private final String text;
    }

    private List<Note> notes = new ArrayList<>();

    @ReadOperation
    public List<Note> notes() {
        return notes;
    }

    @WriteOperation
    public List<Note> addNote(String text) {
        final Note note = new Note(text);
        notes.add(note);
        return notes;
    }

    @DeleteOperation
    public List<Note> addNote(int index) {
        if (index < notes.size()) {
            notes.remove(index);
        }
        return notes;
    }
}
