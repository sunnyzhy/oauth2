package org.springframework.security.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouyi
 * @date 2021/1/11 14:56
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {
	@GetMapping
	public String product() {
		return "product method is ok";
	}
}