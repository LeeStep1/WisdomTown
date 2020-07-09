package com.bit.module.system.controller;

import com.bit.module.system.bean.MessageCategory;
import com.bit.module.system.service.MessageCategoryService;
import com.bit.module.system.vo.MessageCategoryPageVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.bit.base.vo.BaseVo;


/**
 * 
 *
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 15:21:25
 */
@RestController
@RequestMapping("/messagecategory")
public class MessageCategoryController {
    @Autowired
    private MessageCategoryService messageCategoryService;



	/**
	 * 新增数据
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	@PostMapping("/add")
	public BaseVo add(@RequestBody MessageCategory messageCategory){
        return messageCategoryService.add(messageCategory);
    }




	/**
	 * 编辑数据
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	@PutMapping("/update")
	public BaseVo update(@RequestBody MessageCategory messageCategory){
        return messageCategoryService.update(messageCategory);
	}




	/**
	 * 删除数据
	 * @param id
	 * @author chenduo
	 * @since ${date}
	 */
	@DeleteMapping("/delete/{id}")
	public BaseVo delete(@PathVariable(value = "id") Long id){
        return messageCategoryService.delete(id);
    }


	/**
	 * 多参数查询数据
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 * @return List<MessageCategory>
	 */
	@PostMapping("/findByParam")
	public BaseVo findByParam(@RequestBody MessageCategory messageCategory){
		return messageCategoryService.findByParam(messageCategory);
	}

	/**
	 * 单查数据
	 * @param id
	 * @author chenduo
	 * @since ${date}
	 * @return ${entity}
	 */
	@GetMapping("/reflectById/{id}")
	public BaseVo reflectById(@PathVariable(value = "id") Long id){
		return messageCategoryService.reflectById(id);
    }

	/**
    * 分页查询
	* @param messageCategoryPageVO
	* @return
	*/
	@PostMapping("/listPage")
	public BaseVo listPage(@RequestBody MessageCategoryPageVO messageCategoryPageVO){
		return messageCategoryService.listPage(messageCategoryPageVO);
	}
}
