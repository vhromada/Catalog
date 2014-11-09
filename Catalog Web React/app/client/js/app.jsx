goog.provide('App');

goog.require('goog.async.throwException');

/**
 @param {app.Actions} actions
 @param {app.FrontPage} frontPage
 @param {app.Routes} routes
 @param {app.Storage} storage
 @param {este.Router} router
 @constructor
 */
var App = function (actions, frontPage, routes, storage, router) {
  frontPage.init();
  storage.init();

  routes.addToEste(router, function (route, params) {
    return actions.loadRoute(route, params).then(function () {
      return routes.setActive(route, params);
    }).thenCatch(function (reason) {
      return routes.trySetErrorRoute(reason);
    }).then(function () {
      return actions.syncView();
    });
  });

  router.start();
};
