 /**
  *@author: jinwei【jin_wei@founder.com.cn】
  *@description: 用户展示
  *@create: 2018/1/12 17:11
  */
angular.module('WebApp').controller('YhtjCtrl', ['$scope', "$listService", "$ajaxCall","$rootScope","settings", function ($scope, $listService, $ajaxCall, $rootScope, settings) {


    $scope.$on('$viewContentLoaded', function() {
        // initialize core components
        App.initAjax();

        // set default layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;
    });

}]);
