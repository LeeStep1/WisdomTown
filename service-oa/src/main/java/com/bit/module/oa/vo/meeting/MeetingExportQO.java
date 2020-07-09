package com.bit.module.oa.vo.meeting;

import com.bit.module.oa.bean.Meeting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/4/19 9:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MeetingExportQO extends Meeting {
    private Long userId;

    private List<Integer> statusList;

    public MeetingExportQO(Meeting meeting) {
        BeanUtils.copyProperties(meeting, this);
    }
}
