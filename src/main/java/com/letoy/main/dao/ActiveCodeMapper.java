package com.letoy.main.dao;

import com.letoy.main.entity.auth.ActiveCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActiveCodeMapper {
    void createActiveCode(ActiveCode activeCode);
    ActiveCode getActiveCodeByPhoneNumberAndType(String phoneNumber,int type);
    void updateActiveCodeByPhoneNumberAndType(ActiveCode activeCode);
    void delActiveCodeByPhoneNumberAndType(String phoneNumber,int type);
}
