package org.dromara.soul.common.dto.zk;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: soul
 * @description: RolePermRuleZkDTO
 * @author: zhangshaolin
 * @create: 2019-04-05 20:18
 **/
@Data
public class RolePermRuleZkDTO implements Serializable {
    private static final long serialVersionUID = 3919101231074120763L;

    /**
     * 资源URL
     */
    private String url;

    /**
     * 访问资源所需要的角色列表，多个列表用逗号间隔
     */
    private String needRoles;
}
