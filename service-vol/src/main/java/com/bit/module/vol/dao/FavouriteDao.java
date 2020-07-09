package com.bit.module.vol.dao;

import com.bit.module.vol.bean.Favourite;
import com.bit.module.vol.vo.FavouriteVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-08 15:31
 */
@Repository
public interface FavouriteDao {

    void add(Favourite favourite);

    List<Favourite> listPage(FavouriteVO favouriteVO);

    void delete(Favourite favourite);
}
