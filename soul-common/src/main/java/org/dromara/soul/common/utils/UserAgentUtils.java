package org.dromara.soul.common.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * @program: soul
 * @description: UserAgentUtils
 * @author: zhangshaolin
 * @create: 2019-04-07 16:27
 **/
public class UserAgentUtils {
    /**
     * 获取用户代理对象
     *
     * @param exchange
     * @return
     */
    public static UserAgent getUserAgent(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        return UserAgent.parseUserAgentString(request.getHeaders().getFirst("User-Agent"));
    }

    /**
     * 获取设备类型
     *
     * @param exchange
     * @return
     */
    public static DeviceType getDeviceType(ServerWebExchange exchange) {
        return getUserAgent(exchange).getOperatingSystem().getDeviceType();
    }

    /**
     * 是否是PC
     *
     * @param exchange
     * @return
     */
    public static boolean isComputer(ServerWebExchange exchange) {
        return DeviceType.COMPUTER.equals(getDeviceType(exchange));
    }

    /**
     * 是否是手机
     *
     * @param exchange
     * @return
     */
    public static boolean isMobile(ServerWebExchange exchange) {
        return DeviceType.MOBILE.equals(getDeviceType(exchange));
    }

    /**
     * 是否是平板
     *
     * @param exchange
     * @return
     */
    public static boolean isTablet(ServerWebExchange exchange) {
        return DeviceType.TABLET.equals(getDeviceType(exchange));
    }

    /**
     * 是否是手机和平板
     *
     * @param exchange
     * @return
     */
    public static boolean isMobileOrTablet(ServerWebExchange exchange) {
        DeviceType deviceType = getDeviceType(exchange);
        return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
    }

    /**
     * 获取浏览类型
     *
     * @param exchange
     * @return
     */
    public static Browser getBrowser(ServerWebExchange exchange) {
        return getUserAgent(exchange).getBrowser();
    }

    /**
     * 是否IE版本是否小于等于IE8
     *
     * @param exchange
     * @return
     */
    public static boolean isLteIE8(ServerWebExchange exchange) {
        Browser browser = getBrowser(exchange);
        return Browser.IE5.equals(browser) || Browser.IE6.equals(browser)
                || Browser.IE7.equals(browser) || Browser.IE8.equals(browser);
    }
}
