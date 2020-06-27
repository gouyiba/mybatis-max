package cloud.gouyiba.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class BaseEntity extends SuperEntity{
    private String createdBy;
}
