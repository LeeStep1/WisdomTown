package com.bit.module.vol.dao;


import com.bit.module.vol.bean.Board;
import com.bit.module.vol.bean.Campaign;
import com.bit.module.vol.bean.VerifyParam;
import com.bit.module.vol.bean.Volunteer;
import com.bit.module.vol.vo.VolunteerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 14:11
 */
@Repository
public interface VolunteerDao {
    /**
     * 新增记录
     * @param volunteer
     */
    void add(Volunteer volunteer);

    /**
     * 单查记录
     * @param id
     * @return
     */
    Volunteer findById(@Param(value = "id") Long id);

    /**
     * 更新记录
     * @param volunteer
     */
    void update(Volunteer volunteer);

    /**
     * 查询所有激活的志愿者
     * @param volunteer
     * @return
     */
    List<Volunteer> findAllActiveVolunteer(Volunteer volunteer);

    /**
     * 分页查询记录
     * @param volunteerVO
     * @return
     */
    List<Volunteer> listPage(VolunteerVO volunteerVO);

    /**
     * 根据站点计算人数
     * @param stationId
     * @return
     */
    Integer countMan(@Param(value = "stationId") Long stationId);

    /**
     * 按照编号查询志愿者
     * @param serialNumber
     * @return
     */
    Volunteer queryBySerialNumber(@Param(value = "serialNumber")String serialNumber);

    /**
     * 按照身份证查询志愿者
     * @param cardId
     * @return
     */
    Volunteer queryByCardId(@Param(value = "cardId")String cardId);

    /**
     * 按照编号模糊查询
     * @param serialNumber
     * @return
     */
    List<String> queryBynewSerialNumber(@Param(value = "serialNumber")String serialNumber);

    /**
     * 校验身份证和手机号是否重复
     * @param verifyParam
     * @return
     */
    Integer verifyCardIdAndMobile(VerifyParam verifyParam);

    /**
     * 校验身份证是否重复
     * @param verifyParam
     * @return
     */
    Integer verifyCardId(VerifyParam verifyParam);

    /**
     * 多参数查询
     * @param volunteer
     * @return
     */
    List<Volunteer> findByParam(Volunteer volunteer);

    /**
     * 排行榜查询
     * @param volunteerVO
     * @return
     */
    List<Board> board(VolunteerVO volunteerVO);

    /**
     * excel专用不分页查询
     * @param volunteer
     * @return
     */
    List<Volunteer> listPageForExcel(Volunteer volunteer);

    /**
     * 批量更新志愿者记录
     * @param volunteerList
     */
    void batchUpdate(@Param(value = "volunteerList")List<Volunteer> volunteerList);

    /**
     * 获取所有志愿者ID
     * @author liyang
     * @date 2019-04-09
     * @param volunteer : 筛选条件
    */
    List<Long> getAllVolUserIdsSql(@Param("volunteer") Volunteer volunteer);
    /**
     * 批量查询志愿者记录
     * @param ids
     */
    List<String> batchSelectByIds(@Param(value = "ids") List<Long> ids);

    /**
     * 根据身份证批量查询数据
     * @param cardIds
     * @return
     */
    List<Volunteer> batchSelectByCardIds(@Param(value = "cardIds") List<String> cardIds,@Param(value = "orderlist")String orderlist);
}
