# code换取token的流程
1. 在浏览器中访问授权服务器的 /oauth/authorize 接口（GET）
   ```
   http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
   ```
   - response_type=code : 必须，请求的响应类型为 authorization code
   - client_id=messaging-client : 必须，在授权服务器上注册的 client_id
   - redirect_uri=http://localhost : 必须，在授权服务器上注册的重定向的地址

2. 浏览器重定向到授权服务器的 /login 接口
   ```
   http://localhost:8090/login
   ```

3. 输入用户名、密码，点登录之后，浏览器又重定向到授权服务器的 /oauth/authorize 接口（GET）
   ```
   http://localhost:8090/oauth/authorize?response_type=code&client_id=messaging-client&redirect_uri=http://localhost
   ```

4. 点授权之后，调用授权服务器的 /oauth/authorize 接口（POST）
   ```
   http://localhost:8090/oauth/authorize
   ```

5. 浏览器重定向到
   ```
   http://localhost?code=W636oE
   ```
   - code=W636oE : code 就是授权码

6. 访问授权服务器的 /oauth/token 接口（POST）
   ```
   http://localhost:8090/oauth/token?grant_type=authorization_code&code=W636oE&client_id=messaging-client&client_secret=secret&redirect_uri=http://localhost
   ```
   - grant_type=authorization_code : 必须，授权码模式(authorization code)
   - code=W636oE : 必须，授权服务器返回的授权码
   - client_id=messaging-client : 必须，在授权服务器上注册的 client_id
   - client_secret=secret : 必须，在授权服务器上注册的 secret
   - redirect_uri=http://localhost : 必须，在授权服务器上注册的重定向的地址
   
   成功获取 token 如下: 
   
   ```json
   {
       "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlBRRmsvOFEyYlR0UHFCbm5SMmtSVnBLSXVERG0yc1FycU1TRnFaMm5Zbmc9In0.eyJleHAiOjE2MTAxMzI4MjUsInVzZXJfbmFtZSI6InVzZXIxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjkxYzU5MTkxLWI3YzMtNDU5OS04MzZhLWYxMTdmYzk3OWJiYyIsImNsaWVudF9pZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJzY29wZSI6WyJtZXNzYWdlLnJlYWQiLCJtZXNzYWdlLndyaXRlIl19.CAwNYZOn_IXDD3jkCzEEpx_mKAQyEr23SUM_LP4V-K6TB-1KPCetZrenjFN70id6jOmvohFWCVRRQEL_JIAMJ7xO-1FL2Xmp0h3aCop_tvL82_Jc-pqZRzdXhQ1H4pYDySK_cyHsxwsbih7Fwi8G2Z4mnfgMNXx1nF15z4mRwo5ur57ToSLQIhsLG74ERjlNqfZ5h9A9_xdCXCgFqlqSXYam4PNDB3nPLaPW_7QguSfQRl_79qoitc5fVoaEaDuRQVu5bIpy7QZCVELrio16hwLl6-QuRDSoMICxYywgnOMIzr4b7xf6m3aueXaPpU-BITFopTCEzUbS4ayWxvNE5w",
       "token_type": "bearer",
       "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlBRRmsvOFEyYlR0UHFCbm5SMmtSVnBLSXVERG0yc1FycU1TRnFaMm5Zbmc9In0.eyJ1c2VyX25hbWUiOiJ1c2VyMSIsInNjb3BlIjpbIm1lc3NhZ2UucmVhZCIsIm1lc3NhZ2Uud3JpdGUiXSwiYXRpIjoiOTFjNTkxOTEtYjdjMy00NTk5LTgzNmEtZjExN2ZjOTc5YmJjIiwiZXhwIjoxNjEyNjgxNjI1LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiNDk1MzVlY2EtYzdjYS00NWQ1LWJjY2ItYmI4ZGM2ZDk4ZWFhIiwiY2xpZW50X2lkIjoibWVzc2FnaW5nLWNsaWVudCJ9.iYEgPBtM0AHKEbdHgr74LQ8K9uRedT1YCjkFF5UxvLonWzZg_BHxgjI7xFZTtYI_W4I4WAa8P80hPfEltc-fkmotTW-CeEOXxrkSA0SnIsZUdC2IZG6s-8MlNf3qPkWYxtSdc1oDdGFbj8WCJuqffJXQlTTAH9DgNPm0P90PYzBCkAupP6Ud9XwFHtvh9HGEW9ZhmimaonJO2PvZDX3MI37_5E-xwCoFnrfbpgkz8fNMBM0L_3ihQ3NJ3zCWKRrQP4pqau_K9Ke0QHcVPremwv6B_0kER4-thCubVTaqbTF-iEkPKHhXaAIpxQBGmHR53vs1esruvOiiFoUaKYGEiA",
       "expires_in": 43199,
       "scope": "message.read message.write",
       "jti": "91c59191-b7c3-4599-836a-f117fc979bbc"
   }
   ```

