package com.bit.module.vol.service;

import com.bit.base.dto.VolunteerInfo;
import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.VerifyParam;
import com.bit.module.vol.bean.Volunteer;
import com.bit.module.vol.vo.VolunteerVO;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 14:02
 */
public interface VolunteerService {
    /**
     * 添加志愿者信息
     * @param volunteer
     */
    BaseVo add(Volunteer volunteer);
    /**
     * 单查志愿者信息
     * @param id
     */
    BaseVo reflectById(Long id);
    /**
     * 更新志愿者信息
     * @param volunteer
     */
    BaseVo update(Volunteer volunteer);
    /**
     * 分页查询
     * @param volunteerVO
     * @return
     */
    BaseVo listPage(VolunteerVO volunteerVO);
    /**
     * app用查询
     * @param volunteerVO
     * @return
     */
    BaseVo nlistPage(VolunteerVO volunteerVO);
    /**
     * 志愿者参加过的活动
     * @param volunteerVO
     * @return
     */
    BaseVo queryCampaignByVolunteer(VolunteerVO volunteerVO);
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    void exportToExcel(String realName,String cardId,BigDecimal lhour,BigDecimal rhour,String stationName,Integer serviceLevel,Integer volunteerStatus, HttpServletResponse response);

    /**
     * 设置业务信息
     * @param volunteer
     * @return
     */
    BaseVo setBusinessInfo(Volunteer volunteer);
    /**
     * app签到接口
     * @param campaignId
     * @return
     */
    BaseVo sign(Long campaignId);
    /**
     * 根据身份证获取志愿者信息
     * @param cardId
     * @return
     */
    VolunteerInfo getInfo(String cardId);
    /**
     * 判断身份证是否重复
     * @param cardId
     * @return
     */
    BaseVo distinctCardId(String cardId);
    /**
     * 根据身份证和手机号校验是否重复
     * @param verifyParam
     * @return
     */
    BaseVo distinctMobileAndCardId(VerifyParam verifyParam);

    /**
     * 根据手机号和身份证号获取志愿者业务表中信息
     * @return
     */
    BaseVo getVolunteerBusInessInfo();

    /**
     * 获取所有志愿者ID
     * @author liyang
     * @date 2019-04-09
     */
    List<Long> getAllVolUserIds();

    /**
     * 排行榜
     * @return
     */
    BaseVo board(VolunteerVO volunteerVO);
    /**
     * 加锁更新积分
     * @param volunteer
     */
    void updatePointLock(Volunteer volunteer, BigDecimal point);
    /**
     * app端更新志愿者信息
     * @param volunteer
     * @return
     */
    BaseVo appUpdate(Volunteer volunteer);
    /**
     * app端单查志愿者
     * @return
     */
    BaseVo appReflect();

    /**
     * 查询给生成excel专用
     * @param volunteer
     * @return
     */
    List<Volunteer> listPageForExcel(Volunteer volunteer);

    /**
     * 根据站点id分页查询志愿者
     * @return
     */
    BaseVo listPageVolByStationId(VolunteerVO volunteerVO);

	/**
	 * 根据站点id查询志愿者记录
	 * @param volunteer
	 * @return
	 */
    BaseVo queryVolunteer(Volunteer volunteer);
}
