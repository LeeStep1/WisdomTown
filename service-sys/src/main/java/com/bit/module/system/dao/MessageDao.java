package com.bit.module.system.dao;

import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.vo.MessageTemplateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-16 19:42
 */
@Repository
public interface MessageDao {




    /**
     * 根据模板ID 获取所属的APPID
     * @author liyang
     * @date 2019-04-04
     * @param id : 模板ID
     * @return : MessageTemplate 模板
     */
    MessageTemplate getMessageTempByMessageTempIdSql(@Param("id") Long id);

    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
    List<String> getTidByMessageTemplateSql(@Param("messageTemplate") MessageTemplate messageTemplate);



    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyujun
     * @date 2019-09-03
     * @param messageTemplate : 模板详情
     * @return : messageTemplate ：模板实例
     */
    List<MessageTemplateRelTid> getTidByMessageTemplates(@Param("messageTemplate") MessageTemplate messageTemplate);



    /**
     * 根据模板ID获取相应的接入端ID
     * @author lyj
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
    List<MessageTemplateRelTid> getTemplateTidConfigByMessageTemplate(@Param("messageTemplate") MessageTemplate messageTemplate);

    /**
     * 根据模板ID获取相应的接入端ID
     * @author lyj
     * @date 2019-04-08
     * @param id : 模板ID
     * @return : MessageTemplateVO ：接入端ID
     */
    MessageTemplateVO getMessageTemplateCategory(@Param("id") Long id);
}
