package com.bit.officialdoc.controller;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.entity.OfficialDocInbox;
import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.service.ArchiveService;
import com.bit.officialdoc.service.InboxService;
import com.bit.officialdoc.service.OfficialDocService;
import com.bit.officialdoc.service.RecycleBinService;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 发件箱
 *
 * @author terry.jiang[taoj555@163.com] on 2019-01-16.
 */
@RestController
@RequestMapping(value = "/outbox")
public class OutboxController {

    @Autowired
    private OfficialDocService officialDocService;

    @Autowired
    private InboxService inboxService;

    @Autowired
    private RecycleBinService recycleBinService;

    @Autowired
    private ArchiveService archiveService;

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @GetMapping("/{type}/{id}")
    public BaseVo<FullDoc> getDoc(@PathVariable("type") Integer type, @PathVariable("id") Long id) {
        BaseVo<FullDoc> baseVo = new BaseVo<>();
        FullDoc fullDoc = new FullDoc();
        Long docId = null;
        switch (OfficialType.getByValue(type)) {
            case OUTBOX:
                fullDoc = officialDocService.getDoc(id);
                if (!fullDoc.getRead()) {
                    officialDocService.markRead(id);
                }
                fullDoc.setDocId(id);
                fullDoc.setSource(OfficialType.OUTBOX.value);
                break;
            case INBOX:
                OfficialDocInbox inboxDoc = inboxService.findDocIdAndReadById(id);
                // 判断读取状态
                if (!inboxDoc.isRead()) {
                    inboxService.markRead(id);
                }
                // 获取详细
                fullDoc = officialDocService.getDoc(inboxDoc.getDocId());
                docId = fullDoc.getId();
                fullDoc.setDocId(docId);
                fullDoc.setId(id);
                fullDoc.setSource(OfficialType.INBOX.value);
                break;
            case BIN:
                OfficialDocRecycleBin officialDocRecycleBin = recycleBinService.findDocIdAndReadById(id);
                if (!officialDocRecycleBin.isRead()) {
                    // 更改已读状态
                    recycleBinService.markRead(id);
                }
                fullDoc = officialDocService.getDoc(officialDocRecycleBin.getDocId());
                docId = fullDoc.getId();
                fullDoc.setDocId(docId);
                fullDoc.setId(id);
                fullDoc.setSource(OfficialType.BIN.value);
                break;
            case ARCHIVE:
                OfficialDocArchive officialDocArchive = archiveService.findDocIdAndReadById(id);
                // 判断读取状态
                if (!officialDocArchive.isRead()) {
                    archiveService.markRead(id);
                }
                // 获取详细
                fullDoc = officialDocService.getDoc(officialDocArchive.getDocId());
                docId = fullDoc.getId();
                fullDoc.setId(id);
                fullDoc.setDocId(docId);
                fullDoc.setSource(OfficialType.ARCHIVE.value);
                break;
            default:
                break;
        }
        baseVo.setData(fullDoc);
        return baseVo;
    }

