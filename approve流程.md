# approve流程

## oauth2 如何保存 approve

因为 ApprovalStoreUserApprovalHandler#checkForPreApproval 函数是 oauth2 保存 approve 的核心函数，所以先了解一下 checkForPreApproval 函数的源码。

### ApprovalStoreUserApprovalHandler#checkForPreApproval 源码分析

- 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\approval\ApprovalStoreUserApprovalHandler.class
   ```

- 源码
   ```java
   public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        String clientId = authorizationRequest.getClientId();
        // 从上下文里读取 scope ，而上下文里的 scope 读取的是表字段 oauth_client_details.scope 的值
        Collection<String> requestedScopes = authorizationRequest.getScope();
        // 用来存储上下文里的 scope 或者数据表 oauth_approvals 里的 scope
        Set<String> approvedScopes = new HashSet();
        // 用来存储数据表 oauth_approvals 的 scope
        Set<String> validUserApprovedScopes = new HashSet();
        if (this.clientDetailsService != null) {
            try {
                // 读取数据表 oauth_client_details , 此处使用的是表字段 oauth_client_details.autoapprove 的值
                ClientDetails client = this.clientDetailsService.loadClientByClientId(clientId);
                Iterator var8 = requestedScopes.iterator();
                
                // 遍历并筛选上下文里的 scope
                while(var8.hasNext()) {
                    String scope = (String)var8.next();
                    // isAutoApprove 函数用于遍历 oauth_client_details.autoapprove 所有的值，并跟上下文里的 scope 进行比较
                    // while(!auto.equals("true") && !scope.matches(auto))
                    // 等价于
                    // while(![oauth_client_details.autoapprove].equals("true") && !scope.matches([oauth_client_details.autoapprove]))
                    // 当不满足 while 条件时，就退出循环，函数 isAutoApprove 返回 true
                    // 当 while 条件一直循环，并且遍历完所有的 oauth_client_details.autoapprove 才退出, 函数 isAutoApprove 就返回 false
                    if (client.isAutoApprove(scope)) {
                        // 把上下文里的 scope 添加进 approvedScopes
                        approvedScopes.add(scope);
                    }
                }

                // 如果 approvedScopes(经过筛选之后的上下文里的 scope) 包含了上下文里所有的 scope(requestedScopes)，就视为 Approved 通过
                if (approvedScopes.containsAll(requestedScopes)) {
                    Set<Approval> approvals = new HashSet();
                    Date expiry = this.computeExpiry();
                    Iterator var19 = approvedScopes.iterator();

                    while(var19.hasNext()) {
                        String approvedScope = (String)var19.next();
                        approvals.add(new Approval(userAuthentication.getName(), authorizationRequest.getClientId(), approvedScope, expiry, ApprovalStatus.APPROVED));
                    }

                    // 添加或者修改数据表 oauth_approvals
                    this.approvalStore.addApprovals(approvals);
                    // 设置上下文的 Approved = true
                    authorizationRequest.setApproved(true);
                    return authorizationRequest;
                }
            } catch (ClientRegistrationException var12) {
                logger.warn("Client registration problem prevent autoapproval check for client=" + clientId);
            }
        }

        // ...

        // 读取数据表 oauth_approvals 里的 scope
        Collection<Approval> userApprovals = this.approvalStore.getApprovals(userAuthentication.getName(), clientId);
        Date today = new Date();
        Iterator var17 = userApprovals.iterator();

        // 把数据表 oauth_approvals 里的 scope 赋值给 validUserApprovedScopes 和 approvedScopes
        while(var17.hasNext()) {
            Approval approval = (Approval)var17.next();
            if (approval.getExpiresAt().after(today) && approval.getStatus() == ApprovalStatus.APPROVED) {
                validUserApprovedScopes.add(approval.getScope());
                approvedScopes.add(approval.getScope());
            }
        }

        // ...

        // 如果数据表 oauth_approvals 里的 scope(validUserApprovedScopes) 包含了上下文里所有的 scope(requestedScopes)，就视为 Approved 通过
        if (validUserApprovedScopes.containsAll(requestedScopes)) {
            // approvedScopes 与 requestedScopes 取交集
            approvedScopes.retainAll(requestedScopes);
            // 设置上下文的 scope 为经过交集计算的结果 approvedScopes
            authorizationRequest.setScope(approvedScopes);
            // 设置上下文的 Approved = true
            authorizationRequest.setApproved(true);
        }

        return authorizationRequest;
    }
   ```
   
### ApprovalStoreUserApprovalHandler#checkForPreApproval 返回值分析

又因为 ApprovalStoreUserApprovalHandler#checkForPreApproval 函数是 AuthorizationEndpoint#authorize 流程中的一个环节，所以还需要了解一下 authorize 流程。

- AuthorizationEndpoint 源文件
   ```
   \org\springframework\security\oauth\spring-security-oauth2\2.3.4.RELEASE\spring-security-oauth2-2.3.4.RELEASE.jar!\org\springframework\security\oauth2\provider\endpoint\AuthorizationEndpoint.class
   ```

authorize 流程:

1. 先调用以下 AuthorizationEndpoint#authorize 接口
   ```java
   @RequestMapping({"/oauth/authorize"})
   public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal)
   ```
   
2. 如果 ApprovalStoreUserApprovalHandler#checkForPreApproval 函数返回 true, authorize 接口就直接返回 code
   
3. 如果 ApprovalStoreUserApprovalHandler#checkForPreApproval 函数返回 false, authorize 接口就返回 approve 授权页面

4. 授权之后，先调用以下 AuthorizationEndpoint#authorize 接口，再返回 code
   ```java
   @RequestMapping(
        value = {"/oauth/authorize"},
        method = {RequestMethod.POST},
        params = {"user_oauth_approval"}
   )
   public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal)
   ```

### 总结

oauth2 跳转到 approve 授权页面的条件:

1. oauth2 是否跳转到 approve 授权页面跟 oauth_client_details.autoapprove = true/false 没有直接关系，因为 oauth_client_details.autoapprove 存储的是一个数组，可以是任意数据
2. 当 approvedScopes.containsAll(requestedScopes) 时，oauth2 不跳转到 approve 授权页面，后台会自动保存 oauth_approvals; 否则执行步骤 3
3. 当 validUserApprovedScopes.containsAll(requestedScopes) 时，oauth2 不跳转到 approve 授权页面；否则执行步骤 4
4. oauth2 跳转到 approve 授权页面，授权之后，后台保存 oauth_approvals
