 /**
  *@author: jinwei【jin_wei@founder.com.cn】
  *@description: 用户展示
  *@create: 2018/1/12 17:11
  */
angular.module('WebApp').controller('YhzsCtrl', ['$scope', "$listService", "$ajaxCall", function ($scope, $listService, $ajaxCall) {

    $scope.deptName = "用户";
    $scope.modifyDivId = "modifyYhzsModalDiv";
    $scope.controllerName = "login";

    $scope.condition = {isEnabled: true};
    $listService.init($scope, {
        "controller": $scope.controllerName,
        "method": "QueryUser",
        callback: function (success) {
            $scope.list = success.data.rowlist;
        }
    });

    /**
     * 刷新数据
     */
    $scope.load = function () {
        $scope.pageRequest.getResponse();
    };
    $scope.load();

    /**
     * 修改给定实体的状态
     * @param item 给定实体
     * @param isEnabled 新状态
     */
    $scope.changeStatus = function (item, isEnabled) {
        bootbox.dialog({
            title: "请确认",
            message: isEnabled ? "是否确认恢复该" + $scope.deptName + "？" : "是否确认禁用该" + $scope.deptName + "？",
            buttons: {
                main: {label: " 取 消 ", className: "dark icon-ban btn-outline"},
                danger: {
                    label: isEnabled ? " 恢 复 ！ " : " 禁 用 ！",
                    className: isEnabled ? "fa fa-recycle green" : "fa fa-ban red",
                    callback: function () {
                        $ajaxCall.post({
                            data: {
                                controller: $scope.controllerName,
                                method: isEnabled ? "resume" : "remove",
                                id: item.id
                            },
                            success: function () {
                                $scope.load();
                            }
                        });
                    }
                }
            }
        });
    };

    /**
     * 准备添加实体
     */
    $scope.prepareToAdd = function () {
        var scope = $("#" + $scope.modifyDivId).scope();
        scope.title = "添加" + $scope.deptName + "信息";
        scope.method = "AddUser";
        scope.entity = {};

        scope.$on("submitted", function () {
            $scope.load();
        });
        scope.clear();
    };

    /**
     * 准备修改实体
     */
    $scope.prepareToUpdate = function (item) {
        var scope = $("#" + $scope.modifyDivId).scope();
        scope.title = "修改" + $scope.deptName + "信息";
        scope.method = "update";
        scope.entity = item;

        scope.$on("submitted", function () {
            $scope.load();
        });
        scope.clear();
    };

}]);
