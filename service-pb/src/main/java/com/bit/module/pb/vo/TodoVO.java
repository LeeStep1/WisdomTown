package com.bit.module.pb.vo;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import com.bit.module.pb.bean.Done;
import com.bit.module.pb.bean.Todo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Todo
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TodoVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 主题，1党员信息 2党员组织关系转移
     */	
	private Integer topic;
    /**
     * 动作 对于党员信息的主题：1新增2停用3启用； 对于党员组织关系转移的主题：1转移 2转入 3转出 4转出到镇外5转入到镇内
     */	
	private Integer action;
    /**
     * 关联id
     */	
	private Long correlationId;
    /**
     * 内容
     */	
	private String content;
    /**
     * 上一处理方名称，组织（或用户）名称
     */	
	private String lastHandlerName;
    /**
     * 受理人类型，1组织id 2用户id 3角色
     */	
	private Integer assigneeType;
    /**
     * 受理人（泛化），所属组织id或用户id等
     */	
	private String assignee;
    /**
     * 提交时间
     */	
	private Date submitTime;

	//columns END

    public static Todo buildTodo(Done done) {
        if (done != null) {
            Todo todo = new Todo();
            todo.setTopic(done.getTopic());
            todo.setAction(done.getAction());
            todo.setCorrelationId(done.getCorrelationId());
            todo.setSubmitTime(done.getSubmitTime());
            todo.setContent(done.getContent());
            return todo;
        }
        return null;
    }
}


