package com.bit.officialdoc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;

import com.bit.officialdoc.dao.OfficialDocArchiveDao;
import com.bit.officialdoc.dao.OfficialDocDao;
import com.bit.officialdoc.dao.OfficialDocInboxDao;
import com.bit.officialdoc.dao.OfficialDocRecycleBinDao;
import com.bit.officialdoc.entity.OfficialDoc;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.entity.OfficialDocInbox;
import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.service.OfficialDocService;
import com.bit.officialdoc.service.UserService;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.support.PushUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/16 16:58
 */
@Slf4j
@Service("officialDocService")
public class OfficialDocServiceImpl extends BaseService implements OfficialDocService {

    @Autowired
    private OfficialDocDao officialDocDao;

    @Autowired
    private OfficialDocInboxDao officialDocInboxDao;

    @Autowired
    private OfficialDocRecycleBinDao officialDocRecycleBinDao;

    @Autowired
    private OfficialDocArchiveDao officialDocArchiveDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PushUtil pushUtil;

    @Override
    public BaseVo query(DocQuery query) {
        UserInfo userInfo = getUserInfo();
        query.setOwner(userInfo.getId());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        query.setOrderBy("create_at DESC");
        List<SimpleDoc> simpleDocPageInfo = officialDocDao.query(query);
        simpleDocPageInfo.parallelStream().forEach(simpleDoc -> {
            if (simpleDoc.getAttactIds() != null) {
                simpleDoc.setIsAttach(1);
            } else {
                simpleDoc.setIsAttach(0);
            }
            simpleDoc.setAttactIds(null);
        });
        // 获取未数量
        int unReadNum = officialDocDao.countUnRead(query.getOwner(), query.getSent());
        PageInfo<SimpleDoc> pageInfo = new PageInfo<>(simpleDocPageInfo);
        pageInfo.setEndRow(unReadNum);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public void createDoc(FullDoc doc, Boolean sent) {
        // 回填发件人
        getUserInfo(doc);
        // 获取收件人id集合
        doc.setReceivers(getReceivers(doc.getReceiverIds()));
        doc.setCc(getCc(doc.getCcIds()));
        OfficialDoc officialDoc = FullDoc.buildOfficelDoc(doc, sent);
        if (doc.getDocId() != null) {
            officialDoc.setReferenceDocId(doc.getDocId());
        }
        officialDocDao.create(officialDoc);
        // 回填ID
        doc.setDocId(officialDoc.getId());
        // 如果为回复、转发公文，则公文的“待办”状态改为“已办”
        if (doc.getDocId() != null) {
            officialDocInboxDao.dispose(doc.getDocId());
        }
    }

    @Override
    @Transactional
    public void createAndSend(FullDoc doc) {
        // 发送状态
        doc.setId(null);
        doc.setDocCreateAt(new Date());
        createDoc(doc, true);
        // 批量新增待办信息
        batchCreateInbox(doc);
    }

    @Override
    @Transactional
    public void sendDoc(long id) {
        FullDoc fullDoc = officialDocDao.findById(id);
        int resultNum = officialDocDao.sent(id);
        // 将转发或回复的状态改为 已办理
        if (fullDoc.getDocType() != 0) {
            officialDocInboxDao.dispose(fullDoc.getReferenceDocId());
        }
        // 修改成功，才执行发送
        if (resultNum > 0) {
            // 批量新增待办信息
            batchCreateInbox(fullDoc);
        }
    }

    @Override
    public void modifyDoc(FullDoc doc, Boolean sent) {
        doc.setDocId(doc.getId());
        doc.setReceivers(getReceivers(doc.getReceiverIds()));
        doc.setCc(getCc(doc.getCcIds()));
        OfficialDoc officialDoc = FullDoc.buildOfficelDoc(doc, false);
        officialDocDao.modify(officialDoc);
        if (sent != null && sent == true) {
            int resultNum = officialDocDao.sent(doc.getId());
            // 修改成功，才执行发送
            if (resultNum > 0) {
                // 批量新增待办信息
                batchCreateInbox(doc);
            }
        }
    }

    @Override
    public FullDoc getDoc(long docId) {
        return officialDocDao.findById(docId);
    }

    @Override
    @Transactional
    public void logicDelete(long... docIds) {
        // 逻辑删除
        officialDocDao.logicDelete(docIds);
        List<FullDoc> fullDocs = officialDocDao.findByIds(docIds);
        List<OfficialDocRecycleBin> officialDocRecycleBins = batchCreateRecycleBin(fullDocs, OfficialDocRecycleBin.SOURCE_OUTBOX, getUserInfo().getId());
        officialDocRecycleBinDao.batchCreate(officialDocRecycleBins);
    }

    @Override
    @Transactional
    public void archive(long folderId, long... docIds) {
        List<FullDoc> fullDocs = officialDocDao.findByIds(docIds);
        Long userId = getUserInfo().getId();
        List<OfficialDocArchive> officialDocArchives = ArchiveServiceImpl.createArchive(fullDocs, folderId, userId);
        officialDocArchiveDao.createArchive(officialDocArchives);
    }

    @Override
    public void markRead(long... docIds) {
        officialDocDao.markRead(docIds);
    }

    @Override
    public void markUnread(long... docIds) {
        officialDocDao.markUnRead(docIds);
    }

    private List<FullDoc.Receiver> getReceivers(List<Long> ids) {
        List<FullDoc.Receiver> receivers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("appId", 2);
        params.put("uids", ids);
        BaseVo baseVo = userService.listByAppIdAndIds(params);
        List<User> userReceivers = (List<User>) baseVo.getData();
        userReceivers.parallelStream().forEach(user -> {
            FullDoc.Receiver receiver = new FullDoc.Receiver();
            receiver.setId(user.getId());
            receiver.setName(user.getRealName());
            receivers.add(receiver);
        });
        return receivers;
    }

    private List<FullDoc.CcVO> getCc(List<Long> ccIds) {
        List<FullDoc.CcVO> ccVOS = new ArrayList<>();
        if (ccIds != null && ccIds.size() > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("appId", 2);
            params.put("uids", ccIds);
            BaseVo baseVo = userService.listByAppIdAndIds(params);
            List<User> usersCC = (List<User>) baseVo.getData();
            usersCC.parallelStream().forEach(user -> {
                FullDoc.CcVO ccVO = new FullDoc.CcVO();
                ccVO.setId(user.getId());
                ccVO.setName(user.getRealName());
                ccVOS.add(ccVO);
            });
        }
        return ccVOS;
    }

    /**
     * 获取用户信息
     *
     * @param doc
     */
    private FullDoc getUserInfo(FullDoc doc) {
        UserInfo userInfo = getCurrentUserInfo();
        doc.setDocSenderId(userInfo.getId());
        doc.setDocSenderName(userInfo.getRealName());
        return doc;
    }

    /**
     * 信息待办公文
     *
     * @param doc
     */
    private void batchCreateInbox(FullDoc doc) {
        // 新增收件人+抄送人，去重
        Set<Long> receiverIds = new HashSet<>();
        List<OfficialDocInbox> officialDocInboxes = new ArrayList<>();
        if (doc.getReceivers() != null && doc.getReceivers().size() > 0) {
            JSONArray receivers = JSONArray.parseArray(JSON.toJSONString(doc.getReceivers()));
            for (Iterator iterator = receivers.iterator(); iterator.hasNext(); ) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                receiverIds.add(jsonObject.getLong("id"));
            }
        }
        if (doc.getCc() != null && doc.getCc().size() > 0) {
            JSONArray cc = JSONArray.parseArray(JSON.toJSONString(doc.getCc()));
            for (Iterator iterator = cc.iterator(); iterator.hasNext(); ) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                receiverIds.add(jsonObject.getLong("id"));
            }
        }
        receiverIds.parallelStream().forEach(id -> {
            OfficialDocInbox officialDocInbox = FullDoc.buildOfficialDocInbox(doc);
            officialDocInbox.setReceiverId(id);
            officialDocInboxes.add(officialDocInbox);
        });
        officialDocInboxDao.batchCreate(officialDocInboxes);

