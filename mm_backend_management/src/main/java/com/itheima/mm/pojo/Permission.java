package com.itheima.mm.pojo;

import lombok.Data;

/**
 * 权限POJO类
 */
@Data
public class Permission {
  private Integer id;
  private String name;
  private String keyword;
  private String description;
}
