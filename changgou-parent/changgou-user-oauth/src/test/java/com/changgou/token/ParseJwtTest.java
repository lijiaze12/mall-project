package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.CIBtO1KaJ8fAK90jNGsvQ1dbuWondBAI8HwHJUtPnlIifnRYFIa3gHT5eSbVJpT9K3fbrbqdRmxBIsJQtlzqvMDqW6d6kcIvZ0ax7xZ4Y_YWEmut-xNZ7DEZyF_WyuqHfPRDprHc98ZltEtDocnSw9vPw0_hYW00wHEtlBO9qiStubXDNoFGAADBJhDxFIlxtWp6fBnPkjZKYTtw1HCzGkZy6hU2jjGzxdgHMPVOCyvKV5uimDkrk34xdOkzIepUUCd7Of7x_AF2bnHdM37jIpPCAhBK17XmEea3YaNtvB5ObadBIP_TIF9yuVE3kTbqB4vzwnm7gL9cg-6zjLGPxQ";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApAcxS5cxQ1rtw6gZ9BXw0jn1e+3Yyv0AIEIRWet6hQY09v9usttG3fnRT7TksS21+mSfbkEZhwsUvQfEQZtaZMgfIqoNJy9m5hm9CIZGX9eafDMDXYxgtlbZzGQmiWSbWXyU4XWcWEvCS7tUeVllOp0H6IouP6sXrPiY25aQSnigrXXyOwL7nCY/bF2b0xJvQz7sp/D35BTkFUBuz/VDul5Vt1MKEbpfY5m+ZSro9/AgiOzGq0G6n4+FFHCWSD3CY7Bf4mnjEzhOQKXOkjH79tJLVbKi5+RdwIJoX9lZbrNKa/gCEULMUc28nV1zSwvDVmwTTuxJW+O3a5knUi0rsQIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
