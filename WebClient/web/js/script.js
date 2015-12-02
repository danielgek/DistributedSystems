(function(){
    document.addEventListener('DOMContentLoaded', function(){
        var overlay = document.getElementById("overlay");
        var openModalReward = document.getElementById("open-reward-modal");
        var modalReward = document.getElementById("modal-reward");
        var modalAddButton = document.getElementById("reward-add-button");
        var modalCancelButton = document.getElementById("reward-cancel-button");
        var modalDescriptionReward = document.getElementById("reward-description");
        var modalValueReward = document.getElementById("reward-value");
        var rewardsHolder = document.getElementById("rewards");
        openModalReward.addEventListener("click", function(){
            overlay.style.display = "block";
            modalReward.style.display = "block";
        });

        overlay.addEventListener("click", function(){
            overlay.style.display = "none";
            modalReward.style.display = "none";
        });

        modalCancelButton.addEventListener("click", function(){
            overlay.style.display = "none";
            modalReward.style.display = "none";
        });

        modalAddButton.addEventListener("click", function(){
            rewardsHolder.appendChild(createUiReward());
            overlay.style.display = "none";
            modalReward.style.display = "none";

        });

    }, false);

    function createUiReward(){

        var UiReward = document.createElement("div");
        var tooltip = document.createElement("div");
        tooltip.setAttribute("class","mdl-tooltip");

        tooltip.setAttribute("class","mdl-tooltip");
        UiReward.appendChild(document.createTextNode("1 reward"));
        UiReward
        /*<div class="mdl-tooltip" for="tt1">
            Follow
            </div>*/
        return UiReward;
    }
})();