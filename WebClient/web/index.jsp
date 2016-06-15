<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="fundingApp">
    <head>
        <!-- Angular Material CSS now available via Google CDN; version 0.11.2 used here -->
        <link rel="stylesheet" href="bower_components/angular-material/angular-material.min.css">
        <link rel="stylesheet" href="css/style.css">
        <meta charset="UTF-8">
        <title>Funding </title>
    </head>
    <body>
    <!-- Angular Material Dependencies -->
    <script src="bower_components/angular/angular.min.js"></script>
    <script src="bower_components/angular-messages/angular-messages.min.js"></script>
    <script src="bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
    <script src="bower_components/angular-animate/angular-animate.min.js"></script>
    <script src="bower_components/angular-aria/angular-aria.min.js"></script>
    <script src="bower_components/angular-sanitize/angular-sanitize.min.js"></script>


    <div layout="column" flex id="main-container" ui-view></div>
    <!-- Angular Material Javascript now available via Google CDN; version 0.11.2 used here -->
    <script src="bower_components/angular-material/angular-material.min.js"></script>
    <script src="js/script.js"></script>
    <script src="js/loginController.js"></script>
    <script src="js/registerController.js"></script>
    <script src="js/dashboardController.js"></script>
    <script src="js/newProjectController.js"></script>
    <script src="js/editProjectController.js"></script>
    <script src="js/projectController.js"></script>
    <script type="text/javascript">
        <s:if test="tumblrUsername != null">
            var tumblrUser = "<s:property value="tumblrUsername" />";
            var user = {
                id : <s:property value="user.id" />,
                username : "<s:property value="user.username" />",
                balance: <s:property value="user.balance" />
            };
        </s:if>

    </script>
    </body>
</html>