    /**
     * 列表分页（待发公文、已发公文）
     *
     * @param query
     * @return
     */
    @PostMapping("/query")
    public BaseVo<PageInfo<SimpleDoc>> query(@RequestBody DocQuery query) {
        return officialDocService.query(query);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public BaseVo delete(@NotBlank @RequestParam(value = "ids") String ids, @RequestParam("type") Integer type) {///_delete?ids=1,2,3
        long[] idsArray = Arrays.stream(StringUtils.split(ids, ',')).mapToLong(Long::valueOf).toArray();
        switch (OfficialType.getByValue(type)) {
            case OUTBOX:
                officialDocService.logicDelete(idsArray);
                break;
            case INBOX:
                inboxService.logicDelete(idsArray);
                break;
            case BIN:
                recycleBinService.undoDelete(idsArray);
                break;
            case ARCHIVE:
                archiveService.logicDelete(idsArray);
                break;
            case FOLDER:
                if (idsArray.length == 1) {
                    archiveService.deleteFolder(idsArray[0]);
                }
            default:
                break;
        }
        return new BaseVo();
    }

    /**
     * 新增未发送
     *
     * @param doc
     * @return
     */
    @PostMapping("/create")
    public BaseVo createDoc(@RequestBody FullDoc doc) {
        officialDocService.createDoc(doc, false);
        return new BaseVo();
    }

    /**
     * 新增并发送
     *
     * @param doc
     * @return
     */
    @PostMapping("/createAndSend")
    public BaseVo createAndSend(@Validated @RequestBody FullDoc doc) {
        officialDocService.createAndSend(doc);
        return new BaseVo();
    }

    /**
     * 发送公文
     *
     * @param id
     * @return
     */
    @PutMapping("/send")
    public BaseVo send(@NotNull @RequestParam Long id) {
        officialDocService.sendDoc(id);
        return new BaseVo();
    }

    /**
     * 修改公文
     *
     * @param fullDoc
     * @return
     */
    @PutMapping("/modify")
    public BaseVo modify(@NotBlank @RequestBody FullDoc fullDoc) {
        officialDocService.modifyDoc(fullDoc, fullDoc.getSent());
        return new BaseVo();
    }

    /**
     * 存档
     *
     * @param paramQuery
     * @return
     */
    @PostMapping("/archive")
    public BaseVo archive(@RequestBody ParamQuery paramQuery) {
        long[] idsArray = Arrays.stream(StringUtils.split(paramQuery.getIds(), ',')).mapToLong(Long::valueOf).toArray();
        switch (OfficialType.getByValue(paramQuery.getType())) {
            case OUTBOX:
                officialDocService.archive(paramQuery.getFolderId(), idsArray);
                break;
            case INBOX:
                inboxService.archive(paramQuery.getFolderId(), idsArray);
                break;
            case ARCHIVE:
                archiveService.move(paramQuery.getFolderId(), idsArray);
                break;
            case BIN:
                recycleBinService.archive(paramQuery.getFolderId(), idsArray);
                break;
            default:
                break;
        }
        return new BaseVo();
    }

    /**
     * 已读/未读
     *
     * @param readParam
     * @return
     */
    @PostMapping("/read")
    public BaseVo read(@RequestBody ParamQuery readParam) {
        long[] idsArray = Arrays.stream(StringUtils.split(readParam.getIds(), ',')).mapToLong(Long::valueOf).toArray();
        switch (OfficialType.getByValue(readParam.getType())) {
            case OUTBOX:
                readOutbox(readParam.getRead(), idsArray);
                break;
            case INBOX:
                readInbox(readParam.getRead(), idsArray);
                break;
            case BIN:
                readBin(readParam.getRead(), idsArray);
                break;
            case ARCHIVE:
                readArchive(readParam.getRead(), idsArray);
                break;
            default:
                break;
        }
        return new BaseVo();
    }

    /**
     * 发件箱已读未读
     *
     * @param read
     * @param idsArray
     */
    private void readOutbox(Integer read, long[] idsArray) {
        switch (Read.getByValue(read)) {
            case MARKREAD:
                officialDocService.markRead(idsArray);
                break;
            case MARKUNREAD:
                officialDocService.markUnread(idsArray);
                break;
            default:
                break;
        }
    }

    /**
     * 收件箱已读未读
     *
     * @param read
     * @param idsArray
     */
    private void readInbox(Integer read, long[] idsArray) {
        switch (Read.getByValue(read)) {
            case MARKREAD:
                inboxService.markRead(idsArray);
                break;
            case MARKUNREAD:
                inboxService.markUnread(idsArray);
                break;
            default:
                break;
        }
    }

    /**
     * 回收站已读未读
     *
     * @param read
     * @param idsArray
     */
    private void readBin(Integer read, long[] idsArray) {
        switch (Read.getByValue(read)) {
            case MARKREAD:
                recycleBinService.markRead(idsArray);
                break;
            case MARKUNREAD:
                recycleBinService.markUnread(idsArray);
                break;
            default:
                break;
        }
    }

    /**
     * 回收站已读未读
     *
     * @param read
     * @param idsArray
     */
    private void readArchive(Integer read, long[] idsArray) {
        switch (Read.getByValue(read)) {
            case MARKREAD:
                archiveService.markRead(idsArray);
                break;
            case MARKUNREAD:
                archiveService.markUnread(idsArray);
                break;
            default:
                break;
        }
    }

    @AllArgsConstructor
    enum Read {

        MARKREAD(1, "已读"), MARKUNREAD(0, "未读");

        private int value;

        private String name;

        public static Read getByValue(int value) {
            for (Read read : values()) {
                if (read.value == value) {
                    return read;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    @AllArgsConstructor
    enum OfficialType {

        OUTBOX(0, "发送箱"), INBOX(1, "收件箱"), ARCHIVE(2, "个人归档"), BIN(3, "回收箱"), FOLDER(4, "文件夹");

        private int value;

        private String name;

        public static OfficialType getByValue(int value) {
            for (OfficialType officialType : values()) {
                if (officialType.value == value) {
                    return officialType;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    @Data
    public static class ParamQuery implements Serializable {
        // id集合
        private String ids;
        // 0:未读；1：已读
        private Integer read;
        // 0:发件箱；1：收件箱；2：归档；3：回收站；4：文件夹
        private Integer type;
        // 文件夹ID
        private Long folderId;
    }
}
