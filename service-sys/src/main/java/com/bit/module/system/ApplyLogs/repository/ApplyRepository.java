package com.bit.module.system.ApplyLogs.repository;


import com.bit.module.system.ApplyLogs.bean.PageMongo;
import com.bit.module.system.bean.NoticeBell;
import com.bit.module.system.vo.NoticeVO;
import com.bit.soft.push.common.MqBaseConst;
import com.bit.soft.push.payload.UserMessage;
import com.bit.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @Description: 申请日志记录组件
 * @Author: liyujun
 * @Date: 2019-03-19
 **/
@Repository
public class ApplyRepository {

    @Autowired
    private MongoTemplate mongoTemplate;




    /**
     * @description:  分页查询公告日志
     * @author chenduo
     * @date 2019-04-09
     * @param noticeVO :
     * @return : List
     */
    public PageMongo<UserMessage> listPage(NoticeVO noticeVO){
        Query query = new Query();
        Query totallist = new Query();
        //设置起始数
        query.skip((noticeVO.getPageNum()-1)*noticeVO.getPageSize());
        //设置查询条数
        query.limit(noticeVO.getPageSize());

        // 排序
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"recTime")));
        //动态拼接查询条件
        //标题
        if(StringUtil.isNotEmpty(noticeVO.getTitle())){
            Pattern pattern = Pattern.compile("^.*"+noticeVO.getTitle()+".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("content").regex(pattern));
            totallist.addCriteria(Criteria.where("content").regex(pattern));
        }
        //应用
        if (noticeVO.getAppId()!=null){
            query.addCriteria(Criteria.where("appid").is(noticeVO.getAppId().intValue()));
            totallist.addCriteria(Criteria.where("appid").is(noticeVO.getAppId().intValue()));
        }
        //消息类型
        if (noticeVO.getNoticeType()!=null){
            query.addCriteria(Criteria.where("msgType").is(noticeVO.getNoticeType()));
            totallist.addCriteria(Criteria.where("msgType").is(noticeVO.getNoticeType()));
        }
        //类目
        if (noticeVO.getCategoryId()!=null){
            query.addCriteria(Criteria.where("categoryCode").is(noticeVO.getCategoryId().toString()));
            totallist.addCriteria(Criteria.where("categoryCode").is(noticeVO.getCategoryId().toString()));
        }
        //已读
        if (noticeVO.getReaded()!=null){
            query.addCriteria(Criteria.where("status").is(noticeVO.getReaded()));
            totallist.addCriteria(Criteria.where("status").is(noticeVO.getReaded()));
        }
        //用户id
        if (noticeVO.getUserId()!=null){
            query.addCriteria(Criteria.where("userId").is(noticeVO.getUserId()));
            totallist.addCriteria(Criteria.where("userId").is(noticeVO.getUserId()));
        }
        //接入端
        if (noticeVO.getTid()!=null){
            query.addCriteria(Criteria.where("tid").is(noticeVO.getTid().toString()));
            totallist.addCriteria(Criteria.where("tid").is(noticeVO.getTid().toString()));
        }
        List<UserMessage> list= mongoTemplate.find(query,UserMessage.class,"message");
        //获取总条数
        long count = mongoTemplate.count(totallist,UserMessage.class,"message");
        PageMongo<UserMessage> page = new PageMongo<>();
        page.setRows(list);
        page.setTotalCount(count);
        page.setTotalPage(count % noticeVO.getPageSize()==0?1:count / noticeVO.getPageSize() +1);
        return page;
    }

    /**
     * 不分页查询mongo
     * @param noticeVO
     * @return
     */
    public List<UserMessage> listPageWithoutPage(NoticeVO noticeVO){
        Query query = new Query();

        // 排序
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"recTime")));
        //动态拼接查询条件
        //标题
        if(StringUtil.isNotEmpty(noticeVO.getTitle())){
            Pattern pattern = Pattern.compile("^"+noticeVO.getTitle()+".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("content").regex(pattern));
        }
        //应用
        if (noticeVO.getAppId()!=null){
            query.addCriteria(Criteria.where("appid").is(noticeVO.getAppId().intValue()));
        }
        //消息类型
        if (noticeVO.getNoticeType()!=null){
            query.addCriteria(Criteria.where("msgType").is(noticeVO.getNoticeType()));
        }
        //类目
        if (noticeVO.getCategoryId()!=null){
            query.addCriteria(Criteria.where("categoryCode").is(noticeVO.getCategoryId().toString()));
        }
        //已读
        if (noticeVO.getReaded()!=null){
            query.addCriteria(Criteria.where("status").is(noticeVO.getReaded()));
        }
        //用户id
        if (noticeVO.getUserId()!=null){
            query.addCriteria(Criteria.where("userId").is(noticeVO.getUserId()));
        }
        //接入端
        if (noticeVO.getTid()!=null){
            query.addCriteria(Criteria.where("tid").is(noticeVO.getTid().toString()));
        }
        List<UserMessage> list= mongoTemplate.find(query,UserMessage.class,"message");

        return list;
    }

    /**
     * @description:  分页查询铃铛列表
     * @author chenduo
     * @date 2019-04-09
     * @param userId :
     * @return : List
     */
    public PageMongo<NoticeBell> listPageForBell(Long userId, Integer tid){
        Query query = new Query();
        Query total = new Query();
        //设置起始数
        query.skip(0);
        //设置查询条数
        query.limit(6);
        // 排序
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"recTime")));
        //消息状态 0未读 1已读
        Criteria criteria = Criteria.where("status").is(MqBaseConst.MONGO_MESSAGE_STATUS_NOT_READED);
        if (userId!=null){
            criteria.and("userId").is(userId);
        }
        if (tid!=null){
            criteria.and("tid").is(tid.toString());
        }
        criteria.and("msgType").nin(5);
        query.addCriteria(criteria);
        total.addCriteria(criteria);
        List<UserMessage> list= mongoTemplate.find(query,UserMessage.class,"message");
        List<NoticeBell> noticeBells = new ArrayList<>();
        for (UserMessage userMessage : list) {
            NoticeBell bell = new NoticeBell();
            BeanUtils.copyProperties(userMessage,bell);
            bell.setMongoId(userMessage.get_id());
            noticeBells.add(bell);
        }
        //获取总条数
        long count = mongoTemplate.count(total,"message");
        PageMongo<NoticeBell> page = new PageMongo<>();
        page.setRows(noticeBells);
        page.setTotalCount(count);
        return page;
    }

    /**
     * 单条记录更新已读未读
     * @param status
     * @param id
     */
    public void updateStatus(Integer status,String id){
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("status", status);
        mongoTemplate.updateFirst(query, update, UserMessage.class,"message");

    }
    /**
     *
     * 功能描述: 
     * @author: chenduo
     * @description: 根据条件查询结果
     * @param: businessId,categoryCode,msgType
     * @return: list
     * @date: 2019-04-11 8:32
     */
    
    public List<UserMessage> queryParam(Long businessId,String categoryCode,Integer msgType){
        Query query = new Query();
        Criteria criteria = Criteria.where("businessId").is(businessId).and("categoryCode").is(categoryCode).and("msgType").is(msgType);
        query.addCriteria(criteria);
        List<UserMessage> list= mongoTemplate.find(query,UserMessage.class,"message");
        return list;
    }
    /**
     *
     * 功能描述: 
     * @author: chenduo
     * @description: 更新mongo的handled
     * @param: userMessages,handled
     * @return: 
     * @date: 2019-04-11 8:40
     */
    
    public void updateMsgType(UserMessage userMessage,Integer msgType){
        Query query = Query.query(Criteria.where("_id").is(userMessage.get_id()));
        Update update = Update.update("msgType", msgType);
        mongoTemplate.updateFirst(query, update, UserMessage.class,"message");
    }

    /**
     * 单查消息
     * @param mongoId
     * @return
     */
    public UserMessage queryMessage(String mongoId){
        Query query = Query.query(Criteria.where("_id").is(mongoId));
        List<UserMessage> userMessages = mongoTemplate.find(query, UserMessage.class, "message");
        return userMessages.get(0);
    }

    /**
     * 批量更新消息状态
     * @param userMessages
     * @param status
     */
    public void updateMsgStatus(List<UserMessage> userMessages,Integer status){
        for (UserMessage userMessage : userMessages) {
            Query query = Query.query(Criteria.where("_id").is(userMessage.get_id()));
            Update update = Update.update("status", status);
            mongoTemplate.updateFirst(query, update, UserMessage.class,"message");
        }
    }
    /**
     *
     * 功能描述: 删除消息
     * @author: chenduo
     * @description:
     * @param:
     * @return:
     * @date: 2019-04-12 13:38
     */
    public void deleteMessage(String mongoId){
        Query query = Query.query(Criteria.where("_id").is(mongoId));
        mongoTemplate.remove(query,"message");
    }
}
