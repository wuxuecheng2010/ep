package com.enze.ep.service;

import com.enze.ep.entity.EpUser;

public interface EpUserService {
    EpUser findEpUserByUserName(String username);
    EpUser findEpUserByUserCode(String usercode);
    
    EpUser saveOrFindEpUserByWlAmdResponseSUser(EpUser WlAmdResponseSUser);
    
    
}
