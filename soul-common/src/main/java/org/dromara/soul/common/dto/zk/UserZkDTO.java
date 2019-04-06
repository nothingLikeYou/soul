package org.dromara.soul.common.dto.zk;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: soul
 * @description: UserZkDTO
 * @author: zhangshaolin
 * @create: 2019-04-05 20:17
 **/
@Data
public class UserZkDTO implements Serializable {
    private static final long serialVersionUID = 403891850120944958L;

    // ==================用户信息==================
    /**
     * 用户id
     */
    private String userCompanyId;

    /**
     * 账户状态(0:正常 1:禁用)
     */
    private String accountStatus;

    /**
     * 用户状态(0:在职 1:离职)
     */
    private String userStatus;

    /**
     * 删除标记
     */
    private String delFlag;
    // ==================用户信息==================


    /**
     * 公司类型(0:默认公司(无法删除) 1:分包商公司)
     */
    private String companyType;


    // ==================角色信息==================
    /**
     * 角色编号
     */
    private String roleId;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色类型(0:系统管理员 1: LDO 管理员
     * 2:LDO技能导师 3:LDO LM 4:NSB 供应商管理员（VM）5:可配置培训和考试管理员 6:HSS KU 7:HSS项目安全员
     * 8:供应商管理员（Supplier）9:供应商管理员（NSB&Huanuo）10: 项目查询角色 11:分包商普通用户 12:LDO普通用户
     * 13:PM&RM 14:供应商普通用户 15:游客角色 16:自定义角色)
     */

    private String roleType;

    /**
     * 状态   0:正常、1：禁用
     */
    private String roleStatus;

    /**
     * 资源权限范围(0:ALL 1:Nokia 2:供应商)
     */
    private String resourceLimit;

    /**
     * 人员权限范围(0:ALL 1:Nokia 2:供应商 3:本公司 4:本部门)
     */
    private String userLimit;

    /**
     * lm 值  0 N 1 Y
     */
    private String lm;

    /**
     * pm 值  0 N 1 Y
     */
    private String pm;

    /**
     * (是否允许pc登陆0:否 1：是)
     */
    private String pcLogin;

    /**
     * 是否可登录手机端(0:否 1:是)
     */
    private String mobileLogin;

    // ==================角色信息==================
}
