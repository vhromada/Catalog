goog.provide('server.Storage');

goog.require('este.Storage');

/**
 @param {app.Routes} routes
 @constructor
 @extends {este.Storage}
 @final
 */
server.Storage = function (routes) {
  this.routes = routes;
  server.Storage.superClass_.constructor.call(this);
};

goog.inherits(server.Storage, este.Storage);

/**
 @param {este.Route} route
 @param {Object} params
 @return {!goog.Promise}
 */
server.Storage.prototype.load = function (route, params) {
  return this.ok();
};
