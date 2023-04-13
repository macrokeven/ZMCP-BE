package com.letoy.main.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemLogInfoQueryCondition {
    String[] uidList;
    String startDate;
    String endDate;
    int[] logTypeList;
}
