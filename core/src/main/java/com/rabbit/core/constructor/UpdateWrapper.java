package com.rabbit.core.constructor;

import java.io.Serializable;

/**
 * 修改条件构造器
 *
 * @param <E>
 * @author duxiaoyu
 * @since 2019-12-12
 */
public class UpdateWrapper<E extends Serializable> extends QueryWrapper<E> implements Serializable {

    /*
    * UpdateWrapper继承自QueryWrapper，所以拥有QueryWrapper的条件拼接生成能力
    * 目前的思路想法:
    * 在UpdateWrapper中定义修改时，所需要的Method实现，以及Sql的生成拼接规则
    *
    * 使用方法(暂时拟定):
    * User user=new User();
    * user.setId(10010);
    * user.setName("admin");
    * user.setAge(20);
    *
    * 默认按照 @Id 注解标注的主键来修改指定数据
    * UpdateWrapper<User> updateWrapper=new UpdateWrapper<User>();
    * updateWrapper.setEntity(user); 设置要修改的对象
    * 修改时可以传入对象，如上User，生成的Sql是:
    * update set user name=#{name,jdbcType=VARCHAR},age=#{age,jdbcType=INT} where id=#{id,jdbcType=INT}
    *
    *
    * 也可以不传入对象，直接指定条件修改数据，实例化UpdateWrapper，指定泛型并在构造函数中传入指定的bean.class
    * UpdateWrapper<User> updateWrapper=new UpdateWrapper<User>(User.class);
    * Map<String,Object> map=new HashMap<>();
    * map.put("phone","166xxx95036");
    * updateWrapper.setMap(map); 设置要修改的字段value
    * 设置修改条件
    * updateWrapper.where("age",20);
    * updateWrapper.where("sex","男");
    * 如上生成的Sql是:
    * update set user name=#{phone,jdbcType=VARCHAR} where age=#{age,jdbcType=INT} and sex=#{sex,jdbcType=INT}
    *
    * TODO 构建思路待实现 ...
    * */
}
