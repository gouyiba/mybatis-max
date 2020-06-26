package cloub.gouyiba.entity;

import cloub.gouyiba.enumatioon.Sex;
import cloub.gouyiba.core.annotation.Column;
import cloub.gouyiba.core.typehandler.IEnumTypeHandler;
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
