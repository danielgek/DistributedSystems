<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Funding # Join Tumblr User </title>
</head>
<body>
  <div class="join-main">
        <s:form action="joinTumblr" method="post">
            <s:textfield name="username" label="Username" />
            <s:textfield  name="password" label="Password" />
            <s:hidden name="tumblrUsername" value="%{ tumblrUsername }"/>
            <s:submit value="Login"/>
            <s:submit value="I dont have an account, Skip this part"/>
        </s:form>
  </div>
</body>
</html>
