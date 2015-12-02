<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Create Project</title>
        <link rel="stylesheet" href="https://storage.googleapis.com/code.getmdl.io/1.0.6/material.blue_grey-orange.min.css" />
        <script src="https://storage.googleapis.com/code.getmdl.io/1.0.6/material.min.js"></script>
        <script src="js/script.js"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="css/style.css">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
    <div id="overlay"></div>
        <div class="demo-layout mdl-layout mdl-layout--fixed-header mdl-js-layout ">
            <header class="demo-header mdl-layout__header mdl-layout__header--scroll ">
                <div class="mdl-layout__header-row">
                    <s:a action="dashboard">
                        <button class="mdl-button mdl-js-button mdl-button--icon mdl-js-ripple-effect">
                            <i class="material-icons">arrow_back</i>
                        </button>
                    </s:a>
                    <span class="mdl-layout-title">Create Project</span>
                    <div class="mdl-layout-spacer"></div>
                </div>
            </header>
            <div class="extend"></div>
            <main class="demo-main mdl-layout__content">
                <div class="demo-container mdl-grid">
                    <div class="mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone"></div>
                    <div class="demo-content mdl-color--white mdl-shadow--4dp content mdl-color-text--grey-800 mdl-cell mdl-cell--8-col">
                        <div class="mdl-color-text--grey-500">Project:</div>
                        <!-- Textfield with Floating Label -->
                        <form action="createProject.action" method="post">
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" type="text" id="title" name="title">
                                <label class="mdl-textfield__label" for="title">Project Title</label>
                            </div>
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">

                                <input class="mdl-textfield__input" type="text" pattern="-?[0-9]*(\.[0-9]+)?" id="goal">
                                <label class="mdl-textfield__label" for="goal">Project Goal</label>
                                <span class="mdl-textfield__error">Input is not a number!</span>
                            </div>
                            <br>
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <textarea class="mdl-textfield__input" type="text" rows= "6" id="description" name="description" ></textarea>
                                <label class="mdl-textfield__label" for="description">Project description</label>
                            </div>
                        </form>
                        <div class="rewards-holder">
                            <div class="mdl-color-text--grey-500">Rewards</div>
                            <button id="open-reward-modal" class="mdl-button mdl-js-button mdl-button--icon mdl-js-ripple-effect">
                                <i class="material-icons">add</i>
                            </button>
                            <div class="rewards">

                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    <!-- Modal reward -->
    <div id="modal-reward" class="modal-reward mdl-card mdl-color--white mdl-shadow--16dp">
        <div class="mdl-card__title">
            <h4>Add Reward</h4>
        </div>
        <div class="mdl-card__supporting-text">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <textarea class="mdl-textfield__input" type="text" rows= "6" id="reward-description" name="description" ></textarea>
                <label class="mdl-textfield__label" for="description">Reward description</label>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" pattern="-?[0-9]*(\.[0-9]+)?" id="reward-value">
                <label class="mdl-textfield__label" for="goal">Reward Value</label>
                <span class="mdl-textfield__error">Input is not a number!</span>
            </div>
        </div>
        <div class="mdl-card__actions">
            <div class="mdl-layout-spacer"></div>
            <button id="reward-add-button" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                Add
            </button>
            <button id="reward-cancel-button" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                Cancel
            </button>
        </div>
    </div>
    </body>
</html>
