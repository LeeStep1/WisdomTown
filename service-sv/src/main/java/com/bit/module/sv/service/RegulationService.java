package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Regulation;
import com.bit.module.sv.bean.RegulationContent;
import com.bit.module.sv.vo.RegulationContentVO;
import com.bit.module.sv.vo.RegulationPreviewVO;
import com.bit.module.sv.vo.RegulationVO;

import java.util.Collection;
import java.util.List;

/**
 * Regulation的Service
 * @author codeGenerator
 */
public interface RegulationService {
	/**
	 * 根据条件查询Regulation
	 * @param regulationVO
	 * @return
	 */
	BaseVo findByCondition(RegulationVO regulationVO);
	/**
	 * 通过主键查询单个Regulation
	 * @param id
	 * @return
	 */
	Regulation findById(Long id);

	/**
	 * 批量保存Regulation
	 * @param regulations
	 */
	void batchAdd(List<Regulation> regulations);
	/**
	 * 保存Regulation
	 * @param regulation
	 */
	void add(Regulation regulation);
	/**
	 * 更新Regulation
	 * @param regulation
	 */
	void update(Regulation regulation);
	/**
	 * 删除Regulation
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 新增法律正文
	 * @param content
	 */
	void addContent(RegulationContent content);

	/**
	 * 搜索所有法律条文
	 * @param id
	 * @return
	 */
	BaseVo<List<RegulationPreviewVO>> findAll(Long id);

	/**
	 * 查询法规对应的条文
	 * @param regulationId
	 * @return
	 */
	BaseVo findContentByRegulationId(Long regulationId);

	/**
	 * 更新正文
	 * @param content
	 */
    void updateContent(RegulationContent content);

	/**
	 * 删除正文
	 * @param id
	 */
	void deleteContent(Long id);

	/**
	 * 根据正文id查询法律法规
	 * @param contentIds
	 * @return
	 */
    List<RegulationPreviewVO> getRegulationByContentIds(Collection<Long> contentIds);

	/**
	 * 法规正文分页
	 * @param content
	 * @return
	 */
    BaseVo contentListPage(RegulationContentVO content);
}
