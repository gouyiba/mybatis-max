package cloud.gouyiba.entity;

import cloud.gouyiba.core.annotation.Table;
import lombok.Data;

/**
 * this class by created wuyongfei on 2020/7/1 19:55
 **/
@Data
@Table("account")
public class NoInjectMethodEntity {
    private String id;

    private String userName;
}
