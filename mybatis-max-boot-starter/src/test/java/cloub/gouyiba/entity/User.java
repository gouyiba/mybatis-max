package cloub.gouyiba.entity;

import cloub.gouyiba.core.annotation.Column;
import cloub.gouyiba.core.annotation.Id;
import cloub.gouyiba.core.annotation.Table;
import cloub.gouyiba.core.enumation.PrimaryKey;
import cloub.gouyiba.core.typehandler.IEnumTypeHandler;
import cloub.gouyiba.enumatioon.Sex;
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
