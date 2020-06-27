package cloud.gouyiba.entity;

import cloud.gouyiba.core.annotation.Column;
import cloud.gouyiba.core.typehandler.IEnumTypeHandler;
import cloud.gouyiba.enumatioon.Sex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Account extends BaseEntity {
    private String userName;

    private String pass;

    @Column(typeHandler = IEnumTypeHandler.class)
    private Sex sex;
}
