package com.rabbit.entity;

import com.rabbit.core.annotation.Create;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Update;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SuperEntity {
    @Id
    private String id;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @Create
    private void add() {
        this.createdOn = LocalDateTime.now();
    }

    @Update
    private void updated() {
        this.updatedOn = LocalDateTime.now();
    }
}
