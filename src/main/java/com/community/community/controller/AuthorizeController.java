package com.community.community.controller;

import com.community.community.Provider.GithubProvider;
import com.community.community.dto.AccessTokenDTO;
import com.community.community.dto.GithubUser;
import com.community.community.mapper.UserMapper;
import com.community.community.model.User;
import com.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController  {

	@Autowired
	private GithubProvider githubProvider;

	@Value("${github.client.id}")
	private String client_id;
	@Value("${github.client.secret}")
	private String client_secret;
	@Value("${github.redirect.uri}")
	private String redirect_uri;

	@Autowired
	private UserMapper userMapper;

	@GetMapping("/callback")
	public String callback(@RequestParam(name="code") String code,
						   @RequestParam(name="state") String state,
						   HttpServletResponse httpServletResponse) {
		System.out.println(code);
		System.out.println(state);
		AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
		accessTokenDTO.setClient_id(client_id);
		accessTokenDTO.setClient_secret(client_secret);
		accessTokenDTO.setCode(code);
		accessTokenDTO.setRedirect_uri(redirect_uri);
		accessTokenDTO.setState(state);
		String accessToken = githubProvider.getAccessToken(accessTokenDTO);
		GithubUser githubUser = githubProvider.getUser(accessToken);
//		System.out.println(githubUser.getName());

		if (githubUser!=null && githubUser.getId()!=0) {
			// 登录成功，记录cookies和session
			User user = new User();
			user.setName(githubUser.getName());
			user.setAccountId(String.valueOf(githubUser.getId()));
			user.setGmtCreate(System.currentTimeMillis());
			user.setGmtModified(user.getGmtCreate());
			String token = UUID.randomUUID().toString();
			user.setToken(token);
			user.setBio(githubUser.getBio());
			user.setAvatarUrl(githubUser.getAvatar_url());

			UserExample userExample = new UserExample();
			userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
			if (userMapper.selectByExample(userExample).size()!=0) {
				userMapper.updateByExampleSelective(user, userExample);
			} else {
				userMapper.insert(user);
			}
			httpServletResponse.addCookie(new Cookie("token", token));

			return "redirect:/";
		} else {
			// 登录失败，重新登录
			return "redirect:/";
		}
	}
}
