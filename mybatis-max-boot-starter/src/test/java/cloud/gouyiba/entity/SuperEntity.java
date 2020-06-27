package cloud.gouyiba.entity;

import cloud.gouyiba.core.annotation.Create;
import cloud.gouyiba.core.annotation.Id;
import cloud.gouyiba.core.annotation.Update;
import cloud.gouyiba.core.enumation.PrimaryKey;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SuperEntity {
    @Id(generateType = PrimaryKey.UUID36)
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
