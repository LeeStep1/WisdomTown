package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Favourite;
import com.bit.module.vol.service.FavouriteService;
import com.bit.module.vol.vo.FavouriteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenduo
 * @create 2019-03-08 15:20
 */
@RestController
@RequestMapping("/favourite")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    /**
     * 添加收藏
     * @param favourite
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Favourite favourite){
        return favouriteService.add(favourite);
    }

    /**
     * 分页查询收藏
     * @param favouriteVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody FavouriteVO favouriteVO){
        return favouriteService.listPage(favouriteVO);
    }

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @GetMapping("/cancelFavourite/{id}")
    public BaseVo cancelFavouriteById(@PathVariable(value = "id") Long id){
        return favouriteService.cancelFavouriteById(id);
    }
}
