package com.bit.module.vol.bean;

import lombok.Data;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-11 19:00
 */
@Data
public class StationApp {

    private Station station;

    private List<Campaign> campaignList;
}
