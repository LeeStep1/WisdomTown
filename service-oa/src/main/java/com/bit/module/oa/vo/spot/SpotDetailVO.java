package com.bit.module.oa.vo.spot;

import com.bit.module.oa.bean.Spot;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/18 10:06
 */
@Data
public class SpotDetailVO extends Spot implements Serializable {
    private List<Spot> otherSpot;
}
