package org.springframework.security.oauth.util;

import org.springframework.security.oauth.vo.ResponseVo;

public class ResponseVoUtil {
    public static ResponseVo success() {
        ResponseVo resultVO = new ResponseVo();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        return resultVO;
    }

    public static <T> ResponseVo success(T data) {
        ResponseVo resultVO = new ResponseVo();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(data);
        return resultVO;
    }

    public static ResponseVo error(Integer code, String msg) {
        ResponseVo resultVO = new ResponseVo();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
