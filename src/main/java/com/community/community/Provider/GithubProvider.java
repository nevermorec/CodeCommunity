package com.community.community.Provider;


import com.alibaba.fastjson.JSON;
import com.community.community.dto.AccessTokenDTO;
import com.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class GithubProvider {

	public String getAccessToken(AccessTokenDTO accessTokenDTO) {
//		System.setProperty("javax.net.ssl.trustStore", "D:\\Software\\jdk-11.0.6\\jre\\lib\\security\\jssecacerts");

		MediaType mediaType = MediaType.get("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();

		RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
		Request request = new Request.Builder()
				.url("https://github.com/login/oauth/access_token")
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String string = response.body().string();
			System.out.println(string);
			String[] split = string.split("&");
			String token = split[0].split("=")[1];
			System.out.println(token);
			return token;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public GithubUser getUser(String accessToken) {
//		System.setProperty("javax.net.ssl.trustStore", "D:\\Software\\jdk-11.0.6\\lib\\security\\jssecacertss");
		OkHttpClient client = new OkHttpClient();

		OkHttpClient eagerclient = client.newBuilder()
				.readTimeout(15, TimeUnit.SECONDS)
				.connectTimeout(15, TimeUnit.SECONDS)
				.build();

		Request request = new Request.Builder()
				.url("https://api.github.com/user")
				.header("Authorization","token "+accessToken)
				.build();

		try {
			Response response = client.newCall(request).execute();
			String string = response.body().string();
			System.out.println(string);
			GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
			return githubUser;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error1");
		}
		return null;
	}
}


