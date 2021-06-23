package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器 任意的请求经过都需要通过该过滤器
 * @author ljh
 * @version 1.0
 * @date 2020/11/19 15:49
 * @description 标题
 * @package com.changgou.filter
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static  final String AUTHORIZE_TOKEN ="Authorization";

    //判断权限的的逻辑代码
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        //2.获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //3.判断当前的请求路径 如果是以/api/user/login的路径开头 放行即可
        String path = request.getURI().getPath();//
        if(path.startsWith("/api/user/login")){
            return chain.filter(exchange);//放行
        }
        //4.从请求头中获取令牌  如果获取不到
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        if(StringUtils.isEmpty(token)){
            //5.再从请求参数中获取令牌  如果获取不到
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        if(StringUtils.isEmpty(token)){
            //6.再从cookie中获取令牌 如果获取不到
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(cookie!=null){
                token= cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            //7.如果令牌为空，返回；结束请求
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //8.解析令牌，如果解析失败 返回
        try {
             JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //9.放行
        return chain.filter(exchange);//放行
    }

    //过滤器的order的值越低 越优先被执行
    @Override
    public int getOrder() {
        return -1;
    }
}
