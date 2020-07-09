package com.bit.ms.dao;

import com.bit.ms.bean.Jpush;
import org.apache.ibatis.annotations.*;

/**
 * Jpush管理的Dao
 * @author mifei
 * @date 2019-08-09
 */
@Mapper
public interface JpushDao {


	@Select("select * from t_sys_jpush where tid = #{tid}")
	@Results({
			@Result(column = "tid", property = "tid"),
			@Result(column = "app_key", property = "appKey"),
			@Result(column = "master_secret", property = "masterSecret"),
			// map-underscore-to-camel-case = true 可以实现一样的效果
			// @Result(column = "update_time", property = "updateTime"),
	})
	Jpush findByTid(@Param("tid") Integer tid);

}
