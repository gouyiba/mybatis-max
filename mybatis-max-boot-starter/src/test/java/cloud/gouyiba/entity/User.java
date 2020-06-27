package cloud.gouyiba.entity;

import cloud.gouyiba.core.annotation.Column;
import cloud.gouyiba.core.annotation.Id;
import cloud.gouyiba.core.annotation.Table;
import cloud.gouyiba.core.enumation.PrimaryKey;
import cloud.gouyiba.core.typehandler.IEnumTypeHandler;
import cloud.gouyiba.enumatioon.Sex;
import lombok.Data;

@Data
@Table("t_user")
public class User extends BaseBean {

    private Integer id;

    @Id(generateType = PrimaryKey.UUID32)
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    @Column(typeHandler = IEnumTypeHandler.class)
    private Sex sex;

    public User(String stuUid,String stuName,Integer stuAge,Sex sex){
        this.stuUid=stuUid;
        this.stuName=stuName;
        this.stuAge=stuAge;
        this.sex=sex;
    }

    public User(){}
}