        // 查询需要发消息的数据
        List<OfficialDocInbox> docs = officialDocInboxDao.findByDocId(doc.getDocId());
        for (OfficialDocInbox inBoxDoc : docs) {
            pushUtil.sendMessage(inBoxDoc.getId(), MessageTemplateEnum.OFFICIAL_DOC_SEND, TargetTypeEnum.USER,
                    new Long[]{inBoxDoc.getReceiverId()}, new String[]{inBoxDoc.getDocSenderName()},
                    getCurrentUserInfo().getRealName());
        }
    }

    /**
     * 批量新增删除
     *
     * @param fullDocs
     * @param type
     */
    public static List<OfficialDocRecycleBin> batchCreateRecycleBin(List<FullDoc> fullDocs, int type, long userId) {
        List<OfficialDocRecycleBin> officialDocRecycleBins = new ArrayList<>();
        fullDocs.parallelStream().forEach(fullDoc -> {
            OfficialDocRecycleBin officialDocRecycleBin = new OfficialDocRecycleBin();
            // 来源：发件箱
            officialDocRecycleBin.setSource(type);
            officialDocRecycleBin.setOwnerId(userId);
            officialDocRecycleBin.setReferenceId(fullDoc.getId() == null ? fullDoc.getDocId() : fullDoc.getId());
            officialDocRecycleBin.setDeleteAt(new Date());

            officialDocRecycleBin.setDocId(fullDoc.getDocId() == null ? fullDoc.getId() : fullDoc.getDocId());
            officialDocRecycleBin.setDocTitle(fullDoc.getDocTitle());
            officialDocRecycleBin.setDocTextNumber(fullDoc.getDocTextNumber());
            officialDocRecycleBin.setDocSenderId(fullDoc.getDocSenderId());
            officialDocRecycleBin.setDocSenderName(fullDoc.getDocSenderName());

            officialDocRecycleBin.setDocReceivers(fullDoc.getReceivers());
            officialDocRecycleBin.setDocCreateAt(fullDoc.getDocCreateAt());
            officialDocRecycleBin.setRead(fullDoc.getRead() == null ? false : fullDoc.getRead());
            if (fullDoc.getAttactIds() != null) {
                officialDocRecycleBin.setIsAttach(1);
            } else {
                officialDocRecycleBin.setIsAttach(0);
            }
            officialDocRecycleBins.add(officialDocRecycleBin);
        });
        return officialDocRecycleBins;
    }

    private UserInfo getUserInfo() {
        return getCurrentUserInfo();
    }

    @Data
    @AllArgsConstructor
    public static class User implements Serializable {
        // 用户ID
        private Long id;
        // 用户名称
        private String realName;
    }
}
