package org.dromara.soul.web.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.dromara.soul.common.constant.Constants;
import org.dromara.soul.common.enums.RpcTypeEnum;
import org.dromara.soul.common.result.SoulResult;
import org.dromara.soul.common.utils.GsonUtils;
import org.dromara.soul.common.utils.jwt.JsonWebTokenUtil;
import org.dromara.soul.common.utils.jwt.JwtAccount;
import org.dromara.soul.web.request.RequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @program: soul
 * @description: AuthFilter
 * @author: zhangshaolin
 * @create: 2019-04-06 19:03
 **/
public class AuthFilter extends AbstractWebFilter {

    @Override
    protected Mono<Boolean> doFilter(ServerWebExchange exchange, WebFilterChain chain) {
        RequestDTO requestDTO = exchange.getAttribute(Constants.REQUESTDTO);
        assert requestDTO != null;
        if (verify(requestDTO, exchange)) {
            exchange.getAttributes().put(Constants.REQUESTDTO, requestDTO);
        } else {
            return Mono.just(false);
        }
        return Mono.just(true);
    }

    @Override
    protected Mono<Void> doDenyResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus statusCode = response.getStatusCode();
        SoulResult result = null;
        if (statusCode == null) {
            result = SoulResult.error("you param is error please check with doc!");
        }
        if (statusCode != null && statusCode.value() == HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value()) {
            //不传认证token，判断为无效请求
            result = SoulResult.error(10002, "invalid request please check the authorization header!");
        }
        if (statusCode != null && statusCode.value() == HttpStatus.NOT_ACCEPTABLE.value()) {
            //jwt 错误/解析异常等
            result = SoulResult.error(10109, "token is error please checked!");
        }
        if (statusCode != null && statusCode.value() == HttpStatus.UNAUTHORIZED.value()) {
            //jwt过期
            result = SoulResult.error(10108, "token is expired");
        }
        return response.writeWith(Mono.just(response.bufferFactory()
                .wrap(GsonUtils.getInstance().toJson(result).getBytes())));
    }

    private Boolean verify(RequestDTO requestDTO, final ServerWebExchange exchange) {
        if (Objects.isNull(requestDTO)) {
            return false;
        }
        final RpcTypeEnum rpcTypeEnum = RpcTypeEnum.acquireByName(requestDTO.getRpcType());

        if (Objects.isNull(rpcTypeEnum)) {
            return false;
        }
        //if rpcType is http
        if (Objects.equals(rpcTypeEnum.getName(), RpcTypeEnum.HTTP.getName())) {
            ServerHttpResponse response = exchange.getResponse();
            String jwt = requestDTO.getAuthorization();
            if (StringUtils.isBlank(jwt)) {
                response.setStatusCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
                return false;
            }
            JwtAccount jwtAccount = null;
            try {
                //验签token
                jwtAccount = JsonWebTokenUtil.parseJwt(jwt, JsonWebTokenUtil.SECRET_KEY);
            } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
                //令牌错误
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return false;
            } catch (ExpiredJwtException e) {
                //令牌过期
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            } catch (Exception e) {
                //解析异常
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return false;
            }
            if (null == jwtAccount) {
                //令牌错误
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return false;
            }
            requestDTO.setUserId(jwtAccount.getUserId());
        }
        return true;
    }
}
