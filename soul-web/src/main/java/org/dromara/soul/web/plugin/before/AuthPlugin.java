package org.dromara.soul.web.plugin.before;

import org.apache.commons.lang3.StringUtils;
import org.dromara.soul.common.constant.Constants;
import org.dromara.soul.common.dto.zk.UserZkDTO;
import org.dromara.soul.common.enums.PluginEnum;
import org.dromara.soul.common.enums.PluginTypeEnum;
import org.dromara.soul.common.exception.CommonErrorCode;
import org.dromara.soul.common.result.SoulResult;
import org.dromara.soul.common.utils.JsonUtils;
import org.dromara.soul.web.cache.ZookeeperCacheManager;
import org.dromara.soul.web.plugin.SoulPlugin;
import org.dromara.soul.web.plugin.SoulPluginChain;
import org.dromara.soul.web.request.RequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @program: soul
 * @description: AuthPlugin
 * @author: zhangshaolin
 * @create: 2019-04-06 19:45
 **/
public class AuthPlugin implements SoulPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthPlugin.class);

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";

    /**
     * 账号状态(0:正常 1:禁用)
     */
    public static final String ACCOUNT_STATUS_TRUE = "0";
    public static final String ACCOUNT_STATUS_FALSE = "1";

    /**
     * 用户状态(0:在职 1:离职)
     */
    public static final String USER_STATUS_TRUE = "0";
    public static final String USER_STATUS_FALSE = "1";


    private ZookeeperCacheManager mZookeeperCacheManager;

    /**
     * Instantiates a new Auth plugin.
     *
     * @param zookeeperCacheManager the zookeeper cache manager
     */
    public AuthPlugin(final ZookeeperCacheManager zookeeperCacheManager) {
        this.mZookeeperCacheManager = zookeeperCacheManager;
    }

    @Override
    public String named() {
        return PluginEnum.AUTH.getName();
    }

    @Override
    public int getOrder() {
        return PluginEnum.AUTH.getCode();
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, SoulPluginChain chain) {
        final RequestDTO requestDTO = exchange.getAttribute(Constants.REQUESTDTO);
        final SoulResult soulResult = authVerify(Objects.requireNonNull(requestDTO));
        if (!soulResult.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(Objects.requireNonNull(JsonUtils.toJson(soulResult)).getBytes())));
        }
        return chain.execute(exchange);
    }

    /**
     * verify auth .
     *
     * @param requestDTO {@linkplain RequestDTO}
     * @return result : True is pass, False is not pass.
     */
    private SoulResult authVerify(RequestDTO requestDTO) {
        if (StringUtils.isBlank(requestDTO.getUserId())) {
            return SoulResult.success();
        }
        UserZkDTO userZkDTO = mZookeeperCacheManager.findUserZkDTOByUserId(requestDTO.getUserId());
        if (userZkDTO == null || StringUtils.equals(userZkDTO.getDelFlag(), DEL_FLAG_DELETE)) {
            return SoulResult.error(10297, "用户已被删除,请联系管理员恢复!");
        }
        if (StringUtils.equals(userZkDTO.getAccountStatus(), ACCOUNT_STATUS_FALSE)) {
            return SoulResult.error(10298, "用户已被禁用,请联系管理员恢复!");
        }
        if (!StringUtils.equals(userZkDTO.getUserStatus(), USER_STATUS_TRUE)) {
            return SoulResult.error(10299, "用户非在职状态,请联系管理员恢复!");
        }
        if (StringUtils.equals(userZkDTO.getPcLogin(), NO)) {
            return SoulResult.error(10324, "账户已被限制登录pc端,请联系管理员");
        }
        if (StringUtils.equals(userZkDTO.getMobileLogin(), NO)) {
            return SoulResult.error(10325, "账户已被限制登录手机端,请联系管理员");
        }
        List<String> roles = mZookeeperCacheManager.findRolePermRuleListByUrl(requestDTO.getMethod());
        if (!roles.contains(userZkDTO.getRoleCode())) {
            return SoulResult.error(10303, "no permission");
        }
        return SoulResult.success();
    }

    @Override
    public PluginTypeEnum pluginType() {
        return PluginTypeEnum.BEFORE;
    }
}
