package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.DeadResident;
import com.bit.module.cbo.vo.DeadResidentExportVO;
import com.bit.module.cbo.vo.DeadResidentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 死亡居民信息相关dao
 * @author: liyang
 * @date: 2019-07-22
 **/
public interface DeadResidentDao {

    /**
     * 新增死亡居民信息
     * @author liyang
     * @date 2019-07-22
     * @param deadResident : 新增详情
     */
    void add(@Param("de") DeadResident deadResident);

    /**
     * 修改死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param deadResident : 修改详情
     */
    void modify(@Param("de") DeadResident deadResident);

    /**
     * 返回死亡居民信息列表接口
     * @author liyang
     * @date 2019-07-23
     * @param deadResidentVO : 查询条件
     * @return : List<DeadResident>
     */
    List<DeadResident> findAll(@Param("deadResidentVO") DeadResidentVO deadResidentVO);

    /**
     * 返回死亡居民信息列表接口(导出模式)
     * @author liyang
     * @date 2019-07-23
     * @param deadResident : 查询条件
     * @return : List<DeadResident>
     */
    List<DeadResidentExportVO> findAllForExcel(@Param("deadResident") DeadResident deadResident);

    /**
     * 返回死亡信息详情
     * @author liyang
     * @date 2019-07-23
     * @param id : id
     * @return : DeadResident
     */
    DeadResident detail(@Param("id") Long id);

    /**
     * 删除死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param id : 信息id
     */
    void delete(@Param("id") Long id);

    /**
     * 根据卡类型和卡号去重校验
     * @author liyang
     * @date 2019-07-23
     * @param cardType : 卡类型
     * @param cardNum : 卡号
     */
    Integer countByCardTypeAndCardNum(@Param("cardType") Integer cardType,@Param("cardNum")String cardNum);

    /**
     * 根据卡类型和卡号去重校验(修改时使用)
     * @author liyang
     * @date 2019-07-23
     * @param cardType : 卡类型
     * @param cardNum : 卡号
     * @param id : 要修改的ID
     */
    Integer modifyCountByCardTypeAndCardNum(@Param("cardType") Integer cardType,@Param("cardNum")String cardNum,@Param("id")Long id);
}
