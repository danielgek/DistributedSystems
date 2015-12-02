<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Dashboard</title>
        <link rel="stylesheet" href="https://storage.googleapis.com/code.getmdl.io/1.0.6/material.blue_grey-orange.min.css" />
        <script src="https://storage.googleapis.com/code.getmdl.io/1.0.6/material.min.js"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="css/style.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <!-- Always shows a header, even in smaller screens. -->
        <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
            <header class="mdl-layout__header">
                <div class="mdl-layout__header-row">
                    <!-- Title -->
                    <span class="mdl-layout-title">Title</span>
                    <!-- Add spacer, to align navigation to the right -->
                    <div class="mdl-layout-spacer"></div>
                    <!-- Navigation. We hide it in small screens. -->
                    <nav class="mdl-navigation mdl-layout--large-screen-only">
                        <a class="mdl-navigation__link" href="">Rewards</a>
                        <a class="mdl-navigation__link" href="">Create Project</a>
                        <a class="mdl-navigation__link" href="">Profile</a>
                    </nav>
                </div>
                <div class="mdl-layout__tab-bar mdl-js-ripple-effect">
                    <a href="#scroll-tab-1" class="mdl-layout__tab is-active">Active projects</a>
                    <a href="#scroll-tab-2" class="mdl-layout__tab">My Current Porjects</a>
                    <a href="#scroll-tab-3" class="mdl-layout__tab">Old Projects</a>
                </div>
            </header>
            <div class="mdl-layout__drawer">
                <span class="mdl-layout-title">Dashboard</span>
                <nav class="mdl-navigation">
                    <a class="mdl-navigation__link" href="">Rewards</a>
                    <a class="mdl-navigation__link" href="">Create Project</a>
                    <a class="mdl-navigation__link" href="">Profile</a>
                </nav>
            </div>
            <main class="mdl-layout__content">
            <section class="mdl-layout__tab-panel is-active" id="scroll-tab-1">
                <div class="page-content">
                    <div class="mdl-grid">
                        <s:iterator status="stat" value="projects">
                        <div class="demo-card-event mdl-card mdl-shadow--2dp mdl-cell mdl-cell--3-col mdl-cell--4-col-tablet mdl-cell--1-col-phone">
                            <div class="mdl-card__title mdl-card--expand">
                                <h4>
                                <s:property value="name"/>
                                </h4>
                            </div>
                            <div class="mdl-card__supporting-text">
                                <div><s:property value="description"/></div>
                                <div id="p1<s:property value="%{#stat.index}"/>" class="mdl-progress mdl-js-progress"></div>
                            </div>
                            <div class="mdl-card__actions mdl-card--border">
                                <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
                                    View Details
                                </a>
                                <span class="demo-card-image__filename"><s:property value="limit"/></span>
                            </div>
                        </div>
                        </s:iterator>
                    </div>
                </div>
            </section>
            <section class="mdl-layout__tab-panel" id="scroll-tab-2">
                <div class="page-content">
                    <div class="mdl-grid">
                        <s:iterator status="stat" value="currentProjects">
                        <div class="demo-card-event mdl-card mdl-shadow--2dp mdl-cell mdl-cell--3-col mdl-cell--4-col-tablet mdl-cell--1-col-phone">
                            <div class="mdl-card__title mdl-card--expand">
                                <h4>
                                <s:property value="name"/>
                                </h4>
                            </div>
                            <div class="mdl-card__supporting-text">
                                <div><s:property value="description"/></div>
                                <div id="p2<s:property value="%{#stat.index}"/>" class="mdl-progress mdl-js-progress"></div>
                            </div>
                            <div class="mdl-card__actions mdl-card--border">
                                <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
                                    View Details
                                </a>
                                <span class="demo-card-image__filename"><s:property value="limit"/></span>
                            </div>
                        </div>
                        </s:iterator>
                    </div>
                </div>
            </section>
            <section class="mdl-layout__tab-panel" id="scroll-tab-3">
                <div class="page-content">
                    <div class="mdl-grid">
                        <s:iterator status="stat" value="oldProjects">
                        <div class="demo-card-event mdl-card mdl-shadow--2dp mdl-cell mdl-cell--3-col mdl-cell--4-col-tablet mdl-cell--1-col-phone">
                            <div class="mdl-card__title mdl-card--expand">
                                <h4>
                                <s:property value="name"/>
                                </h4>
                            </div>
                            <div class="mdl-card__supporting-text">
                                <div><s:property value="description"/></div>
                                <div id="p3<s:property value="%{#stat.index}"/>" class="mdl-progress mdl-js-progress"></div>
                            </div>
                            <div class="mdl-card__actions mdl-card--border">
                                <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
                                    View Details
                                </a>
                                <span class="demo-card-image__filename"><s:property value="limit"/></span>
                            </div>
                        </div>
                        </s:iterator>
                    </div>
                </div>
            </section>
            <s:a action="createProjectForm">
            <button class="mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--colored add-button">
            <i class="material-icons">add</i>
            </button>
            </s:a>
            </main>
        </div>
        <script type="application/javascript">
        <s:iterator status="stat" value="projects">
        document.querySelector('#p1<s:property value="%{#stat.index}"/>').addEventListener('mdl-componentupgraded', function() {
        this.MaterialProgress.setProgress(<s:property value="progressDescription"/>);
        });
        </s:iterator>
        <s:iterator status="stat" value="currentProjects">
        document.querySelector('#p2<s:property value="%{#stat.index}"/>').addEventListener('mdl-componentupgraded', function() {
        this.MaterialProgress.setProgress(<s:property value="progressDescription"/>);
        });
        </s:iterator>
        <s:iterator status="stat" value="oldProjects">
        document.querySelector('#p3<s:property value="%{#stat.index}"/>').addEventListener('mdl-componentupgraded', function() {
        this.MaterialProgress.setProgress(<s:property value="progressDescription"/>);
        });
        </s:iterator>
        </script>
    </body>
</html>