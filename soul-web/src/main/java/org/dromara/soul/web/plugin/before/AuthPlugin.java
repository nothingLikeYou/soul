package org.dromara.soul.web.plugin.before;

import org.dromara.soul.common.constant.Constants;
import org.dromara.soul.common.dto.zk.RuleZkDTO;
import org.dromara.soul.common.dto.zk.SelectorZkDTO;
import org.dromara.soul.common.enums.PluginEnum;
import org.dromara.soul.common.enums.PluginTypeEnum;
import org.dromara.soul.common.result.SoulResult;
import org.dromara.soul.common.utils.JsonUtils;
import org.dromara.soul.web.cache.ZookeeperCacheManager;
import org.dromara.soul.web.plugin.AbstractSoulPlugin;
import org.dromara.soul.web.plugin.SoulPluginChain;
import org.dromara.soul.web.request.RequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @program: soul
 * @description: AuthPlugin
 * @author: zhangshaolin
 * @create: 2019-04-06 19:45
 **/
public class AuthPlugin extends AbstractSoulPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthPlugin.class);


    private ZookeeperCacheManager mZookeeperCacheManager;

    /**
     * Instantiates a new Auth plugin.
     *
     * @param zookeeperCacheManager the zookeeper cache manager
     */
    public AuthPlugin(final ZookeeperCacheManager zookeeperCacheManager) {
        super(zookeeperCacheManager);
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
    protected Mono<Void> doExecute(ServerWebExchange exchange, SoulPluginChain chain, SelectorZkDTO selector, RuleZkDTO rule) {
        final RequestDTO requestDTO = exchange.getAttribute(Constants.REQUESTDTO);
        final Boolean success = authVerify(Objects.requireNonNull(requestDTO));
        if (!success) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            final SoulResult error = SoulResult.error(HttpStatus.UNAUTHORIZED.value(), Constants.AUTH_IS_NOT_PASS);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(Objects.requireNonNull(JsonUtils.toJson(error)).getBytes())));
        }
        return chain.execute(exchange);
    }

    /**
     * verify auth .
     *
     * @param requestDTO {@linkplain RequestDTO}
     * @return result : True is pass, False is not pass.
     */
    private Boolean authVerify(RequestDTO requestDTO) {
        // TODO: 19-4-6  verify auth .
        return null;
    }

    @Override
    public PluginTypeEnum pluginType() {
        return PluginTypeEnum.BEFORE;
    }
}
