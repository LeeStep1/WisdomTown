package com.bit.module.vol.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-25 12:52
 */
@Data
public class Board implements Serializable{

    private Long id;

    private String realName;

    private String stationName;

    private BigDecimal campaignHour;
}
