package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description :
 * @Date ： 2019/1/29 14:19
 */
@Data
public class PartyDueStatistics implements Serializable {
    private Integer amount;

    private Integer month;

    public static List<PartyDueStatistics> groupPartyDueByMonth(List<PartyDueStatistics> toGroup) {
        // 分开每月的党费金额
        Map<Integer, List<PartyDueStatistics>> collect = toGroup.stream()
                .collect(Collectors.groupingBy(PartyDueStatistics::getMonth));
        // 汇总每月的党费金额
        return collect.entrySet().stream().map(entry -> {
            PartyDueStatistics partyDueStatistics = new PartyDueStatistics();
            partyDueStatistics.setMonth(entry.getKey());
            partyDueStatistics.setAmount(entry.getValue()
                    .stream().filter(result -> result.getAmount() != null)
                    .map(PartyDueStatistics::getAmount).reduce(0, Integer::sum));
            return partyDueStatistics;
        }).collect(Collectors.toList());
    }
}
