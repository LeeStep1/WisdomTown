package com.bit.data.mongodb;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
/*import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;*/
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Description:  Mongodb数据库基本操作
 * @Author: liyujun
 * @Date: 2020-05-14
 **/
public abstract class MongodbBaseRepository/*<T, K extends Serializable> */{


   /* private MongoTemplate mongoTemplate;


    private Class getCollectionNameByClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }


    protected abstract MongoTemplate setMongoTemplate();

    *//**
     * @param document :  文件数据
     * @return : void
     * @description:
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpAdd(T document) {
        this.mongoTemplate.save(document);
    }


    *//**
     * @param query :  查询条件
     * @return : void
     * @description: 删除某条数据
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpDelete(Query query) {

        this.mongoTemplate.remove(query, getCollectionNameByClass());
    }

    *//**
     * @param query       :查询条件
     * @param collectionName :表名称
     * @return : void
     * @description:
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpDelete(Query query, String collectionName) {

        this.mongoTemplate.remove(query, collectionName);
    }

    *//**
     * @param id : 数据ID
     * @return : void
     * @description:
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpDeleteById(K id) {

        this.mongoTemplate.remove((Query.query(Criteria.where("_id").is(id))), getCollectionNameByClass());
    }


    *//**
     * @param id          : 数据id
     * @param collectionName : 表名称
     * @return : void
     * @description: 根据id删除数据
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpDeleteById(K id, String collectionName) {
        this.mongoTemplate.remove((Query.query(Criteria.where("_id").is(id))), collectionName);
    }

    *//**
     * @param id             :數據id
     * @param update         :更新的数据
     * @param collectionName : 表名称
     * @return : void
     * @description: 根据id删除数据
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpUpdateById(K id, Update update, String collectionName) {

        this.mongoTemplate.updateFirst((Query.query(Criteria.where("_id").is(id))), update, collectionName);
    }

    *//**
     * @param query          : 查询条件
     * @param update         :更新的数据
     * @param collectionName :表名称
     * @return : void
     * @description: 只更新一条
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpUpdate(Query query, Update update, String collectionName) {

        this.mongoTemplate.updateFirst(query, update, collectionName);
    }

    *//**
     * @param query          :查询条件
     * @param update         :更新的数据
     * @param collectionName :表名称
     * @return : void
     * @description:  只要匹配到的额全部更新
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpUpdateMulti(Query query, Update update, String collectionName) {

        this.mongoTemplate.updateMulti(query, update, collectionName);
    }

    *//**
     * @param query  :查询条件
     * @param update :更新数据
     * @return : void
     * @description: 更新数据操作
     * @author liyujun
     * @date 2020-05-15
     *//*
    public void tpUpdateMulti(Query query, Update update) {

        this.mongoTemplate.updateMulti(query, update, getCollectionNameByClass());
    }

    *//**
     * @param query     :查询条件
     * @param pageIndex :当前页
     * @param pageSize  :每页条数
     * @return : void
     * @description:  分页不查总数
     * @author liyujun
     * @date 2020-05-15
     *//*
    public PageImpl<T> tpQueryPageNoCount(Query query, int pageIndex, int pageSize) {

        Pageable pageable = new PageRequest(pageIndex, pageSize);
        List<T> items = this.mongoTemplate.find(query, getCollectionNameByClass());
        return (PageImpl<T>) PageableExecutionUtils.getPage(items, pageable, () -> 0);
    }

    *//**
     * @param query          :查询条件
     * @param pageIndex      :当前页
     * @param pageSize       : 每页条数
     * @param collectionName : 表名称
     * @return : org.springframework.data.domain.PageImpl<T>  分页数据结构
     * @description: 分页不查总数
     * @author liyujun
     * @date 2020-05-15
     *//*
    public PageImpl<T> tpQueryPageNoCount(Query query, int pageIndex, int pageSize, String collectionName) {

        Pageable pageable = new PageRequest(pageIndex, pageSize);
        List<T> items = this.mongoTemplate.find(query, getCollectionNameByClass(), collectionName);
        return (PageImpl<T>) PageableExecutionUtils.getPage(items, pageable, () -> 0);
    }

    *//**
     * @param query          :查询条件
     * @param pageIndex      :当前页
     * @param pageSize       : 每页条数
     * @return : void
     * @description: 分页
     * @author liyujun
     * @date 2020-05-15
     *//*
    public PageImpl<T> tpQueryPageCount(Query query, int pageIndex, int pageSize) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        int count = (int) this.mongoTemplate.count(query, getCollectionNameByClass());
        List<T> items = this.mongoTemplate.find(query, getCollectionNameByClass());
        return (PageImpl<T>) PageableExecutionUtils.getPage(items, pageable, () -> count);
    }

    *//**
     * @param query          :查询条件
     * @param pageIndex      :当前页
     * @param pageSize       : 每页条数
     * @return : void
     * @description:  分页
     * @author liyujun
     * @date 2020-05-15
     *//*
    public PageImpl<T> tpQueryPageCount(Query query, int pageIndex, int pageSize, String collectionName) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        int count = (int) this.mongoTemplate.count(query, getCollectionNameByClass());
        List<T> items = this.mongoTemplate.find(query, getCollectionNameByClass(), collectionName);
        return (PageImpl<T>) PageableExecutionUtils.getPage(items, pageable, () -> count);
    }*/

}
