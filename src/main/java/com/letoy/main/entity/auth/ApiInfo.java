package com.letoy.main.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {
  private long id;
  private String name;
  private String url;
  private String method;
  private String role;

}
