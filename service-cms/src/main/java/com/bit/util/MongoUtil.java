package com.bit.util;

import com.bit.module.manager.bean.OperationLog;
import com.bit.module.manager.bean.PageResult;
import com.bit.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.bit.common.Const.OPERATIONLOGTABLE;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 14:38
 */
@Component
public class MongoUtil {

    public static final int FIRST_PAGE_NUM = 1;
    public static final int INIT_PAGE_size = 10;
    public static final String ID = "_id";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoUtil(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 分页查询
     * @param query 查询query
     * @param total 查询总数query
     * @param criteria 查询条件
     * @param order 排序
     * @param pageNum 页码数
     * @param pageSize 显示数量
     * @param collectionName 表名
     * @param entityClass 返回实体类名
     * @param <T>
     * @return
     */
    public <T> PageResult<T> listPage(Query query, Query total, Criteria criteria, Sort.Order order,Integer pageNum, Integer pageSize,String collectionName,Class<T> entityClass){
        if (pageNum == null){
            pageNum = FIRST_PAGE_NUM;
        }
        if (pageSize == null){
            pageSize = INIT_PAGE_size;
        }
        //分页
        int skip = pageSize * (pageNum - 1);
        query.skip(skip).limit(pageSize);
        //排序
        query.with(new Sort(order));
        //绑定条件
        query.addCriteria(criteria);
        total.addCriteria(criteria);
        //查询结果
        List<T> list= mongoTemplate.find(query,entityClass,collectionName);
        //获取总条数
        Long count = mongoTemplate.count(total,collectionName);
        Integer sum = count.intValue();
        //计算总页数
        Integer pages = sum % pageSize == 0?sum / pageSize:(sum / pageSize)+1;


        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setList(list);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setPages(pages);
        pageResult.setTotal(sum);

        return pageResult;
    }

    /**
     * 根据主键id查询
     * @param mongoId 主键id
     * @param collectionName 表名
     * @param entityClass 返回实体类名
     * @param <T>
     * @return
     */
    public <T> T findById(String mongoId,String collectionName,Class<T> entityClass){
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (StringUtil.isNotEmpty(mongoId)){
            criteria.and("_id").is(mongoId);
        }
        //绑定条件
        query.addCriteria(criteria);
        //根据主键查询
        T byId = mongoTemplate.findById(query, entityClass, collectionName);
        return byId;
    }
    
    /**
     * 增加日志处理
     * @author liyang
     * @date 2019-05-07
     * @param id 业务Id
     * @param contendCode 操作码
     * @param operationType 操作方式
     * @param title 标题
     * @param ip IP地址
     * @param operationAccountNumber 操作人账号
     * @param name 操作人姓名
     * @param tableId 表ID
    */
    public void addOperationToMongo(Long id,String contendCode,String operationType,String title,String categoryName,String ip,
                                    String operationAccountNumber,String name,Integer tableId){

        OperationLog operationLog = new OperationLog();

        //内容ID
        operationLog.setContentId(id);

        //操作码
        operationLog.setContentCode(contendCode);

        //操作方式
        operationLog.setOperationType(operationType);

        //标题
        operationLog.setTitle(title);

        //ip
        operationLog.setOperationAddress(ip);

        //栏目关联
        operationLog.setCategoryName(categoryName);

        //操作员账号
        operationLog.setOperationAccountNumber(operationAccountNumber);

        //操作员名称
        operationLog.setOperationAccountUser(name);

        //数据库表名
        operationLog.setTableId(tableId);

        //增加时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String operationTime = sdf.format(date);

        operationLog.setOperationTime(operationTime);

        mongoTemplate.insert(operationLog,OPERATIONLOGTABLE);
    }

}
