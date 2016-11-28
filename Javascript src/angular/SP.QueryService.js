(function(angular) {

    'use strict';
    var app = angular.module("SP.QueryService", [])

    app.value('appweburl', decodeURIComponent(
        getQueryStringParameter("SPAppWebUrl")));

    var executeQuery = function($http, $q, appweburl) {
        this.executor = new SP.RequestExecutor(appweburl);
        this.appweburl = appweburl;
        this.$http = $http;
        this.$q = $q;
    }

    executeQuery.prototype.searchJsom = function(queryText, queryTemplate, rowsperpage, rowlimit, startRow, graphQuery, graphRankingModel, rankingModelId) {
        var deferred = this.$q.defer();

        var ctx = SP.ClientContext.get_current();
        var kwQuery = new Microsoft.SharePoint.Client.Search.Query.KeywordQuery(ctx);
        kwQuery.set_queryText(queryText);

        if (queryTemplate)
            kwQuery.set_queryTemplate(queryTemplate)

        if (rowsperpage)
            kwQuery.set_rowsPerPage(rowsperpage);

        if (rowlimit)
            kwQuery.set_rowLimit(rowlimit);

        if (startRow)
            kwQuery.set_startRow(startRow);

        if (rankingModelId)
            kwQuery.set_rankingModelId(rankingModelId);

        if (graphQuery)
            kwQuery.get_properties().setQueryPropertyValue('GraphQuery', graphQuery);

        if (graphRankingModel)
            kwQuery.get_properties().setQueryPropertyValue('GraphRankingModel', graphRankingModel);

        var executor = new Microsoft.SharePoint.Client.Search.Query.SearchExecutor(ctx);
        var results = executor.executeQuery(kwQuery);

        ctx.executeQueryAsync(function() {
            deferred.resolve(results.m_value.ResultTables[0]);
        }, function(sender, args) {
            deferred.reject(args.get_message());
        });

        return deferred.promise;
    }

    executeQuery.prototype.getAppWebData = function(url, cacheResult) {
        var restQueryUrl = this.appweburl + url;
        if (url.indexOf('?') == -1) {
            restQueryUrl = restQueryUrl + "?";
        } else {
            restQueryUrl = restQueryUrl + "&";
        }
        return this.$http({
            url: restQueryUrl + "$top=500",
            method: 'GET',
            headers: { "Accept": "application/json" },
            cache: cacheResult
        });
    }

    executeQuery.prototype.getTargetWebData = function(url, target) {
        var deferred = this.$q.defer();

        var requestUrl = this.appweburl + "/_api/SP.AppContextSite(@target)" + url;
        if (url.indexOf('?') == -1) {
            requestUrl = requestUrl + "?";
        } else {
            requestUrl = requestUrl + "&";
        }

        this.executor.executeAsync({
            url: requestUrl + "@target='" + target + "'",
            method: 'GET',
            headers: { "Accept": "application/json" },
            success: function(data) {
                deferred.resolve(data);
            },
            error: function(xhr, a, s) {
                deferred.reject(xhr);
            }
        })
        return deferred.promise;
    }
    executeQuery.prototype.postContextInfo = function() {
        var deferred = this.$q.defer();
        this.executor.executeAsync({
            url: this.appweburl +
                "/_api/contextinfo",
            method: "POST",
            headers: {
                "Accept": "application/json; odata=verbose",
                "content-type": "application/json;odata=verbose"
            },
            success: function(data) {
                deferred.resolve(data);
            },
            error: function(xhr, a, s) {
                deferred.reject(xhr);
            }
        })
        return deferred.promise;
    }


    executeQuery.$inject = ["$http", "$q", "appweburl", ];

    app.service('executeQuery', executeQuery);

})(window.angular)