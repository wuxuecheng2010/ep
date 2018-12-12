package com.enze.ep.service;

import com.enze.ep.entity.EpConfig;

public interface EpConfigService {
    
    EpConfig findEpConfigByCfgName(String cfgName);
    
}
