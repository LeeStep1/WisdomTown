package com.bit.module.vol.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Favourite;
import com.bit.module.vol.vo.FavouriteVO;

/**
 * @author chenduo
 * @create 2019-03-08 15:21
 */
public interface FavouriteService {

    BaseVo add(Favourite favourite);

    BaseVo listPage(FavouriteVO favouriteVO);

    BaseVo cancelFavouriteById(Long id);
}
