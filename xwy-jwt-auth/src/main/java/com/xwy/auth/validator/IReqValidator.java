package com.xwy.auth.validator;

import java.util.Map;

import com.xwy.auth.vo.AuthRequestVo;
import com.xwy.framework.utils.DataformResult;

/**
 * 验证请求/auth接口时,请求参数的正确性
 * 如果想拓展验证方法只需实现这个接口,然后在AuthenticationRestController类中注意相应实现的本接口的类即可
 * 
 * @author xiangwy
 * @date: 2020-12-01 17:57:52
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public interface IReqValidator {

	/**
	 * 通过请求参数验证
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-01 17:59:48
	 * @param authRequestVo
	 * @return
	 */
	DataformResult<Map<String, Object>> validate(AuthRequestVo authRequestVo);
}
