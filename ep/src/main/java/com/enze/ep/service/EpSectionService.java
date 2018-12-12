package com.enze.ep.service;

import com.enze.ep.entity.EpSection;

public interface EpSectionService {
    
    EpSection findEpSectionByID(int sectionid);
    
    /**
     * 返回EpSection对象
    * @Title: saveOrFindEpSectionByWlAmdResponseSSection
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param WlAmdResponseSSection  远程访问得到的科室信息
    * @param @return    参数
    * @author wuxuecheng
    * @return EpSection    返回类型
    * @throws
     */
    EpSection saveOrFindEpSectionByWlAmdResponseSSection(EpSection WlAmdResponseSSection);
   
}
